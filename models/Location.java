package main.models;


public class Location implements DataModel {

    private boolean edited = false;

    private int id;
    private String name;
    private String locType;
    private String flavour;
    private String songLink;
    private String description;
    private String culture;
    private String government;
    private String crime;
    private String demographic;
    private String religion;
    private String history;
    private Location parent;
    private String parentName; //Used for Location tableView column
    private int parentID;

    public Location() {
        id = 0;
        name = "";
        locType = "";
        flavour = "";
        songLink = "";
        description = "";
        culture = "";
        government = "";
        crime = "";
        demographic = "";
        religion = "";
        history = "";
        parentID = 0;
    }

    public Location(int id, String name, String locType, String flavour, String songLink, String description, String culture,
                    String government, String crime, String demographic, String religion, String history, int parentID) {
        this.id = id;
        this.name = name;
        this.locType = locType;
        this.flavour = flavour;
        this.songLink = songLink;
        this.culture = culture;
        this.government = government;
        this.crime = crime;
        this.demographic = demographic;
        this.religion = religion;
        this.history = history;
        this.description = description;
        this.parentID = parentID;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, locType);
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocType() {
        return locType;
    }

    public void setLocType(String locType) {
        this.locType = locType;
    }

    public String getFlavour() {
        return flavour;
    }

    public void setFlavour(String flavour) {
        this.flavour = flavour;
    }

    public String getSongLink() {
        return songLink;
    }

    public void setSongLink(String songLink) {
        this.songLink = songLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCulture() {
        return culture;
    }

    public void setCulture(String culture) {
        this.culture = culture;
    }

    public String getGovernment() {
        return government;
    }

    public void setGovernment(String government) {
        this.government = government;
    }

    public String getCrime() {
        return crime;
    }

    public void setCrime(String crime) {
        this.crime = crime;
    }

    public String getDemographic() {
        return demographic;
    }

    public void setDemographic(String demographic) {
        this.demographic = demographic;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public Location getParent() {
        return parent;
    }

    public void setParent(Location parent) {
        this.parent = parent;
    }

    public String getParentName() {
        try {
            return parent.getName();
        } catch (NullPointerException e) {
            return "";
        }
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }
}

/**
package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Location implements DataModel {

    private boolean edited = false;

    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty locType;
    private SimpleStringProperty flavour;
    private SimpleStringProperty songLink;
    private SimpleStringProperty description;
    private SimpleStringProperty culture;
    private SimpleStringProperty government;
    private SimpleStringProperty crime;
    private SimpleStringProperty demographic;
    private SimpleStringProperty religion;
    private SimpleStringProperty history;
    private Location parent;
    private int parentID;

    public Location() {
        id = new SimpleIntegerProperty(0);
        name = new SimpleStringProperty("");
        locType = new SimpleStringProperty("");
        flavour = new SimpleStringProperty("");
        songLink = new SimpleStringProperty("");
        description = new SimpleStringProperty("");
        culture = new SimpleStringProperty("");
        government = new SimpleStringProperty("");
        crime = new SimpleStringProperty("");
        demographic = new SimpleStringProperty("");
        religion = new SimpleStringProperty("");
        history = new SimpleStringProperty("");
        parentID = 0;
    }

    public Location(int id, String name, String locType, String flavour, String songLink, String description, String culture,
                    String government, String crime, String demographic, String religion, String history, int parentID) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.locType = new SimpleStringProperty(locType);
        this.flavour = new SimpleStringProperty(flavour);
        this.songLink = new SimpleStringProperty(songLink);
        this.culture = new SimpleStringProperty(culture);
        this.government = new SimpleStringProperty(government);
        this.crime = new SimpleStringProperty(crime);
        this.demographic = new SimpleStringProperty(demographic);
        this.religion = new SimpleStringProperty(religion);
        this.history = new SimpleStringProperty(history);
        this.description = new SimpleStringProperty(description);
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

    public String getFlavour() {
        return flavour.get();
    }

    public void setFlavour(String flavour) {
        this.flavour.set(flavour);
    }

    public String getSongLink() {
        return songLink.get();
    }

    public void setSongLink(String songLink) {
        this.songLink.set(songLink);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getCulture() {
        return culture.get();
    }

    public void setCulture(String culture) {
        this.culture.set(culture);
    }

    public String getGovernment() {
        return government.get();
    }

    public void setGovernment(String government) {
        this.government.set(government);
    }

    public String getCrime() {
        return crime.get();
    }

    public void setCrime(String crime) {
        this.crime.set(crime);
    }

    public String getDemographic() {
        return demographic.get();
    }

    public void setDemographic(String demographic) {
        this.demographic.set(demographic);
    }

    public String getReligion() {
        return religion.get();
    }

    public void setReligion(String religion) {
        this.religion.set(religion);
    }

    public String getHistory() {
        return history.get();
    }

    public void setHistory(String history) {
        this.history.set(history);
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
}
**/