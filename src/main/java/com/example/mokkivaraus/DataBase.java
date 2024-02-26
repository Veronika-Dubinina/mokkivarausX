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
     * Returns result of executed SQL query. For SELECT statement
     * @param sql SQL Query
     * @return returns the ResultSet object
     */
    public ResultSet getData(String sql) {
        ResultSet res = null;
        try (Connection conn = getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             // Execute query
             ResultSet result = ps.executeQuery();){
                res = result;
        } catch (Exception ex) {
            System.out.println("!!Exception DataBase.getData:" + ex);
        }

        return res;
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
     * @param <TableClass>
     */
    public <TableClass> ObservableList<TableClass> getAllRows(String tableName, String orderByColumn, Class<TableClass> tableClass) {
        // Sql query
        String sql = "SELECT * FROM " + tableName + " ORDER BY " + orderByColumn;
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
        String sql = "SELECT * FROM " + tableName + " WHERE " + identifierKey + " = " + identifierValue;
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
    public void addRow(String tableName, HashMap<String, Object> values) {
        // Check if values is empty
        if (values.isEmpty()) {
            return;
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
        } catch (Exception ex) {
            System.out.println("!!Exception DataBase.addRow : " + ex);
        }
    }

    /**
     * Updates row in table with values where idKey equals idValue
     * @param tableName Name of the table
     * @param values New row values
     * @param identifierKey Row identifier
     * @param identifierValue Row identifier value
     */
    public void updateRow(String tableName, HashMap<String, Object> values, String identifierKey, Object identifierValue) {
        // Sql query
        List<String> keyList = new ArrayList<String>(values.keySet());;
        String columns = keyList.stream().collect(Collectors.joining("=?, ", "", "=?"));
        String sql = "UPDATE " + tableName + " SET " + columns + " WHERE " + identifierKey + "=" + identifierValue;

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
        } catch (Exception ex) {
            System.out.println("!!Exception DataBase.updateRow : " + ex);
        }

    }

    /**
     * Removes row in table where idKey equals idValue
     * @param tableName Name of the table
     * @param identifierKey Row identifier
     * @param identifierValue Row identifier value
     */
    public void deleteRow(String tableName, String identifierKey, Object identifierValue) {
        // Remove row in mokki-table
        String sql = "DELETE FROM " + tableName + " WHERE " + identifierKey + " = " + identifierValue;

        try (Connection conn = getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("!!Exception DataBase.deleteRow : " + ex);
        }
    }

}
