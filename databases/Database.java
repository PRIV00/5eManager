package databases;

import databases.CalendarTable;
import databases.CharacterTable;
import databases.LocationTable;
import databases.NoteTable;

public class Database {

    private LocationTable locationTable;
    private CharacterTable characterTable;
    private CalendarTable calendarTable;
    private NoteTable noteTable;

    public Database(String fileName) {
        locationTable = new LocationTable(fileName);
        characterTable = new CharacterTable(fileName);
        calendarTable = new CalendarTable(fileName);
        noteTable = new NoteTable(fileName);
    }

    public LocationTable getLocationTable() {
        return locationTable;
    }

    public CharacterTable getCharacterTable() {
        return characterTable;
    }

    public CalendarTable getCalendarTable() {
        return calendarTable;
    }

    public NoteTable getNoteTable() {
        return noteTable;
    }
}
