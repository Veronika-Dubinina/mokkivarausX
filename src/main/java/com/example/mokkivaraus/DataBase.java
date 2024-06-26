package com.example.mokkivaraus;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.lang.reflect.Constructor;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;


// Database
public class DataBase {
    // Attributes
    private final String HOST = "mokki-project.mysql.database.azure.com";
    private final String PORT = "3306";
    private final String DB_NAME = "vn";
    private final String USER = "dbadmin";
    private final String PASS = "Kuopio1234";

    // Methods
    /**
     * Creates a vn-database connection.
     * @return database connection
     */
    private Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionUrl = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME;
        return DriverManager.getConnection(connectionUrl, USER, PASS);
    }

    /**
     * To do with result set
     * @param resultSet ResultSet object
     */
    public void toDoResultSet(ResultSet resultSet) {}

    /**
     * Returns result of executed SQL query and works with it
     * @param sql SQL Query
     */
    public boolean getData(String sql) {
        ResultSet res = null;
        try (Connection conn = getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             // Execute query
             ResultSet result = ps.executeQuery();){
                toDoResultSet(result);
                return true;
        } catch (Exception ex) {
            System.out.println("!!Exception DataBase.getData:" + ex);
            return false;
        }
    }

    /**
     * Executes the SQL query. For statement, such as INSERT, UPDATE or DELETE;
     * @param sql SQL Query
     */
    public void setData(String sql) {
        try (Connection conn = getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql);){
            // Execute query
            ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("!!Exception DataBase.setData : " + ex);
        }
    }

    /**
     * Returns data from the database table packed into a list.
     * @param tableName Name of the table
     * @param orderByColumn Column by which the table is ordered
     * @param tableClass Class representing the table row
     * @return ObservableList of TableClass-objects
     *
     */
    public <TableClass> ObservableList<TableClass> getAllRows(String tableName, String orderByColumn, Class<TableClass> tableClass) {
        return getAllRows(tableName, orderByColumn, tableClass, "");
    };

    /**
     * Returns data from the database table packed into a list.
     * @param tableName Name of the table
     * @param orderByColumn Column by which the table is ordered
     * @param tableClass Class representing the table row
     * @param filter Selection filter (WHERE)
     * @return ObservableList of TableClass-objects
     *
     */
    public <TableClass> ObservableList<TableClass> getAllRows(String tableName, String orderByColumn, Class<TableClass> tableClass, String filter) {
        // Sql query
        String sql = "SELECT * FROM " + tableName + " ";
        // Add filter
        if (!filter.isEmpty()) {
            sql += filter + " ";
        }
        // Order by
        sql += "ORDER BY " + orderByColumn;

        // List of TableClass-objects
        ObservableList<TableClass> tableClassObservableList = FXCollections.observableArrayList();
        // List of TableClass constructors
        Constructor[] constructors = tableClass.getConstructors();

        try (Connection conn = getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             // Execute query
             ResultSet res = ps.executeQuery();){
            // Go throw rows
            while (res.next()) {
                // Create new TableClass-object with row values
                TableClass obj = (TableClass) constructors[1].newInstance(res);
                // Add TableClass-object to list
                tableClassObservableList.add(obj);
            }
        } catch (Exception ex) {
            System.out.println("!!Exception DataBase.getAllRows:" + ex);
        }

        return tableClassObservableList;
    }

    /**
     * Returns row from the database where idKey equals idValue packed into object of TableClass
     * @param tableName Noame of the table
     * @param identifierKey Row identifier
     * @param identifierValue Row identifier value
     * @param tableClass Class representing the table row
     * @return TableClass-objects
     * @param <TableClass>
     */
    public <TableClass> TableClass getRow(String tableName, String identifierKey, Object identifierValue, Class<TableClass> tableClass) {
        // Sql query
        String filter = "WHERE " + identifierKey + " = " + identifierValue;
        return getRow(tableName, filter, tableClass);
    }

    public <TableClass> TableClass getRow(String tableName, String filter, Class<TableClass> tableClass) {
        // Sql query
        String sql = "SELECT * FROM " + tableName + " " + filter;
        // TableClass-object
        TableClass obj = null;
        // List of TableClass constructors
        Constructor[] constructors = tableClass.getConstructors();

        try (Connection conn = getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             // Execute query
             ResultSet res = ps.executeQuery();){
            // Go throw rows
            while (res.next()) {
                // Create new TableClass-object with row values
                obj = (TableClass) constructors[1].newInstance(res);
            }
        } catch (Exception ex) {
            System.out.println("!!Exception DataBase.getAllRows:" + ex);
        }
        
        return obj;
    }

    /**
     * Adds new row into table with values
     * @param tableName Name of the Table
     * @param values HasMap of row values
     */
    public boolean addRow(String tableName, HashMap<String, Object> values) {
        // Check if values is empty
        if (values.isEmpty()) {
            return false;
        }

        // Sql query
        List<String> keyList = new ArrayList<String>(values.keySet());;
        String columns = keyList.stream().collect(Collectors.joining(", ", " (", ") "));
        String sql_columns = "INSERT INTO " + tableName + columns;
        String sql_values = "VALUES (" + "?, ".repeat(values.size() - 1) + "?)";

        try (Connection conn = getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql_columns + sql_values);){
            // Replace ? with values
            int i = 1;
            for (Object value : values.values()) {
                ps.setObject(i, value);
                i++;
            }
            // Execute query
            ps.executeUpdate();
            return true;
        } catch (Exception ex) {
            System.out.println("!!Exception DataBase.addRow : " + ex);
            return false;
        }
    }

    /**
     * Updates row in table with values where idKey equals idValue
     * @param tableName Name of the table
     * @param values New row values
     * @param identifierKey Row identifier
     * @param identifierValue Row identifier value
     */
    public boolean updateRow(String tableName, HashMap<String, Object> values, String identifierKey, Object identifierValue) {
        String filter = "WHERE " + identifierKey + "=" + identifierValue;
        return updateRow(tableName, values, filter);
    }

    public boolean updateRow(String tableName, HashMap<String, Object> values, String filter) {
        // Sql query
        List<String> keyList = new ArrayList<String>(values.keySet());;
        String columns = keyList.stream().collect(Collectors.joining("=?, ", "", "=?"));
        String sql = "UPDATE " + tableName + " SET " + columns + " " + filter;

        try (Connection conn = getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql);){
            // Replace ? with values
            int i = 1;
            for (Object value : values.values()) {
                ps.setObject(i, value);
                i++;
            }
            // Execute query
            ps.executeUpdate();
            return true;
        } catch (Exception ex) {
            System.out.println("!!Exception DataBase.updateRow : " + ex);
            return false;
        }

    }

    /**
     * Removes row in table where idKey equals idValue
     * @param tableName Name of the table
     * @param identifierKey Row identifier
     * @param identifierValue Row identifier value
     */
    public boolean deleteRow(String tableName, String identifierKey, Object identifierValue) {
        String filter = "WHERE " + identifierKey + " = " + identifierValue;
        return deleteRow(tableName, filter);
    }

    public boolean deleteRow(String tableName, String filter) {
        // Remove row in mokki-table
        String sql = "DELETE FROM " + tableName + " " + filter;

        try (Connection conn = getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.executeUpdate();
            return true;
        } catch (Exception ex) {
            System.out.println("!!Exception DataBase.deleteRow : " + ex);
            return false;
        }
    }

}
