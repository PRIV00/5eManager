package main.models.characterfields;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import main.models.TableModel;


public class Attack implements TableModel {

    private int id;
    private SimpleStringProperty name;
    private SimpleStringProperty category;
    private SimpleStringProperty ability;
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
        ability = new SimpleStringProperty("STR");
        attackBonus = new SimpleIntegerProperty(0);
        range = new SimpleIntegerProperty(5);
        numDice = new SimpleIntegerProperty(1);
        damageDice = new SimpleIntegerProperty(4);
        damageType = new SimpleStringProperty("");
        characterID = 0;
    }

    public Attack(int id, String name, String category, String ability, int attackBonus, int range, int numDice, int damageDice, String damageType, int characterID) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.category = new SimpleStringProperty(category);
        this.ability = new SimpleStringProperty(ability);
        this.attackBonus = new SimpleIntegerProperty(attackBonus);
        this.range = new SimpleIntegerProperty(range);
        this.numDice = new SimpleIntegerProperty(numDice);
        this.damageDice = new SimpleIntegerProperty(damageDice);
        this.damageType = new SimpleStringProperty(damageType);
        this.characterID = characterID;
    }

    @Override
    public String toString() {
        return String.format("ID: %d\nName: %s\n Category: %s\nAbility: %s",id, name.get(), category.get(), ability.get());
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

    public String getAbility() {
        return ability.get();
    }

    public void setAbility(String ability) {
        this.ability.set(ability);
    }

    public int getAttackBonus() {
        return attackBonus.get();
    }

    public void setAttackBonus(int attackBonus) {
        this.attackBonus.set(attackBonus);
    }

    public int getRange() {
        return range.get();
    }

    public void setRange(int range) {
        this.range.set(range);
    }

    public int getNumDice() {
        return numDice.get();
    }

    public void setNumDice(int numDice) {
        this.numDice.set(numDice);
    }

    public int getDamageDice() {
        return damageDice.get();
    }

    public void setDamageDice(int damageDice) {
        this.damageDice.set(damageDice);
    }

    public String getDamageType() {
        return damageType.get();
    }

    public void setDamageType(String damageType) {
        this.damageType.set(damageType);
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
