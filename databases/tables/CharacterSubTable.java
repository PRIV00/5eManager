package main.databases.tables;

import java.util.List;
import main.model.modeldata.Character;

public interface CharacterSubTable {
    List getDataByCharacter(Character c);

    /**
     * Used when a character is deleted, so that there are no orphaned attacks left in the table.
     *
     * @param c The character that is being deleted.
     */
    void deleteDataByCharacter(Character c);
}
