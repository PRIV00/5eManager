package main.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public class CalendarDate implements TableModel {

    private int dateID;
    private SimpleIntegerProperty dayNum;
    private SimpleStringProperty dayName;
    private SimpleIntegerProperty monthNum;
    private SimpleStringProperty monthName;
    private SimpleIntegerProperty year;
    private List<Note> notes;

    public CalendarDate(int dateID, int dayNum, String dayName, int monthNum, String monthName, int year, List<Note> notes) {
        this.dateID = dateID;
        this.dayNum = new SimpleIntegerProperty(dayNum);
        this.dayName = new SimpleStringProperty(dayName);
        this.monthNum = new SimpleIntegerProperty(monthNum);
        this.monthName = new SimpleStringProperty(monthName);
        this.year = new SimpleIntegerProperty(year);
        this.notes = notes;
    }

    public void setId(int dateID) {
        this.dateID = dateID;
    }

    public int getDateID() {
        return dateID;
    }

    public int getDayNum() {
        return dayNum.get();
    }

    public String getDayName() {
        return dayName.get();
    }

    public int getMonthNum() {
        return monthNum.get();
    }

    public String getMonthName() {
        return monthName.get();
    }

    public int getYear() {
        return year.get();
    }

    public List<Note> getNotes() {
        return notes;
    }
}
