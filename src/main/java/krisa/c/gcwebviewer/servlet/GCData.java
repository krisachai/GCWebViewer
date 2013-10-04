package krisa.c.gcwebviewer.servlet;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import krisa.c.gcwebviewer.util.DateTimeUtil;

import krisa.c.gcwebviewer.cache.GCResultCache;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class GCData
 */
public class GCData extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static org.slf4j.Logger log = (org.slf4j.Logger) LoggerFactory.getLogger(GCData.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GCData() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException {
        PrintWriter out = null;
        String uid = null;
        try {
            out = response.getWriter();
            
            
            response.setContentType("text/html;charset=UTF-8");
            uid = request.getParameter("uid");
            if (uid == null) {
                out.println("ERROR");
                log.error("missing input parameter"+request.getRequestURI());
            } else {
                out.print(GCResultCache.getCSV(uid));
            }
            out.close();
        } catch (IOException e) {
            log.error("Exception Caught!", e);
        } finally {

        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }

    public static String ListToString(List<Point2D> a) {
        String res = "{";
        DecimalFormat df = new DecimalFormat("##############.############");
        for (int i = 0; i < a.size(); i++) {
            double x = a.get(i).getX();
            if (i != 0) {
                if (x == a.get(i - 1).getX()) {
                    x += 0.05;
                }
            }
            double y = a.get(i).getY();
            res += "\"" + x + "\":" + df.format(y);
            if ((i + 1) != a.size()) {
                res += ",";
            }
        }
        res += "}";
        return res;
    }

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
            // double y = young.get(i).getY();
            res = res.concat(String.valueOf(DateTimeUtil.SDF_DEFAULT_DATE_TIME_FORMAT.get().format(new Date((long) ((x * 1000) + gcStartTime)))).concat(",".concat(df.format(young.get(i).getY() / 1024)
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
        // res += "}";
        return res;
    }
}
