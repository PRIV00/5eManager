package main.models;

import java.util.ArrayList;
import java.util.List;

public class Saves {

    private int[] saves = new int[6];

    public Saves(int strengthSave, int dexteritySave, int constitutionSave, int intelligenceSave, int wisdomSave, int charismaSave) {
        saves[0] = strengthSave;
        saves[1] = dexteritySave;
        saves[2] = constitutionSave;
        saves[3] = intelligenceSave;
        saves[4] = wisdomSave;
        saves[5] = charismaSave;
    }

    /**
     * Returns a string containing any saves that are over 0. Useful for displaying on a character sheet or statblock.
     *
     * @return string of saves that aren't 0
     */
    @Override
    public String toString() {
        List<String> savesStrings = new ArrayList<>();
        for (int i = 0; i < saves.length; i++) {
            if (saves[i] > 0) {
                switch (i) {
                    case 0: savesStrings.add("Strength +" + String.valueOf(saves[0]));
                            break;
                    case 1: savesStrings.add("Dexterity +" + String.valueOf(saves[1]));
                            break;
                    case 2: savesStrings.add("Constitution +" + String.valueOf(saves[2]));
                            break;
                    case 3: savesStrings.add("Intelligence +" + String.valueOf(saves[3]));
                            break;
                    case 4: savesStrings.add("Wisdom +" + String.valueOf(saves[4]));
                            break;
                    case 5: savesStrings.add("Charisma +" + String.valueOf(saves[5]));
                            break;
                }
            }
        }

        return String.join(", ", savesStrings);
    }

    public int[] getSaves() {
        return saves;
    }

    public int getStrengthSave() {
        return saves[0];
    }

    public void setStrengthSave(int strengthSave) {
        this.saves[0] = strengthSave;
    }

    public int getDexteritySave() {
        return saves[1];
    }

    public void setDexteritySave(int dexteritySave) {
        this.saves[1] = dexteritySave;
    }

    public int getConstitutionSave() {
        return saves[2];
    }

    public void setConstitutionSave(int constitutionSave) {
        this.saves[2] = constitutionSave;
    }

    public int getIntelligenceSave() {
        return saves[3];
    }

    public void setIntelligenceSave(int intelligenceSave) {
        this.saves[3] = intelligenceSave;
    }

    public int getWisdomSave() {
        return saves[4];
    }

    public void setWisdomSave(int wisdomSave) {
        this.saves[4] = wisdomSave;
    }

    public int getCharismaSave() {
        return saves[5];
    }

    public void setCharismaSave(int charismaSave) {
        this.saves[5] = charismaSave;
    }
}
