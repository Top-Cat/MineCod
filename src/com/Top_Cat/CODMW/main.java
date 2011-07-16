package com.Top_Cat.CODMW;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkitcontrib.BukkitContrib;

import com.Top_Cat.CODMW.gamemodes.CTF;
import com.Top_Cat.CODMW.gamemodes.FFA;
import com.Top_Cat.CODMW.gamemodes.TDM;
import com.Top_Cat.CODMW.gamemodes.gamemode;
import com.Top_Cat.CODMW.gamemodes.team_gm;
import com.Top_Cat.CODMW.listeners.CODBlockListener;
import com.Top_Cat.CODMW.listeners.CODEntityListener;
import com.Top_Cat.CODMW.listeners.CODInputListener;
import com.Top_Cat.CODMW.listeners.CODInventoryListener;
import com.Top_Cat.CODMW.listeners.CODPlayerListener;
import com.Top_Cat.CODMW.listeners.CODWeatherListener;
import com.Top_Cat.CODMW.objects.CWolfPack;
import com.Top_Cat.CODMW.objects.chopper;
import com.Top_Cat.CODMW.objects.claymore;
import com.Top_Cat.CODMW.objects.door;
import com.Top_Cat.CODMW.objects.map;
import com.Top_Cat.CODMW.objects.player;
import com.Top_Cat.CODMW.objects.redstone;
import com.Top_Cat.CODMW.objects.sentry;
import com.Top_Cat.CODMW.sql.conn;
import com.Top_Cat.CODMW.vote.GameModes;
import com.Top_Cat.CODMW.vote.GameTypeVote;
import com.Top_Cat.CODMW.vote.MapVote;
import com.Top_Cat.CODMW.vote.Vote;

public class main extends JavaPlugin {

    public World currentWorld;
    public Location teamselect;
    public Location prespawn;
    public HashMap<String, Integer> maps = new HashMap<String, Integer>();
    public HashMap<Player, player> players = new HashMap<Player, player>();
    public int gold, diam, tot, minplayers = 0;
    public CODPlayerListener playerListener;
    public CODBlockListener blockListener;
    public CODEntityListener entityListener;
    public CODInventoryListener inventoryListener;
    public CODWeatherListener weatherListener;
    public CODInputListener inputListener;
    public ArrayList<claymore> clays = new ArrayList<claymore>();
    public ArrayList<CWolfPack> wolves = new ArrayList<CWolfPack>();
    public ArrayList<sentry> sentries = new ArrayList<sentry>();
    public ArrayList<Player> totele = new ArrayList<Player>();
    public ArrayList<chopper> choppers = new ArrayList<chopper>();
    public final String d = "\u00C2\u00A7";
    door d1, d2, d3, d4;
    public gamemode game;
    public Vote v;
    public boolean activeGame = false;
    public redstone r;
    Timer t = new Timer();
    public conn sql = new conn();
    public map currentMap;
    Random gen = new Random();
    public GameModes gm = GameModes.FFA;
    public String ip;
    
    public void clearinv(Player p) {
        PlayerInventory i = p.getInventory();
        i.clear();
        i.clear(39);
        i.clear(38);
        i.clear(37);
        i.clear(36);
    }

    public player p(Player p) {
        return players.get(p);
    }

    @Override
    public void onDisable() {
        if (game != null) {
            game.destroy();
        }
        System.out.println("Minecod disabled!");
    }

    public void setDoors() {
        d1.open();
        d2.open();
        d3.open();
        d4.open();
        if (gold > diam) {
            d1.close();
            d2.close();
        } else if (diam > gold) {
            d3.close();
            d4.close();
        }
    }

    public class sun extends TimerTask {

        @Override
        public void run() {
            currentWorld.setTime(currentMap.time);
            currentWorld.setStorm(currentMap.storm);
            currentWorld.setThundering(false);
            
            updateServerStatus(false);
        }

    }

    public void preparemap() {
        if (activeGame) { game.destroy(); }
        currentWorld = getServer().createWorld(currentMap.folder, Environment.NORMAL);
        currentWorld.setPVP(true);
        currentWorld.setSpawnFlags(true, true);
        for (Entity i : currentWorld.getEntities()) {
            if (i instanceof Item) {
                i.remove();
            }
        }
        teamselect = new Location(currentWorld, -14, 64, 13, 270, 0);
        prespawn = new Location(currentWorld, -15.5, 64, 2, 270, 0);

        d1 = new door(currentWorld.getBlockAt(-10, 64, 14));
        d2 = new door(currentWorld.getBlockAt(-9, 64, 14));

        d3 = new door(currentWorld.getBlockAt(-10, 64, 11));
        d4 = new door(currentWorld.getBlockAt(-9, 64, 11));

        r = new redstone(currentWorld.getBlockAt(-6, 64, 0), this);
        
        switch (gm) {
            case TDM: game = new TDM(this); break; 
            case CTF: game = new CTF(this); break;
            case FFA: game = new FFA(this); break;
        }
        
        players.clear();
        diam = 0;
        gold = 0;
        tot = 0;
        setDoors();
        for (Player i : getServer().getOnlinePlayers()) {
            clearinv(i);
            game.jointele(i);
        }
        updateServerStatus(false);
    }
    
    public void loadmap(Integer mapid) {
        ResultSet _r = sql.query("SELECT * FROM cod_maps WHERE `Id` = '" + mapid + "'");
        try {
            _r.next();
            currentMap = new map(sql, this, _r);
            preparemap();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void loadmap() {
        String w = "";
        if (currentMap != null) {
            w = " WHERE `Id` != '" + currentMap.id + "'";
        }
        ResultSet _r = sql.query("SELECT * FROM cod_maps" + w + " ORDER BY RAND()");
        try {
            _r.next();
            currentMap = new map(sql, this, _r);
            preparemap();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    

    @Override
    public boolean onCommand(CommandSender sender, Command command,
            String label, String[] args) {
        if (sender instanceof Player) {
            Player p = ((Player) sender);
            if (command.getName().equals("r")) {
                int arrows = 0;
                int ammo = 0;
                for (ItemStack i : p.getInventory().getContents()) {
                    if (i != null) {
                        if (i.getType() == Material.ARROW) {
                            arrows += i.getAmount();
                        }
                        if (i.getType() == Material.FEATHER) {
                            ammo += i.getAmount();
                        }
                    }
                }
                int togive = ammo < 15 - arrows ? ammo : 15 - arrows;
                
                if (togive > 0) {
                    if (arrows == 0) {
                        p.getInventory().setItem(8,
                                new ItemStack(Material.ARROW, togive));
                    } else {
                        p.getInventory().addItem(
                                new ItemStack(Material.ARROW, togive));
                    }
                }
                p.getInventory().removeItem(
                        new ItemStack(Material.FEATHER, togive));
                return true;
            } else if (command.getName().equalsIgnoreCase("s")) {
                if (args.length == 0) {
                    game.printScore(p, team.BOTH);
                    return true;
                } else if (args[0].equalsIgnoreCase("d")) {
                    game.printScore(p, team.DIAMOND);
                    return true;
                } else if (args[0].equalsIgnoreCase("g")) {
                    game.printScore(p, team.GOLD);
                    return true;
                }
            } else if (command.getName().equalsIgnoreCase("team")) {
                if (args[0].equalsIgnoreCase("switch")) {
                    switchplayer(p);
                    return true;
                } else if (args[0].equalsIgnoreCase("noswitch")) {
                    p.sendMessage("Your team was not switched!");
                    return true;
                }
            } else if (command.getName().equalsIgnoreCase("vote") && players.containsKey(sender)) {
                if (v == null) {
                    if (args.length > 1) {
                        if (args[0].equalsIgnoreCase("map")) {
                            if (maps.containsKey(args[1])) {
                                v = new MapVote(this, args[1], maps.get(args[1]), p((Player) sender));
                            } else {
                                ((Player) sender).sendMessage("Could not find map!");
                            }
                        } else if (args[0].equalsIgnoreCase("mode")) {
                            if (GameModes.getGMFromId(args[1].toUpperCase()) != null) {
                                v = new GameTypeVote(this, args[1].toUpperCase(), p((Player) sender));
                            } else {
                                ((Player) sender).sendMessage("Could not find game type!");
                            }
                        } else {
                            ((Player) sender).sendMessage("Invalid vote type. Possible types are 'map' and 'mode'");
                        }
                    } else {
                        ((Player) sender).sendMessage("Wrong number of arguments. Correct format is /vote <type> <for>");
                    }
                } else {
                    ((Player) sender).sendMessage("Vote already in progress!");
                }
            } else if (command.getName().equalsIgnoreCase("y") && v != null) {
                v.VoteUp((Player) sender);
            } else if (command.getName().equalsIgnoreCase("n") && v != null) {
                v.VoteDown((Player) sender);
            }
            if ((command.getName().equalsIgnoreCase("y") || command.getName().equalsIgnoreCase("n")) && v == null) {
                ((Player) sender).sendMessage("No vote in progress!");
            }
        }
        return false;
    }

    public void switchplayer(Player p) {
        if (game instanceof team_gm) {
            if (p(p).getTeam() == team.GOLD && gold > diam) {
                p(p).setTeam(team.DIAMOND);
                gold--;
                diam++;
            } else if (p(p).getTeam() == team.DIAMOND && gold < diam) {
                p(p).setTeam(team.GOLD);
                gold++;
                diam--;
            } else {
                p.sendMessage("Teams cannot be stacked!");
                return;
            }
            player _p = p(p);
            for (CWolfPack i : wolves) {
                if (i.getOwner() == p) {
                    i.removeAll();
                }
            }
            game.sendMessage(team.BOTH, d + _p.getTeam().getColour() + _p.nick + " switched to " + _p.getTeam().toString() + " team!");
            _p.resetScore();
            _p.clearinv();
            _p.setinv();
            p.teleport(prespawn);
            _p.dead = true;
            setDoors();
        } else {
            p.sendMessage("Not possible in this game type");
        }
    }
    
    public void updateServerStatus(boolean start) {
        int mid = 0;
        if (currentMap != null) { mid = currentMap.id; }
        String s = "";
        if (start) {
            s = "INSERT INTO cod_servers VALUES('" + ip + "', '" + getServer().getPort() + "', NOW(), " + players.size() + ", " + mid + ", '" + gm.toString() + "') ON DUPLICATE KEY UPDATE ";
        } else {
            s = "UPDATE cod_servers SET ";
        }
        s += "`lastup`=NOW(), `players`='" + players.size() + "', `map`='" + mid + "', `mode`='" + gm.toString() + "'";
        if (!start) {
            s += " WHERE ip='" + ip + "' and port='" + getServer().getPort() + "'";
        }
        sql.update(s);
    }

    @Override
    public void onEnable() {
        Logger log = getServer().getLogger();
        final PluginManager pm = getServer().getPluginManager();
        if (pm.getPlugin("BukkitContrib") == null) {
            try {
                download(log, new URL("http://bit.ly/autoupdateBukkitContrib"), new File("plugins/BukkitContrib.jar"));
                pm.loadPlugin(new File("plugins" + File.separator + "BukkitContrib.jar"));
                pm.enablePlugin(pm.getPlugin("BukkitContrib"));
            } catch (final Exception ex) {
                log.warning("[LogBlock] Failed to install BukkitContrib, you may have to restart your server or install it manually.");
            }
        }
        
        playerListener = new CODPlayerListener(this);
        blockListener = new CODBlockListener(this);
        entityListener = new CODEntityListener(this);
        inventoryListener = new CODInventoryListener(this);
        weatherListener = new CODWeatherListener(this);
        inputListener = new CODInputListener(this);
        
        try {
            URL whatismyip = new URL("http://automation.whatismyip.com/n09230945.asp");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            ip = in.readLine();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        updateServerStatus(true);
        
        loadmap();
        pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_BED_ENTER, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.INVENTORY_OPEN, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_PICKUP_ITEM, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_CHAT, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_DROP_ITEM, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_TOGGLE_SNEAK, playerListener, Priority.Normal, this);

        pm.registerEvent(Event.Type.BLOCK_DAMAGE, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Priority.Normal, this);

        pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.CREATURE_SPAWN, entityListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PAINTING_BREAK, entityListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PAINTING_PLACE, entityListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.ENTITY_REGAIN_HEALTH, entityListener, Priority.Normal, this);

        pm.registerEvent(Event.Type.CUSTOM_EVENT, inventoryListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.CUSTOM_EVENT, inputListener, Priority.Normal, this);

        pm.registerEvent(Event.Type.WEATHER_CHANGE, weatherListener, Priority.Normal, this);

        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");

        t.schedule(new sun(), 0, 20000);

        BukkitContrib.getItemManager().setItemName(Material.FEATHER, "Ammo");
        BukkitContrib.getItemManager().setItemName(Material.WALL_SIGN, "Wall Sign");
        BukkitContrib.getItemManager().setItemName(Material.APPLE, "Invulnerability");
        BukkitContrib.getItemManager().setItemName(Material.BONE, "Dogs");
        BukkitContrib.getItemManager().setItemName(Material.DISPENSER, "Sentry");
        BukkitContrib.getItemManager().setItemName(Material.DIAMOND, "Chopper");
        BukkitContrib.getItemManager().setItemName(Material.IRON_SWORD, "Knife");
        
        setDoors();
        for (Player p : getServer().getOnlinePlayers()) {
            if (!(game instanceof team_gm)) {
                new player(this, p, team.BOTH);
            }
            clearinv(p);
            p.sendMessage(d + "9Welcome to The Gigcast's MineCod Server!");
            p.sendMessage(d + "9Please choose your team!");
            p.setHealth(20);
        }
        
        ResultSet _r = sql.query("SELECT * FROM cod_maps");
        try {
            while (_r.next()) {
                maps.put(_r.getString("name"), _r.getInt("Id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void download(Logger log, URL url, File file) throws IOException {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdir();
        if (file.exists())
            file.delete();
        file.createNewFile();
        final int size = url.openConnection().getContentLength();
        log.info("Downloading " + file.getName() + " (" + size / 1024 + "kb) ...");
        final InputStream in = url.openStream();
        final OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        final byte[] buffer = new byte[1024];
        int len, downloaded = 0, msgs = 0;
        final long start = System.currentTimeMillis();
        while ((len = in.read(buffer)) >= 0) {
            out.write(buffer, 0, len);
            downloaded += len;
            if ((int)((System.currentTimeMillis() - start) / 500) > msgs) {
                log.info((int)((double)downloaded / (double)size * 100d) + "%");
                msgs++;
            }
        }
        in.close();
        out.close();
        log.info("Download finished");
    }

}