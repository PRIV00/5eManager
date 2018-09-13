package main.databases;

import main.models.DataModel;

import java.sql.SQLException;

public interface Modifiable {
    void insertData(DataModel data) throws SQLException;
    void deleteData(DataModel data) throws SQLException;
    void updateData(DataModel data) throws SQLException;
    int getNextID();
}
