/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package krisa.c.gcweb.job;

import com.tagtraum.perf.gcviewer.imp.DataReader;
import com.tagtraum.perf.gcviewer.imp.DataReaderFactory;
import com.tagtraum.perf.gcviewer.model.GCModel;
import java.awt.geom.Point2D;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import krisa.c.gcweb.api.GCTimes;
import krisa.c.gcweb.api.UsedTenured;
import krisa.c.gcweb.api.UsedYoung;
import krisa.c.gcweb.inputprocessor.InputProcessor;
import krisa.c.gcweb.cache.GCResultCache;
import krisa.c.gcweb.util.DateTimeUtil;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Krisa.Chaijaroen
 */
public class GCProcessorJob implements Job {

    private static org.slf4j.Logger log = (org.slf4j.Logger) LoggerFactory.getLogger(GCProcessorJob.class);
    
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        
        JobDataMap jobData = jec.getJobDetail().getJobDataMap();
        InputProcessor inp = (InputProcessor) jobData.get("inp");
        log.info("Job is called for uid : " + jobData.getString("uid"));
        inp.load();
        String gcdata = inp.getRawGCData();
        try {
            if (gcdata != null) {
                
                DataReaderFactory factory = new DataReaderFactory();
                InputStream in = new ByteArrayInputStream(gcdata.getBytes());
                DataReader reader = factory.getDataReader(in);
                GCModel model = reader.read();
                log.debug("GC Log begins at " + model.getFirstDateStamp());
                log.debug("GC Log ends at " + model.getLastPauseTimeStamp());
                
                UsedYoung young = new UsedYoung();
                List<Point2D> points_young = young.computePolygon(model)
                        .getPoints();
                UsedTenured tenured = new UsedTenured();
                List<Point2D> points_tenur = tenured.computePolygon(model)
                        .getPoints();
                GCTimes times = new GCTimes();
                List<Point2D> points_times = times.computePolygon(model)
                        .getPoints();
                long ms = System.currentTimeMillis();
                
                String CSVres = ListToCSV(points_young, points_tenur, points_times, model.getFirstDateStamp().getTime());
                log.info("String to CSV took " + (System.currentTimeMillis() - ms) + " ms");
                //Update cache
                GCResultCache.setCSV(jobData.getString("uid"), CSVres);
                
            } else {
                log.error("GC Raw Data is null...skip processing [" + jobData.getString("uid")+"]");
                
            }
        } catch (IOException ex) {
           log.error("Exception Caught!",ex);
        }
    }

    //8000 events of GC took around 2 seconds on Mobile Core i5-3320M
    //It would be nice, if anyone can make it runs faster.
    public static String ListToCSV(List<Point2D> young, List<Point2D> tenur,
            List<Point2D> time, long gcStartTime) {
        
        String res = "Date,UsedYoung,UsedTenured,GC Time\n";
        int i_t = 0;
        DecimalFormat df = new DecimalFormat("##############.############");
        for (int i = 0; i < young.size(); i++) {
            double x = young.get(i).getX();
            if (i != 0) {
                if (x == young.get(i - 1).getX()) {
                    x += 0.05;
                }
            }

            //Transform Long to Dygraph date format.
            //Convert from KB to MB.
            res = res.concat(String.valueOf(DateTimeUtil.SDF_DEFAULT_DATE_TIME_FORMAT.get().
                    format(new Date((long) ((x * 1000) + gcStartTime)))).
                    concat(",".concat(df.format(young.get(i).getY() / 1024)
                                    + "," + df.format(tenur.get(i).getY() / 1024))));
            if (i % 2 == 0) {
                res = res.concat("," + df.format(time.get(i_t).getY()));
                i_t += 1;
            } else {
                res = res.concat("," + df.format(time.get(i_t - 1).getY()));
            }
            if ((i + 1) != young.size()) {
                res = res.concat("\n");
            }
        }
        
        return res;
    }
    
}
