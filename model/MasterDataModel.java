/**
 * https://stevemichelotti.com/aspnet-mvc-view-model-patterns/
 */

package main.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.assets.GuiTools;
import main.databases.Database;
import main.databases.tables.*;
import main.model.characterfields.Attack;
import main.model.characterfields.Skill;
import main.model.characterfields.Trait;
import main.model.modeldata.Character;
import main.model.modeldata.Location;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MasterDataModel {


    private LocationTable locationTable;
    private CharacterTable characterTable;
    private SkillTable skillTable;
    private AttackTable attackTable;
    private TraitTable traitTable;

    private List<Location> masterLocations;
    private List<Character> masterCharacters;

    private ObservableList<Location> filteredLocations;
    private ObservableList<Character> filteredCharacters;

    private Location selectedLocation;
    private Character selectedCharacter;

    public MasterDataModel(Database db) {
        locationTable = db.getLocationTable();
        characterTable = db.getCharacterTable();
        skillTable = db.getSkillTable();
        attackTable = db.getAttackTable();
        traitTable = db.getTraitTable();

        masterLocations = locationTable.getAllData();
        masterCharacters = characterTable.getAllData();
        initLocations();
        initCharacters();

        filteredLocations = FXCollections.observableArrayList(masterLocations);
        filteredCharacters = FXCollections.observableArrayList(masterCharacters);

        try {
            selectedLocation = filteredLocations.get(0);
        } catch (IndexOutOfBoundsException e) {
            selectedLocation = new Location(); // Required for new databases.
        }
        try {
            selectedCharacter = filteredCharacters.get(0);
        } catch (IndexOutOfBoundsException e) {
            selectedCharacter = new Character();
        }
    }

    /**
     * Sets the objects to appropriate parents based on ID.
     *
     */
    private void initLocations() {
        // First pass to set parents
        for (Location x : masterLocations) {
            for (Location y : masterLocations) {
                if (x.getParentID() == y.getId()) {
                    x.setParent(y);
                    break;
                }
            }
        }

        // Second pass to set child locations and characters
        for (Location x : masterLocations) {
            setChildLocations(x.getChildLocations(), x);
            getCharacterHierarchy(x.getCharacters(), x);
            x.setInvalidParentIDs(invalidParents(x));
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

        for (Location loc : topLocation.getChildLocations()) {
            invalidIDs.add(loc.getId());
        }

        return invalidIDs;
    } //TODO: fix. need to call it each time you update parent combo box

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
    private void setChildLocations(List<Location> recursiveList, Location anchorLocation) {
        try {
            for (Location loc : masterLocations) {
                if (loc.getParentID() == anchorLocation.getId()) {
                    recursiveList.add(loc);
                    setChildLocations(recursiveList, loc);
                }
            }
        } catch (NullPointerException e) {
            System.out.println("NullPointerException");
        }
    }

    private void getCharacterHierarchy(List<Character> recursiveList, Location anchorLocation) {
        for (Character c : masterCharacters) {
            if (c.getLocationID() == anchorLocation.getId()) {
                recursiveList.add(c);
            }
        }
        for (Location loc : masterLocations) {
            if (loc.getParentID() == anchorLocation.getId()) {
                getCharacterHierarchy(recursiveList, loc);
            }
        }
    }

    private void initCharacters() {
        for (Character c : masterCharacters) {
            // Set location for each character.
            for (Location loc : masterLocations) {
                if (c.getLocationID() == loc.getId()) {
                    c.setLocation(loc);
                    break;
                }
            }

            c.setSkillList(skillTable.getDataByCharacter(c));
            c.setTraitList(traitTable.getDataByCharacter(c));
            c.setAttackList(attackTable.getDataByCharacter(c));
        }
    }

    public void saveAll() {
        //Locations
        for (Location loc : filteredLocations) {
            if (loc.isEdited()) {
                loc.setName(GuiTools.trim(loc.getName()));
                try {
                    locationTable.updateData(loc);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                loc.setEdited(false);
            }
        }

        // Characters
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
                ObservableList<Skill> activeSkills = FXCollections.observableArrayList(new ArrayList<>()); // Not a huge fan of this way of doing it. Want to look into writing something cleaner.
                System.out.println("skill size: " + c.getActiveSkillList().size());
                for (Skill skill : c.getSkillList()) {
                    System.out.println(skill.isSetToDelete());
                    if (skill.isSetToDelete()) {
                        skillTable.deleteData(skill);
                        System.out.println("data deleted");
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
                ObservableList<Attack> activeAttacks = FXCollections.observableArrayList(new ArrayList<>());
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
                ObservableList<Trait> activeTraits = FXCollections.observableArrayList(new ArrayList<>());
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
    }

    public LocationTable getLocationTable() {
        return locationTable;
    }

    public CharacterTable getCharacterTable() {
        return characterTable;
    }

    public SkillTable getSkillTable() {
        return skillTable;
    }

    public AttackTable getAttackTable() {
        return attackTable;
    }

    public TraitTable getTraitTable() {
        return traitTable;
    }

    public List<Location> getMasterLocations() {
        return masterLocations;
    }

    public List<Character> getMasterCharacters() {
        return masterCharacters;
    }

    public ObservableList<Location> getFilteredLocations() {
        return filteredLocations;
    }

    public ObservableList<Character> getFilteredCharacters() {
        return filteredCharacters;
    }

    public Location getSelectedLocation() {
        return selectedLocation;
    }

    public void setSelectedLocation(Location selectedLocation) {
        this.selectedLocation = selectedLocation;
    }

    public Character getSelectedCharacter() {
        return selectedCharacter;
    }

    public void setSelectedCharacter(Character selectedCharacter) {
        this.selectedCharacter = selectedCharacter;
    }

    public void filterLocations(String filter) {
        filteredLocations.clear();
        filteredLocations.addAll(locationTable.query(filter));

        for (Location loc : filteredLocations) {
            for (Location i : masterLocations) {
                if (loc.getParentID() == i.getId()) {
                    loc.setParent(i);
                    break;
                }
            }
        }
    }

    public void filterCharacters(String filter) {
        filteredCharacters.clear();
        filteredCharacters.addAll(characterTable.query(filter));
        for (Character c : filteredCharacters) {
            for (Location i : masterLocations) {
                if (c.getLocationID() == i.getId()) {
                    c.setLocation(i);
                    break;
                }
            }
        }
    }

    public void addLocationToLists(Location location) { //TODO: use this as a way to easily add and remove from both lists and not yet effect the database until user hits save.
        masterLocations.add(location);
        filteredLocations.add(location);
    }

    public void removeLocationFromLists(Location location) {
        masterLocations.remove(location);
        filteredLocations.remove(location);
    }

    public void addCharacterToLists(Character character) {
        masterCharacters.add(character);
        filteredCharacters.add(character);
    }

    public void removeCharacterFromLists(Character character) {
        masterCharacters.remove(character);
        filteredCharacters.remove(character);
    }
}
