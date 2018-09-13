package main.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Attack implements DataModel {

    private int id;
    private SimpleStringProperty name;
    private SimpleStringProperty category;
    private SimpleIntegerProperty attackBonus;
    private SimpleIntegerProperty range;
    private SimpleIntegerProperty numDice;
    private SimpleIntegerProperty damageDice;
    private SimpleStringProperty damageType;
    private int characterID;
    private boolean setToDelete = false;

    public Attack() {
        id = 0;
        name = new SimpleStringProperty("");
        category = new SimpleStringProperty("Melee");
        attackBonus = new SimpleIntegerProperty(0);
        range = new SimpleIntegerProperty(0);
        numDice = new SimpleIntegerProperty(0);
        damageDice = new SimpleIntegerProperty(0);
        damageType = new SimpleStringProperty("");
        characterID = 0;
    }


    public Attack(int id, String name, String category, int attackBonus, int range, int numDice, int damageDice, String
                  damageType, int characterID) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.category = new SimpleStringProperty(category);
        this.attackBonus = new SimpleIntegerProperty(attackBonus);
        this.range = new SimpleIntegerProperty(range);
        this.numDice = new SimpleIntegerProperty(numDice);
        this.damageDice = new SimpleIntegerProperty(damageDice);
        this.damageType = new SimpleStringProperty(damageType);
        this.characterID = characterID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public int getAttackBonus() {
        return attackBonus.get();
    }

    public int getRange() {
        return range.get();
    }

    public int getNumDice() {
        return numDice.get();
    }

    public int getDamageDice() {
        return damageDice.get();
    }

    public String getDamageType() {
        return damageType.get();
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
