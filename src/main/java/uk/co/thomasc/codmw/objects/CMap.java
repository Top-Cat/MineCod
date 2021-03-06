package uk.co.thomasc.codmw.objects;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
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
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.sql.Conn;

@SuppressWarnings("unchecked")
public class CMap {
	
	public String name, title;
	public int time;
	public boolean storm;
	private Conn sql;
	private Main plugin;
	private ConfigurationSection spawns;
	private int BUFFER = 2048;
	
	public CMap(Conn _c, Main instance, String mapname) throws Exception {
		sql = _c;
		plugin = instance;
		
		Logger log = plugin.getServer().getLogger();
		File f = new File("./" + mapname);
		if (!f.isDirectory()) {
			File ZipFile = new File("./" + mapname + ".zip");
			File unzipDestinationDirectory = new File("./");
			Main.download(log, new URL("http://www.thegigcast.net/minecod/maps/" + mapname + ".zip"), ZipFile);
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
		Configuration mapconfig = YamlConfiguration.loadConfiguration(new File("./" + mapname + "/minecod_data.yml"));
		
		name = mapname;
		time = mapconfig.getInt("time", 0);
		title = mapconfig.getString("title", mapname);
		storm = mapconfig.getBoolean("storm", false);
		spawns = mapconfig.getConfigurationSection("spawns");
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
			List<?> s = spawns.getList(t);
			for (Object j : s) {
				Map<String, Object> i = (Map<String, Object>) j;
				out.add(new Location(plugin.getServer().getWorld(name), (Double) i.get("x"), (Double) i.get("y"), (Double) i.get("z"), (float) ((Integer) i.get("r")), 0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
}