package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Attack implements DataModel {

    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty category;
    private SimpleIntegerProperty attackBonus;
    private SimpleIntegerProperty range;
    private SimpleIntegerProperty numDice;
    private SimpleIntegerProperty damageDice;
    private SimpleStringProperty damageType;
    private int characterID;


    public Attack(int id, String name, String category, int attackBonus, int range, int numDice, int damageDice, String
                  damageType, int characterID) {
        this.id = new SimpleIntegerProperty(id);
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
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public String getCategory() {
        return category.get();
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
}
