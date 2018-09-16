package main.databases.tables;

import main.models.TableModel;


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
    public void insertData(TableModel note) {

    }

    @Override
    public void deleteData(TableModel note) {

    }

    @Override
    public void updateData(TableModel note) {

    }

    @Override
    public int getNextID() {
        return 0;
    }
}
