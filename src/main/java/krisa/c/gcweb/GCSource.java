/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package krisa.c.gcweb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Krisa.Chaijaroen
 */
public class GCSource {
    private static org.slf4j.Logger log = (org.slf4j.Logger) LoggerFactory.getLogger(GCSource.class);

    private String type;
    private String path;
    private String password=null;
    private String hostname=null;
    private String user=null;
    private int refreshRate;
    private String uid=null;
    public List<GCSource> parseConfig(Config conf) {
        log.info("Parsing Configuration");
        conf.load();
        List<Map> source = conf.getSources();
        List<GCSource> sources = new ArrayList<>();

        for (Map map : source) {
            GCSource gcs = new GCSource();
            String ty = (String) map.get("type");
            gcs.setPath((String) map.get("path"));
            gcs.setType(ty);
            gcs.setUid((String) map.get("uid"));
            if(map.get("refresh")!=null){
                gcs.setRefreshRate((int) map.get("refresh"));
            }else{
                //Default refresh rate 5 minutes
                gcs.setRefreshRate(300);
            }
            if (ty.equals("ssh")) {
                gcs.setHostname((String) map.get("hostname"));
                gcs.setUser((String) map.get("user"));
                gcs.setPassword((String) map.get("password"));
            }
            sources.add(gcs);
        }

        return sources;
    }

    public static void main(String[] args) {
        GCSource gcs = new GCSource();
        List<GCSource> sources = gcs.parseConfig(new Config());
        System.out.println(sources.size());
        System.out.println(sources.get(0).getType());

        for (GCSource gCSource : sources) {
            System.out.println("Called - >> " + gCSource.getPath());
        }
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * @param hostname the hostname to set
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the refreshRate
     */
    public int getRefreshRate() {
        return refreshRate;
    }

    /**
     * @param refreshRate the refreshRate to set
     */
    public void setRefreshRate(int refreshRate) {
        this.refreshRate = refreshRate;
    }

    /**
     * @return the uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }
}
