package com.Top_Cat.CODMW.objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Location;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.sql.conn;

public class map {
    
    public String folder, name;
    public int time, id;
    public boolean storm;
    conn sql;
    main plugin;
    
    public map(conn _c, main instance, ResultSet r) {
        sql = _c;
        plugin = instance;
        try {
            id = r.getInt("Id");
            name = r.getString("name");
            folder = r.getString("mapfolder");
            time = r.getInt("time");
            storm = r.getBoolean("storm");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList<Location> getSpawns(int type) {
        ArrayList<Location> out = new ArrayList<Location>();
        ResultSet r = sql.query("SELECT * FROM cod_spawns WHERE map = '" + id + "' and type = '" + type + "'");
        try {
            while (r.next()) {
                out.add(new Location(plugin.currentWorld, r.getDouble("x"), r.getDouble("y"), r.getDouble("z"), r.getFloat("rot"), 0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return out;
    }
    
}