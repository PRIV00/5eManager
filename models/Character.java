package main.models;

import javafx.beans.property.SimpleStringProperty;
import main.models.characterfields.Abilities;
import main.models.characterfields.Saves;
import main.models.characterfields.Skill;
import main.models.characterfields.Trait;

import java.util.ArrayList;
import java.util.List;

public class Character implements TableModel {

    private boolean edited = false;

    private int id;
    private int locationID;
    private String name;
    private String look;
    private String title;
    private String race;
    private String voice;
    private String personality;
    private String desires;
    private String fears;
    private String background;
    private String knowledge;
    private String opinion;
    private String descriptor;
    private int armorClass;
    private String armor;
    private int hitPointMax;
    private int hitPointCurrent;
    private int speed;
    private int proficiency;
    private Saves saves;
    private Abilities stats;
    private String senses;
    private String languages;
    private String inventory;

    private Location location;
    private SimpleStringProperty locName;
    private List<Attack> attackList = new ArrayList<>();
    private List<Skill> skillList = new ArrayList<>();
    private List<Trait> traitList = new ArrayList<>();

    public Character() {
        this.id = 0;
        this.name = "";
        this.look = "";
        this.title = "";
        this.race = "";
        this.voice = "";
        this.personality = "";
        this.desires = "";
        this.fears = "";
        this.background = "";
        this.knowledge = "";
        this.opinion = "";
        this.descriptor = "";
        this.armorClass = 10;
        this.armor = "";
        this.hitPointMax = 10;
        this.hitPointCurrent = 10;
        this.speed = 30;
        this.proficiency = 0;
        this.stats = new Abilities(10, 10, 10, 10, 10, 10);
        this.saves = new Saves(0, 0, 0, 0, 0, 0);
        this.senses = "";
        this.languages = "";
        this.inventory = "";
        this.locationID = 0;
    }

    public Character(int id, String name, String look, String title, String race, String voice, String personality, String desires,
                     String fears, String background, String knowledge, String opinion, String descriptor, int armorClass,
                     String armor, int hitPointMax, int hitPointCurrent, int speed, int proficiency, int[] s, int[] saves, String senses,
                     String languages, String inventory, int locationID) {
        this.id = id;
        this.name = name;
        this.look = look;
        this.title = title;
        this.race = race;
        this.voice = voice;
        this.personality = personality;
        this.desires = desires;
        this.fears = fears;
        this.background = background;
        this.knowledge = knowledge;
        this.opinion = opinion;
        this.descriptor = descriptor;
        this.armorClass = armorClass;
        this.armor = armor;
        this.hitPointMax = hitPointMax;
        this.hitPointCurrent = hitPointCurrent;
        this.speed = speed;
        this.proficiency = proficiency;
        this.stats = new Abilities(s[0], s[1], s[2], s[3], s[4], s[5]);
        this.saves = new Saves(saves[0], saves[1], saves[2], saves[3], saves[4], saves[5]);
        this.senses = senses;
        this.languages = languages;
        this.inventory = inventory;
        this.locationID = locationID;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean status) {
        edited = status;
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

    public String getLook() {
        return look;
    }

    public void setLook(String look) {
        this.look = look;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getPersonality() {
        return personality;
    }

    public void setPersonality(String personality) {
        this.personality = personality;
    }

    public String getDesires() {
        return desires;
    }

    public void setDesires(String desires) {
        this.desires = desires;
    }

    public String getFears() {
        return fears;
    }

    public void setFears(String fears) {
        this.fears = fears;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(String knowledge) {
        this.knowledge = knowledge;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public int getArmorClass() {
        return armorClass;
    }

    public void setArmorClass(int armorClass) {
        this.armorClass = armorClass;
    }

    public String getArmor() {
        return armor;
    }

    public void setArmor(String armor) {
        this.armor = armor;
    }

    public int getHitPointMax() {
        return hitPointMax;
    }

    public void setHitPointMax(int hitPointMax) {
        this.hitPointMax = hitPointMax;
    }

    public int getHitPointCurrent() {
        return hitPointCurrent;
    }

    public void setHitPointCurrent(int hitPointCurrent) {
        this.hitPointCurrent = hitPointCurrent;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getProficiency() {
        return proficiency;
    }

    public void setProficiency(int proficiency) {
        this.proficiency = proficiency;
    }

    public Saves getSaves() {
        return saves;
    }

    public Abilities getStats() {
        return stats;
    }

    public String getSenses() {
        return senses;
    }

    public void setSenses(String senses) {
        this.senses = senses;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocName(String locName) {
        this.locName.set(locName);
    }

    public String getLocName() {
        try {
            return location.getName();
        } catch (NullPointerException e) {
            return "";
        }
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Attack> getAttackList() {
        return attackList;
    }

    public void setAttackList(List<Attack> list) {
        this.attackList = list;
    }

    public void addAttack(Attack attack) {
        this.attackList.add(attack);
    }

    public List<Skill> getSkillList() {
        return skillList;
    }

    public void setSkillList(List<Skill> list) {
        this.skillList = list;
    }

    public void addSkill(Skill skill) {
        skillList.add(skill);
    }

    public List<Trait> getTraitList() {
        return traitList;
    }

    public void setTraitList(List<Trait> traitList) {
        this.traitList = traitList;
    }

    public void addTrait(Trait trait) {
        traitList.add(trait);
    }

    /**
     * Used to get the attack bonus based on an ability modifier code. Both used to set the label in the statblock and
     * to set the attackBonus column for the attack SQLite table.
     *
     * @param attackAbilityCode code to get the stat from (STR, DEX, CON, INT, WIS, or CHA)
     * @return the calculated attack bonus.
     */
    public int getAbilityMod(String attackAbilityCode) {
        int bonus = 0;
        switch (attackAbilityCode) {
            case "STR": bonus = this.getStats().getStrength(1) + this.getProficiency();
                                break;
            case "DEX": bonus = this.getStats().getDexterity(1) + this.getProficiency();
                                break;
            case "CON": bonus = this.getStats().getConstitution(1) + this.getProficiency();
                                break;
            case "INT": bonus = this.getStats().getIntelligence(1) + this.getProficiency();
                                break;
            case "WIS": bonus = this.getStats().getWisdom(1) + this.getProficiency();
                                break;
            case "CHA": bonus = this.getStats().getCharisma(1) + this.getProficiency();
                                break;
        }
        return bonus;
    }
}