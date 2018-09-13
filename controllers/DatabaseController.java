package main.controllers;

import main.assets.GuiTools;
import main.databases.AttackTable;
import main.databases.CharacterTable;
import main.databases.Database;
import main.databases.LocationTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import main.models.Attack;
import main.models.Character;
import main.models.Location;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseController {

    /* ------------------------------ NON-FXML FIELDS ------------------------------ */

    private LocationTable locationTable;
    private List<Location> masterLocations;
    private ObservableList<Location> filteredLocations;
    private ObservableList<Location> childLocations = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<Character> locationCharacters = FXCollections.observableArrayList(new ArrayList<>());

    private CharacterTable characterTable;
    private List<Character> masterCharacters;
    private ObservableList<Character> filteredCharacters;

    private AttackTable attackTable;
    private ObservableList<Attack> currentCharacterAttacks = FXCollections.observableArrayList();

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

    /* Character Attack Table*/
    @FXML private Button charAttackAddButton;
    @FXML private Button charAttackRemoveButton;

    @FXML private TableView<Attack> attackTableView;
    @FXML private TableColumn<Attack, String> attackNameColumn = new TableColumn<>();
    @FXML private TableColumn<Attack, String> attackCategoryColumn;
    @FXML private TableColumn<Attack, Integer> attackBonusColumn;
    @FXML private TableColumn<Attack, Integer> attackRangeColumn;
    @FXML private TableColumn<Attack, Integer> attackNumDiceColumn;
    @FXML private TableColumn<Attack, Integer> attackDiceTypeColumn;
    @FXML private TableColumn<Attack, String> attackDmgTypeColumn;


    public DatabaseController() { } /* Constructor for controller must be empty */

    /**
     * Used as a pseudo constructor for the controller to set up necessary variables
     * for the app. First sets up the models.Location tab, then the models.Character tab. Includes many event listeners for
     * each control in the tab.
     *
     * @param db the database
     */
    @FXML
    public void initialize(Database db) {
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

        // Prep the observable lists, set attack and location fields for each object in master list.
        masterCharacters = db.getCharacterTable().getAllData();
        setCharacterAttacks(masterCharacters);
        setCharacterLocations(masterCharacters, masterLocations);
        filteredCharacters = FXCollections.observableArrayList(masterCharacters);

        // Prep controls for Character tab
        characterTableViewSetup(filteredCharacters);
        attacksTableViewSetup(currentCharacterAttacks);

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

                    // Save Attacks
                    for (Attack atk : c.getAttackList()) {
                        if (atk.isSetToDelete()) {
                            attackTable.deleteData(atk);
                            continue;
                        }

                        if (atk.getId() != 0) { // Ensures that only atks which have ids already are updated, otherwise it can be assumed that they have just been added since the ID will be 0.
                            attackTable.updateData(atk);
                        } else {
                            attackTable.insertData(atk);
                        }
                    }

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
        });
        attackBonusColumn.setCellValueFactory(new PropertyValueFactory<>("attackBonus"));
        attackRangeColumn.setCellValueFactory(new PropertyValueFactory<>("range"));
        attackNumDiceColumn.setCellValueFactory(new PropertyValueFactory<>("numDice"));
        attackDiceTypeColumn.setCellValueFactory(new PropertyValueFactory<>("damageDice"));
        attackDmgTypeColumn.setCellValueFactory(new PropertyValueFactory<>("damageType"));

        attackTableView.setItems(attackObservableList);
    }

    /**
     * Fetch data from SQL table Attacks, assigns the attack list to each character based on ID.
     */
    private void setCharacterAttacks(List<Character> characterList) {
        for (Character c : characterList) {
            c.setAttackList(attackTable.getAttacksByCharacter(c));
        }
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
                    try {
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
                        charStrengthTextField.setText(String.valueOf(c.getStats().getStrength(0)));
                        charDexterityTextField.setText(String.valueOf(c.getStats().getDexterity(0)));
                        charConstitutionTextField.setText(String.valueOf(c.getStats().getConstitution(0)));
                        charIntelligenceTextField.setText(String.valueOf(c.getStats().getIntelligence(0)));
                        charWisdomTextField.setText(String.valueOf(c.getStats().getWisdom(0)));
                        charCharismaTextField.setText(String.valueOf(c.getStats().getCharisma(0)));
                        charStrengthSaveTextField.setText(String.valueOf(c.getSaveByIndex(0)));
                        charDexteritySaveTextField.setText(String.valueOf(c.getSaveByIndex(1)));
                        charConstitutionSaveTextField.setText(String.valueOf(c.getSaveByIndex(2)));
                        charIntelligenceSaveTextField.setText(String.valueOf(c.getSaveByIndex(3)));
                        charWisdomSaveTextField.setText(String.valueOf(c.getSaveByIndex(4)));
                        charCharismaSaveTextField.setText(String.valueOf(c.getSaveByIndex(5)));
                        charSensesTextField.setText(c.getSenses());
                        charLanguagesTextField.setText(c.getLanguages());

                        currentCharacterAttacks.clear();
                        for (Attack atk : c.getAttackList()) {
                            if (!atk.isSetToDelete()) {
                                currentCharacterAttacks.add(atk);
                            }
                        }

                        boolean editPending = false;
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
            } catch (SQLException e) {
                // Status update here
            }
            masterCharacters.remove(c);
            filteredCharacters.remove(c);
        });

        /* Listeners for character information entry */
        // Basic Info
        charNameTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            c.setName(charNameTextField.getText());
            charNameLabel.setText(c.getName());
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
            characterEditDisplay(c);
        });

        charArmorClassTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            c.setArmorClass(Integer.parseInt(charArmorClassTextField.getText()));
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
                c.setHitPointCurrent(Integer.parseInt(charHitPointMaxTextField.getText()));
            } catch (NumberFormatException e) {
                //
            }
            characterEditDisplay(c);
        });

        charSpeedTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            c.setSpeed(Integer.parseInt(charSpeedTextField.getText()));
            characterEditDisplay(c);
        });

        charStrengthTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            try {
                c.getStats().setStrength(Integer.parseInt(charStrengthTextField.getText()));
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(c);
        });

        charDexterityTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            try {
                c.getStats().setDexterity(Integer.parseInt(charDexterityTextField.getText()));
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(c);
        });

        charConstitutionTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            try {
                c.getStats().setConstitution(Integer.parseInt(charConstitutionTextField.getText()));
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(c);
        });

        charIntelligenceTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            try {
                c.getStats().setIntelligence(Integer.parseInt(charIntelligenceTextField.getText()));
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(c);
        });

        charWisdomTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            try {
                c.getStats().setWisdom(Integer.parseInt(charWisdomTextField.getText()));
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(c);
        });

        charCharismaTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            try {
                c.getStats().setCharisma(Integer.parseInt(charCharismaTextField.getText()));
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(c);
        });

        charStrengthSaveTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            try {
                c.getSaves()[0] = Integer.parseInt(charStrengthSaveTextField.getText());
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(c);
        });

        charDexteritySaveTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            try {
                c.getSaves()[1] = Integer.parseInt(charDexteritySaveTextField.getText());
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(c);
        });

        charConstitutionSaveTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            try {
                c.getSaves()[2] = Integer.parseInt(charConstitutionSaveTextField.getText());
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(c);
        });

        charIntelligenceSaveTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            try {
                c.getSaves()[3] = Integer.parseInt(charIntelligenceSaveTextField.getText());
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(c);
        });

        charWisdomSaveTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            try {
                c.getSaves()[4] = Integer.parseInt(charWisdomSaveTextField.getText());
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(c);
        });

        charCharismaSaveTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            try {
                c.getSaves()[5] = Integer.parseInt(charCharismaSaveTextField.getText());
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(c);
        });

        charSensesTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            c.setSenses(charSensesTextField.getText());
            characterEditDisplay(c);
        });

        charLanguagesTextField.setOnKeyReleased(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            c.setLanguages(charLanguagesTextField.getText());
            characterEditDisplay(c);
        });

        // Attacks
        charAttackAddButton.setOnAction(event -> { //NEED TO SET TABLE UPDATES IN SAVEALL
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            Attack atk = new Attack();
            atk.setCharacterID(c.getId());
            c.addAttack(atk);
            currentCharacterAttacks.add(atk);
            saveAllButton.setDisable(false);
            characterEditDisplay(c);
        });

        charAttackRemoveButton.setOnAction(event -> {
            Character c = characterTableView.getSelectionModel().getSelectedItem();
            Attack atk = attackTableView.getSelectionModel().getSelectedItem();
            atk.setToDelete(true);
            currentCharacterAttacks.remove(atk);
            saveAllButton.setDisable(false);
            characterEditDisplay(c);
        });
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
}
