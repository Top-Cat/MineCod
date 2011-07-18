package com.Top_Cat.CODMW.objects;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.bukkit.Location;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.sql.conn;

@SuppressWarnings("unchecked")
public class map {
    
    private static final Yaml yaml = new Yaml(new SafeConstructor());
    public String name;
    public int time;
    public boolean storm;
    conn sql;
    main plugin;
    Object spawns;
    int BUFFER = 2048;
    
    public map(conn _c, main instance, String mapname) throws Exception {
        sql = _c;
        plugin = instance;
        
        Logger log = plugin.getServer().getLogger();
        File f = new File("./" + mapname);
        if (!f.isDirectory()) {
            File ZipFile = new File("./" + mapname + ".zip");
            File unzipDestinationDirectory = new File("./");
            main.download(log, new URL("http://www.thegigcast.net/minecod/" + mapname + ".zip"), ZipFile);
            ZipFile zipFile = new ZipFile(ZipFile);
            Enumeration<? extends ZipEntry> zipFileEntries = zipFile.entries();
            
            while (zipFileEntries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
                String currentEntry = entry.getName();
                
                log.info("Extracting '" + currentEntry + "' from '" + mapname + ".zip'");
                File destFile = new File(unzipDestinationDirectory, currentEntry);
                File destinationParent = destFile.getParentFile();
                destinationParent.mkdirs();
                
                try {
                    if (!entry.isDirectory()) {
                        BufferedInputStream is = new BufferedInputStream(zipFile.getInputStream(entry));
                        int currentByte;
                        byte data[] = new byte[BUFFER];

                        FileOutputStream fos = new FileOutputStream(destFile);
                        BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);

                        while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                            dest.write(data, 0, currentByte);
                        }
                        dest.flush();
                        dest.close();
                        is.close();
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            zipFile.close();
            ZipFile.delete();
        }
        try {
        	Map<String, Object> map = (Map<String, Object>) yaml.load(new FileInputStream("./" + mapname + "/minecod_data.yml"));
        	
            name = mapname;
            try {
            	time = Integer.parseInt(map.get("time").toString());
            } catch (NullPointerException ex) {
                throw new Exception("world time for '" + mapname + "' is not defined");
            }
            try {
            	storm = (Boolean) map.get("storm");
            } catch (NullPointerException ex) {
                throw new Exception("storm for '" + mapname + "' is not defined");
            }
            try {
            	spawns = map.get("spawns");
            } catch (NullPointerException ex) {
                throw new Exception("no spawns for '" + mapname + "' defined");
            }
        	
        } catch (FileNotFoundException e) {
        	throw new Exception("Missing minecod_data.yml for '" + mapname + "'! Please redownload the map");
        }
    }
    
    public ArrayList<Location> getSpawns(int type) {
        ArrayList<Location> out = new ArrayList<Location>();
        try {
        String t = "";
        switch (type) {
            case 0: t = "goldspawns"; break;
            case 1: t = "diamondspawns"; break;
            case 2: t = "gamespawns"; break;
            case 3: t = "goldflag"; break;
            case 4: t = "diamondflag"; break;
        }
        List<Map<String, Object>> s = ((Map<String, List<Map<String, Object>>>) spawns).get(t);
        
        for (Map<String, Object> i : s) {
            out.add(new Location(plugin.getServer().getWorld(name), (Double) i.get("x"), (Double) i.get("y"), (Double) i.get("z"), (float) ((Integer) i.get("r")), 0));
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }
    
}