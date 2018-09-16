package main.controllers;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.util.converter.IntegerStringConverter;
import main.assets.GuiTools;
import main.databases.tables.AttackTable;
import main.databases.tables.CharacterTable;
import main.databases.Database;
import main.databases.tables.LocationTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import main.databases.tables.SkillTable;
import main.models.Attack;
import main.models.Character;
import main.models.Location;
import main.models.Skill;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseController {

    /* ------------------------------ MODEL FIELDS ------------------------------ */
    //TODO: field for selectedCharacter and selectedLocation to update only on tableview select. Will result in a lot less code for listeners.


    private LocationTable locationTable;
    private List<Location> masterLocations;
    private ObservableList<Location> filteredLocations;
    private ObservableList<Location> childLocations = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<Character> locationCharacters = FXCollections.observableArrayList(new ArrayList<>());
    private Location observedLocation;

    private CharacterTable characterTable;
    private List<Character> masterCharacters;
    private ObservableList<Character> filteredCharacters;
    private Character observedCharacter;

    private AttackTable attackTable;
    private ObservableList<Attack> observedCharacterAttacks = FXCollections.observableArrayList();

    private SkillTable skillTable;
    private ObservableList<Skill> observedCharacterSkills = FXCollections.observableArrayList();

    /* ------------------------------ TOOLBAR CONTROLS ------------------------------ */
    private Button saveAllButton = new Button("Save All"); // Not an FXML control as it needs to be added dynamically.

    /* ------------------------------ LOCATION TAB CONTROLS ------------------------------ */

    @FXML private TextField locationFilterTextField;
    @FXML private Button locationAddButton;
    @FXML private Button locationRemoveButton;

    /* Location TableView and Left display */
    @FXML private TableView<Location> locationTableView = new TableView<>();
    @FXML private TableColumn<Location, Integer> locIDCol;
    @FXML private TableColumn<Location, String> locNameCol;
    @FXML private TableColumn<Location, String> locTypeCol;
    @FXML private TableColumn<Location, String> locParentCol;

    @FXML private Label locNameLabel;
    @FXML private Label locTypeLabel;
    @FXML private Label locParentNameLabel;
    @FXML private Label locFlavourLabel;

    @FXML private ListView<Location> childLocListView;
    @FXML private ListView<Character> locCharListView;

    /* Location Basic Info Entry */
    @FXML private TextField locNameTextField;
    @FXML private TextField locTypeTextField;
    @FXML private ComboBox<Location> locParentChoiceComboBox = new ComboBox<>();
    @FXML private TextArea locFlavourTextField;

    /* Location Other Info Entry*/
    @FXML private TextField locSongLinkTextField;
    @FXML private TextArea locDescriptionTextArea;
    @FXML private TextArea locCultureTextArea;
    @FXML private TextArea locGovernmentTextArea;
    @FXML private TextArea locCrimeTextArea;
    @FXML private TextArea locDemographicTextArea;
    @FXML private TextArea locReligionTextArea;
    @FXML private TextArea locHistoryTextArea;

    /* ------------------------------ CHARACTER TAB CONTROLS ------------------------------ */

    @FXML private TextField characterFilterTextField;
    @FXML private Button characterAddButton;
    @FXML private Button characterRemoveButton;

    /* Character TableView and Left display */
    @FXML private TableView<Character> characterTableView = new TableView<>();
    @FXML private TableColumn<Character, Integer> charIDCol;
    @FXML private TableColumn<Character, String> charNameCol;
    @FXML private TableColumn<Character, String> charRaceCol;
    @FXML private TableColumn<Character, String> charTitleCol;
    @FXML private TableColumn<Character, String> charLocCol;

    @FXML private Label charNameLabel;
    @FXML private Label charTitleLabel;
    @FXML private Label charLookLabel;
    @FXML private Label charBackgroundLabel;
    @FXML private Label charKnowledgeLabel;
    @FXML private Label charOpinionLabel;
    @FXML private Label charVoiceLabel;
    @FXML private Label charPersonalityLabel;
    @FXML private Label charDesiresLabel;
    @FXML private Label charFearsLabel;

    /* Character Statblock Display */
    @FXML private GridPane statblockGridPane;

    // sb for Statblock :D
    @FXML private Label sbNameLabel;
    @FXML private Label sbDescriptorLabel;
    @FXML private Label sbACLabel;
    @FXML private TextField sbCurrentHPTextField;
    @FXML private Label sbMaxHPLabel;
    @FXML private Label sbSpeedLabel;
    @FXML private Label sbStrLabel;
    @FXML private Label sbDexLabel;
    @FXML private Label sbConLabel;
    @FXML private Label sbIntLabel;
    @FXML private Label sbWisLabel;
    @FXML private Label sbChaLabel;
    @FXML private Label sbSavesLabel;
    @FXML private Label sbSkillsLabel;
    @FXML private Label sbSensesLabel;
    @FXML private Label sbLanguagesLabel;

    /* Character Basic Info Entry*/
    @FXML private TextField charNameTextField;
    @FXML private TextField charTitleTextField;
    @FXML private TextField charRaceTextField;
    @FXML private ComboBox<Location> charLocationComboBox = new ComboBox<>();

    /* Character Personality & Description Entry*/
    @FXML private TextField charVoiceTextField;
    @FXML private TextField charPersonalityTextField;
    @FXML private TextField charDesiresTextField;
    @FXML private TextField charFearsTextField;
    @FXML private TextArea charLookTextArea;
    @FXML private TextArea charBackgroundTextArea;
    @FXML private TextArea charKnowledgeTextArea;
    @FXML private TextArea charOpinionTextArea;

    /* Character Statblock Entry */
    @FXML private TextField charDescriptorTextField;
    @FXML private TextField charArmorClassTextField;
    @FXML private TextField charArmorTextField;
    @FXML private TextField charHitPointMaxTextField;
    @FXML private TextField charSpeedTextField;
    @FXML private TextField charProficiencyTextField;
    @FXML private TextField charStrengthTextField;
    @FXML private TextField charDexterityTextField;
    @FXML private TextField charConstitutionTextField;
    @FXML private TextField charIntelligenceTextField;
    @FXML private TextField charWisdomTextField;
    @FXML private TextField charCharismaTextField;
    @FXML private TextField charStrengthSaveTextField;
    @FXML private TextField charDexteritySaveTextField;
    @FXML private TextField charConstitutionSaveTextField;
    @FXML private TextField charIntelligenceSaveTextField;
    @FXML private TextField charWisdomSaveTextField;
    @FXML private TextField charCharismaSaveTextField;
    @FXML private TextField charSensesTextField;
    @FXML private TextField charLanguagesTextField;

    /* Character Skills Tableview */
    @FXML private Button charSkillAddButton;
    @FXML private Button charSkillRemoveButton;

    @FXML private TableView<Skill> skillTableView;
    @FXML private TableColumn<Skill, String> skillNameColumn;
    @FXML private TableColumn<Skill, String> skillAbilityColumn;
    @FXML private TableColumn<Skill, Integer> skillBonusColumn;

    /* Character Attack Tableview */
    @FXML private Button charAttackAddButton;
    @FXML private Button charAttackRemoveButton;

    @FXML private TableView<Attack> attackTableView;
    @FXML private TableColumn<Attack, String> attackNameColumn;
    @FXML private TableColumn<Attack, String> attackCategoryColumn;
    @FXML private TableColumn<Attack, String> attackAbilityColumn;
    @FXML private TableColumn<Attack, Integer> attackRangeColumn;
    @FXML private TableColumn<Attack, Integer> attackNumDiceColumn;
    @FXML private TableColumn<Attack, Integer> attackDiceTypeColumn;
    @FXML private TableColumn<Attack, String> attackDmgTypeColumn;

    /* Character Inventory */
    @FXML private TextArea charInventoryTextArea;

    public DatabaseController() { } /* Constructor for controller must be empty */

    /* ------------------------------ UNIVERSAL GUI METHODS ------------------------------ */

    /**
     * Used as a pseudo constructor for the controller to set up necessary variables
     * for the app. First sets up the models.Location tab, then the models.Character tab. Includes many event listeners for
     * each control in the tab.
     *
     * @param db the database
     */
    @FXML
    void initialize(Database db) {
        /* ------------------------------ TOOLBAR SETUP ------------------------------ */
        ToolBar toolBar = MasterController.getMasterToolBar();
        toolBar.getItems().clear();
        toolBar.getItems().add(saveAllButton);
        saveAllButton.setDisable(true);
        setToolbarListeners();

        /* ------------------------------ LOCATION TAB SETUP ------------------------------ */
        // Create/Set the SQLite tables
        this.locationTable = db.getLocationTable();

        // Prep the observable lists, set parent locations for each object in master list.
        masterLocations = db.getLocationTable().getAllData();
        setParentLocations(masterLocations, masterLocations);
        filteredLocations = FXCollections.observableArrayList(masterLocations);

        // Prep controls for the Location Tab
        locationTableViewSetup(filteredLocations);
        updateLocationComboBoxes(new Location());
        childLocListView.setItems(childLocations);
        locCharListView.setItems(locationCharacters);

        // Set Location listeners and select the first row of the table view - ready to go!
        setLocationTabListeners();
        locationTableView.getSelectionModel().selectFirst();

        /* ------------------------------ CHARACTER TAB SETUP ------------------------------ */
        // Create/Set the SQLite tables
        this.characterTable = db.getCharacterTable();
        this.attackTable = db.getAttackTable();
        this.skillTable = db.getSkillTable();

        // Prep the observable lists, set skills, attacks, location fields for each object in master list.
        masterCharacters = db.getCharacterTable().getAllData();
        setCharacterSkills(masterCharacters);
        setCharacterAttacks(masterCharacters);
        setCharacterLocations(masterCharacters, masterLocations);
        filteredCharacters = FXCollections.observableArrayList(masterCharacters); //Start it as a copy of the master list

        // Prep controls for Character tableviews
        characterTableViewSetup(filteredCharacters);
        attacksTableViewSetup(observedCharacterAttacks);
        skillsTableViewSetup(observedCharacterSkills);

        // Set Character listeners and select first row of the table view - ready to go!
        setCharacterTabListeners();
        characterTableView.getSelectionModel().selectFirst();
    }

    /**
     * Sets listeners for controls in the toolbar at the top of the View.
     */
    private void setToolbarListeners() {
        saveAllButton.setOnAction(event -> {
            // Save Locations
            for (Location loc : filteredLocations) {
                if (loc.isEdited()) {
                    loc.setName(GuiTools.trim(loc.getName()));
                    try {
                        locationTable.updateData(loc);
                        // Status update here
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    loc.setEdited(false);
                }
            }
            locationTableView.refresh();

            // Save Characters
            for (Character c : filteredCharacters) {
                if (c.isEdited()) {
                    try {
                        c.setLocationID(c.getLocation().getId());
                    } catch (NullPointerException e) {
                        c.setLocationID(0);
                    } finally {
                        c.setName(GuiTools.trim(c.getName()));  // The name currently has asterisks, saving trims them off.
                    }

                    // Save Skills
                    List<Skill> activeSkills = new ArrayList<>();
                    for (Skill skill : c.getSkillList()) {
                        if (skill.isSetToDelete()) {
                            skillTable.deleteData(skill);
                            continue;
                        } else {
                            activeSkills.add(skill);
                        }
                        if (skill.getId() != 0) {
                            skillTable.updateData(skill);
                        } else {
                            skillTable.insertData(skill);
                        }
                    }
                    c.setSkillList(activeSkills);

                    // Save Attacks
                    List<Attack> activeAttacks = new ArrayList<>();
                    for (Attack atk : c.getAttackList()) {
                        if (atk.isSetToDelete()) {
                            attackTable.deleteData(atk);
                            continue;
                        } else {
                            activeAttacks.add(atk);
                        }

                        if (atk.getId() != 0) { // Ensures that only atks which have ids already are updated, otherwise it can be assumed that they have just been added since the ID will be 0.
                            attackTable.updateData(atk);
                        } else {
                            attackTable.insertData(atk);
                        }
                    }
                    c.setAttackList(activeAttacks); // Sets the list so it doesn't include any "setToDelete" status attacks.

                    // Update database
                    try {
                        characterTable.updateData(c);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    c.setEdited(false);
                }
            }

            saveAllButton.setDisable(true);
            characterTableView.refresh();
        });
    }

    /* ------------------------------ LOCATION TAB METHODS ------------------------------ */

    /**
     * Sets the objects to appropriate parents based on ID.
     *
     * @param locationList : list to set the parents for.
     * @param masterLocationList : master list of models.Location objects to pull from.
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
     * Initializes the tableView for Locations. Sets the columns and CellValueFactories.
     *
     * @param locationObservableList the observable list of locations to view.
     */
    private void locationTableViewSetup(ObservableList<Location> locationObservableList) {
        locIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        locNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        locTypeCol.setCellValueFactory(new PropertyValueFactory<>("locType"));
        locParentCol.setCellValueFactory(new PropertyValueFactory<>("parentName"));

        locationTableView.setItems(locationObservableList);
    }

    /**
     * Sets the models.Location tab listeners. Basically just put everything under here that only has a single listener to
     * avoid flooding the source code with dozens of methods.
     */
    private void setLocationTabListeners() {

        /* Left side display and tableview listeners */
        locationFilterTextField.setOnKeyReleased(event -> {
            filteredLocations.clear();
            filteredLocations.addAll(locationTable.query(locationFilterTextField.getText()));
            for (Location loc : filteredLocations) {
                for (Location i : masterLocations) {
                    if (loc.getParentID() == i.getId()) {
                        loc.setParent(i);
                        break;
                    }
                }
            }
        });

        locationAddButton.setOnAction(event -> {
            Location loc = new Location();
            loc.setId(locationTable.getNextID());

            try {
                locationTable.insertData(loc);
                masterLocations.add(loc);
                filteredLocations.add(loc);
                locationTableView.getSelectionModel().select(loc);
                // Status update here
            } catch (SQLException e) {
                e.printStackTrace();
                // Status update here
            }
        });

        locationRemoveButton.setOnAction(event -> {
            Location loc = locationTableView.getSelectionModel().getSelectedItem();
            try {
                locationTable.deleteData(loc);
                // Status update here
            } catch (SQLException e) {
                e.printStackTrace();
                // Status update here
            }
            for (Location i : masterLocations) {
                if (i.getId() == loc.getId()) {
                    masterLocations.remove(i);
                    break;
                }
            }
            filteredLocations.remove(loc);
        });

        locationTableView.getSelectionModel().selectedItemProperty().addListener(event -> {
            Location loc = locationTableView.getSelectionModel().getSelectedItem();

            childLocListView.setDisable(false);
            locCharListView.setDisable(false);

            locTypeTextField.setDisable(false);
            locNameTextField.setDisable(false);
            locParentChoiceComboBox.setDisable(false);
            locFlavourTextField.setDisable(false);

            locSongLinkTextField.setDisable(false);
            locDescriptionTextArea.setDisable(false);
            locCultureTextArea.setDisable(false);
            locGovernmentTextArea.setDisable(false);
            locCrimeTextArea.setDisable(false);
            locDemographicTextArea.setDisable(false);
            locReligionTextArea.setDisable(false);
            locHistoryTextArea.setDisable(false);

            try {
                updateLocationComboBoxes(loc);

                locNameLabel.setText(loc.getName());
                locTypeLabel.setText(loc.getLocType() + " ");
                try {
                    locParentNameLabel.setText(String.format("(%s)", GuiTools.trim(loc.getParent().getName())));
                } catch (NullPointerException e) {
                    locParentNameLabel.setText("");
                }
                locFlavourLabel.setText(loc.getFlavour());

                childLocListView.getItems().clear();
                locCharListView.getItems().clear();

                List<Location> x = new ArrayList<>();
                getChildLocations(x, loc);
                childLocations.addAll(x);

                List<Character> y = new ArrayList<>();
                getCharacters(y, loc);
                locationCharacters.addAll(y);

                locNameTextField.setText(GuiTools.trim(loc.getName()));
                locTypeTextField.setText(loc.getLocType());
                locParentChoiceComboBox.setValue(loc.getParent());
                locFlavourTextField.setText(loc.getFlavour());

                locSongLinkTextField.setText(loc.getSongLink());
                locDescriptionTextArea.setText(loc.getDescription());
                locCultureTextArea.setText(loc.getCulture());
                locGovernmentTextArea.setText(loc.getGovernment());
                locCrimeTextArea.setText(loc.getCrime());
                locDemographicTextArea.setText(loc.getDemographic());
                locReligionTextArea.setText(loc.getReligion());
                locHistoryTextArea.setText(loc.getHistory());

            } catch (NullPointerException e) {
                childLocListView.setDisable(true);
                locCharListView.setDisable(true);

                locNameTextField.setDisable(true);
                locTypeTextField.setDisable(true);
                locParentChoiceComboBox.setDisable(true);
                locFlavourTextField.setDisable(true);

                locSongLinkTextField.setDisable(true);
                locDescriptionTextArea.setDisable(true);
                locCultureTextArea.setDisable(true);
                locGovernmentTextArea.setDisable(true);
                locCrimeTextArea.setDisable(true);
                locDemographicTextArea.setDisable(true);
                locReligionTextArea.setDisable(true);
                locHistoryTextArea.setDisable(true);
            }
        });

        /* Info entry control listeners*/
        locNameTextField.setOnKeyReleased(event -> {
            Location loc = locationTableView.getSelectionModel().getSelectedItem();
            loc.setName(locNameTextField.getText());
            locNameLabel.setText(loc.getName());
            locationEditDisplay(loc);
        });

        locTypeTextField.setOnKeyReleased(event -> {
            Location loc = locationTableView.getSelectionModel().getSelectedItem();
            loc.setLocType(locTypeTextField.getText());
            locTypeLabel.setText(loc.getLocType());
            locationEditDisplay(loc);
        });

        locParentChoiceComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Location loc = locationTableView.getSelectionModel().getSelectedItem();

            try {
                if (loc.getParentID() != newValue.getId()) {
                    loc.setParent(locParentChoiceComboBox.getValue());
                    try {
                        loc.setParentID(loc.getParent().getId());
                    } catch (NullPointerException e) {
                        loc.setParentID(0);
                    }
                    locationEditDisplay(loc);
                    locParentNameLabel.setText(String.format("(%s)", loc.getParent().getName()));
                }
            } catch (NullPointerException e) {
                // Catches when the combobox clears
            }
        });

        locFlavourTextField.setOnKeyReleased(event -> {
            Location loc = locationTableView.getSelectionModel().getSelectedItem();
            loc.setFlavour(locFlavourTextField.getText());
            locFlavourLabel.setText(loc.getFlavour());
            locationEditDisplay(loc);
        });

        locSongLinkTextField.setOnKeyReleased(event -> {
            Location loc = locationTableView.getSelectionModel().getSelectedItem();
            loc.setSongLink(locSongLinkTextField.getText());
            locationEditDisplay(loc);
        });

        locDescriptionTextArea.setOnKeyReleased(event -> {
            Location loc = locationTableView.getSelectionModel().getSelectedItem();
            loc.setDescription(locDescriptionTextArea.getText());
            locationEditDisplay(loc);
        });

        locCultureTextArea.setOnKeyReleased(event -> {
            Location loc = locationTableView.getSelectionModel().getSelectedItem();
            loc.setCulture(locCultureTextArea.getText());
            locationEditDisplay(loc);
        });

        locGovernmentTextArea.setOnKeyReleased(event -> {
            Location loc = locationTableView.getSelectionModel().getSelectedItem();
            loc.setGovernment(locGovernmentTextArea.getText());
            locationEditDisplay(loc);
        });

        locCrimeTextArea.setOnKeyReleased(event -> {
            Location loc = locationTableView.getSelectionModel().getSelectedItem();
            loc.setCrime(locCrimeTextArea.getText());
            locationEditDisplay(loc);
        });

        locDemographicTextArea.setOnKeyReleased(event -> {
            Location loc = locationTableView.getSelectionModel().getSelectedItem();
            loc.setDemographic(locDemographicTextArea.getText());
            locationEditDisplay(loc);
        });

        locReligionTextArea.setOnKeyReleased(event -> {
            Location loc = locationTableView.getSelectionModel().getSelectedItem();
            loc.setReligion(locReligionTextArea.getText());
            locationEditDisplay(loc);
        });

        locHistoryTextArea.setOnKeyReleased(event -> {
            Location loc = locationTableView.getSelectionModel().getSelectedItem();
            loc.setHistory(locHistoryTextArea.getText());
            locationEditDisplay(loc);
        });
    }

    /**
     * Quick method to avoid code duplication for setLocationTabListeners. Sets the Location object as edited.
     */
    private void locationEditDisplay(Location loc) {
        loc.setName("**" + GuiTools.trim(loc.getName()) + "**");
        loc.setEdited(true);
        locationTableView.refresh();
        saveAllButton.setDisable(false);
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
     * Functions updates the comboboxes on both the models.Location and Characters tab. Takes a location object to avoid
     * possibility of setting a models.Location's parent object to itself and starting an infinite loop.
     *
     * @param location : models.Location object to avoid adding to the combo box.
     */
    private void updateLocationComboBoxes(Location location) {
        locParentChoiceComboBox.getItems().clear();
        charLocationComboBox.getItems().clear();

        Location blank = new Location() {
            @Override
            public String toString() {
                return "";
            }
        };

        locParentChoiceComboBox.getItems().add(blank);
        charLocationComboBox.getItems().add(blank);

        List<Integer> invalidIDs = invalidParents(location);

        for (Location loc : masterLocations) {
            if (location != null && loc.getId() != location.getId() && !(invalidIDs.contains(loc.getId()))) {
                locParentChoiceComboBox.getItems().add(loc);
            }
            charLocationComboBox.getItems().add(loc);
        }
        try {
            assert location != null;
            locParentChoiceComboBox.valueProperty().set(location.getParent());
        } catch (NullPointerException e) {
            //
        }
    }

    /**
     * Recursive method to fetch the hierarchy of models.Location objects allowing to add all sub locations to a
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
     * Recursive method to get all Characters within a models.Location and any Characters in that models.Location's children.
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

    /* ------------------------------ CHARACTER TAB METHODS ------------------------------ */

    /**
     * Initializes the tableView for Character, sets the columns to the appropriate fields.
     *
     * @param characterObservableList The observable list of characters to view.
     */
    private void characterTableViewSetup(ObservableList<Character> characterObservableList) {
        charNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        charRaceCol.setCellValueFactory(new PropertyValueFactory<>("race"));
        charTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        charLocCol.setCellValueFactory(new PropertyValueFactory<>("locName"));
        charIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        characterTableView.setItems(characterObservableList);
    }

    /**
     * Sets the listeners for the character tab. Includes listeners for selecting different rows and updating the
     * display, as well as listeners that set the models.Character's attributes based on key presses, allowing the user to
     * edit multiple rows and save them all at once.
     */
    private void setCharacterTabListeners() {

        /* Left side tableView, add/remove and filter field listeners */
        characterFilterTextField.setOnKeyReleased(event -> {
            filteredCharacters.clear();
            filteredCharacters.addAll(characterTable.query(characterFilterTextField.getText()));
            for (Character c : filteredCharacters) {
                for (Location i : masterLocations) {
                    if (c.getLocationID() == i.getId()) {
                        c.setLocation(i);
                        break;
                    }
                }
            }
        });

        characterTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    Character c = characterTableView.getSelectionModel().getSelectedItem();

                    charNameTextField.setDisable(false);
                    charTitleTextField.setDisable(false);
                    charRaceTextField.setDisable(false);
                    charLocationComboBox.setDisable(false);

                    charVoiceTextField.setDisable(false);
                    charPersonalityTextField.setDisable(false);
                    charDesiresTextField.setDisable(false);
                    charFearsTextField.setDisable(false);
                    charLookTextArea.setDisable(false);
                    charBackgroundTextArea.setDisable(false);
                    charKnowledgeTextArea.setDisable(false);
                    charOpinionTextArea.setDisable(false);

                    charDescriptorTextField.setDisable(false);
                    charArmorClassTextField.setDisable(false);
                    charArmorTextField.setDisable(false);
                    charHitPointMaxTextField.setDisable(false);
                    charSpeedTextField.setDisable(false);
                    charProficiencyTextField.setDisable(false);
                    charStrengthTextField.setDisable(false);
                    charDexterityTextField.setDisable(false);
                    charConstitutionTextField.setDisable(false);
                    charIntelligenceTextField.setDisable(false);
                    charWisdomTextField.setDisable(false);
                    charCharismaTextField.setDisable(false);
                    charStrengthSaveTextField.setDisable(false);
                    charDexteritySaveTextField.setDisable(false);
                    charConstitutionSaveTextField.setDisable(false);
                    charIntelligenceSaveTextField.setDisable(false);
                    charWisdomSaveTextField.setDisable(false);
                    charSensesTextField.setDisable(false);
                    charCharismaSaveTextField.setDisable(false);
                    charLanguagesTextField.setDisable(false);
                    charInventoryTextArea.setDisable(false);
                    try {
                        //Left side character display
                        charNameLabel.setText(GuiTools.trim(c.getName()));
                        charTitleLabel.setText(c.getTitle());
                        charLookLabel.setText(c.getLook());

                        charBackgroundLabel.setText(c.getBackground());
                        charKnowledgeLabel.setText(c.getKnowledge());
                        charOpinionLabel.setText(c.getOpinion());
                        charVoiceLabel.setText(c.getVoice());
                        charPersonalityLabel.setText(c.getPersonality());
                        charDesiresLabel.setText(c.getDesires());
                        charFearsLabel.setText(c.getFears());

                        // Stablock Display
                        sbNameLabel.setText(c.getName());
                        sbDescriptorLabel.setText(c.getDescriptor());
                        sbACLabel.setText(String.valueOf(c.getArmorClass()));
                        sbCurrentHPTextField.setText(String.valueOf(c.getHitPointCurrent()));
                        sbMaxHPLabel.setText(String.valueOf(c.getHitPointMax()));
                        sbSpeedLabel.setText(String.valueOf(c.getSpeed()) + " ft.");

                        sbStrLabel.setText(c.getStats().getModText("STR"));
                        sbDexLabel.setText(c.getStats().getModText("DEX"));
                        sbConLabel.setText(c.getStats().getModText("CON"));
                        sbIntLabel.setText(c.getStats().getModText("INT"));
                        sbWisLabel.setText(c.getStats().getModText("WIS"));
                        sbChaLabel.setText(c.getStats().getModText("CHA"));
                        sbSavesLabel.setText(c.getSaves().toString());

                        sbSensesLabel.setText(c.getSenses());
                        sbLanguagesLabel.setText(c.getLanguages());

                        setSkillsLabelForStatblock(c);
                        setAttacksForStatblock(c);

                        // Character Entry
                        charNameTextField.setText(GuiTools.trim(c.getName()));
                        charTitleTextField.setText(c.getTitle());
                        charRaceTextField.setText(c.getRace());
                        charLocationComboBox.setValue(c.getLocation());

                        charVoiceTextField.setText(c.getVoice());
                        charPersonalityTextField.setText(c.getPersonality());
                        charDesiresTextField.setText(c.getDesires());
                        charFearsTextField.setText(c.getFears());
                        charLookTextArea.setText(c.getLook());
                        charBackgroundTextArea.setText(c.getBackground());
                        charKnowledgeTextArea.setText(c.getKnowledge());
                        charOpinionTextArea.setText(c.getOpinion());

                        charDescriptorTextField.setText(c.getDescriptor());
                        charArmorClassTextField.setText(String.valueOf(c.getArmorClass()));
                        charArmorTextField.setText(c.getArmor());
                        charHitPointMaxTextField.setText(String.valueOf(c.getHitPointMax()));
                        charSpeedTextField.setText(String.valueOf(c.getSpeed()));
                        charProficiencyTextField.setText(String.valueOf(c.getProficiency()));
                        charStrengthTextField.setText(String.valueOf(c.getStats().getStrength(0)));
                        charDexterityTextField.setText(String.valueOf(c.getStats().getDexterity(0)));
                        charConstitutionTextField.setText(String.valueOf(c.getStats().getConstitution(0)));
                        charIntelligenceTextField.setText(String.valueOf(c.getStats().getIntelligence(0)));
                        charWisdomTextField.setText(String.valueOf(c.getStats().getWisdom(0)));
                        charCharismaTextField.setText(String.valueOf(c.getStats().getCharisma(0)));
                        charStrengthSaveTextField.setText(String.valueOf(c.getSaves().getStrengthSave()));
                        charDexteritySaveTextField.setText(String.valueOf(c.getSaves().getDexteritySave()));
                        charConstitutionSaveTextField.setText(String.valueOf(c.getSaves().getConstitutionSave()));
                        charIntelligenceSaveTextField.setText(String.valueOf(c.getSaves().getIntelligenceSave()));
                        charWisdomSaveTextField.setText(String.valueOf(c.getSaves().getWisdomSave()));
                        charCharismaSaveTextField.setText(String.valueOf(c.getSaves().getCharismaSave()));
                        charSensesTextField.setText(c.getSenses());
                        charLanguagesTextField.setText(c.getLanguages());
                        charInventoryTextArea.setText(c.getInventory());

                        // Character skills
                        observedCharacterSkills.clear();
                        for (Skill skill : c.getSkillList()) {
                            if (!skill.isSetToDelete()) {
                                observedCharacterSkills.add(skill);
                            }
                        }

                        // Character attacks
                        observedCharacterAttacks.clear();
                        for (Attack atk : c.getAttackList()) {
                            if (!atk.isSetToDelete()) {
                                observedCharacterAttacks.add(atk);
                            }
                        }

                        boolean editPending = false; //check if any other characters have edits pending.
                        for (Character c2 : filteredCharacters) {
                            if (c2.isEdited()) {
                                editPending = true;
                            }
                        }
                        if (!editPending) {
                            saveAllButton.setDisable(true);
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        charNameTextField.setDisable(true);
                        charTitleTextField.setDisable(true);
                        charRaceTextField.setDisable(true);
                        charLocationComboBox.setDisable(true);

                        charVoiceTextField.setDisable(true);
                        charPersonalityTextField.setDisable(true);
                        charDesiresTextField.setDisable(true);
                        charFearsTextField.setDisable(true);
                        charLookTextArea.setDisable(true);
                        charBackgroundTextArea.setDisable(true);
                        charKnowledgeTextArea.setDisable(true);
                        charOpinionTextArea.setDisable(true);

                        charDescriptorTextField.setDisable(true);
                        charArmorClassTextField.setDisable(true);
                        charArmorTextField.setDisable(true);
                        charHitPointMaxTextField.setDisable(true);
                        charSpeedTextField.setDisable(true);
                        charProficiencyTextField.setDisable(true);
                        charStrengthTextField.setDisable(true);
                        charDexterityTextField.setDisable(true);
                        charConstitutionTextField.setDisable(true);
                        charIntelligenceTextField.setDisable(true);
                        charWisdomTextField.setDisable(true);
                        charCharismaTextField.setDisable(true);
                        charStrengthSaveTextField.setDisable(true);
                        charDexteritySaveTextField.setDisable(true);
                        charConstitutionSaveTextField.setDisable(true);
                        charIntelligenceSaveTextField.setDisable(true);
                        charWisdomSaveTextField.setDisable(true);
                        charCharismaSaveTextField.setDisable(true);
                        charSensesTextField.setDisable(true);
                        charLanguagesTextField.setDisable(true);
                        charInventoryTextArea.setDisable(true);
                    }
                });

        characterAddButton.setOnAction(event -> {
            Character c = new Character();
            c.setId(characterTable.getNextID());

            try {
                characterTable.insertData(c);
                masterCharacters.add(c);
                filteredCharacters.add(c);
                characterTableView.getSelectionModel().select(c);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        characterRemoveButton.setOnAction(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            try {
                characterTable.deleteData(c);
                attackTable.deleteDataByCharacter(c);
            } catch (SQLException e) {
                // Status update here
            }
            masterCharacters.remove(c);
            filteredCharacters.remove(c);
        });

        /* Listeners for statblock */
        sbCurrentHPTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            c.setHitPointCurrent(Integer.parseInt(sbCurrentHPTextField.getText()));
            characterEditDisplay(c);
        });

        /* Listeners for character information entry */
        // Basic Info
        charNameTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            c.setName(charNameTextField.getText());
            charNameLabel.setText(c.getName());
            sbNameLabel.setText(c.getName());
            characterEditDisplay(c);
        });

        charTitleTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            c.setTitle(charTitleTextField.getText());
            charTitleLabel.setText(c.getTitle());
            characterEditDisplay(c);
        });

        charRaceTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            c.setRace(charRaceTextField.getText());
            characterEditDisplay(c);
        });

        charLocationComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            try {
                if (c.getLocationID() != newValue.getId()) {
                    c.setLocation(charLocationComboBox.getValue());
                    try {
                        c.setLocationID(c.getLocation().getId());
                    } catch (NullPointerException e) {
                        c.setLocationID(0);
                    }
                    characterEditDisplay(c);
                }
            } catch (NullPointerException e) {
                System.out.println("k");
            }
        });

        // Personality & Description
        charVoiceTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            c.setVoice(charVoiceTextField.getText());
            charVoiceLabel.setText(c.getVoice());
            characterEditDisplay(c);
        });

        charPersonalityTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            c.setPersonality(charPersonalityTextField.getText());
            charPersonalityLabel.setText(c.getPersonality());
            characterEditDisplay(c);
        });

        charDesiresTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            c.setDesires(charDesiresTextField.getText());
            charDesiresLabel.setText(c.getDesires());
            characterEditDisplay(c);
        });

        charFearsTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            c.setFears(charFearsTextField.getText());
            charFearsLabel.setText(c.getFears());
            characterEditDisplay(c);
        });

        charLookTextArea.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            c.setLook(charLookTextArea.getText());
            charLookLabel.setText(c.getLook());
            characterEditDisplay(c);
        });

        charBackgroundTextArea.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            c.setBackground(charBackgroundTextArea.getText());
            charBackgroundLabel.setText(c.getBackground());
            characterEditDisplay(c);
        });

        charKnowledgeTextArea.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            c.setKnowledge(charKnowledgeTextArea.getText());
            charKnowledgeLabel.setText(c.getKnowledge());
            characterEditDisplay(c);
        });

        charOpinionTextArea.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            c.setOpinion(charOpinionTextArea.getText());
            charOpinionLabel.setText(c.getOpinion());
            characterEditDisplay(c);
        });

        // Statblock
        charDescriptorTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            c.setDescriptor(charDescriptorTextField.getText());
            sbDescriptorLabel.setText(c.getDescriptor());
            characterEditDisplay(c);
        });

        charArmorClassTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            c.setArmorClass(Integer.parseInt(charArmorClassTextField.getText()));
            sbACLabel.setText(String.valueOf(c.getArmorClass()));
            characterEditDisplay(c);
        });

        charArmorTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            c.setArmor(charArmorTextField.getText());
            characterEditDisplay(c);
        });

        charHitPointMaxTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            try {
                c.setHitPointMax(Integer.parseInt(charHitPointMaxTextField.getText()));
                sbMaxHPLabel.setText(String.valueOf(c.getHitPointMax()));
            } catch (NumberFormatException e) {
                //
            }
            characterEditDisplay(c);
        });

        charSpeedTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            c.setSpeed(Integer.parseInt(charSpeedTextField.getText()));
            sbSpeedLabel.setText(String.valueOf(c.getSpeed()) + " ft.");
            characterEditDisplay(c);
        });

        charProficiencyTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            try {
                c.setProficiency(Integer.parseInt(charProficiencyTextField.getText()));
            } catch (NumberFormatException e) {
                // No big deal
            }
            characterEditDisplay(c);
        });

        charStrengthTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            try {
                c.getStats().setStrength(Integer.parseInt(charStrengthTextField.getText()));
                sbStrLabel.setText(c.getStats().getModText("STR"));
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(c);
        });

        charDexterityTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            try {
                c.getStats().setDexterity(Integer.parseInt(charDexterityTextField.getText()));
                sbDexLabel.setText(c.getStats().getModText("DEX"));
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(c);
        });

        charConstitutionTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            try {
                c.getStats().setConstitution(Integer.parseInt(charConstitutionTextField.getText()));
                sbConLabel.setText(c.getStats().getModText("CON"));
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(c);
        });

        charIntelligenceTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            try {
                c.getStats().setIntelligence(Integer.parseInt(charIntelligenceTextField.getText()));
                sbIntLabel.setText(c.getStats().getModText("INT"));
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(c);
        });

        charWisdomTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            try {
                c.getStats().setWisdom(Integer.parseInt(charWisdomTextField.getText()));
                sbWisLabel.setText(c.getStats().getModText("WIS"));
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(c);
        });

        charCharismaTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            try {
                c.getStats().setCharisma(Integer.parseInt(charCharismaTextField.getText()));
                sbChaLabel.setText(c.getStats().getModText("CHA"));
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(c);
        });

        charStrengthSaveTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            try {
                c.getSaves().setStrengthSave(Integer.parseInt(charStrengthSaveTextField.getText()));
                sbSavesLabel.setText(c.getSaves().toString());
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(c);
        });

        charDexteritySaveTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            try {
                c.getSaves().setDexteritySave(Integer.parseInt(charDexteritySaveTextField.getText()));
                sbSavesLabel.setText(c.getSaves().toString());
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(c);
        });

        charConstitutionSaveTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            try {
                c.getSaves().setConstitutionSave(Integer.parseInt(charConstitutionSaveTextField.getText()));
                sbSavesLabel.setText(c.getSaves().toString());
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(c);
        });

        charIntelligenceSaveTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            try {
                c.getSaves().setIntelligenceSave(Integer.parseInt(charIntelligenceSaveTextField.getText()));
                sbSavesLabel.setText(c.getSaves().toString());
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(c);
        });

        charWisdomSaveTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            try {
                c.getSaves().setWisdomSave(Integer.parseInt(charWisdomSaveTextField.getText()));
                sbSavesLabel.setText(c.getSaves().toString());
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(c);
        });

        charCharismaSaveTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            try {
                c.getSaves().setCharismaSave(Integer.parseInt(charCharismaSaveTextField.getText()));
                sbSavesLabel.setText(c.getSaves().toString());
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(c);
        });

        charSensesTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            c.setSenses(charSensesTextField.getText());
            sbSensesLabel.setText(c.getSenses());
            characterEditDisplay(c);
        });

        charLanguagesTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            c.setLanguages(charLanguagesTextField.getText());
            sbLanguagesLabel.setText(c.getLanguages());
            characterEditDisplay(c);
        });

        //Skills
        charSkillAddButton.setOnAction(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            Skill skill = new Skill();
            skill.setCharacterID(c.getId());
            c.addSkill(skill);
            observedCharacterSkills.add(skill);
            characterEditDisplay(c);
        });

        charSkillRemoveButton.setOnAction(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            Skill skill = skillTableView.getSelectionModel().getSelectedItem();
            try {
                skill.setToDelete(true);
                observedCharacterSkills.remove(skill);
                characterEditDisplay(c);
            } catch (NullPointerException e) {
                System.out.println("No Skill selected.");
            }
        });

        // Attacks
        charAttackAddButton.setOnAction(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            Attack atk = new Attack();
            atk.setCharacterID(c.getId());
            c.addAttack(atk);
            observedCharacterAttacks.add(atk);
            characterEditDisplay(c);
        });

        charAttackRemoveButton.setOnAction(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            Attack atk = attackTableView.getSelectionModel().getSelectedItem();
            try {
                atk.setToDelete(true);
                observedCharacterAttacks.remove(atk);
                characterEditDisplay(c);
            } catch (NullPointerException e) {
                System.out.println("No attack selected");
            }
        });

        //Inventory
        charInventoryTextArea.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            c.setInventory(charInventoryTextArea.getText());
            characterEditDisplay(c);
        });

    }

    private void skillsTableViewSetup(ObservableList<Skill> skillsObservableList) {
        skillNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ObservableList<String> skillNameList = FXCollections.observableArrayList(
               new ArrayList<>(Arrays.asList("Animal Handling", "Acrobatics", "Arcana", "Athletics", "Deception", "History", "Insight",
                       "Intimidation", "Investigation", "Medicine", "Nature", "Perception", "Performance", "Persuasion",
                       "Religion", "Sleight of Hand", "Stealth", "Survival"))
        );
        skillNameColumn.setCellFactory(ComboBoxTableCell.forTableColumn(skillNameList));
        skillNameColumn.setOnEditCommit(event -> {
            TablePosition<Skill, String> pos = event.getTablePosition();
            String newName = event.getNewValue();
            int row = pos.getRow();
            Skill skill = event.getTableView().getItems().get(row);
            skill.setName(newName);
            skill.setAbility(newName);
            skillTableView.refresh();
            characterEditDisplay(characterTableView.getSelectionModel().getSelectedItem());
        });

        skillAbilityColumn.setCellValueFactory(new PropertyValueFactory<>("Ability"));

        skillBonusColumn.setCellValueFactory(new PropertyValueFactory<>("bonus"));
        skillBonusColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        skillBonusColumn.setOnEditCommit(event -> {
            TablePosition<Skill, Integer> pos = event.getTablePosition();
            int newBonus = event.getNewValue();
            int row = pos.getRow();
            Skill skill = event.getTableView().getItems().get(row);
            skill.setBonus(newBonus);
            characterEditDisplay(characterTableView.getSelectionModel().getSelectedItem());
        });

        skillTableView.setItems(skillsObservableList);
    }

    private void setCharacterSkills(List<Character> characterList) {
        for (Character c : characterList) {
            c.setSkillList(skillTable.getDataByCharacter(c));
        }
    }

    /**
     * Initializes the smaller tableView for Attacks. More advanced CellFactories here since I wanted the cells
     * to be editable directly on the table itself.
     *
     * @param attackObservableList The observable list of attacks to view.
     */
    private void attacksTableViewSetup(ObservableList<Attack> attackObservableList) {
        attackNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        attackNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        attackNameColumn.setOnEditCommit(event -> {
            TablePosition<Attack, String> pos = event.getTablePosition();
            String newName = event.getNewValue();
            int row = pos.getRow();
            Attack atk = event.getTableView().getItems().get(row);
            atk.setName(newName);
            characterEditDisplay(characterTableView.getSelectionModel().getSelectedItem());
        });

        attackCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        ObservableList<String> attackCatObsList = FXCollections.observableArrayList(
                new ArrayList<>(Arrays.asList("Melee", "Ranged"))
        );
        attackCategoryColumn.setCellFactory(ComboBoxTableCell.forTableColumn(attackCatObsList));
        attackCategoryColumn.setOnEditCommit(event -> {
            TablePosition<Attack, String> pos = event.getTablePosition();
            String newCat = event.getNewValue();
            int row = pos.getRow();
            Attack atk = event.getTableView().getItems().get(row);
            atk.setCategory(newCat);
            characterEditDisplay(characterTableView.getSelectionModel().getSelectedItem());
        });

        attackAbilityColumn.setCellValueFactory(new PropertyValueFactory<>("ability"));
        ObservableList<String> attackAbilityObsList = FXCollections.observableArrayList(
                new ArrayList<>(Arrays.asList("STR", "DEX", "CON", "INT", "WIS", "CHA"))
        );
        attackAbilityColumn.setCellFactory(ComboBoxTableCell.forTableColumn(attackAbilityObsList));
        attackAbilityColumn.setOnEditCommit(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            TablePosition<Attack, String> pos = event.getTablePosition();
            String newAbility = event.getNewValue();
            int row = pos.getRow();
            Attack atk = event.getTableView().getItems().get(row);
            atk.setAbility(newAbility);
            atk.setAttackBonus(c.getAbilityMod(newAbility));
            characterEditDisplay(c);
        });

        attackRangeColumn.setCellValueFactory(new PropertyValueFactory<>("range"));
        attackRangeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        attackRangeColumn.setOnEditCommit(event -> {
            TablePosition<Attack, Integer> pos = event.getTablePosition();
            int newRange = event.getNewValue();
            int row = pos.getRow();
            Attack atk = event.getTableView().getItems().get(row);
            atk.setRange(newRange);
            characterEditDisplay(characterTableView.getSelectionModel().getSelectedItem());
        });

        attackNumDiceColumn.setCellValueFactory(new PropertyValueFactory<>("numDice"));
        attackNumDiceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        attackNumDiceColumn.setOnEditCommit(event -> {
            TablePosition<Attack, Integer> pos = event.getTablePosition();
            int newNumDice = event.getNewValue();
            int row = pos.getRow();
            Attack atk = event.getTableView().getItems().get(row);
            atk.setNumDice(newNumDice);
            characterEditDisplay(characterTableView.getSelectionModel().getSelectedItem());
        });

        attackDiceTypeColumn.setCellValueFactory(new PropertyValueFactory<>("damageDice"));
        attackDiceTypeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        attackDiceTypeColumn.setOnEditCommit(event -> {
            TablePosition<Attack, Integer> pos = event.getTablePosition();
            int newDamageDice = event.getNewValue();
            int row = pos.getRow();
            Attack atk = event.getTableView().getItems().get(row);
            atk.setDamageDice(newDamageDice);
            characterEditDisplay(characterTableView.getSelectionModel().getSelectedItem());
        });

        attackDmgTypeColumn.setCellValueFactory(new PropertyValueFactory<>("damageType"));
        attackDmgTypeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        attackDmgTypeColumn.setOnEditCommit(event -> {
            TablePosition<Attack, String> pos = event.getTablePosition();
            String newDamageType = event.getNewValue();
            int row = pos.getRow();
            Attack atk = event.getTableView().getItems().get(row);
            atk.setDamageType(newDamageType);
            characterEditDisplay(characterTableView.getSelectionModel().getSelectedItem());
        });

        attackTableView.setItems(attackObservableList);
    }

    /**
     * Fetch data from SQL table Attacks, assigns the attack list to each character based on ID.
     */
    private void setCharacterAttacks(List<Character> characterList) {
        for (Character c : characterList) {
            c.setAttackList(attackTable.getDataByCharacter(c));
        }
    }

    /**
     * Quick method to avoid code duplication for setCharacterTabListeners. Sets the Character object as edited.
     */
    private void characterEditDisplay(Character c) {
        c.setName("**" + GuiTools.trim(c.getName()) + "**");
        c.setEdited(true);
        characterTableView.refresh();
        saveAllButton.setDisable(false);
    }

    private void setSkillsLabelForStatblock(Character c) {
        List<Skill> skills = c.getSkillList();

        List<String> skillsStrings = new ArrayList<>();
        String symbol = "+";
        for (Skill s : skills) {
            int bonusTotal = s.getBonus() + c.getAbilityMod(s.getAbility()) + c.getProficiency();
            if (bonusTotal < 0) {
                symbol = "";
            }
            skillsStrings.add(s.getName() + " " + symbol + bonusTotal);
        }

        sbSkillsLabel.setText(String.join(", ", skillsStrings));
    }

    /**
     * Used to set the display on the statblock for attacks. Adjusts rows dynamically based on the number of attacks
     * the character possesses.
     *
     * @param c The character to retrieve the attacks from.
     */
    private void setAttacksForStatblock(Character c) {
        statblockGridPane.getChildren().clear();
        List<Attack> attacks = c.getAttackList();

        for (int i = 0; i < c.getAttackList().size(); i++) {
            Attack a = attacks.get(i);
            int damageBonus = c.getAbilityMod(a.getAbility());

            String attackBonusSymbol = "+";
            if (a.getAttackBonus() < 0) {
                attackBonusSymbol = "-";
            }
            String damageBonusSymbol = "+";
            if (damageBonus < 0) {
                damageBonusSymbol = "-";
            }
            String rangeWording = "reach";
            if (a.getRange() > 10 ) {
                rangeWording = "range";
            }

            FlowPane fp = new FlowPane();
            fp.getChildren().add(new Label(a.getName() + ". "));
            fp.getChildren().add(new Label(a.getCategory() + " Weapon Attack: "));
            fp.getChildren().add(new Label(attackBonusSymbol + a.getAttackBonus() + " to hit, "));
            fp.getChildren().add(new Label(rangeWording + " " + a.getRange() + " ft., one target. "));
            fp.getChildren().add(new Label("Hit: "));
            fp.getChildren().add(new Label(a.getNumDice() + "d" + a.getDamageDice() + " " + damageBonusSymbol + " " + damageBonus));
            fp.getChildren().add(new Label(" " + a.getDamageType() + " damage."));
            statblockGridPane.add(fp, 0, i);
        }
    }

    /**
     * Sets the models.Location object references for the master list of Characters. Does this by matching the foreign key
     * ID to the location's ID using for loops.
     *
     * @param characterList : The master models.models.models.Character list to loop through
     * @param masterLocationList : The master models.models.models.Location list to loop through
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
}
