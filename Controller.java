import databases.CharacterTable;
import databases.LocationTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.HTMLEditor;
import models.Character;
import databases.Database;
import models.Location;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private LocationTable locationTable;
    private CharacterTable characterTable;
    private List<Location> masterLocations;
    private List<Character> masterCharacters;
    private ObservableList<Location> filteredLocations;
    private ObservableList<Character> filteredCharacters;
    private ObservableList<Location> childLocations;
    private ObservableList<Character> locationCharacters;

    /* Location Tab Widgets */
    @FXML private TableView<Location> locationTableView = new TableView<>();
    @FXML private TableColumn<Location, String> locNameCol;
    @FXML private TableColumn<Location, String> locTypeCol;
    @FXML private TableColumn<Location, Integer> locIDCol;

    @FXML private TextField locName;
    @FXML private TextField locType;
    @FXML private ComboBox<Location> parentChoice = new ComboBox<>();
    @FXML private HTMLEditor locDesc;
    @FXML private ListView<Location> childLocBox;
    @FXML private ListView<Character> locCharListView;
    @FXML private Button locSaveButton;

    @FXML private TextField locationFilterField;
    @FXML private Label locStatusLabel;

    /* Character Tab Widgets */
    @FXML private TableView<Character> characterTableView = new TableView<>();
    @FXML private TableColumn<Character, Integer> charIDCol;
    @FXML private TableColumn<Character, String> charNameCol;
    @FXML private TableColumn<Character, String> charTitleCol;
    @FXML private TableColumn<Character, String> charLocCol;

    @FXML private TextField charName;
    @FXML private TextField charTitle;
    @FXML private ComboBox<Location> charLocation = new ComboBox<>();
    @FXML private TextField strText;
    @FXML private Label strLabel;
    @FXML private TextField dexText;
    @FXML private Label dexLabel;
    @FXML private TextField conText;
    @FXML private Label conLabel;
    @FXML private TextField intText;
    @FXML private Label intLabel;
    @FXML private TextField wisText;
    @FXML private Label wisLabel;
    @FXML private TextField chaText;
    @FXML private Label chaLabel;
    @FXML private HTMLEditor charDesc;
    @FXML private Button charSaveButton;

    @FXML private TextField characterFilterField;
    @FXML private Label charStatusLabel;

    public Controller() { } /* Constructor for controller must be empty */

    /**
     * Used as a pseudo constructor for the controller to set up necessary variables
     * for the app. First sets up the Location tab, then the Character tab. Includes many event listeners for
     * each control in the tab.
     *
     * @param db the database
     */
    void setup(Database db) {

        this.locationTable = db.getLocationTable();
        this.characterTable = db.getCharacterTable();

        masterLocations = db.getLocationTable().getAllData();
        masterCharacters = db.getCharacterTable().getAllData();
        setParentLocations(masterLocations, masterLocations);
        setCharacterLocations(masterCharacters, masterLocations);
        filteredLocations = FXCollections.observableArrayList(masterLocations);
        filteredCharacters = FXCollections.observableArrayList(masterCharacters);


        /* Initial setup for the Location tab - Setting up the TableView */

        locNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        locTypeCol.setCellValueFactory(new PropertyValueFactory<>("locType"));
        locIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        locationTableView.setItems(filteredLocations);

        updateLocationComboBoxes(new Location(-1, "", "", ""));
        childLocations = FXCollections.observableArrayList(new ArrayList<>());
        childLocBox.setItems(childLocations);

        locationCharacters = FXCollections.observableArrayList(new ArrayList<>());
        locCharListView.setItems(locationCharacters);


        /* Initial setup for the Character Tab */
        charNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        charTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        charLocCol.setCellValueFactory(new PropertyValueFactory<>("locName"));
        charIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        characterTableView.setItems(filteredCharacters);
        charSaveButton.setDisable(true);

        setLocationListeners();
        setCharacterListeners();

        characterTableView.getSelectionModel().selectFirst();
        locationTableView.getSelectionModel().selectFirst();
    }

    /**
     * Sets the listeners for the character tab. Includes listeners for selecting different rows and updating the
     * display, as well as listeners that set the Character's attributes based on key presses, allowing the user to
     * edit multiple rows and save them all at once.
     */
    private void setCharacterListeners() {
        characterTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    Character c = characterTableView.getSelectionModel().getSelectedItem();

                    charName.setDisable(false);
                    charTitle.setDisable(false);
                    charLocation.setDisable(false);
                    strText.setDisable(false);
                    dexText.setDisable(false);
                    conText.setDisable(false);
                    intText.setDisable(false);
                    wisText.setDisable(false);
                    chaText.setDisable(false);
                    charDesc.setDisable(false);
                    try {
                        charName.setText(Main.trim(c.getName()));
                        charTitle.setText(c.getTitle());
                        charDesc.setHtmlText(c.getDescription());
                        charLocation.setValue(c.getLocation());
                        strText.setText(String.valueOf(c.getStats().getStrength(0)));
                        dexText.setText(String.valueOf(c.getStats().getDexterity(0)));
                        conText.setText(String.valueOf(c.getStats().getConstitution(0)));
                        intText.setText(String.valueOf(c.getStats().getIntelligence(0)));
                        wisText.setText(String.valueOf(c.getStats().getWisdom(0)));
                        chaText.setText(String.valueOf(c.getStats().getCharisma(0)));
                        setStatLabels(c);
                        charLocation.setValue(c.getLocation());
                        boolean editPending = false;
                        for (Character c2 : filteredCharacters) {
                            if (c2.isEdited()) {
                                editPending = true;
                            }
                        }
                        if (!editPending) {
                            charSaveButton.setDisable(true);
                        }
                    } catch (NullPointerException e) {
                        charName.setDisable(true);
                        charTitle.setDisable(true);
                        charLocation.setDisable(true);
                        strText.setDisable(true);
                        dexText.setDisable(true);
                        conText.setDisable(true);
                        intText.setDisable(true);
                        wisText.setDisable(true);
                        chaText.setDisable(true);
                        charDesc.setDisable(true);
                        charSaveButton.setDisable(true);
                    }
                });

        charLocation.valueProperty().addListener((observable, oldValue, newValue) -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            try {
                if (c.getLocationID() != newValue.getId()) {
                    c.setLocation(charLocation.getValue());
                    try {
                        c.setLocationID(c.getLocation().getId());
                    } catch (NullPointerException e) {
                        c.setLocationID(0);
                    }
                    c.setName("**" + Main.trim(c.getName()) + "**");
                    c.setEdited(true);
                    characterTableView.refresh();
                }
                charSaveButton.setDisable(false);
            } catch (NullPointerException e) {
                System.out.println("k");
            }
        });

        charName.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            c.setName(charName.getText());
            c.setName("**" + Main.trim(c.getName()) + "**");
            c.setEdited(true);
            characterTableView.refresh();
            charSaveButton.setDisable(false);
        });

        charDesc.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            c.setDescription(charDesc.getHtmlText());
            c.setName("**" + Main.trim(c.getName()) + "**");
            c.setEdited(true);
            characterTableView.refresh();
            charSaveButton.setDisable(false);
        });

        charTitle.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            c.setTitle(charTitle.getText());
            c.setName("**" + Main.trim(c.getName()) + "**");
            c.setEdited(true);
            characterTableView.refresh();
            charSaveButton.setDisable(false);
        });
    }

    /**
     * Sets the Location tab listeners. Basically just put everything under here that only has a single listener to
     * avoid flooding the source code with dozens of methods.
     */
    private void setLocationListeners() {

        locationTableView.getSelectionModel().selectedItemProperty().addListener(event -> {
            Location loc = locationTableView.getSelectionModel().getSelectedItem();
            locType.setDisable(false);
            locName.setDisable(false);
            locDesc.setDisable(false);
            parentChoice.setDisable(false);
            childLocBox.setDisable(false);
            locCharListView.setDisable(false);

            try {
                updateLocationComboBoxes(loc);

                locName.setText(Main.trim(loc.getName()));
                locType.setText(loc.getLocType());
                locDesc.setHtmlText(loc.getDescription());
                childLocBox.getItems().clear();
                locCharListView.getItems().clear();
                parentChoice.setValue(loc.getParent());

                List<Location> x = new ArrayList<>();
                getChildLocations(x, loc);
                childLocations.addAll(x);

                List<Character> y = new ArrayList<>();
                getCharacters(y, loc);
                locationCharacters.addAll(y);
            } catch (NullPointerException e) {
                locName.setDisable(true);
                locType.setDisable(true);
                locDesc.setDisable(true);
                parentChoice.setDisable(true);
                locSaveButton.setDisable(true);
                childLocBox.setDisable(true);
                locCharListView.setDisable(true);
            }
        });

        locName.setOnKeyReleased(event -> {
            Location loc = locationTableView.getSelectionModel().getSelectedItem();
            loc.setName(locName.getText());
            loc.setName("**" + Main.trim(loc.getName()) + "**");
            loc.setEdited(true);
            locationTableView.refresh();
            locSaveButton.setDisable(false);
        });

        locType.setOnKeyReleased(event -> {
            Location loc = locationTableView.getSelectionModel().getSelectedItem();
            loc.setLocType(locType.getText());
            loc.setName("**" + Main.trim(loc.getName()) + "**");
            loc.setEdited(true);
            locationTableView.refresh();
            locSaveButton.setDisable(false);
        });

        locDesc.setOnKeyReleased(event -> {
            Location loc = locationTableView.getSelectionModel().getSelectedItem();
            loc.setDescription(locDesc.getHtmlText());
            loc.setName("**" + Main.trim(loc.getName()) + "**");
            loc.setEdited(true);
            locationTableView.refresh();
            locSaveButton.setDisable(false);
        });

        parentChoice.valueProperty().addListener((observable, oldValue, newValue) -> {
            Location loc = locationTableView.getSelectionModel().getSelectedItem();

            try {
                if (loc.getParentID() != newValue.getId()) {
                    loc.setParent(parentChoice.getValue());
                    try {
                        loc.setParentID(loc.getParent().getId());
                    } catch (NullPointerException e) {
                        loc.setParentID(0);
                    }
                    loc.setName("**" + Main.trim(loc.getName()) + "**");
                    loc.setEdited(true);
                    locationTableView.refresh();
                    locSaveButton.setDisable(false);
                }
            } catch (NullPointerException e) {
                // Catches when the combobox clears
            }
        });
    }

    /**
     * Dynamically sets the labels above the stat entries to reflect the mod bonuses (i.e. STR (+1)).
     *
     * @param c The Character to retrieve the stats from.
     */
    private void setStatLabels(Character c) {
        strLabel.setText(c.getStats().getModText("STR"));
        dexLabel.setText(c.getStats().getModText("DEX"));
        conLabel.setText(c.getStats().getModText("CON"));
        intLabel.setText(c.getStats().getModText("INT"));
        wisLabel.setText(c.getStats().getModText("WIS"));
        chaLabel.setText(c.getStats().getModText("CHA"));
    }

    /**
     * Sets the objects to appropriate parents based on ID.
     *
     * @param locationList : list to set the parents for.
     * @param masterLocationList : master list of Location objects to pull from.
     */
    private void setParentLocations(List<Location> locationList, List<Location> masterLocationList) {
        for (Location x : locationList) {
            for (Location y : masterLocationList) {
                if (x.getParentID() == y.getId()) {
                    x.setParent(y);
                    break;
                }
            }
        }
    }

    /**
     * Sets the Location object references for the master list of Characters. Does this by matching the foreign key
     * ID to the location's ID using for loops.
     *
     * @param characterList : The master models.models.Character list to loop through
     * @param masterLocationList : The master models.models.Location list to loop through
     */
    private void setCharacterLocations(List<Character> characterList,
                                       List<Location> masterLocationList) {
        for (Character c : characterList) {
            for (Location loc : masterLocationList) {
                if (c.getLocationID() == loc.getId()) {
                    c.setLocation(loc);
                    break;
                }
            }
        }
    }

    /**
     * Functions updates the comboboxes on both the Location and Characters tab. Takes a location object to avoid
     * possibility of setting a Location's parent object to itself and starting an infinite loop.
     *
     * @param location : Location object to avoid adding to the combo box.
     */
    private void updateLocationComboBoxes(Location location) {
        parentChoice.getItems().clear();
        charLocation.getItems().clear();

        Location blank = new Location(0, "", "", "") {
            @Override
            public String toString() {
                return "";
            }
        };

        parentChoice.getItems().add(blank);
        charLocation.getItems().add(blank);

        List<Integer> invalidIDs = invalidParents(location);

        for (Location loc : masterLocations) {
            if (location != null && loc.getId() != location.getId() && !(invalidIDs.contains(loc.getId()))) {
                parentChoice.getItems().add(loc);
            }
            charLocation.getItems().add(loc);
        }
        try {
            assert location != null;
            parentChoice.valueProperty().set(location.getParent());
        } catch (NullPointerException e) {
            //
        }
    }

    /**
     * Used by updateLocationComboBoxes to fill the combobox in the Locations tab with locations that won't cause
     * an infinite loop. This essentially means any sub locations can't be assigned as the top location's parent.
     *
     * @param topLocation The location to check for invalid parents
     * @return return the list of invalid IDs
     */
    private List<Integer> invalidParents(Location topLocation) {
        List<Integer> invalidIDs = new ArrayList<>();

        List<Location> childTreeList = new ArrayList<>();
        getChildLocations(childTreeList, topLocation);

        for (Location loc : childTreeList) {
            invalidIDs.add(loc.getId());
        }

        return invalidIDs;
    }

    /**
     * Recursive method to fetch the hierarchy of Location objects allowing to add all sub locations to a
     * list based on a chosen anchor object - In this case, will always be the selected TableView location.
     *
     * Loops through the master list and adds the current location to the recursive list if the current location's
     * parentID matches the anchor's ID.
     *
     * If a location in the master list has the same parentID as the anchor location, it adds it to the recursive
     * list and then it calls the function on itself, setting that current location as the anchor.
     * Continues until locations have no more parentIDs remaining.
     *
     * Throws a null pointer exception in case it receives a null anchorLocation
     *
     * @param recursiveList : ArrayList to add to
     * @param anchorLocation : Base location to start the recursion. This variable is set each time it calls itself.
     */
    private void getChildLocations(List<Location> recursiveList, Location anchorLocation) throws NullPointerException {
        try {
            for (Location loc : masterLocations) {
                if (loc.getParentID() == anchorLocation.getId()) {
                    recursiveList.add(loc);
                    getChildLocations(recursiveList, loc);
                }
            }
        } catch (NullPointerException e) {
            System.out.println("NullPointerException");
        }
    }

    /**
     * Recursive method to get all Characters within a Location and any Characters in that Location's children.
     * This way when a specific location is searched, it will display all Characters within that location's hierarchy.
     *
     * @param recursiveList : Recursive list to add characters to.
     * @param anchorLocation : Base location to start the recursion.
     */
    private void getCharacters(List<Character> recursiveList, Location anchorLocation) {
        for (Character c : masterCharacters) {
            if (c.getLocationID() == anchorLocation.getId()) {
                recursiveList.add(c);
            }
        }
        for (Location loc : masterLocations) {
            if (loc.getParentID() == anchorLocation.getId()) {
                getCharacters(recursiveList, loc);
            }
        }
    }

    /* --------------------Character tab FXML methods-------------------- */

    /**
     * This is staying as a method, since I am having multiple controls reference this method to listen for changes.
     * Otherwise I would have to override each individual control listener in the source code.
     */
    @FXML
    private void charStatsListener() {
        Character c = characterTableView.getSelectionModel().getSelectedItem();
        try {
            c.getStats().setStrength(Integer.parseInt(strText.getText()));
            c.getStats().setDexterity(Integer.parseInt(dexText.getText()));
            c.getStats().setConstitution(Integer.parseInt(conText.getText()));
            c.getStats().setIntelligence(Integer.parseInt(intText.getText()));
            c.getStats().setWisdom(Integer.parseInt(wisText.getText()));
            c.getStats().setCharisma(Integer.parseInt(chaText.getText()));
            setStatLabels(c);
        } catch (NumberFormatException e) {
            // Ignore for now. Catch it if it tries to save later.
        }

        c.setName("**" + Main.trim(c.getName()) + "**");
        c.setEdited(true);
        characterTableView.refresh();
        charSaveButton.setDisable(false);
    }

    @FXML
    private void insertCharacter() {
        Character c = new Character(characterTable.getNextID(), "", "",
                new int[] { 10, 10, 10, 10, 10, 10 }, 0, "<html dir=\"ltr\"><head></head><body contenteditable=\"true\">" +
                "<p><font face=\"Cambria\" size=\"4\"><b>DESCRIPTION</b></font></p><hr><p><font face=\"Cambria\">&nbsp;" +
                "</font></p><div><font face=\"Cambria\" size=\"4\"><b>LOOK</b></font></div><div><hr></div><div><p></p><p" +
                "></p></div><p></p><div></div><p></p><p><font face=\"Cambria\">&nbsp;</font></p><p><font face=\"Cambria" +
                "\" size=\"4\"><b>VOICE</b></font></p><hr><p><font face=\"Cambria\">&nbsp;</font></p><p><font size=\"4\"" +
                " face=\"Cambria\"><b>PERSONALITY</b></font></p><p></p><p></p><hr><p><font face=\"Cambria\"><i>Ideals:</" +
                "i>&nbsp;</font></p><p></p><p></p><p></p><p><font face=\"Cambria\"><i>Desires:</i></font></p><p><font fac" +
                "e=\"Cambria\"><i>Fears:</i></font></p><p></p><p><font size=\"4\" face=\"Cambria\"><b>KNOWLEDGE/SKILLS</b" +
                "></font></p><p></p><hr><p><font face=\"Cambria\">&nbsp;</font></p><p></p><p><b style=\"font-family: Camb" +
                "ria;\"><font size=\"4\">COMBAT</font></b></p><p></p><p></p><hr><p></p><p></p><p><i style=\"font-family: " +
                "Cambria;\">AC:</i><span style=\"font-family: Cambria;\">&nbsp;</span><span class=\"Apple-tab-span\" style=" +
                "\"font-family: Cambria; white-space: pre;\">\t\t</span><i style=\"font-family: Cambria;\">HP:</i><span styl" +
                "e=\"font-family: Cambria;\">&nbsp;/</span><span class=\"Apple-tab-span\" style=\"font-family: Cambria; whit" +
                "e-space: pre;\">\t\t</span><i style=\"font-family: Cambria;\">SPD:</i><span style=\"font-family: Cambria;\"" +
                ">&nbsp; ft.</span><span class=\"Apple-tab-span\" style=\"font-family: Cambria; white-space: pre;\">\t\t</" +
                "span><i style=\"font-family: Cambria;\">Proficiency:</i><span style=\"font-family: Cambria;\">&nbsp;+</s" +
                "pan></p><p><font face=\"Cambria\" size=\"4\"><b>PLAYER INTERACTIONS</b></font></p><hr><p></p><p></p><p><" +
                "font face=\"Cambria\">&nbsp;</font></p><p></p><p></p>\n" +
                "\n</body></html>");

        try {
            characterTable.insertData(c);
            masterCharacters.add(c);
            filteredCharacters.add(c);
            characterTableView.getSelectionModel().select(c);
            charStatusLabel.setText("New character created. " + Main.getCurrentTime());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void saveCharacters() {
        for (Character c : filteredCharacters) {
            if (c.isEdited()) {
                try {
                    c.setLocationID(c.getLocation().getId());
                } catch (NullPointerException e) {
                    c.setLocationID(0);
                } finally {
                    c.setName(Main.trim(c.getName()));  // The name currently has asterisks, saving trims them off.
                }

                try {
                    characterTable.updateData(c);
                    charStatusLabel.setText("Characters saved successfully. " + Main.getCurrentTime());
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                c.setEdited(false);
            }
        }

        charSaveButton.setDisable(true);
        characterTableView.refresh();
    }

    @FXML
    private void deleteCharacter() {
        Character c = characterTableView.getSelectionModel().getSelectedItem();
        try {
            characterTable.deleteData(c);
        } catch (SQLException e) {
            charStatusLabel.setText("Error deleting " + c.getName() + " " + Main.getCurrentTime());
        }
        masterCharacters.remove(c);
        filteredCharacters.remove(c);
        charStatusLabel.setText(c.getName() + " deleted successfully. " + Main.getCurrentTime());
    }

    @FXML
    private void searchCharacters() {
        filteredCharacters.clear();
        filteredCharacters.addAll(characterTable.query(characterFilterField.getText()));
        for (Character c : filteredCharacters) {
            for (Location i : masterLocations) {
                if (c.getLocationID() == i.getId()) {
                    c.setLocation(i);
                    break;
                }
            }
        }
    }

    /* --------------------Location tab FXML methods-------------------- */


    @FXML
    private void saveLocations() {
        for (Location loc : filteredLocations) {
            if (loc.isEdited()) {
                loc.setName(Main.trim(loc.getName()));
                try {
                    locationTable.updateData(loc);
                    locStatusLabel.setText("Locations saved successfully " + Main.getCurrentTime());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                loc.setEdited(false);
            }
        }

        locSaveButton.setDisable(true);
        locationTableView.refresh();
    }

    /**
     * Simple method to add a new line in the Location TableView. Basically the idea is to create a new Location object, which adds a row
     * to the tableview. The object has all empty values except for the ID, which is generated using the 'getNextID()' database method.
     * This is, as the name suggests, is the latest entry's id + 1.
     *
     * User can then edit the information and hit save. It will call the saveLocations() method, fail updating a pre-existing
     * location (as there is not yet any row with that ID) and then proceed to insert the location as a new entry.
     */
    @FXML
    private void insertLocation() {
        Location loc = new Location(locationTable.getNextID(), "", "<html dir=\"ltr\"><head></head><body contenteditab" +
                "le=\"true\"><p><b><font size=\"4\" face=\"Cambria\">DESCRIPTION</font></b></p><hr><p><font face=\"Cam" +
                "bria\">", "");
        try {
            locationTable.insertData(loc);
            masterLocations.add(loc);
            filteredLocations.add(loc);
            locationTableView.getSelectionModel().select(loc);
            locStatusLabel.setText(loc.getName() + "New location added. " + Main.getCurrentTime());
        } catch (SQLException e) {
            e.printStackTrace();
            locStatusLabel.setText("Error adding new location entry. " + Main.getCurrentTime());
        }
    }

    @FXML
    private void deleteLocation() {
        Location loc = locationTableView.getSelectionModel().getSelectedItem();
        try {
            locationTable.deleteData(loc);
            locStatusLabel.setText(loc.getName() + " deleted successfully. " + Main.getCurrentTime());
        } catch (SQLException e) {
            e.printStackTrace();
            locStatusLabel.setText("Error deleting " + loc.getName() + " " + Main.getCurrentTime());
        }
        for (Location i : masterLocations) {
            if (i.getId() == loc.getId()) {
                masterLocations.remove(i);
                break;
            }
        }
        filteredLocations.remove(loc);
    }

    @FXML
    private void searchLocations() {
        filteredLocations.clear();
        filteredLocations.addAll(locationTable.query(locationFilterField.getText()));
        for (Location loc : filteredLocations) {
            for (Location i : masterLocations) {
                if (loc.getParentID() == i.getId()) {
                    loc.setParent(i);
                    break;
                }
            }
        }
    }
}
