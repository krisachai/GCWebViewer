/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package krisa.c.ssh;

import java.io.IOException;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Krisa.Chaijaroen
 */
public class RemoteGCLog {

    String logpath;
    RemoteSSH client;
    String gcfile;
    private static org.slf4j.Logger log = (org.slf4j.Logger) LoggerFactory.getLogger(RemoteGCLog.class);

    public RemoteGCLog(String host, String user, String passwd, String path) throws InterruptedException, IOException {
        client = new RemoteSSH(host, user, passwd);
        logpath = path;
    }
    /*
     This method has been tested on Linux only
     */

    public String findGCLog() throws Exception {

        String cmd = "find " + logpath + " -type f  -printf '%T@ %p\\n'  | sort -n | tail -6 | cut -f2- -d\" \" |grep gc \n";
        log.debug("finding for possible GC log with this comomand [" + cmd + "]");
        String res = client.sendCMD(cmd, 3000);
         System.out.println(res);
        String[] results = res.split("\n");

        gcfile = results[results.length - 2].trim();

        return results[results.length - 2].trim();

    }
   
    public void close() {
        client.disconnect();
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
  
}
