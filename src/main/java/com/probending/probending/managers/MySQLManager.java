package com.probending.probending.managers;

import com.probending.probending.ProBending;
import com.probending.probending.core.PBPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

import static com.probending.probending.ProBending.configM;
import static com.probending.probending.ProBending.playerM;

public class MySQLManager {

    public final ConcurrentLinkedQueue<Runnable> sqlQueue = new ConcurrentLinkedQueue<>();


    public Connection connection;
    private ProBending plugin;
    private String dbType = "sqlite";
    private String myHost = null;
    private String myPort = null;
    private String myDB = null;
    private String myUser = null;
    private String myPassword = null;
    private String tablePrefix = "pb_";

    // String fields
    public static final String UUID = "uuid";
    public static final String NAME = "username";

    // Int fields
    public static final String WINS = "wins";
    public static final String LOST = "lost";
    public static final String KILLS = "kills";
    public static final String TIES = "ties";


    public MySQLManager(ProBending plugin) {

        this.plugin = plugin;

        OnEnable();
        initDatabase();

    }


    public void OnEnable() {
        configM.getConfig().addDefault("databasetype", "sqlite");
        configM.getConfig().addDefault("table-prefix", "pb_");
        configM.getConfig().addDefault("mysql.host", "localhost");
        configM.getConfig().addDefault("mysql.user", "root");
        configM.getConfig().addDefault("mysql.password", "1234");
        configM.getConfig().addDefault("mysql.database", "minecraft");
        configM.getConfig().addDefault("mysql.port", "3306");

        configM.getConfig().save();

        if (this.plugin.getConfig().contains("databasetype") &&
                this.plugin.getConfig().getString("databasetype").equalsIgnoreCase("mysql")) {

            boolean mysqlLegit = true;

            if (this.plugin.getConfig().contains("mysql.host")) {

                this.myHost = this.plugin.getConfig().getString("mysql.host");

            } else {

                mysqlLegit = false;

            }

            if (this.plugin.getConfig().contains("mysql.port")) {

                this.myPort = this.plugin.getConfig().getString("mysql.port");

            } else {

                mysqlLegit = false;

            }

            if (this.plugin.getConfig().contains("mysql.database")) {

                this.myDB = this.plugin.getConfig().getString("mysql.database");

            } else {

                mysqlLegit = false;

            }

            if (this.plugin.getConfig().contains("mysql.user")) {

                this.myUser = this.plugin.getConfig().getString("mysql.user");

            } else {

                mysqlLegit = false;

            }

            if (this.plugin.getConfig().contains("mysql.password")) {

                this.myPassword = this.plugin.getConfig().getString("mysql.password");

            } else {

                mysqlLegit = false;

            }

            if (mysqlLegit) {

                this.dbType = "mysql";

            }

        }


        if (this.plugin.getConfig().contains("table-prefix")) {

            this.tablePrefix = this.plugin.getConfig().getString("table-prefix");

        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!sqlQueue.isEmpty()) {
                    openConnection();
                    while (!sqlQueue.isEmpty()) {
                        sqlQueue.poll().run();
                    }
                    closeConnection();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, 5);

    }


    public void onDisable() {

        try {

            if (connection != null && connection.isClosed()) {

                connection.close();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void openConnection() {

        try {

            if (this.dbType.equals("mysql")) {
                this.connection = DriverManager.getConnection("jdbc:mysql://" + myHost + ":" + myPort + "/" + myDB, myUser, myPassword);
            } else {

                Class.forName("org.sqlite.JDBC");

                this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.plugin.getDataFolder() + "/data.db");

            }

        } catch (Exception e) {

            this.plugin.log(Level.SEVERE, "Couldn't connect to database");

            this.plugin.log(Level.SEVERE, "This is a fatal error, disabling plugin");

            e.printStackTrace();

            plugin.shutOffPlugin();

        }

    }


    public void closeConnection() {

        try {

            this.connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void initDatabase() {

        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {

            MySQLManager.this.openConnection();

            try {

                PreparedStatement sql = MySQLManager.this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS `" + MySQLManager.this.tablePrefix + "PBBenders` (`UUID` varchar(100) NOT NULL UNIQUE, `username` varchar(100), `wins` INT, `lost` INT, `kills` INT, `ties` INT);");

                sql.execute();

                sql.close();

            } catch (Exception e) {

                e.printStackTrace();

                return;

            } finally {

                MySQLManager.this.closeConnection();

            }

            MySQLManager.this.closeConnection();

        });

    }


    public void createTeamPlayer(Player p) {

        createTeamPlayer(p.getUniqueId(), p.getName());

    }


    public void createTeamPlayer(final UUID uuid, final String playerName) {

        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {

            MySQLManager.this.openConnection();

            try {

                PreparedStatement sql = MySQLManager.this.connection.prepareStatement("SELECT * FROM `" + MySQLManager.this.tablePrefix + "PBBenders` WHERE `UUID`=?;");

                sql.setString(1, uuid.toString());

                ResultSet rs = sql.executeQuery();

                if (rs.next()) {

                    PreparedStatement sql3 = MySQLManager.this.connection.prepareStatement("UPDATE `" + MySQLManager.this.tablePrefix + "PBBenders` SET `username`=? WHERE `UUID`=?;");

                    sql3.setString(1, playerName);

                    sql3.setString(2, uuid.toString());

                    sql3.executeUpdate();

                    sql3.close();

                    playerM.addPBPlayer(new PBPlayer(
                            rs.getString(UUID),
                            rs.getString(NAME),
                            rs.getInt(WINS),
                            rs.getInt(LOST),
                            rs.getInt(KILLS),
                            rs.getInt(TIES)));

                } else {

                    PreparedStatement sql2 = MySQLManager.this.connection.prepareStatement("INSERT INTO `" + MySQLManager.this.tablePrefix + "PBBenders` (`UUID`, `username`, `wins`, `lost`, `kills`, `ties`) VALUES(?,?,?,?,?,?);");

                    sql2.setString(1, uuid.toString());

                    sql2.setString(2, playerName);

                    sql2.setInt(3, 0);

                    sql2.setInt(4, 0);

                    sql2.setInt(5, 0);

                    sql2.setInt(6, 0);

                    sql2.execute();

                    sql2.close();

                    playerM.addPBPlayer(new PBPlayer(uuid.toString(), playerName, 0, 0, 0, 0));

                }

                sql.close();

                rs.close();

            } catch (SQLException e) {
                e.printStackTrace();

                return;

            } finally {

                MySQLManager.this.closeConnection();

            }

            MySQLManager.this.closeConnection();

        });

    }


    public void setStringField(final UUID u, final String field, final String data) {

        sqlQueue.add(() -> {
            try {

                PreparedStatement sql1 = MySQLManager.this.connection.prepareStatement("UPDATE `" + MySQLManager.this.tablePrefix + "PBBenders` SET `" + field + "`=? WHERE `UUID`=?;");

                sql1.setString(1, data);

                sql1.setString(2, u.toString());

                sql1.executeUpdate();

                sql1.close();

            } catch (Exception e) {

                e.printStackTrace();

            }
        });
    }


    public void setIntegerField(final UUID u, final String field, final int data) {
        sqlQueue.add(() -> {
            try {

                PreparedStatement sql1 = MySQLManager.this.connection.prepareStatement("UPDATE `" + MySQLManager.this.tablePrefix + "PBBenders` SET `" + field + "`=? WHERE `UUID`=?;");

                sql1.setInt(1, data);

                sql1.setString(2, u.toString());

                sql1.executeUpdate();

                sql1.close();

            } catch (Exception e) {

                e.printStackTrace();

            }
        });
    }

    public String listToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s).append(';');
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public ArrayList<String> stringToList(String string) {
        ArrayList<String> s = new ArrayList<>();
        Collections.addAll(s, string.split(";"));
        return s;
    }
}
