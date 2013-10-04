/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package krisa.c.gcwebviewer.inputprocessor;

import krisa.c.ssh.RemoteGCLog;
import java.io.InputStream;
import krisa.c.ssh.SFTP;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Krisa.Chaijaroen
 */
public class SSHInputProcessor extends InputProcessor {

    private static org.slf4j.Logger log = (org.slf4j.Logger) LoggerFactory.getLogger(SSHInputProcessor.class);
    private String hostname, user, password, path;
    String uid;

    public SSHInputProcessor(String hostname, String user, String password, String path, String uid) {
        this.hostname = hostname;
        this.user = user;
        this.password = password;
        this.path = path;
        this.uid = uid;
    }

    @Override
    public void load() {
        String gcpath = null;
        try {
            log.debug("[+" + uid + "]Start loading file from ssh");
            RemoteGCLog rgc = new RemoteGCLog(hostname, user, password, path);
            gcpath = rgc.findGCLog();
            log.debug("[+" + uid + "]Returned GC File : " + gcpath);
            rgc.close();
            SFTP sftp = new SFTP(hostname, user, password);
            InputStream ins = sftp.getFile(gcpath);
            String gc = convertStreamToString(ins);
            //System.out.println(rawgc);
            ins.close();
            sftp.close();
            super.rawgc = gc;
        } catch (Exception ex) {
            log.error("[+" + uid + "]Exception Caught! ->" + gcpath, ex);
        }

    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
