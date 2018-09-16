package main.models;

public class Note implements TableModel {

    private int noteID;
    private String timeStamp;
    private String note;
    private int entryID;

    public Note(int noteID, String timeStamp, String note, int entryID) {
        this.noteID = noteID;
        this.timeStamp = timeStamp;
        this.note = note;
        this.entryID = entryID;
    }
}
