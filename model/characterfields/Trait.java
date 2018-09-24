package main.model.characterfields;

import main.model.modeldata.ModelData;


public class Trait implements ModelData {
    private int id;
    private String name;
    private String description;
    private int characterID;

    private boolean setToDelete = false;

    public Trait() {
        id = 0;
        name = "-";
        description = "-";
        characterID = 0;
    }

    public Trait(int id, String name, String description, int characterID) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.characterID = characterID;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
