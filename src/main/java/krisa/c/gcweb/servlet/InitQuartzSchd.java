/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package krisa.c.gcweb.servlet;

import java.util.List;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import krisa.c.gcweb.inputprocessor.InputProcessor;
import krisa.c.gcweb.inputprocessor.FileInputProcessor;
import krisa.c.gcweb.inputprocessor.SSHInputProcessor;
import krisa.c.gcweb.job.GCProcessorJob;
import krisa.c.gcweb.Config;
import krisa.c.gcweb.GCSource;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Krisa.Chaijaroen
 */
public class InitQuartzSchd implements ServletContextListener {

    private static org.slf4j.Logger log = (org.slf4j.Logger) LoggerFactory.getLogger(InitQuartzSchd.class);

    static SchedulerFactory schedFact;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            log.info("Loading Log4j configuration from : " + Config.prefix);
            DOMConfigurator.configure(Config.prefix + "log4j_conf.xml");

        } catch (Exception e) {
            log.error("Exception Caught!", e);
            BasicConfigurator.configure();
            log.warn("Load Basic Configuration for Log4j");
        }

        GCSource gcs = new GCSource();

        List<GCSource> sources = gcs.parseConfig(new Config());

        for (GCSource gCSource : sources) {
            runJob(mapInputType(gCSource), gCSource.getUid(), gCSource.getRefreshRate());
        }

    }

    public void runJob(InputProcessor inp, String uid, int refresh) {
        if (schedFact == null) {
            log.info("Setting up Quartz scheduler");
            schedFact = new StdSchedulerFactory();
            initJob(inp, uid, refresh);
        } else {
            initJob(inp, uid, refresh);
        }
    }

    public void initJob(InputProcessor inp, String uid, int refresh) {
        try {

            log.info("Create Job for " + uid + " with " + refresh + " sec. refresh rate");
            Scheduler sched = schedFact.getScheduler();
            JobDetail gcjob = newJob(GCProcessorJob.class).withIdentity(uid, "group1").build();
            gcjob.getJobDataMap().put("inp", inp);
            gcjob.getJobDataMap().put("uid", uid);
            Trigger trigger = newTrigger().withIdentity("trigger" + uid, "group1")
                    .startNow().withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(refresh).repeatForever()).build();
            sched.scheduleJob(gcjob, trigger);
            sched.start();
        } catch (SchedulerException ex) {
            log.error("Exception Caught!", ex);
        }
    }

    public InputProcessor mapInputType(GCSource src) {
        InputProcessor inp = null;
        switch (src.getType()) {
            case "file":
                inp = new FileInputProcessor(src.getPath(), src.getUid());
                break;
            case "ssh":
                if (src.getHostname() != null || src.getUser() != null || src.getPassword() != null) {
                    inp = new SSHInputProcessor(src.getHostname(), src.getUser(), src.getPassword(), src.getPath(), src.getUid());
                } else {
                    log.error(src.getPath() + " ssh type without password [" + src.getType() + "]");
                }
                break;
            default:
                log.error(src.getPath() + " has invalid type [" + src.getType() + "]");

                break;
        }
        return inp;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("Shutting down scheduler");
        try {
            schedFact.getScheduler().shutdown();
        } catch (SchedulerException ex) {
            log.error("Exception Caught!", ex);
        }
    }

}
