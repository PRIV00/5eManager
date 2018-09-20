package main.controllers;

import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.converter.IntegerStringConverter;
import main.assets.GuiTools;
import main.assets.TextAreaTableCell;
import main.databases.tables.*;
import main.databases.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import main.models.characterfields.Attack;
import main.models.Character;
import main.models.Location;
import main.models.characterfields.Skill;
import main.models.characterfields.Trait;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseController {

    /* ------------------------------ MODEL FIELDS ------------------------------ */
    //TODO: Cleanup fields. See where you can delete or combine unnecessary code.

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
    private ObservableList<Attack> selectedCharacterAttacks = FXCollections.observableArrayList();

    private SkillTable skillTable;
    private ObservableList<Skill> selectedCharacterSkills = FXCollections.observableArrayList();

    private TraitTable traitTable;
    private ObservableList<Trait> selectedCharacterTraits = FXCollections.observableArrayList();

    /* ------------------------------ TOOLBAR CONTROLS ------------------------------ */
    private Button saveAllButton = new Button("Save All"); // Not an FXML control as it needs to be added dynamically.

    /* ------------------------------ LOCATION TAB CONTROLS ------------------------------ */
    @FXML private Accordion locationAccordion;

    @FXML private TextField locationFilterTextField;
    @FXML private Button locationAddButton;
    @FXML private Button locationRemoveButton;

    /* Location display tab */
    @FXML private GridPane locationDisplayGridPane;

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
    @FXML private Accordion characterAccordion; // Entry for character tabs.

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
    @FXML private GridPane statblockTraitsGridPane;
    @FXML private GridPane statblockAttacksGridPane;

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

    /* Character Trait Tableview */
    @FXML private Button charTraitAddButton;
    @FXML private Button charTraitRemoveButton;

    @FXML private TableView<Trait> traitTableView;
    @FXML private TableColumn<Trait, String> traitNameColumn;
    @FXML private TableColumn<Trait, String> traitDescriptionColumn;

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
        this.skillTable = db.getSkillTable();
        this.attackTable = db.getAttackTable();
        this.traitTable = db.getTraitTable();

        // Prep the observable lists, set skills, attacks, location fields for each object in master list.
        masterCharacters = db.getCharacterTable().getAllData();
        setCharacterSkills(masterCharacters);
        setCharacterAttacks(masterCharacters);
        setCharacterTraits(masterCharacters);
        setCharacterLocations(masterCharacters, masterLocations);
        filteredCharacters = FXCollections.observableArrayList(masterCharacters); //Start it as a copy of the master list

        // Prep controls for Character tableviews
        characterTableViewSetup(filteredCharacters);
        skillsTableViewSetup(selectedCharacterSkills);
        attacksTableViewSetup(selectedCharacterAttacks);
        traitsTableViewSetup(selectedCharacterTraits);

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

            //TODO: Any cleaner alternatives to saving?
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
                    List<Skill> activeSkills = new ArrayList<>(); // Not a huge fan of this way of doing it. Want to look into writing something cleaner.
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

                    // Save Traits
                    List<Trait> activeTraits = new ArrayList<>();
                    for (Trait trait : c.getTraitList()) {
                        if (trait.isSetToDelete()) {
                            traitTable.deleteData(trait);
                            continue;
                        } else {
                            activeTraits.add(trait);
                        }

                        if (trait.getId() != 0) {
                            traitTable.updateData(trait);
                        } else {
                            traitTable.insertData(trait);
                        }
                    }
                    c.setTraitList(activeTraits);

                    // Update database
                    try {
                        characterTable.updateData(c);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    c.setEdited(false);
                }
            }

            setSkillsForStatblock(observedCharacter);
            setAttacksForStatblock(observedCharacter);
            setTraitsForStatblock(observedCharacter);
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
        locationTableView.setColumnResizePolicy(param -> true);

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
            try {
                locationTable.deleteData(observedLocation);
                // Status update here
            } catch (SQLException e) {
                e.printStackTrace();
                // Status update here
            }
            for (Location i : masterLocations) {
                if (i.getId() == observedLocation.getId()) {
                    masterLocations.remove(i);
                    break;
                }
            }
            filteredLocations.remove(observedLocation);
        });

        locationTableView.getSelectionModel().selectedItemProperty().addListener(event -> {
            observedLocation = locationTableView.getSelectionModel().getSelectedItem();

            childLocListView.setDisable(false);
            locCharListView.setDisable(false);
            locationAccordion.setDisable(false);
            try {
                updateLocationComboBoxes(observedLocation);

                locNameLabel.setText(observedLocation.getName());
                locTypeLabel.setText(observedLocation.getLocType() + " ");
                try {
                    locParentNameLabel.setText(String.format("(%s)", GuiTools.trim(observedLocation.getParent().getName())));
                } catch (NullPointerException e) {
                    locParentNameLabel.setText("");
                }
                locFlavourLabel.setText(observedLocation.getFlavour());

                childLocListView.getItems().clear();
                locCharListView.getItems().clear();

                List<Location> x = new ArrayList<>();
                getChildLocations(x, observedLocation);
                childLocations.addAll(x);

                List<Character> y = new ArrayList<>();
                getCharacters(y, observedLocation);
                locationCharacters.addAll(y);

                locNameTextField.setText(GuiTools.trim(observedLocation.getName()));
                locTypeTextField.setText(observedLocation.getLocType());
                locParentChoiceComboBox.setValue(observedLocation.getParent());
                locFlavourTextField.setText(observedLocation.getFlavour());

                locSongLinkTextField.setText(observedLocation.getSongLink());
                locDescriptionTextArea.setText(observedLocation.getDescription());
                locCultureTextArea.setText(observedLocation.getCulture());
                locGovernmentTextArea.setText(observedLocation.getGovernment());
                locCrimeTextArea.setText(observedLocation.getCrime());
                locDemographicTextArea.setText(observedLocation.getDemographic());
                locReligionTextArea.setText(observedLocation.getReligion());
                locHistoryTextArea.setText(observedLocation.getHistory());

                setLocationDisplayTab();

            } catch (NullPointerException e) {
                childLocListView.setDisable(true);
                locCharListView.setDisable(true);
                locationAccordion.setDisable(true);
            }
        });

        /* Info entry control listeners*/
        locNameTextField.setOnKeyReleased(event -> {
            observedLocation.setName(locNameTextField.getText());
            locNameLabel.setText(observedLocation.getName());
            locationEditDisplay(observedLocation);
        });

        locTypeTextField.setOnKeyReleased(event -> {
            observedLocation = locationTableView.getSelectionModel().getSelectedItem();
            observedLocation.setLocType(locTypeTextField.getText());
            locTypeLabel.setText(observedLocation.getLocType());
            locationEditDisplay(observedLocation);
        });

        locParentChoiceComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {

            try {
                if (observedLocation.getParentID() != newValue.getId()) {
                    observedLocation.setParent(locParentChoiceComboBox.getValue());
                    try {
                        observedLocation.setParentID(observedLocation.getParent().getId());
                    } catch (NullPointerException e) {
                        observedLocation.setParentID(0);
                    }
                    locationEditDisplay(observedLocation);
                    locParentNameLabel.setText(String.format("(%s)", observedLocation.getParent().getName()));
                }
            } catch (NullPointerException e) {
                // Catches when the combobox clears
            }
        });

        locFlavourTextField.setOnKeyReleased(event -> {
            observedLocation.setFlavour(locFlavourTextField.getText());
            locFlavourLabel.setText(observedLocation.getFlavour());
            locationEditDisplay(observedLocation);
        });

        locSongLinkTextField.setOnKeyReleased(event -> {
            observedLocation.setSongLink(locSongLinkTextField.getText());
            locationEditDisplay(observedLocation);
        });

        locDescriptionTextArea.setOnKeyReleased(event -> {
            observedLocation.setDescription(locDescriptionTextArea.getText());
            locationEditDisplay(observedLocation);
        });

        locCultureTextArea.setOnKeyReleased(event -> {
            observedLocation.setCulture(locCultureTextArea.getText());
            locationEditDisplay(observedLocation);
        });

        locGovernmentTextArea.setOnKeyReleased(event -> {
            observedLocation.setGovernment(locGovernmentTextArea.getText());
            locationEditDisplay(observedLocation);
        });

        locCrimeTextArea.setOnKeyReleased(event -> {
            observedLocation.setCrime(locCrimeTextArea.getText());
            locationEditDisplay(observedLocation);
        });

        locDemographicTextArea.setOnKeyReleased(event -> {
            observedLocation.setDemographic(locDemographicTextArea.getText());
            locationEditDisplay(observedLocation);
        });

        locReligionTextArea.setOnKeyReleased(event -> {
            observedLocation.setReligion(locReligionTextArea.getText());
            locationEditDisplay(observedLocation);
        });

        locHistoryTextArea.setOnKeyReleased(event -> {
            observedLocation.setHistory(locHistoryTextArea.getText());
            locationEditDisplay(observedLocation);
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

    private void setLocationDisplayTab() {
        locationDisplayGridPane.getChildren().clear();

        int rowCount = 0;
        if (!observedLocation.getDescription().equals("")) {
            Label descriptionTitleLabel = new Label("Description");
            descriptionTitleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
            descriptionTitleLabel.setPadding(new Insets(10, 0, 0, 0));
            locationDisplayGridPane.add(descriptionTitleLabel, 0, rowCount);
            rowCount += 1;

            Label descriptionLabel = new Label(observedLocation.getDescription());
            descriptionLabel.setFont(Font.font("Georgia", 12));
            descriptionLabel.setWrapText(true);
            locationDisplayGridPane.add(descriptionLabel, 0, rowCount);
            rowCount +=1;
        }

        if (!observedLocation.getCulture().equals("")) {
            Label cultureTitleLabel = new Label("Culture");
            cultureTitleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
            cultureTitleLabel.setPadding(new Insets(10, 0, 0, 0));
            locationDisplayGridPane.add(cultureTitleLabel, 0, rowCount);
            rowCount += 1;

            Label cultureLabel = new Label(observedLocation.getCulture());
            cultureLabel.setFont(Font.font("Georgia", 12));
            cultureLabel.setWrapText(true);
            locationDisplayGridPane.add(cultureLabel, 0, rowCount);
            rowCount += 1;
        }

        if (!observedLocation.getGovernment().equals("")) {
            Label governmentTitleLabel = new Label("Government");
            governmentTitleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
            governmentTitleLabel.setPadding(new Insets(10, 0, 0, 0));
            locationDisplayGridPane.add(governmentTitleLabel, 0, rowCount);
            rowCount += 1;

            Label governmentLabel = new Label(observedLocation.getGovernment());
            governmentLabel.setFont(Font.font("Georgia", 12));
            governmentLabel.setWrapText(true);
            locationDisplayGridPane.add(governmentLabel, 0, rowCount);
            rowCount += 1;
        }

        if (!observedLocation.getCrime().equals("")) {
            Label crimeTitleLabel = new Label("Crime");
            crimeTitleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
            crimeTitleLabel.setPadding(new Insets(10, 0, 0, 0));
            locationDisplayGridPane.add(crimeTitleLabel, 0, rowCount);
            rowCount += 1;

            Label crimeLabel = new Label(observedLocation.getCrime());
            crimeLabel.setFont(Font.font("Georgia", 12));
            crimeLabel.setWrapText(true);
            locationDisplayGridPane.add(crimeLabel, 0, rowCount);
            rowCount += 1;
        }

        if (!observedLocation.getDemographic().equals("")) {
            Label demographicTitleLabel = new Label("Demographic");
            demographicTitleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
            demographicTitleLabel.setPadding(new Insets(10, 0, 0, 0));
            locationDisplayGridPane.add(demographicTitleLabel, 0, rowCount);
            rowCount += 1;

            Label demographicLabel = new Label(observedLocation.getDemographic());
            demographicLabel.setFont(Font.font("Georgia", 12));
            demographicLabel.setWrapText(true);
            locationDisplayGridPane.add(demographicLabel, 0, rowCount);
            rowCount += 1;
        }

        if (!observedLocation.getReligion().equals("")) {
            Label religionTitleLabel = new Label("Religion");
            religionTitleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
            religionTitleLabel.setPadding(new Insets(10, 0, 0, 0));
            locationDisplayGridPane.add(religionTitleLabel, 0, rowCount);
            rowCount += 1;

            Label religionLabel = new Label(observedLocation.getReligion());
            religionLabel.setFont(Font.font("Georgia", 12));
            religionLabel.setWrapText(true);
            locationDisplayGridPane.add(religionLabel, 0, rowCount);
            rowCount += 1;
        }

        if (!observedLocation.getHistory().equals("")) {
            Label historyTitleLabel = new Label("History");
            historyTitleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
            historyTitleLabel.setPadding(new Insets(10, 0, 0, 0));
            locationDisplayGridPane.add(historyTitleLabel, 0, rowCount);
            rowCount += 1;

            Label historyLabel = new Label(observedLocation.getHistory());
            historyLabel.setFont(Font.font("Georgia", 12));
            historyLabel.setWrapText(true);
            locationDisplayGridPane.add(historyLabel, 0, rowCount);
        }
    }

    /* ------------------------------ CHARACTER TAB METHODS ------------------------------ */

    /**
     * Initializes the tableView for Character, sets the columns to the appropriate fields.
     *
     * @param characterObservableList The observable list of characters to view.
     */
    private void characterTableViewSetup(ObservableList<Character> characterObservableList) {
        characterTableView.setColumnResizePolicy(param -> true);

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
                    observedCharacter = characterTableView.getSelectionModel().getSelectedItem();

                    characterAccordion.setDisable(false);
                    try {
                        //Left side character display
                        charNameLabel.setText(GuiTools.trim(observedCharacter.getName()));
                        charTitleLabel.setText(observedCharacter.getTitle());
                        charLookLabel.setText(observedCharacter.getLook());

                        charBackgroundLabel.setText(observedCharacter.getBackground());
                        charKnowledgeLabel.setText(observedCharacter.getKnowledge());
                        charOpinionLabel.setText(observedCharacter.getOpinion());
                        charVoiceLabel.setText(observedCharacter.getVoice());
                        charPersonalityLabel.setText(observedCharacter.getPersonality());
                        charDesiresLabel.setText(observedCharacter.getDesires());
                        charFearsLabel.setText(observedCharacter.getFears());

                        // Stablock Display
                        sbNameLabel.setText(observedCharacter.getName());
                        sbDescriptorLabel.setText(observedCharacter.getDescriptor());
                        sbACLabel.setText(String.valueOf(observedCharacter.getArmorClass()));
                        sbCurrentHPTextField.setText(String.valueOf(observedCharacter.getHitPointCurrent()));
                        sbMaxHPLabel.setText(String.valueOf(observedCharacter.getHitPointMax()));
                        sbSpeedLabel.setText(String.valueOf(observedCharacter.getSpeed()) + " ft.");

                        sbStrLabel.setText(observedCharacter.getStats().getModText("STR"));
                        sbDexLabel.setText(observedCharacter.getStats().getModText("DEX"));
                        sbConLabel.setText(observedCharacter.getStats().getModText("CON"));
                        sbIntLabel.setText(observedCharacter.getStats().getModText("INT"));
                        sbWisLabel.setText(observedCharacter.getStats().getModText("WIS"));
                        sbChaLabel.setText(observedCharacter.getStats().getModText("CHA"));
                        sbSavesLabel.setText(observedCharacter.getSaves().toString());

                        sbSensesLabel.setText(observedCharacter.getSenses());
                        sbLanguagesLabel.setText(observedCharacter.getLanguages());

                        setSkillsForStatblock(observedCharacter);
                        setTraitsForStatblock(observedCharacter);
                        setAttacksForStatblock(observedCharacter);

                        // Character Entry
                        charNameTextField.setText(GuiTools.trim(observedCharacter.getName()));
                        charTitleTextField.setText(observedCharacter.getTitle());
                        charRaceTextField.setText(observedCharacter.getRace());
                        charLocationComboBox.setValue(observedCharacter.getLocation());

                        charVoiceTextField.setText(observedCharacter.getVoice());
                        charPersonalityTextField.setText(observedCharacter.getPersonality());
                        charDesiresTextField.setText(observedCharacter.getDesires());
                        charFearsTextField.setText(observedCharacter.getFears());
                        charLookTextArea.setText(observedCharacter.getLook());
                        charBackgroundTextArea.setText(observedCharacter.getBackground());
                        charKnowledgeTextArea.setText(observedCharacter.getKnowledge());
                        charOpinionTextArea.setText(observedCharacter.getOpinion());

                        charDescriptorTextField.setText(observedCharacter.getDescriptor());
                        charArmorClassTextField.setText(String.valueOf(observedCharacter.getArmorClass()));
                        charArmorTextField.setText(observedCharacter.getArmor());
                        charHitPointMaxTextField.setText(String.valueOf(observedCharacter.getHitPointMax()));
                        charSpeedTextField.setText(String.valueOf(observedCharacter.getSpeed()));
                        charProficiencyTextField.setText(String.valueOf(observedCharacter.getProficiency()));
                        charStrengthTextField.setText(String.valueOf(observedCharacter.getStats().getStrength(0)));
                        charDexterityTextField.setText(String.valueOf(observedCharacter.getStats().getDexterity(0)));
                        charConstitutionTextField.setText(String.valueOf(observedCharacter.getStats().getConstitution(0)));
                        charIntelligenceTextField.setText(String.valueOf(observedCharacter.getStats().getIntelligence(0)));
                        charWisdomTextField.setText(String.valueOf(observedCharacter.getStats().getWisdom(0)));
                        charCharismaTextField.setText(String.valueOf(observedCharacter.getStats().getCharisma(0)));
                        charStrengthSaveTextField.setText(String.valueOf(observedCharacter.getSaves().getStrengthSave()));
                        charDexteritySaveTextField.setText(String.valueOf(observedCharacter.getSaves().getDexteritySave()));
                        charConstitutionSaveTextField.setText(String.valueOf(observedCharacter.getSaves().getConstitutionSave()));
                        charIntelligenceSaveTextField.setText(String.valueOf(observedCharacter.getSaves().getIntelligenceSave()));
                        charWisdomSaveTextField.setText(String.valueOf(observedCharacter.getSaves().getWisdomSave()));
                        charCharismaSaveTextField.setText(String.valueOf(observedCharacter.getSaves().getCharismaSave()));
                        charSensesTextField.setText(observedCharacter.getSenses());
                        charLanguagesTextField.setText(observedCharacter.getLanguages());
                        charInventoryTextArea.setText(observedCharacter.getInventory());

                        // Character skills  //TODO: Not a big fan of this process either.
                        selectedCharacterSkills.clear();
                        for (Skill skill : observedCharacter.getSkillList()) {
                            if (!skill.isSetToDelete()) {
                                selectedCharacterSkills.add(skill);
                            }
                        }

                        // Character attacks
                        selectedCharacterAttacks.clear();
                        for (Attack atk : observedCharacter.getAttackList()) {
                            if (!atk.isSetToDelete()) {
                                selectedCharacterAttacks.add(atk);
                            }
                        }

                        // Character traits
                        selectedCharacterTraits.clear();
                        for (Trait trait : observedCharacter.getTraitList()) {
                            if (!trait.isSetToDelete()) {
                                selectedCharacterTraits.add(trait);
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
                        characterAccordion.setDisable(true); // So that info can't be changed or entered when character is not selected.
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
            try {
                characterTable.deleteData(observedCharacter);
                // The following 3 lines are to ensure there are no orphaned entries of the sub tables.
                skillTable.deleteDataByCharacter(observedCharacter);
                attackTable.deleteDataByCharacter(observedCharacter);
                traitTable.deleteDataByCharacter(observedCharacter);
            } catch (SQLException e) {
                // Status update here
            }
            masterCharacters.remove(observedCharacter);
            filteredCharacters.remove(observedCharacter);
        });

        /* Listeners for statblock */
        sbCurrentHPTextField.setOnKeyReleased(event -> {
            observedCharacter.setHitPointCurrent(Integer.parseInt(sbCurrentHPTextField.getText()));
            characterEditDisplay(observedCharacter);
        });

        /* Listeners for character information entry */
        // Basic Info
        charNameTextField.setOnKeyReleased(event -> {
            observedCharacter.setName(charNameTextField.getText());
            charNameLabel.setText(observedCharacter.getName());
            sbNameLabel.setText(observedCharacter.getName());
            characterEditDisplay(observedCharacter);
        });

        charTitleTextField.setOnKeyReleased(event -> {
            observedCharacter.setTitle(charTitleTextField.getText());
            charTitleLabel.setText(observedCharacter.getTitle());
            characterEditDisplay(observedCharacter);
        });

        charRaceTextField.setOnKeyReleased(event -> {
            observedCharacter.setRace(charRaceTextField.getText());
            characterEditDisplay(observedCharacter);
        });

        charLocationComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (observedCharacter.getLocationID() != newValue.getId()) {
                    observedCharacter.setLocation(charLocationComboBox.getValue());
                    try {
                        observedCharacter.setLocationID(observedCharacter.getLocation().getId());
                    } catch (NullPointerException e) {
                        observedCharacter.setLocationID(0);
                    }
                    characterEditDisplay(observedCharacter);
                }
            } catch (NullPointerException e) {
                System.out.println("k");
            }
        });

        // Personality & Description
        charVoiceTextField.setOnKeyReleased(event -> {
            observedCharacter.setVoice(charVoiceTextField.getText());
            charVoiceLabel.setText(observedCharacter.getVoice());
            characterEditDisplay(observedCharacter);
        });

        charPersonalityTextField.setOnKeyReleased(event -> {
            observedCharacter.setPersonality(charPersonalityTextField.getText());
            charPersonalityLabel.setText(observedCharacter.getPersonality());
            characterEditDisplay(observedCharacter);
        });

        charDesiresTextField.setOnKeyReleased(event -> {
            observedCharacter.setDesires(charDesiresTextField.getText());
            charDesiresLabel.setText(observedCharacter.getDesires());
            characterEditDisplay(observedCharacter);
        });

        charFearsTextField.setOnKeyReleased(event -> {
            observedCharacter.setFears(charFearsTextField.getText());
            charFearsLabel.setText(observedCharacter.getFears());
            characterEditDisplay(observedCharacter);
        });

        charLookTextArea.setOnKeyReleased(event -> {
            observedCharacter.setLook(charLookTextArea.getText());
            charLookLabel.setText(observedCharacter.getLook());
            characterEditDisplay(observedCharacter);
        });

        charBackgroundTextArea.setOnKeyReleased(event -> {
            observedCharacter.setBackground(charBackgroundTextArea.getText());
            charBackgroundLabel.setText(observedCharacter.getBackground());
            characterEditDisplay(observedCharacter);
        });

        charKnowledgeTextArea.setOnKeyReleased(event -> {
            observedCharacter.setKnowledge(charKnowledgeTextArea.getText());
            charKnowledgeLabel.setText(observedCharacter.getKnowledge());
            characterEditDisplay(observedCharacter);
        });

        charOpinionTextArea.setOnKeyReleased(event -> {
            observedCharacter.setOpinion(charOpinionTextArea.getText());
            charOpinionLabel.setText(observedCharacter.getOpinion());
            characterEditDisplay(observedCharacter);
        });

        // Statblock
        charDescriptorTextField.setOnKeyReleased(event -> {
            observedCharacter.setDescriptor(charDescriptorTextField.getText());
            sbDescriptorLabel.setText(observedCharacter.getDescriptor());
            characterEditDisplay(observedCharacter);
        });

        charArmorClassTextField.setOnKeyReleased(event -> {
            observedCharacter.setArmorClass(Integer.parseInt(charArmorClassTextField.getText()));
            sbACLabel.setText(String.valueOf(observedCharacter.getArmorClass()));
            characterEditDisplay(observedCharacter);
        });

        charArmorTextField.setOnKeyReleased(event -> {
            observedCharacter.setArmor(charArmorTextField.getText());
            characterEditDisplay(observedCharacter);
        });

        charHitPointMaxTextField.setOnKeyReleased(event -> {
            try {
                observedCharacter.setHitPointMax(Integer.parseInt(charHitPointMaxTextField.getText()));
                sbMaxHPLabel.setText(String.valueOf(observedCharacter.getHitPointMax()));
            } catch (NumberFormatException e) {
                //
            }
            characterEditDisplay(observedCharacter);
        });

        charSpeedTextField.setOnKeyReleased(event -> {
            observedCharacter.setSpeed(Integer.parseInt(charSpeedTextField.getText()));
            sbSpeedLabel.setText(String.valueOf(observedCharacter.getSpeed()) + " ft.");
            characterEditDisplay(observedCharacter);
        });

        charProficiencyTextField.setOnKeyReleased(event -> {
            try {
                observedCharacter.setProficiency(Integer.parseInt(charProficiencyTextField.getText()));
                for (Attack atk : observedCharacter.getAttackList()) {
                    atk.setAttackBonus(observedCharacter.getAbilityMod(atk.getAbility()) +
                            observedCharacter.getProficiency());
                }
            } catch (NumberFormatException e) {
                // No big deal
            }
            characterEditDisplay(observedCharacter);
        });

        charStrengthTextField.setOnKeyReleased(event -> {
            try {
                observedCharacter.getStats().setStrength(Integer.parseInt(charStrengthTextField.getText()));
                sbStrLabel.setText(observedCharacter.getStats().getModText("STR"));
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(observedCharacter);
        });

        charDexterityTextField.setOnKeyReleased(event -> {
            try {
                observedCharacter.getStats().setDexterity(Integer.parseInt(charDexterityTextField.getText()));
                sbDexLabel.setText(observedCharacter.getStats().getModText("DEX"));
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(observedCharacter);
        });

        charConstitutionTextField.setOnKeyReleased(event -> {
            try {
                observedCharacter.getStats().setConstitution(Integer.parseInt(charConstitutionTextField.getText()));
                sbConLabel.setText(observedCharacter.getStats().getModText("CON"));
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(observedCharacter);
        });

        charIntelligenceTextField.setOnKeyReleased(event -> {
            try {
                observedCharacter.getStats().setIntelligence(Integer.parseInt(charIntelligenceTextField.getText()));
                sbIntLabel.setText(observedCharacter.getStats().getModText("INT"));
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(observedCharacter);
        });

        charWisdomTextField.setOnKeyReleased(event -> {
            try {
                observedCharacter.getStats().setWisdom(Integer.parseInt(charWisdomTextField.getText()));
                sbWisLabel.setText(observedCharacter.getStats().getModText("WIS"));
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(observedCharacter);
        });

        charCharismaTextField.setOnKeyReleased(event -> {
            try {
                observedCharacter.getStats().setCharisma(Integer.parseInt(charCharismaTextField.getText()));
                sbChaLabel.setText(observedCharacter.getStats().getModText("CHA"));
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(observedCharacter);
        });

        charStrengthSaveTextField.setOnKeyReleased(event -> {
            try {
                observedCharacter.getSaves().setStrengthSave(Integer.parseInt(charStrengthSaveTextField.getText()));
                sbSavesLabel.setText(observedCharacter.getSaves().toString());
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(observedCharacter);
        });

        charDexteritySaveTextField.setOnKeyReleased(event -> {
            try {
                observedCharacter.getSaves().setDexteritySave(Integer.parseInt(charDexteritySaveTextField.getText()));
                sbSavesLabel.setText(observedCharacter.getSaves().toString());
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(observedCharacter);
        });

        charConstitutionSaveTextField.setOnKeyReleased(event -> {
            try {
                observedCharacter.getSaves().setConstitutionSave(Integer.parseInt(charConstitutionSaveTextField.getText()));
                sbSavesLabel.setText(observedCharacter.getSaves().toString());
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(observedCharacter);
        });

        charIntelligenceSaveTextField.setOnKeyReleased(event -> {
            try {
                observedCharacter.getSaves().setIntelligenceSave(Integer.parseInt(charIntelligenceSaveTextField.getText()));
                sbSavesLabel.setText(observedCharacter.getSaves().toString());
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(observedCharacter);
        });

        charWisdomSaveTextField.setOnKeyReleased(event -> {
            try {
                observedCharacter.getSaves().setWisdomSave(Integer.parseInt(charWisdomSaveTextField.getText()));
                sbSavesLabel.setText(observedCharacter.getSaves().toString());
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(observedCharacter);
        });

        charCharismaSaveTextField.setOnKeyReleased(event -> {
            try {
                observedCharacter.getSaves().setCharismaSave(Integer.parseInt(charCharismaSaveTextField.getText()));
                sbSavesLabel.setText(observedCharacter.getSaves().toString());
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(observedCharacter);
        });

        charSensesTextField.setOnKeyReleased(event -> {
            observedCharacter.setSenses(charSensesTextField.getText());
            sbSensesLabel.setText(observedCharacter.getSenses());
            characterEditDisplay(observedCharacter);
        });

        charLanguagesTextField.setOnKeyReleased(event -> {
            observedCharacter.setLanguages(charLanguagesTextField.getText());
            sbLanguagesLabel.setText(observedCharacter.getLanguages());
            characterEditDisplay(observedCharacter);
        });

        //Skills
        charSkillAddButton.setOnAction(event -> {
            Skill skill = new Skill();
            skill.setCharacterID(observedCharacter.getId());
            observedCharacter.addSkill(skill);
            selectedCharacterSkills.add(skill);
            characterEditDisplay(observedCharacter);
        });

        charSkillRemoveButton.setOnAction(event -> {
            Skill skill = skillTableView.getSelectionModel().getSelectedItem();
            try {
                skill.setToDelete(true);
                selectedCharacterSkills.remove(skill);
                characterEditDisplay(observedCharacter);
            } catch (NullPointerException e) {
                System.out.println("No Skill selected.");
            }
        });

        // Attacks
        charAttackAddButton.setOnAction(event -> {
            Attack atk = new Attack();
            atk.setCharacterID(observedCharacter.getId());
            atk.setAttackBonus(observedCharacter.getAbilityMod(atk.getAbility()) + observedCharacter.getProficiency()); //TODO: might need to change. NEED to decide if you want sql table to save the total (+prof) or just the abilitybonus, or NEITHER?!
            observedCharacter.addAttack(atk);
            selectedCharacterAttacks.add(atk);
            characterEditDisplay(observedCharacter);
        });

        charAttackRemoveButton.setOnAction(event -> {
            Attack atk = attackTableView.getSelectionModel().getSelectedItem();
            try {
                atk.setToDelete(true);
                selectedCharacterAttacks.remove(atk);
                characterEditDisplay(observedCharacter);
            } catch (NullPointerException e) {
                System.out.println("No attack selected");
            }
        });

        // Traits
        charTraitAddButton.setOnAction(event -> {
            Trait trait = new Trait();
            trait.setCharacterID(observedCharacter.getId());
            observedCharacter.addTrait(trait);
            selectedCharacterTraits.add(trait);
            characterEditDisplay(observedCharacter);
        });

        charTraitRemoveButton.setOnAction(event -> {
            Trait trait = traitTableView.getSelectionModel().getSelectedItem();
            try {
                trait.setToDelete(true);
                selectedCharacterTraits.remove(trait);
                characterEditDisplay(observedCharacter);
            } catch (NullPointerException e) {
                System.out.println("No trait selected");
            }
        });

        //Inventory
        charInventoryTextArea.setOnKeyReleased(event -> {
            observedCharacter.setInventory(charInventoryTextArea.getText());
            characterEditDisplay(observedCharacter);
        });

    }

    private void skillsTableViewSetup(ObservableList<Skill> skillsObservableList) {
        skillTableView.setColumnResizePolicy(param -> true);

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
        attackTableView.setColumnResizePolicy(param -> true);

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
            TablePosition<Attack, String> pos = event.getTablePosition();
            String newAbility = event.getNewValue();
            int row = pos.getRow();
            Attack atk = event.getTableView().getItems().get(row);
            atk.setAbility(newAbility);
            atk.setAttackBonus(observedCharacter.getAbilityMod(newAbility) + observedCharacter.getProficiency());
            characterEditDisplay(observedCharacter);
            System.out.println(atk.toString());
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

    private void traitsTableViewSetup(ObservableList<Trait> traitsObservableList) {
        traitTableView.setColumnResizePolicy(param -> true);

        traitNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        traitNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        traitNameColumn.setOnEditCommit(event -> {
            TablePosition<Trait, String> pos = event.getTablePosition();
            String newName = event.getNewValue();
            int row = pos.getRow();
            Trait trait = event.getTableView().getItems().get(row);
            trait.setName(newName);
            characterEditDisplay(observedCharacter);
        });

        traitDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        traitDescriptionColumn.setCellFactory(TextAreaTableCell.forTableColumn());
        traitDescriptionColumn.setOnEditCommit(event -> {
            TablePosition<Trait, String> pos = event.getTablePosition();
            String newName = event.getNewValue();
            int row = pos.getRow();
            Trait trait = event.getTableView().getItems().get(row);
            trait.setDescription(newName);
            characterEditDisplay(observedCharacter);
        });

        traitTableView.setItems(traitsObservableList);
    }

    private void setCharacterTraits(List<Character> characterList) {
        for (Character c : characterList) {
            c.setTraitList(traitTable.getDataByCharacter(c));
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

    private void setSkillsForStatblock(Character c) {
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

    private void setTraitsForStatblock(Character c) {
        statblockTraitsGridPane.getChildren().clear();
        List<Trait> traits = c.getTraitList();

        for (int i = 0; i < traits.size(); i++) {
            Trait t = traits.get(i);
            FlowPane fp = new FlowPane();

            //Prep the labels to insert
            Label nameLabel = new Label(t.getName() + ". ");
            nameLabel.setFont(Font.font("Georgia", FontWeight.BOLD, FontPosture.ITALIC, 12));
            Label descriptionLabel = new Label(t.getDescription());
            descriptionLabel.setFont(Font.font("Georgia"));

            // Add to flowpane
            fp.rowValignmentProperty().set(VPos.TOP);
            fp.getChildren().add(nameLabel);
            fp.getChildren().add(descriptionLabel);
            statblockTraitsGridPane.add(fp, 0, i);
        }
    }

    /**
     * Used to set the display on the statblock for attacks. Adjusts rows dynamically based on the number of attacks
     * the character possesses.
     *
     * @param c The character to retrieve the attacks from.
     */
    private void setAttacksForStatblock(Character c) {
        statblockAttacksGridPane.getChildren().clear();
        List<Attack> attacks = c.getAttackList();

        if (!attacks.isEmpty()) {
            Label attackTitleLabel = new Label("Attacks");

            attackTitleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
            statblockAttacksGridPane.add(attackTitleLabel, 0, 0);
        }

        for (int i = 0; i < attacks.size(); i++) {
            Attack a = attacks.get(i);
            System.out.println(a.toString());
            int damageBonus = c.getAbilityMod(a.getAbility());

            String attackBonusSymbol = "+";
            if (a.getAttackBonus() < 0) {
                attackBonusSymbol = "-";
            }
            String damageBonusSymbol = "+";
            if (damageBonus < 0) {
                damageBonusSymbol = "";
            }
            String rangeWording = "reach";
            if (a.getRange() > 10 ) {
                rangeWording = "range";
            }

            Label nameLabel = new Label(a.getName() + ". ");
            nameLabel.setFont(Font.font("Georgia", FontWeight.BOLD, FontPosture.ITALIC, 12));
            Label categoryLabel = new Label(a.getCategory() + " Weapon Attack: ");
            categoryLabel.setFont(Font.font("Georgia", FontPosture.ITALIC, 12));
            Label attackLabel = new Label(attackBonusSymbol + a.getAttackBonus() + " to hit, ");
            attackLabel.setFont(Font.font("Georgia", 12));
            Label rangeLabel = new Label(rangeWording + " " + a.getRange() + " ft., one target. ");
            rangeLabel.setFont(Font.font("Georgia", 12));
            Label hitLabel = new Label("Hit: ");
            hitLabel.setFont(Font.font("Georgia", FontPosture.ITALIC, 12));
            Label damageLabel = new Label(a.getNumDice() + "d" + a.getDamageDice() + " " + damageBonusSymbol + " " + damageBonus);
            damageLabel.setFont(Font.font("Georgia", 12));
            Label damageTypeLabel = new Label(" " + a.getDamageType() + " damage.");
            damageTypeLabel.setFont(Font.font("Georgia", 12));

            FlowPane fp = new FlowPane();
            fp.getChildren().add(nameLabel);
            fp.getChildren().add(categoryLabel);
            fp.getChildren().add(attackLabel);
            fp.getChildren().add(rangeLabel);
            fp.getChildren().add(hitLabel);
            fp.getChildren().add(damageLabel);
            fp.getChildren().add(damageTypeLabel);
            statblockAttacksGridPane.add(fp, 0, i + 1);
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
