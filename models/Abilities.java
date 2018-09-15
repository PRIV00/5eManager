package main.models;

public class Abilities {
    private int[] strength;
    private int[] dexterity;
    private int[] constitution;
    private int[] intelligence;
    private int[] wisdom;
    private int[] charisma;

    Abilities(int strength, int dexterity, int constitution, int intelligence, int wisdom, int charisma) {
        this.strength = new int[] { strength, mod(strength) };
        this.dexterity = new int[] { dexterity, mod(dexterity) };
        this.constitution = new int[] { constitution, mod(constitution) };
        this.intelligence = new int[] { intelligence, mod(intelligence) };
        this.wisdom = new int[] { wisdom, mod(wisdom) };
        this.charisma = new int[] { charisma, mod(charisma) };
    }

    private int mod(int score) {
        return score / 2 - 5;
    }

    public String getModText(String identifier) {
        int mod;
        int score;
        switch (identifier) {
            case "STR": score = strength[0];
                        mod = strength[1];
                        break;
            case "DEX": score = dexterity[0];
                        mod = dexterity[1];
                        break;
            case "CON": score = constitution[0];
                        mod = constitution[1];
                        break;
            case "INT": score = intelligence[0];
                        mod = intelligence[1];
                        break;
            case "WIS": score = wisdom[0];
                        mod = wisdom[1];
                        break;
            case "CHA": score = charisma[0];
                        mod = charisma[1];
                        break;
            default: throw new NullPointerException("Invalid modText");
        }
        if (mod < 0) {
            return String.format("%d (%d)", score, mod);
        } else {
            return String.format("%s (+%d)", score, mod);
        }
    }

    public int[] getScores() {
        int[] stats = new int[] {strength[0], dexterity[0], constitution[0],
                intelligence[0], wisdom[0], charisma[0] };
        return stats;
    }

    public int getStrength(int i) {
        return strength[i];
    }

    public void setStrength(int score) {
        this.strength[0] = score;
        this.strength[1] = mod(score);
    }

    public int getDexterity(int i) {
        return dexterity[i];
    }

    public void setDexterity(int score) {
        this.dexterity[0] = score;
        this.dexterity[1] = mod(score);
    }

    public int getConstitution(int i) {
        return constitution[i];
    }

    public void setConstitution(int score) {
        this.constitution[0] = score;
        this.constitution[1] = mod(score);
    }

    public int getIntelligence(int i) {
        return intelligence[i];
    }

    public void setIntelligence(int score) {
        this.intelligence[0] = score;
        this.intelligence[1] = mod(score);
    }

    public int getWisdom(int i) {
        return wisdom[i];
    }

    public void setWisdom(int score) {
        this.wisdom[0] = score;
        this.wisdom[1] = mod(score);
    }

    public int getCharisma(int i) {
        return charisma[i];
    }

    public void setCharisma(int score) {
        this.charisma[0] = score;
        this.charisma[1] = mod(score);
    }
}
