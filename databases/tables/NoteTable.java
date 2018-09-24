package main.databases.tables;

import main.model.modeldata.ModelData;


public class NoteTable extends Table {

    public NoteTable(String fileName) {
        super(fileName,
                "CREATE TABLE IF NOT EXISTS Notes \n" +
                        "(\n" +
                        "noteID INTEGER PRIMARY KEY, \n" +
                        "time TIMESTAMP, \n" +
                        "note TEXT, \n" +
                        "dateID INTEGER, \n" +
                        "FOREIGN KEY (dateID) REFERENCES FantasyCalendar(dateID) \n" +
                        ")");
    }

    @Override
    public void insertData(ModelData note) {

    }

    @Override
    public void deleteData(ModelData note) {

    }

    @Override
    public void updateData(ModelData note) {

    }

    @Override
    public int getNextID() {
        return 0;
    }
}
