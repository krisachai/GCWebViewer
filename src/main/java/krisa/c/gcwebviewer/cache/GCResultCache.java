/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package krisa.c.gcwebviewer.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Krisa.Chaijaroen 
 * Processing Raw GC to CSV could take up more than 10
 * seconds. For the best UX, let's use caching and trigger the job by Quartz
 */
public class GCResultCache {

    private static org.slf4j.Logger log = (org.slf4j.Logger) LoggerFactory.getLogger(GCResultCache.class);

    static Map<String, String> cache;

    static {
        cache = new ConcurrentHashMap();
    }

    public static String getCSV(String uid) {
        return cache.get(uid);
    }

    public static void setCSV(String uid, String csvString) {
        //System.out.println("Updating cache : "+uid);
        log.info("Updating cache : " + uid);
        cache.put(uid, csvString);
    }
}
