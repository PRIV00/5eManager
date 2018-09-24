package main.model.characterfields;

import main.model.modeldata.ModelData;

public class Skill implements ModelData {

    private int id;
    private String name;
    private String ability;
    private int bonus;
    private int characterID;
    private boolean setToDelete = false;

    public Skill() {
        id = 0;
        name = "Athletics";
        ability = "STR";
        bonus = 0;
        characterID = 0;
    }

    public Skill(int id, String name, String ability, int bonus, int characterID) {
        this.id = id;
        this.name = name;
        this.ability = ability;
        this.bonus = bonus;
        this.characterID = characterID;
    }

    @Override
    public String toString() {
        return String.format("---SKILL---\nID: %d\n NAME: %s\nABILITY: %s\nBONUS: %d\ncharacterID: %d",
                id, name, ability, bonus, characterID);
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

    public String getAbility() {
        return ability;
    }

    public void setAbility(String skillName) {
        switch (skillName) {
            case "Athletics":
                ability = "STR";
                break;
            case "Acrobatics":
            case "Sleight of Hand":
            case "Stealth":
                ability = "DEX";
                break;
            case "Arcana":
            case "History":
            case "Investigation":
            case "Nature":
            case "Religion":
                ability = "INT";
                break;
            case "Animal Handling":
            case "Insight":
            case "Medicine":
            case "Perception":
            case "Survival":
                ability = "WIS";
                break;
            case "Deception":
            case "Intimidation":
            case "Performance":
            case "Persuasion":
                ability = "CHA";
                break;
        }
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public int getCharacterID() {
        return characterID;
    }

    public void setCharacterID(int characterID) {
        this.characterID = characterID;
    }

    public boolean isSetToDelete() {
        return setToDelete;
    }

    public void setToDelete(boolean setToDelete) {
        this.setToDelete = setToDelete;
    }
}
