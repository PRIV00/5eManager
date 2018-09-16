package main.databases;

import main.databases.tables.*;

public class Database {

    private LocationTable locationTable;
    private CharacterTable characterTable;
    private AttackTable attackTable;
    private SkillTable skillTable;
    private CalendarTable calendarTable;
    private NoteTable noteTable;

    public Database(String fileName) {
        locationTable = new LocationTable(fileName);
        characterTable = new CharacterTable(fileName);
        attackTable = new AttackTable(fileName);
        skillTable = new SkillTable(fileName);
        calendarTable = new CalendarTable(fileName);
        noteTable = new NoteTable(fileName);
    }

    public LocationTable getLocationTable() {
        return locationTable;
    }

    public CharacterTable getCharacterTable() {
        return characterTable;
    }

    public AttackTable getAttackTable() {
        return attackTable;
    }

    public SkillTable getSkillTable() {
        return skillTable;
    }

    public CalendarTable getCalendarTable() {
        return calendarTable;
    }

    public NoteTable getNoteTable() {
        return noteTable;
    }
}
