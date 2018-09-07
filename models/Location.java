package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Location implements DataModel {

    private boolean edited = false;

    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty description;
    private SimpleStringProperty locType;
    private Location parent;
    private int parentID;

    public Location(int id, String name, String description, String locType) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.locType = new SimpleStringProperty(locType);
    }

    public Location(int id, String name, String description, String locType, int parentID) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.locType = new SimpleStringProperty(locType);
        this.parentID = parentID;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name.get(), locType.get());
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getLocType() {
        return locType.get();
    }

    public void setLocType(String locType) {
        this.locType.set(locType);
    }

    public Location getParent() {
        return parent;
    }

    public void setParent(Location parent) {
        this.parent = parent;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }
}
