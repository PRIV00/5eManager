package main.controllers;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.text.*;
import javafx.util.converter.IntegerStringConverter;
import main.assets.GuiTools;
import main.assets.TextAreaTableCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import main.model.MasterDataModel;
import main.model.characterfields.Attack;
import main.model.modeldata.Character;
import main.model.modeldata.Location;
import main.model.characterfields.Skill;
import main.model.characterfields.Trait;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TODO: Fix skill adding, trait and attack

public class DatabaseController {

    /* ------------------------------ MODEL FIELDS ------------------------------ */
    private MasterDataModel model;

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
     * for the app. First sets up the model.Location tab, then the model.Character tab. Includes many event listeners for
     * each control in the tab.
     *
     * @param model The model to retrieve data from
     */
    @FXML
    void initialize(MasterDataModel model) {

        this.model = model;

        /* ------------------------------ TOOLBAR SETUP ------------------------------ */
        ToolBar toolBar = MasterController.getMasterToolBar();
        toolBar.getItems().clear();
        toolBar.getItems().add(saveAllButton);
        saveAllButton.setDisable(true);
        setToolbarListeners();

        /* ------------------------------ LOCATION TAB SETUP ------------------------------ */

        // Prep controls for the Location Tab
        locationTableViewSetup(model.getFilteredLocations());
        updateLocationComboBoxes(new Location());

        // Set Location listeners and select the first row of the table view - ready to go!
        setLocationTabListeners();
        locationTableView.getSelectionModel().selectFirst();

        /* ------------------------------ CHARACTER TAB SETUP ------------------------------ */

        // Prep controls for Character tableviews
        characterTableViewSetup(model.getFilteredCharacters());
        skillsTableViewSetup();
        attacksTableViewSetup();
        traitsTableViewSetup();

        // Set Character listeners and select first row of the table view - ready to go!
        setCharacterTabListeners();
        characterTableView.getSelectionModel().selectFirst();
    }

    /**
     * Sets listeners for controls in the toolbar at the top of the View.
     */
    private void setToolbarListeners() {
        saveAllButton.setOnAction(event -> {
            model.saveAll();
            setSkillsForStatblock(model.getSelectedCharacter());
            setAttacksForStatblock(model.getSelectedCharacter());
            setTraitsForStatblock(model.getSelectedCharacter());
            saveAllButton.setDisable(true);
            characterTableView.refresh();
            locationTableView.refresh();
        });
    }

    /* ------------------------------ LOCATION TAB METHODS ------------------------------ */

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
     * Sets the model.Location tab listeners. Basically just put everything under here that only has a single listener to
     * avoid flooding the source code with dozens of methods.
     */
    private void setLocationTabListeners() {

        /* Left side display and tableview listeners */
        locationFilterTextField.setOnKeyReleased(event -> model.filterLocations(locationFilterTextField.getText()));

        locationAddButton.setOnAction(event -> {
            locationTableView.getSelectionModel().select(model.addNewLocation());
            saveAllButton.setDisable(false);
        });

        locationRemoveButton.setOnAction(event -> {
            model.removeLocation(model.getSelectedLocation());
            saveAllButton.setDisable(false);
        });

        locationTableView.getSelectionModel().selectedItemProperty().addListener(event -> {
            model.setSelectedLocation(locationTableView.getSelectionModel().getSelectedItem());

            childLocListView.setDisable(false);
            locCharListView.setDisable(false);
            locationAccordion.setDisable(false);
            try {
                updateLocationComboBoxes(model.getSelectedLocation());

                locNameLabel.setText(model.getSelectedLocation().getName());
                locTypeLabel.setText(model.getSelectedLocation().getLocType() + " ");
                try {
                    locParentNameLabel.setText(String.format("(%s)", GuiTools.trim(model.getSelectedLocation().getParent().getName())));
                } catch (NullPointerException e) {
                    locParentNameLabel.setText("");
                }
                locFlavourLabel.setText(model.getSelectedLocation().getFlavour());

                locNameTextField.setText(GuiTools.trim(model.getSelectedLocation().getName()));
                locTypeTextField.setText(model.getSelectedLocation().getLocType());
                locParentChoiceComboBox.setValue(model.getSelectedLocation().getParent());
                locFlavourTextField.setText(model.getSelectedLocation().getFlavour());

                locSongLinkTextField.setText(model.getSelectedLocation().getSongLink());
                locDescriptionTextArea.setText(model.getSelectedLocation().getDescription());
                locCultureTextArea.setText(model.getSelectedLocation().getCulture());
                locGovernmentTextArea.setText(model.getSelectedLocation().getGovernment());
                locCrimeTextArea.setText(model.getSelectedLocation().getCrime());
                locDemographicTextArea.setText(model.getSelectedLocation().getDemographic());
                locReligionTextArea.setText(model.getSelectedLocation().getReligion());
                locHistoryTextArea.setText(model.getSelectedLocation().getHistory());

                childLocListView.setItems(model.getSelectedLocation().getChildLocations());
                locCharListView.setItems(model.getSelectedLocation().getCharacters());
                setLocationDisplayTab();
            } catch (NullPointerException e) {
                childLocListView.setDisable(true);
                locCharListView.setDisable(true);
                locationAccordion.setDisable(true);
            }
        });

        /* Info entry control listeners*/
        locNameTextField.setOnKeyReleased(event -> {
            model.getSelectedLocation().setName(locNameTextField.getText());
            locNameLabel.setText(model.getSelectedLocation().getName() + " ");
            locationEditDisplay(model.getSelectedLocation());
        });

        locTypeTextField.setOnKeyReleased(event -> {
            model.getSelectedLocation().setLocType(locTypeTextField.getText());
            locTypeLabel.setText(model.getSelectedLocation().getLocType());
            locationEditDisplay(model.getSelectedLocation());
        });

        locParentChoiceComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (model.getSelectedLocation().getParentID() != newValue.getId()) {
                    model.getSelectedLocation().setParent(locParentChoiceComboBox.getValue());
                    locationEditDisplay(model.getSelectedLocation());
                    locParentNameLabel.setText(String.format("(%s)", model.getSelectedLocation().getParent().getName()));
                }
            } catch (NullPointerException e) {
                // Catches when the combobox clears
            }
        });

        locFlavourTextField.setOnKeyReleased(event -> {
            model.getSelectedLocation().setFlavour(locFlavourTextField.getText());
            locFlavourLabel.setText(model.getSelectedLocation().getFlavour());
            locationEditDisplay(model.getSelectedLocation());
        });

        locSongLinkTextField.setOnKeyReleased(event -> {
            model.getSelectedLocation().setSongLink(locSongLinkTextField.getText());
            locationEditDisplay(model.getSelectedLocation());
        });

        locDescriptionTextArea.setOnKeyReleased(event -> {
            model.getSelectedLocation().setDescription(locDescriptionTextArea.getText());
            locationEditDisplay(model.getSelectedLocation());
        });

        locCultureTextArea.setOnKeyReleased(event -> {
            model.getSelectedLocation().setCulture(locCultureTextArea.getText());
            locationEditDisplay(model.getSelectedLocation());
        });

        locGovernmentTextArea.setOnKeyReleased(event -> {
            model.getSelectedLocation().setGovernment(locGovernmentTextArea.getText());
            locationEditDisplay(model.getSelectedLocation());
        });

        locCrimeTextArea.setOnKeyReleased(event -> {
            model.getSelectedLocation().setCrime(locCrimeTextArea.getText());
            locationEditDisplay(model.getSelectedLocation());
        });

        locDemographicTextArea.setOnKeyReleased(event -> {
            model.getSelectedLocation().setDemographic(locDemographicTextArea.getText());
            locationEditDisplay(model.getSelectedLocation());
        });

        locReligionTextArea.setOnKeyReleased(event -> {
            model.getSelectedLocation().setReligion(locReligionTextArea.getText());
            locationEditDisplay(model.getSelectedLocation());
        });

        locHistoryTextArea.setOnKeyReleased(event -> {
            model.getSelectedLocation().setHistory(locHistoryTextArea.getText());
            locationEditDisplay(model.getSelectedLocation());
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
     * Functions updates the comboboxes on both the model.Location and Characters tab. Takes a location object to avoid
     * possibility of setting a model.Location's parent object to itself and starting an infinite loop.
     *
     * @param location : model.Location object to avoid adding to the combo box.
     */
    private void updateLocationComboBoxes(Location location) { //TODO: Have this update all controls that will change output based on parent change. I.e. also childLocationListView and childCharactersListView
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

        model.updateLocationHierarchies();

        for (Location loc : model.getMasterLocations()) {
            if (location != null && !(location.getInvalidParentIDs().contains(loc.getId()))) {
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

    private void setLocationDisplayTab() {
        locationDisplayGridPane.getChildren().clear();

        int rowCount = 0;
        if (!model.getSelectedLocation().getDescription().equals("")) {
            Label descriptionTitleLabel = new Label("Description");
            descriptionTitleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 16)); //TODO: CSSify
            descriptionTitleLabel.setPadding(new Insets(10, 0, 0, 0));
            locationDisplayGridPane.add(descriptionTitleLabel, 0, rowCount);
            rowCount += 1;

            Label descriptionLabel = new Label(model.getSelectedLocation().getDescription());
            descriptionLabel.setFont(Font.font("Georgia", 12));
            descriptionLabel.setWrapText(true);
            locationDisplayGridPane.add(descriptionLabel, 0, rowCount);
            rowCount +=1;
        }

        if (!model.getSelectedLocation().getCulture().equals("")) {
            Label cultureTitleLabel = new Label("Culture");
            cultureTitleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
            cultureTitleLabel.setPadding(new Insets(10, 0, 0, 0));
            locationDisplayGridPane.add(cultureTitleLabel, 0, rowCount);
            rowCount += 1;

            Label cultureLabel = new Label(model.getSelectedLocation().getCulture());
            cultureLabel.setFont(Font.font("Georgia", 12));
            cultureLabel.setWrapText(true);
            locationDisplayGridPane.add(cultureLabel, 0, rowCount);
            rowCount += 1;
        }

        if (!model.getSelectedLocation().getGovernment().equals("")) {
            Label governmentTitleLabel = new Label("Government");
            governmentTitleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
            governmentTitleLabel.setPadding(new Insets(10, 0, 0, 0));
            locationDisplayGridPane.add(governmentTitleLabel, 0, rowCount);
            rowCount += 1;

            Label governmentLabel = new Label(model.getSelectedLocation().getGovernment());
            governmentLabel.setFont(Font.font("Georgia", 12));
            governmentLabel.setWrapText(true);
            locationDisplayGridPane.add(governmentLabel, 0, rowCount);
            rowCount += 1;
        }

        if (!model.getSelectedLocation().getCrime().equals("")) {
            Label crimeTitleLabel = new Label("Crime");
            crimeTitleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
            crimeTitleLabel.setPadding(new Insets(10, 0, 0, 0));
            locationDisplayGridPane.add(crimeTitleLabel, 0, rowCount);
            rowCount += 1;

            Label crimeLabel = new Label(model.getSelectedLocation().getCrime());
            crimeLabel.setFont(Font.font("Georgia", 12));
            crimeLabel.setWrapText(true);
            locationDisplayGridPane.add(crimeLabel, 0, rowCount);
            rowCount += 1;
        }

        if (!model.getSelectedLocation().getDemographic().equals("")) {
            Label demographicTitleLabel = new Label("Demographic");
            demographicTitleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
            demographicTitleLabel.setPadding(new Insets(10, 0, 0, 0));
            locationDisplayGridPane.add(demographicTitleLabel, 0, rowCount);
            rowCount += 1;

            Label demographicLabel = new Label(model.getSelectedLocation().getDemographic());
            demographicLabel.setFont(Font.font("Georgia", 12));
            demographicLabel.setWrapText(true);
            locationDisplayGridPane.add(demographicLabel, 0, rowCount);
            rowCount += 1;
        }

        if (!model.getSelectedLocation().getReligion().equals("")) {
            Label religionTitleLabel = new Label("Religion");
            religionTitleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
            religionTitleLabel.setPadding(new Insets(10, 0, 0, 0));
            locationDisplayGridPane.add(religionTitleLabel, 0, rowCount);
            rowCount += 1;

            Label religionLabel = new Label(model.getSelectedLocation().getReligion());
            religionLabel.setFont(Font.font("Georgia", 12));
            religionLabel.setWrapText(true);
            locationDisplayGridPane.add(religionLabel, 0, rowCount);
            rowCount += 1;
        }

        if (!model.getSelectedLocation().getHistory().equals("")) {
            Label historyTitleLabel = new Label("History");
            historyTitleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
            historyTitleLabel.setPadding(new Insets(10, 0, 0, 0));
            locationDisplayGridPane.add(historyTitleLabel, 0, rowCount);
            rowCount += 1;

            Label historyLabel = new Label(model.getSelectedLocation().getHistory());
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
     * display, as well as listeners that set the model.Character's attributes based on key presses, allowing the user to
     * edit multiple rows and save them all at once.
     */
    private void setCharacterTabListeners() {

        /* Left side tableView, add/remove and filter field listeners */
        characterFilterTextField.setOnKeyReleased(event -> model.filterCharacters(characterFilterTextField.getText()));

        characterTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    model.setSelectedCharacter(characterTableView.getSelectionModel().getSelectedItem());

                    characterAccordion.setDisable(false);
                    if (model.getSelectedCharacter() == null) {
                        characterAccordion.setDisable(true);
                        return;
                    }

                    skillTableView.setItems(model.getSelectedCharacter().getActiveSkillList());
                    attackTableView.setItems(model.getSelectedCharacter().getActiveAttackList());
                    traitTableView.setItems(model.getSelectedCharacter().getActiveTraitList());

                    //Left side character display
                    charNameLabel.setText(GuiTools.trim(model.getSelectedCharacter().getName()));
                    charTitleLabel.setText(model.getSelectedCharacter().getTitle());
                    charLookLabel.setText(model.getSelectedCharacter().getLook());

                    charBackgroundLabel.setText(model.getSelectedCharacter().getBackground());
                    charKnowledgeLabel.setText(model.getSelectedCharacter().getKnowledge());
                    charOpinionLabel.setText(model.getSelectedCharacter().getOpinion());
                    charVoiceLabel.setText(model.getSelectedCharacter().getVoice());
                    charPersonalityLabel.setText(model.getSelectedCharacter().getPersonality());
                    charDesiresLabel.setText(model.getSelectedCharacter().getDesires());
                    charFearsLabel.setText(model.getSelectedCharacter().getFears());

                    // Stablock Display
                    sbNameLabel.setText(model.getSelectedCharacter().getName());
                    sbDescriptorLabel.setText(model.getSelectedCharacter().getDescriptor());
                    sbACLabel.setText(String.valueOf(model.getSelectedCharacter().getArmorClass()));
                    sbCurrentHPTextField.setText(String.valueOf(model.getSelectedCharacter().getHitPointCurrent()));
                    sbMaxHPLabel.setText(String.valueOf(model.getSelectedCharacter().getHitPointMax()));
                    sbSpeedLabel.setText(String.valueOf(model.getSelectedCharacter().getSpeed()) + " ft.");

                    sbStrLabel.setText(model.getSelectedCharacter().getStats().getModText("STR"));
                    sbDexLabel.setText(model.getSelectedCharacter().getStats().getModText("DEX"));
                    sbConLabel.setText(model.getSelectedCharacter().getStats().getModText("CON"));
                    sbIntLabel.setText(model.getSelectedCharacter().getStats().getModText("INT"));
                    sbWisLabel.setText(model.getSelectedCharacter().getStats().getModText("WIS"));
                    sbChaLabel.setText(model.getSelectedCharacter().getStats().getModText("CHA"));
                    sbSavesLabel.setText(model.getSelectedCharacter().getSaves().toString());

                    sbSensesLabel.setText(model.getSelectedCharacter().getSenses());
                    sbLanguagesLabel.setText(model.getSelectedCharacter().getLanguages());

                    setSkillsForStatblock(model.getSelectedCharacter());
                    setTraitsForStatblock(model.getSelectedCharacter());
                    setAttacksForStatblock(model.getSelectedCharacter());

                    // Character Entry
                    charNameTextField.setText(GuiTools.trim(model.getSelectedCharacter().getName()));
                    charTitleTextField.setText(model.getSelectedCharacter().getTitle());
                    charRaceTextField.setText(model.getSelectedCharacter().getRace());
                    charLocationComboBox.setValue(model.getSelectedCharacter().getLocation());

                    charVoiceTextField.setText(model.getSelectedCharacter().getVoice());
                    charPersonalityTextField.setText(model.getSelectedCharacter().getPersonality());
                    charDesiresTextField.setText(model.getSelectedCharacter().getDesires());
                    charFearsTextField.setText(model.getSelectedCharacter().getFears());
                    charLookTextArea.setText(model.getSelectedCharacter().getLook());
                    charBackgroundTextArea.setText(model.getSelectedCharacter().getBackground());
                    charKnowledgeTextArea.setText(model.getSelectedCharacter().getKnowledge());
                    charOpinionTextArea.setText(model.getSelectedCharacter().getOpinion());

                    charDescriptorTextField.setText(model.getSelectedCharacter().getDescriptor());
                    charArmorClassTextField.setText(String.valueOf(model.getSelectedCharacter().getArmorClass()));
                    charArmorTextField.setText(model.getSelectedCharacter().getArmor());
                    charHitPointMaxTextField.setText(String.valueOf(model.getSelectedCharacter().getHitPointMax()));
                    charSpeedTextField.setText(String.valueOf(model.getSelectedCharacter().getSpeed()));
                    charProficiencyTextField.setText(String.valueOf(model.getSelectedCharacter().getProficiency()));
                    charStrengthTextField.setText(String.valueOf(model.getSelectedCharacter().getStats().getStrength(0)));
                    charDexterityTextField.setText(String.valueOf(model.getSelectedCharacter().getStats().getDexterity(0)));
                    charConstitutionTextField.setText(String.valueOf(model.getSelectedCharacter().getStats().getConstitution(0)));
                    charIntelligenceTextField.setText(String.valueOf(model.getSelectedCharacter().getStats().getIntelligence(0)));
                    charWisdomTextField.setText(String.valueOf(model.getSelectedCharacter().getStats().getWisdom(0)));
                    charCharismaTextField.setText(String.valueOf(model.getSelectedCharacter().getStats().getCharisma(0)));
                    charStrengthSaveTextField.setText(String.valueOf(model.getSelectedCharacter().getSaves().getStrengthSave()));
                    charDexteritySaveTextField.setText(String.valueOf(model.getSelectedCharacter().getSaves().getDexteritySave()));
                    charConstitutionSaveTextField.setText(String.valueOf(model.getSelectedCharacter().getSaves().getConstitutionSave()));
                    charIntelligenceSaveTextField.setText(String.valueOf(model.getSelectedCharacter().getSaves().getIntelligenceSave()));
                    charWisdomSaveTextField.setText(String.valueOf(model.getSelectedCharacter().getSaves().getWisdomSave()));
                    charCharismaSaveTextField.setText(String.valueOf(model.getSelectedCharacter().getSaves().getCharismaSave()));
                    charSensesTextField.setText(model.getSelectedCharacter().getSenses());
                    charLanguagesTextField.setText(model.getSelectedCharacter().getLanguages());
                    charInventoryTextArea.setText(model.getSelectedCharacter().getInventory());

                    boolean editPending = false; //check if any other characters have edits pending.
                    for (Character c2 : model.getFilteredCharacters()) {
                        if (c2.isEdited()) {
                            editPending = true;
                            break;
                        }
                    }
                    for (Location loc : model.getFilteredLocations()) {
                        if (loc.isEdited()) {
                            editPending = true;
                            break;
                        }
                    }
                    if (!editPending) {
                        saveAllButton.setDisable(true);
                    }
                });

        characterAddButton.setOnAction(event -> {
            model.addNewCharacter();
            characterTableView.getSelectionModel().select(model.getSelectedCharacter());
            saveAllButton.setDisable(false);
        });

        characterRemoveButton.setOnAction(event -> {
            model.removeCharacter(model.getSelectedCharacter());
            saveAllButton.setDisable(false);
        });

        /* Listeners for statblock */
        sbCurrentHPTextField.setOnKeyReleased(event -> {
            model.getSelectedCharacter().setHitPointCurrent(Integer.parseInt(sbCurrentHPTextField.getText()));
            characterEditDisplay(model.getSelectedCharacter());
        });

        /* Listeners for character information entry */
        // Basic Info
        charNameTextField.setOnKeyReleased(event -> {
            model.getSelectedCharacter().setName(charNameTextField.getText());
            charNameLabel.setText(model.getSelectedCharacter().getName());
            sbNameLabel.setText(model.getSelectedCharacter().getName());
            characterEditDisplay(model.getSelectedCharacter());
        });

        charTitleTextField.setOnKeyReleased(event -> {
            model.getSelectedCharacter().setTitle(charTitleTextField.getText());
            charTitleLabel.setText(model.getSelectedCharacter().getTitle());
            characterEditDisplay(model.getSelectedCharacter());
        });

        charRaceTextField.setOnKeyReleased(event -> {
            model.getSelectedCharacter().setRace(charRaceTextField.getText());
            characterEditDisplay(model.getSelectedCharacter());
        });

        charLocationComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (model.getSelectedCharacter().getLocationID() != newValue.getId()) {
                    model.getSelectedCharacter().setLocation(charLocationComboBox.getValue());
                    characterEditDisplay(model.getSelectedCharacter());
                }
            } catch (NullPointerException e) {
                //
            }
        });

        // Personality & Description
        charVoiceTextField.setOnKeyReleased(event -> {
            model.getSelectedCharacter().setVoice(charVoiceTextField.getText());
            charVoiceLabel.setText(model.getSelectedCharacter().getVoice());
            characterEditDisplay(model.getSelectedCharacter());
        });

        charPersonalityTextField.setOnKeyReleased(event -> {
            model.getSelectedCharacter().setPersonality(charPersonalityTextField.getText());
            charPersonalityLabel.setText(model.getSelectedCharacter().getPersonality());
            characterEditDisplay(model.getSelectedCharacter());
        });

        charDesiresTextField.setOnKeyReleased(event -> {
            model.getSelectedCharacter().setDesires(charDesiresTextField.getText());
            charDesiresLabel.setText(model.getSelectedCharacter().getDesires());
            characterEditDisplay(model.getSelectedCharacter());
        });

        charFearsTextField.setOnKeyReleased(event -> {
            model.getSelectedCharacter().setFears(charFearsTextField.getText());
            charFearsLabel.setText(model.getSelectedCharacter().getFears());
            characterEditDisplay(model.getSelectedCharacter());
        });

        charLookTextArea.setOnKeyReleased(event -> {
            model.getSelectedCharacter().setLook(charLookTextArea.getText());
            charLookLabel.setText(model.getSelectedCharacter().getLook());
            characterEditDisplay(model.getSelectedCharacter());
        });

        charBackgroundTextArea.setOnKeyReleased(event -> {
            model.getSelectedCharacter().setBackground(charBackgroundTextArea.getText());
            charBackgroundLabel.setText(model.getSelectedCharacter().getBackground());
            characterEditDisplay(model.getSelectedCharacter());
        });

        charKnowledgeTextArea.setOnKeyReleased(event -> {
            model.getSelectedCharacter().setKnowledge(charKnowledgeTextArea.getText());
            charKnowledgeLabel.setText(model.getSelectedCharacter().getKnowledge());
            characterEditDisplay(model.getSelectedCharacter());
        });

        charOpinionTextArea.setOnKeyReleased(event -> {
            model.getSelectedCharacter().setOpinion(charOpinionTextArea.getText());
            charOpinionLabel.setText(model.getSelectedCharacter().getOpinion());
            characterEditDisplay(model.getSelectedCharacter());
        });

        // Statblock
        charDescriptorTextField.setOnKeyReleased(event -> {
            model.getSelectedCharacter().setDescriptor(charDescriptorTextField.getText());
            sbDescriptorLabel.setText(model.getSelectedCharacter().getDescriptor());
            characterEditDisplay(model.getSelectedCharacter());
        });

        charArmorClassTextField.setOnKeyReleased(event -> {
            model.getSelectedCharacter().setArmorClass(Integer.parseInt(charArmorClassTextField.getText()));
            sbACLabel.setText(String.valueOf(model.getSelectedCharacter().getArmorClass()));
            characterEditDisplay(model.getSelectedCharacter());
        });

        charArmorTextField.setOnKeyReleased(event -> {
            model.getSelectedCharacter().setArmor(charArmorTextField.getText());
            characterEditDisplay(model.getSelectedCharacter());
        });

        charHitPointMaxTextField.setOnKeyReleased(event -> {
            try {
                model.getSelectedCharacter().setHitPointMax(Integer.parseInt(charHitPointMaxTextField.getText()));
                sbMaxHPLabel.setText(String.valueOf(model.getSelectedCharacter().getHitPointMax()));
            } catch (NumberFormatException e) {
                //
            }
            characterEditDisplay(model.getSelectedCharacter());
        });

        charSpeedTextField.setOnKeyReleased(event -> {
            model.getSelectedCharacter().setSpeed(Integer.parseInt(charSpeedTextField.getText()));
            sbSpeedLabel.setText(String.valueOf(model.getSelectedCharacter().getSpeed()) + " ft.");
            characterEditDisplay(model.getSelectedCharacter());
        });

        charProficiencyTextField.setOnKeyReleased(event -> {
            try {
                model.getSelectedCharacter().setProficiency(Integer.parseInt(charProficiencyTextField.getText()));
                for (Attack atk : model.getSelectedCharacter().getAttackList()) {
                    atk.setAttackBonus(model.getSelectedCharacter().getAbilityMod(atk.getAbility()) +
                            model.getSelectedCharacter().getProficiency());
                }
            } catch (NumberFormatException e) {
                // No big deal
            }
            characterEditDisplay(model.getSelectedCharacter());
        });

        charStrengthTextField.setOnKeyReleased(event -> {
            try {
                model.getSelectedCharacter().getStats().setStrength(Integer.parseInt(charStrengthTextField.getText()));
                sbStrLabel.setText(model.getSelectedCharacter().getStats().getModText("STR"));
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(model.getSelectedCharacter());
        });

        charDexterityTextField.setOnKeyReleased(event -> {
            try {
                model.getSelectedCharacter().getStats().setDexterity(Integer.parseInt(charDexterityTextField.getText()));
                sbDexLabel.setText(model.getSelectedCharacter().getStats().getModText("DEX"));
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(model.getSelectedCharacter());
        });

        charConstitutionTextField.setOnKeyReleased(event -> {
            try {
                model.getSelectedCharacter().getStats().setConstitution(Integer.parseInt(charConstitutionTextField.getText()));
                sbConLabel.setText(model.getSelectedCharacter().getStats().getModText("CON"));
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(model.getSelectedCharacter());
        });

        charIntelligenceTextField.setOnKeyReleased(event -> {
            try {
                model.getSelectedCharacter().getStats().setIntelligence(Integer.parseInt(charIntelligenceTextField.getText()));
                sbIntLabel.setText(model.getSelectedCharacter().getStats().getModText("INT"));
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(model.getSelectedCharacter());
        });

        charWisdomTextField.setOnKeyReleased(event -> {
            try {
                model.getSelectedCharacter().getStats().setWisdom(Integer.parseInt(charWisdomTextField.getText()));
                sbWisLabel.setText(model.getSelectedCharacter().getStats().getModText("WIS"));
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(model.getSelectedCharacter());
        });

        charCharismaTextField.setOnKeyReleased(event -> {
            try {
                model.getSelectedCharacter().getStats().setCharisma(Integer.parseInt(charCharismaTextField.getText()));
                sbChaLabel.setText(model.getSelectedCharacter().getStats().getModText("CHA"));
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(model.getSelectedCharacter());
        });

        charStrengthSaveTextField.setOnKeyReleased(event -> {
            try {
                model.getSelectedCharacter().getSaves().setStrengthSave(Integer.parseInt(charStrengthSaveTextField.getText()));
                sbSavesLabel.setText(model.getSelectedCharacter().getSaves().toString());
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(model.getSelectedCharacter());
        });

        charDexteritySaveTextField.setOnKeyReleased(event -> {
            try {
                model.getSelectedCharacter().getSaves().setDexteritySave(Integer.parseInt(charDexteritySaveTextField.getText()));
                sbSavesLabel.setText(model.getSelectedCharacter().getSaves().toString());
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(model.getSelectedCharacter());
        });

        charConstitutionSaveTextField.setOnKeyReleased(event -> {
            try {
                model.getSelectedCharacter().getSaves().setConstitutionSave(Integer.parseInt(charConstitutionSaveTextField.getText()));
                sbSavesLabel.setText(model.getSelectedCharacter().getSaves().toString());
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(model.getSelectedCharacter());
        });

        charIntelligenceSaveTextField.setOnKeyReleased(event -> {
            try {
                model.getSelectedCharacter().getSaves().setIntelligenceSave(Integer.parseInt(charIntelligenceSaveTextField.getText()));
                sbSavesLabel.setText(model.getSelectedCharacter().getSaves().toString());
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(model.getSelectedCharacter());
        });

        charWisdomSaveTextField.setOnKeyReleased(event -> {
            try {
                model.getSelectedCharacter().getSaves().setWisdomSave(Integer.parseInt(charWisdomSaveTextField.getText()));
                sbSavesLabel.setText(model.getSelectedCharacter().getSaves().toString());
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(model.getSelectedCharacter());
        });

        charCharismaSaveTextField.setOnKeyReleased(event -> {
            try {
                model.getSelectedCharacter().getSaves().setCharismaSave(Integer.parseInt(charCharismaSaveTextField.getText()));
                sbSavesLabel.setText(model.getSelectedCharacter().getSaves().toString());
            } catch (NumberFormatException e) {
                // Ignore for now. Catch it if it tries to save later.
            }
            characterEditDisplay(model.getSelectedCharacter());
        });

        charSensesTextField.setOnKeyReleased(event -> {
            model.getSelectedCharacter().setSenses(charSensesTextField.getText());
            sbSensesLabel.setText(model.getSelectedCharacter().getSenses());
            characterEditDisplay(model.getSelectedCharacter());
        });

        charLanguagesTextField.setOnKeyReleased(event -> {
            model.getSelectedCharacter().setLanguages(charLanguagesTextField.getText());
            sbLanguagesLabel.setText(model.getSelectedCharacter().getLanguages());
            characterEditDisplay(model.getSelectedCharacter());
        });

        //Skills
        charSkillAddButton.setOnAction(event -> {
            Skill skill = new Skill();
            skill.setCharacterID(model.getSelectedCharacter().getId());
            model.getSelectedCharacter().addSkill(skill);
            characterEditDisplay(model.getSelectedCharacter());
        });

        charSkillRemoveButton.setOnAction(event -> {
            Skill skill = skillTableView.getSelectionModel().getSelectedItem();
            try {
                model.getSelectedCharacter().removeSkill(skill);
                characterEditDisplay(model.getSelectedCharacter());
            } catch (NullPointerException e) {
                System.out.println("No Skill selected.");
            }
        });

        // Attacks
        charAttackAddButton.setOnAction(event -> {
            Attack atk = new Attack();
            atk.setCharacterID(model.getSelectedCharacter().getId());
            atk.setAttackBonus(model.getSelectedCharacter().getAbilityMod(atk.getAbility()) + model.getSelectedCharacter().getProficiency()); //TODO: might need to change. NEED to decide if you want sql table to save the total (+prof) or just the abilitybonus, or NEITHER?!
            model.getSelectedCharacter().addAttack(atk);
            characterEditDisplay(model.getSelectedCharacter());
        });

        charAttackRemoveButton.setOnAction(event -> {
            Attack atk = attackTableView.getSelectionModel().getSelectedItem();
            try {
                model.getSelectedCharacter().removeAttack(atk);
                characterEditDisplay(model.getSelectedCharacter());
            } catch (NullPointerException e) {
                System.out.println("No attack selected");
            }
        });

        // Traits
        charTraitAddButton.setOnAction(event -> {
            Trait trait = new Trait();
            trait.setCharacterID(model.getSelectedCharacter().getId());
            model.getSelectedCharacter().addTrait(trait);
            characterEditDisplay(model.getSelectedCharacter());
        });

        charTraitRemoveButton.setOnAction(event -> {
            Trait trait = traitTableView.getSelectionModel().getSelectedItem();
            try {
                model.getSelectedCharacter().removeTrait(trait);
                characterEditDisplay(model.getSelectedCharacter());
            } catch (NullPointerException e) {
                System.out.println("No trait selected");
            }
        });

        //Inventory
        charInventoryTextArea.setOnKeyReleased(event -> {
            model.getSelectedCharacter().setInventory(charInventoryTextArea.getText());
            characterEditDisplay(model.getSelectedCharacter());
        });

    }

    private void skillsTableViewSetup() {
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
    }

    /**
     * Initializes the smaller tableView for Attacks. More advanced CellFactories here since I wanted the cells
     * to be editable directly on the table itself.
     *
     */
    private void attacksTableViewSetup() {
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
            atk.setAttackBonus(model.getSelectedCharacter().getAbilityMod(newAbility) + model.getSelectedCharacter().getProficiency());
            characterEditDisplay(model.getSelectedCharacter());
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
    }

    private void traitsTableViewSetup() {
        traitTableView.setColumnResizePolicy(param -> true);

        traitNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        traitNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        traitNameColumn.setOnEditCommit(event -> {
            TablePosition<Trait, String> pos = event.getTablePosition();
            String newName = event.getNewValue();
            int row = pos.getRow();
            Trait trait = event.getTableView().getItems().get(row);
            trait.setName(newName);
            characterEditDisplay(model.getSelectedCharacter());
        });

        traitDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        traitDescriptionColumn.setCellFactory(TextAreaTableCell.forTableColumn());
        traitDescriptionColumn.setOnEditCommit(event -> {
            TablePosition<Trait, String> pos = event.getTablePosition();
            String newName = event.getNewValue();
            int row = pos.getRow();
            Trait trait = event.getTableView().getItems().get(row);
            trait.setDescription(newName);
            characterEditDisplay(model.getSelectedCharacter());
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

    private void setSkillsForStatblock(Character c) {
        List<Skill> skills = c.getActiveSkillList();

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
        List<Trait> traits = c.getActiveTraitList();

        for (int i = 0; i < traits.size(); i++) {
            Trait t = traits.get(i);
            TextFlow tf = new TextFlow();

            //Prep the labels to insert
            Text nameLabel = new Text(t.getName() + ". ");
            nameLabel.setStyle("-fx-font-family: ScalySans; -fx-font-size: 10pt; -fx-font-weight: bold;");
            Text descriptionLabel = new Text(t.getDescription());
            descriptionLabel.setStyle("-fx-font-family: ScalySans; -fx-font-size: 10pt;");

            // Add to flowpane
            tf.getChildren().add(nameLabel);
            tf.getChildren().add(descriptionLabel);
            if (i > 0) {
                tf.setPadding(new Insets(10, 0, 0, 0));
            }

            statblockTraitsGridPane.add(tf, 0, i);
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
        List<Attack> attacks = c.getActiveAttackList();

        if (!attacks.isEmpty()) {
            Label attackTitleLabel = new Label("Attacks");

            attackTitleLabel.setStyle("-fx-font-family: \"Scaly Sans Caps\"; -fx-font-size: 14pt;");
            statblockAttacksGridPane.add(attackTitleLabel, 0, 0);
        }

        for (int i = 0; i < attacks.size(); i++) {
            Attack a = attacks.get(i);
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
            Text nameLabel = new Text(a.getName() + ". ");
            nameLabel.setStyle("-fx-font-family: ScalySans; -fx-font-size: 10pt; -fx-font-weight: bold; -fx-font-style: italic;");
            Text categoryLabel = new Text(a.getCategory() + " Weapon Attack: ");
            categoryLabel.setStyle("-fx-font-family: ScalySans; -fx-font-size: 10pt; -fx-font-style: italic;");
            Text attackLabel = new Text(attackBonusSymbol + a.getAttackBonus() + " to hit, ");
            attackLabel.setStyle("-fx-font-family: ScalySans; -fx-font-size: 10pt;");
            Text rangeLabel = new Text(rangeWording + " " + a.getRange() + " ft., one target. ");
            rangeLabel.setStyle("-fx-font-family: ScalySans; -fx-font-size: 10pt;");
            Text hitLabel = new Text("Hit: ");
            hitLabel.setStyle("-fx-font-family: ScalySans; -fx-font-size: 10pt; -fx-font-style: italic;");
            Text damageLabel = new Text(a.getNumDice() + "d" + a.getDamageDice() + " " + damageBonusSymbol + " " + damageBonus);
            damageLabel.setStyle("-fx-font-family: ScalySans; -fx-font-size: 10pt;");
            Text damageTypeLabel = new Text(" " + a.getDamageType() + " damage.");
            damageTypeLabel.setStyle("-fx-font-family: ScalySans; -fx-font-size: 10pt;");

            TextFlow tf = new TextFlow();
            tf.setPadding(new Insets(10, 0, 0, 0));
            tf.getChildren().add(nameLabel);
            tf.getChildren().add(categoryLabel);
            tf.getChildren().add(attackLabel);
            tf.getChildren().add(rangeLabel);
            tf.getChildren().add(hitLabel);
            tf.getChildren().add(damageLabel);
            tf.getChildren().add(damageTypeLabel);
            statblockAttacksGridPane.add(tf, 0, i + 1);
        }
    }
}
