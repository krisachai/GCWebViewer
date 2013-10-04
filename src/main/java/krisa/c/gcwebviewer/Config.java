/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package krisa.c.gcwebviewer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author Krisa.Chaijaroen
 */
public class Config {

    private Map conf=null;
    private static org.slf4j.Logger log = (org.slf4j.Logger) LoggerFactory.getLogger(Config.class);
 public static final String prefix = System.getProperty("user.home") + File.separator + "conf" + File.separator + "gcweb" + File.separator;
    public Map load() {
        try {
            Yaml yml = new Yaml();
            conf = (Map) yml.load(new FileReader(prefix+"sources.yml"));
            log.info("Reading configuration file : "+prefix+"sources.yml");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conf;
    }

    public List<Map> getSources() {
        return (List) conf.get("source");
    }

    public static void main(String[] args) throws FileNotFoundException {

    }

}
