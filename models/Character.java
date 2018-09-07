package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Character implements DataModel {

    private boolean edited = false;

    private SimpleIntegerProperty id;
    private int locationID;
    private SimpleStringProperty name;
    private SimpleStringProperty title;
    private Abilities stats;
    private Location location;
    private SimpleStringProperty description;
    private SimpleStringProperty locName;

    public Character(String name, String title, int[] s, String description) {
        this.name = new SimpleStringProperty(name);
        this.title = new SimpleStringProperty(title);
        this.stats = new Abilities(s[0], s[1], s[2], s[3], s[4], s[5]);
        this.description = new SimpleStringProperty(description);
    }

    public Character(String name, String title, int[] s, int locationID, String description) {
        this.name = new SimpleStringProperty(name);
        this.title = new SimpleStringProperty(title);
        this.stats = new Abilities(s[0], s[1], s[2], s[3], s[4], s[5]);
        this.locationID = locationID;
        this.description = new SimpleStringProperty(description);
    }


    public Character(int id, String name, String title, int[] s, int locationID, String description) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.title = new SimpleStringProperty(title);
        this.stats = new Abilities(s[0], s[1], s[2], s[3], s[4], s[5]);
        this.locationID = locationID;
        this.description = new SimpleStringProperty(description);
    }

    public String getAllDetails() {
        return String.format("charID: %d\nname: %s\ntitle: %s\nlocation: %s\nlocationID: %d\n",
        id.get(), name.get(), title.get(), location.getName(), locationID);
    }

    @Override
    public String toString() {
        return name.get();
    }


    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean status) {
        edited = status;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public Abilities getStats() {
        return stats;
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocName(String locName) {
        this.locName.set(locName);
    }

    public String getLocName() {
        try {
            return location.getName();
        } catch (NullPointerException e) {
            return "";
        }
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }


}
