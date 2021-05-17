package com.probending.probending.managers.database;

import com.probending.probending.ProBending;
import com.probending.probending.managers.PBManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

import static com.probending.probending.ProBending.configM;

public class DataBaseManager extends PBManager {

    public final ConcurrentLinkedQueue<Runnable> sqlQueue = new ConcurrentLinkedQueue<>();

    public final PlayerDataTable playerTable;


    public Connection connection;
    private String dbType = "sqlite";
    private String myHost = null;
    private String myPort = null;
    private String myDB = null;
    private String myUser = null;
    private String myPassword = null;
    private String tablePrefix = "pb_";

    public String getTablePrefix() {
        return tablePrefix;
    }

    public DataBaseManager(ProBending plugin) {

        super(plugin);

        OnEnable();

        playerTable = new PlayerDataTable(this);

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

            DataBaseManager.this.openConnection();

            try {

                Statement sql = DataBaseManager.this.connection.createStatement();
                sql.addBatch(playerTable.initTable());
                sql.executeBatch();
                sql.close();

            } catch (Exception e) {

                e.printStackTrace();

                return;

            } finally {

                DataBaseManager.this.closeConnection();

            }

            DataBaseManager.this.closeConnection();

        });

    }

    /*

    public void createTeamPlayer(Player p) {
        createTeamPlayer(p.getUniqueId(), p.getName());
    }


    public void createTeamPlayer(final UUID uuid, final String playerName) {

        sqlQueue.add(() -> {

            try {

                PreparedStatement sql = DataBaseManager.this.connection.prepareStatement("SELECT * FROM `" + DataBaseManager.this.tablePrefix + "PBBenders` WHERE `UUID`=?;");

                sql.setString(1, uuid.toString());

                ResultSet rs = sql.executeQuery();

                if (rs.next()) {

                    PreparedStatement sql3 = DataBaseManager.this.connection.prepareStatement("UPDATE `" + DataBaseManager.this.tablePrefix + "PBBenders` SET `username`=? WHERE `UUID`=?;");

                    sql3.setString(1, playerName);

                    sql3.setString(2, uuid.toString());

                    sql3.executeUpdate();

                    sql3.close();

                    playerM.addPBPlayer(new PBPlayer(
                            rs.getString(UUID.getName()),
                            rs.getString(NAME.getName()),
                            rs.getInt(WINS.getName()),
                            rs.getInt(LOST.getName()),
                            rs.getInt(KILLS.getName()),
                            rs.getInt(TIES.getName())));

                } else {

                    StringBuilder path = new StringBuilder("INSERT INTO `" + DataBaseManager.this.tablePrefix + "PBBenders` (`UUID`, `username`");
                    StringBuilder values = new StringBuilder(") VALUES('" + uuid.toString() + "','" + playerName + "'");
                    for (StringValues string : StringValues.values()) {
                        if (string != StringValues.NAME && string != StringValues.UUID) {
                            path.append(", `").append(string.getName()).append("`");
                            values.append(", '").append(string.getDefault()).append("'");
                        }
                    }
                    for (NumberValues string : NumberValues.values()) {
                        path.append(", `").append(string.getName()).append("`");
                        values.append(", ").append(string.getDefault());
                    }

                    path.append(values).append(");");

                    PreparedStatement sql2 = DataBaseManager.this.connection.prepareStatement(path.toString());

                    sql2.execute();

                    sql2.close();

                    playerM.addPBPlayer(new PBPlayer(uuid.toString(), playerName, 0, 0, 0, 0));

                }

                sql.close();

                rs.close();

            } catch (SQLException e) {
                e.printStackTrace();

            }

        });

    }

     */

    /*
    public void setStringField(final UUID u, final StringValues field, final String data) {

        sqlQueue.add(() -> {
            try {

                PreparedStatement sql1 = DataBaseManager.this.connection.prepareStatement("UPDATE `" + DataBaseManager.this.tablePrefix + "PBBenders` SET `" + field.getName() + "`=? WHERE `UUID`=?;");

                sql1.setString(1, data);

                sql1.setString(2, u.toString());

                sql1.executeUpdate();

                sql1.close();

            } catch (Exception e) {

                e.printStackTrace();

            }
        });
    }

     */

    /*
    public void setIntegerField(final UUID u, final NumberValues field, final int data) {
        sqlQueue.add(() -> {
            try {

                PreparedStatement sql1 = DataBaseManager.this.connection.prepareStatement("UPDATE `" + DataBaseManager.this.tablePrefix + "PBBenders` SET `" + field.getName() + "`=? WHERE `UUID`=?;");

                sql1.setInt(1, data);

                sql1.setString(2, u.toString());

                sql1.executeUpdate();

                sql1.close();

            } catch (Exception e) {

                e.printStackTrace();

            }
        });
    }

     */

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
    /*

    public enum StringValues {
        UUID("uuid", 100, "NULL"),
        NAME("username", 100, "NULL");

        private String name;

        private int size;

        private String defaultValue;

        StringValues(String name, int size, String defaultValue) {
            this.name = name;
            this.size = size;
            this.defaultValue = defaultValue;
        }

        public String getName() {
            return name;
        }

        public int getSize() {
            return size;
        }

        public String getDefault() {
            return defaultValue;
        }
    }

    public enum NumberValues {
        WINS("wins"),
        LOST("lost"),
        KILLS("kills"),
        TIES("ties");

        private String name;

        private int defaultValue = 0;

        NumberValues(String name) {
            this.name = name;
        }

        NumberValues(String name, int defaultValue) {
            this.name = name;
            this.defaultValue = defaultValue;
        }

        public String getName() {
            return name;
        }

        public int getDefault() {
            return defaultValue;
        }
    }
     */
}
