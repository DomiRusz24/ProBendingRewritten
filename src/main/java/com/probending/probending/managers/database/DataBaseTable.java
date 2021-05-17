package com.probending.probending.managers.database;

import com.probending.probending.managers.database.values.DataBaseValue;
import javafx.util.Pair;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.function.Consumer;

public abstract class DataBaseTable {

    private final DataBaseManager manager;

    protected final String index;

    private final DataBaseValue<?>[] values;

    public DataBaseTable(DataBaseManager manager) {
        this.manager = manager;
        this.index = getIndex();
        this.values = getValues();
    }

    public String initTable() {
        StringBuilder path = new StringBuilder("CREATE TABLE IF NOT EXISTS `" + manager.getTablePrefix() + getName() + "` (`" + index + "` varchar(100) NOT NULL UNIQUE");
        for (DataBaseValue<?> value : values) {
            path.append(", `").append(value.getName()).append("` ").append(value.getValue());
        }
        path.append(");");
        return path.toString();
    }

    public abstract String getName();

    public abstract DataBaseValue<?>[] getValues();

    public abstract String getIndex();

    public void setStringField(String index, final String field, final String data) {
        manager.sqlQueue.add(() -> {
            try {

                PreparedStatement sql1 = manager.connection.prepareStatement("UPDATE `" + manager.getTablePrefix() + getName() + "` SET `" + field + "`=? WHERE `" + this.index + "`=?;");

                sql1.setString(1, data);

                sql1.setString(2, index);

                sql1.executeUpdate();

                sql1.close();

            } catch (Exception e) {

                e.printStackTrace();

            }
        });
    }


    public void setIntegerField(String index, String field, final int data) {
        manager.sqlQueue.add(() -> {
            try {

                PreparedStatement sql1 = manager.connection.prepareStatement("UPDATE `" + manager.getTablePrefix() + getName() + "` SET `" + field + "`=? WHERE `" + this.index + "`=?;");

                sql1.setInt(1, data);

                sql1.setString(2, index);

                sql1.executeUpdate();

                sql1.close();

            } catch (Exception e) {

                e.printStackTrace();

            }
        });
    }

    public void setBooleanField(String index, String field, boolean bool) {
        manager.sqlQueue.add(() -> {
            try {

                PreparedStatement sql1 = manager.connection.prepareStatement("UPDATE `" + manager.getTablePrefix() + getName() + "` SET `" + field + "`=? WHERE `" + this.index + "`=?;");

                sql1.setInt(1, bool ? 1 : 0);

                sql1.setString(2, index);

                sql1.executeUpdate();

                sql1.close();

            } catch (Exception e) {

                e.printStackTrace();

            }
        });
    }

    public void setListField(String index, String field, ArrayList<String> list) {
        manager.sqlQueue.add(() -> {
            try {

                PreparedStatement sql1 = manager.connection.prepareStatement("UPDATE `" + manager.getTablePrefix() + getName() + "` SET `" + field + "`=? WHERE `" + this.index + "`=?;");

                sql1.setString(1, listToString(list));

                sql1.setString(2, index);

                sql1.executeUpdate();

                sql1.close();

            } catch (Exception e) {

                e.printStackTrace();

            }
        });
    }

    public void getIndex(String index, Consumer<ResultSet> consumer) {
        manager.sqlQueue.add(() -> {
            try {
                PreparedStatement sql = manager.connection.prepareStatement("SELECT * FROM `" + manager.getTablePrefix() + getName() + "` WHERE `" + this.index + "`=?;");

                sql.setString(1, index);

                ResultSet rs = sql.executeQuery();

                if (rs.next()) {
                    consumer.accept(rs);
                } else {
                    consumer.accept(null);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void putDefault(String index) {
        manager.sqlQueue.add(() -> {
            try {

                StringBuilder pathString = new StringBuilder("INSERT INTO `" + manager.getTablePrefix() + getName() + "` (`" + this.index + "`");
                StringBuilder valuesString = new StringBuilder(") VALUES('" + index + "'");
                for (DataBaseValue<?> value : values) {
                    pathString.append(", `").append(value.getName()).append("`");
                    valuesString.append(", '").append(value.getDefaultValue()).append("'");
                }

                pathString.append(valuesString).append(");");

                PreparedStatement statement = manager.connection.prepareStatement(pathString.toString());

                statement.executeUpdate();

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
