package main.databases;

import main.databases.tables.CharacterTable;

public class TableUpdater {


    public static void main(String[] args) {
        CharacterTable ct = new CharacterTable("datav3");
        ct.readAllData();
    }
}
