package main.databases.tables;

import main.model.modeldata.ModelData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

abstract class Table {

    private String url;
    private String sqlCreateTableStatement;
    Connection conn;
    Statement stmt;

    Table(String fileName, String sqlCreateTableStatement) {
        url = "jdbc:sqlite:C:/Users/Bryn/IdeaProjects/5eManagerV2/" + fileName + ".db";
        this.sqlCreateTableStatement = sqlCreateTableStatement;

        connect();
        setUpTables();
        closeConnection();
    }

    void connect() {
        try {
            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setUpTables() {
        try {
            stmt.execute("PRAGMA foreign_keys = ON");
            stmt.execute(sqlCreateTableStatement);
        } catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            closeConnection();
        }
    }

    public abstract void insertData(ModelData data) throws SQLException;
    public abstract void deleteData(ModelData data) throws SQLException;
    public abstract void updateData(ModelData data) throws SQLException;
    public abstract int getNextID();
}