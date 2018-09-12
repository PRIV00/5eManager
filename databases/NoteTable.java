package databases;

import java.util.ArrayList;
import java.util.List;

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

    public List<String> getAllData() {
        return new ArrayList<String>();
    }
}
