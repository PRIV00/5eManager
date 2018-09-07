package models;

import javafx.beans.property.SimpleIntegerProperty;

public class Abilities {
    private SimpleIntegerProperty[] strength;
    private SimpleIntegerProperty[] dexterity;
    private SimpleIntegerProperty[] constitution;
    private SimpleIntegerProperty[] intelligence;
    private SimpleIntegerProperty[] wisdom;
    private SimpleIntegerProperty[] charisma;

    Abilities(int strength, int dexterity, int constitution, int intelligence, int wisdom, int charisma) {
        this.strength = new SimpleIntegerProperty[] { new SimpleIntegerProperty(strength),
                new SimpleIntegerProperty(mod(strength)) };
        this.dexterity = new SimpleIntegerProperty[] { new SimpleIntegerProperty(dexterity),
                new SimpleIntegerProperty(mod(dexterity)) };
        this.constitution = new SimpleIntegerProperty[] { new SimpleIntegerProperty(constitution),
                new SimpleIntegerProperty(mod(constitution)) };
        this.intelligence = new SimpleIntegerProperty[] { new SimpleIntegerProperty(intelligence),
                new SimpleIntegerProperty(mod(intelligence)) };
        this.wisdom = new SimpleIntegerProperty[] { new SimpleIntegerProperty(wisdom),
                new SimpleIntegerProperty(mod(wisdom)) };
        this.charisma = new SimpleIntegerProperty[] { new SimpleIntegerProperty(charisma),
                new SimpleIntegerProperty(mod(charisma)) };
    }

    private int mod(int score) {
        return score / 2 - 5;
    }

    public String getModText(String identifier) {
        int mod;
        switch (identifier) {
            case "STR": mod = strength[1].get();
                        break;
            case "DEX": mod = dexterity[1].get();
                        break;
            case "CON": mod = constitution[1].get();
                        break;
            case "INT": mod = intelligence[1].get();
                        break;
            case "WIS": mod = wisdom[1].get();
                        break;
            case "CHA": mod = charisma[1].get();
                        break;
            default: throw new NullPointerException("Invalid modText");
        }
        if (mod < 0) {
            return String.format("%s (%d)", identifier, mod);
        } else {
            return String.format("%s (+%d)", identifier, mod);
        }
    }

    public int[] getScores() {
        int[] stats = new int[] {strength[0].get(), dexterity[0].get(), constitution[0].get(),
                intelligence[0].get(), wisdom[0].get(), charisma[0].get() };
        return stats;
    }

    public int getStrength(int i) {
        return strength[i].get();
    }

    public void setStrength(int score) {
        this.strength[0].set(score);
        this.strength[1].set(mod(score));
    }

    public int getDexterity(int i) {
        return dexterity[i].get();
    }

    public void setDexterity(int score) {
        this.dexterity[0].set(score);
        this.dexterity[1].set(mod(score));
    }

    public int getConstitution(int i) {
        return constitution[i].get();
    }

    public void setConstitution(int score) {
        this.constitution[0].set(score);
        this.constitution[1].set(mod(score));
    }

    public int getIntelligence(int i) {
        return intelligence[i].get();
    }

    public void setIntelligence(int score) {
        this.intelligence[0].set(score);
        this.intelligence[1].set(mod(score));
    }

    public int getWisdom(int i) {
        return wisdom[i].get();
    }

    public void setWisdom(int score) {
        this.wisdom[0].set(score);
        this.wisdom[1].set(mod(score));
    }

    public int getCharisma(int i) {
        return charisma[i].get();
    }

    public void setCharisma(int score) {
        this.charisma[0].set(score);
        this.charisma[1].set(mod(score));
    }
}
