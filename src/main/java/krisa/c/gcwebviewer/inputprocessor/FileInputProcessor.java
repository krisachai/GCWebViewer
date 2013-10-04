/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package krisa.c.gcwebviewer.inputprocessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Krisa.Chaijaroen
 */
public class FileInputProcessor extends InputProcessor {

    private static org.slf4j.Logger log = (org.slf4j.Logger) LoggerFactory.getLogger(FileInputProcessor.class);

    private String path;
    String uid;
    public FileInputProcessor(String path,String uid) {
        this.path = path;
        this.uid = uid;
    }

    @Override
    public void load() {
        log.debug("Start loading file");
        File f = new File(getPath());
        if (f.isDirectory()) {
            log.info("Input path is directory...searching for latest gc log");
            f = getLatestGCFile(path);
        }
        BufferedReader br = null;
        StringBuilder sb;
        try {
            br = new BufferedReader(new FileReader(f));
            sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append('\n');
                line = br.readLine();
            }
            super.rawgc = sb.toString();
            log.debug("Done loading file");
        } catch (IOException ex) {
            log.error("Exception caught!", ex);
        } finally {
            try {

                if (br != null) {
                    br.close();
                }

            } catch (IOException e) {
            }

        }
    }

    public File getLatestGCFile(String path) {
        File dir = new File(path);
        FileFilter fileFilter = new WildcardFileFilter("*gc*");
        File[] files = dir.listFiles(fileFilter);
        log.debug("Found "+files.length +" gc logs under "+path);
        File targetFile = null;
        for (File file : files) {
            System.out.println(file + "," + file.lastModified());
            if (targetFile == null) {
                targetFile = file;
            } else {
                if (targetFile.lastModified() < file.lastModified()) {
                    targetFile = file;
                }
            }
        }
        return targetFile;
    }

    public static void main(String[] args) {
        //InputProcessor inp = new FileInputProcessor("C:\\Users\\KZ\\Downloads\\gc.log");
        //inp.load();
        //  System.out.println(inp.rawgc);
        File dir = new File("C:\\Users\\KZ\\Downloads");
        FileFilter fileFilter = new WildcardFileFilter("*GC*");
        File[] files = dir.listFiles(fileFilter);
        File targetFile = null;
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i] + "," + files[i].lastModified());
            if (targetFile == null) {
                targetFile = files[i];
            } else {
                if (targetFile.lastModified() < files[i].lastModified()) {
                    targetFile = files[i];
                }
            }
        }

        System.out.println("Target -> " + targetFile.getName());
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

}
