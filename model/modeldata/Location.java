package main.model.modeldata;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Location implements ModelData {

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

    private ObservableList<Location> childLocations;
    private ObservableList<Character> characters;

    private List<Integer> invalidParentIDs;

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

        childLocations = FXCollections.observableArrayList(new ArrayList<>());
        characters = FXCollections.observableArrayList(new ArrayList<>());

        invalidParentIDs = new ArrayList<>();
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

        childLocations = FXCollections.observableArrayList(new ArrayList<>());
        characters = FXCollections.observableArrayList(new ArrayList<>());

        invalidParentIDs = new ArrayList<>();
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

    public ObservableList<Location> getChildLocations() {
        return childLocations;
    }

    public ObservableList<Character> getCharacters() {
        return characters;
    }

    public List<Integer> getInvalidParentIDs() {
        return invalidParentIDs;
    }

    public void setInvalidParentIDs(List<Integer> invalidParentIDs) {
        this.invalidParentIDs = invalidParentIDs;
    }
}
