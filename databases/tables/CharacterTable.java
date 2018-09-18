package main.databases.tables;

import main.models.TableModel;
import main.models.Character;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CharacterTable extends Table implements Searchable, TopLevelTable {

    public CharacterTable(String fileName) {
        super(fileName,
                "CREATE TABLE IF NOT EXISTS Characters (\n" +
                        " characterID INTEGER PRIMARY KEY, \n" +
                        " name TEXT, \n" +
                        " look TEXT, \n" +
                        " title TEXT, \n" +
                        " race TEXT, \n" +
                        " voice TEXT, \n" +
                        " personality TEXT, \n" +
                        " desires TEXT, \n" +
                        " fears TEXT, \n" +
                        " background TEXT, \n" +
                        " knowledge TEXT, \n" +
                        " opinion TEXT, \n" +
                        " descriptor TEXT, \n" +
                        " armorClass INTEGER, \n" +
                        " armor TEXT, \n" +
                        " hitPointMax INTEGER, \n" +
                        " hitPointCurrent INTEGER, \n" +
                        " speed INTEGER, \n" +
                        " proficiency INTEGER, \n" +
                        " strength INTEGER, \n" +
                        " dexterity INTEGER, \n" +
                        " constitution INTEGER, \n" +
                        " intelligence INTEGER, \n" +
                        " wisdom INTEGER, \n" +
                        " charisma INTEGER, \n" +
                        " strSave INTEGER, \n" +
                        " dexSave INTEGER, \n" +
                        " conSave INTEGER, \n" +
                        " intSave INTEGER, \n" +
                        " wisSave INTEGER, \n" +
                        " chaSave INTEGER, \n" +
                        " senses INTEGER, \n" +
                        " languages INTEGER, \n" +
                        " inventory TEXT, \n" +
                        " locationID INTEGER, \n" +
                        " FOREIGN KEY(locationID) REFERENCES Locations(locationID) \n" +
                        ")");
    }

    //Test method
    public void readAllData() {
        connect();
        String sql = "SELECT * FROM Characters";

        try {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String s = "";
                for (int i = 1; i < 34; i++) {
                        s = s + rs.getString(i) +"\n";
                }
                System.out.println(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }


    /**
     * Overrides Modifiable's method. Takes a models.Character object and uses its data to insert a new row into the Characters
     * table.
     *
     * @param characterData Character object to retrieve the data from for the row entry.
     * @throws SQLException sql throw.
     */
    @Override
    public void insertData(TableModel characterData) throws SQLException {
        connect();
        String sql = "INSERT OR IGNORE INTO Characters(name, look, title, race, voice, personality, desires, fears, background, " +
                "knowledge, opinion, descriptor, armorClass, armor, hitPointMax, hitPointCurrent, speed, proficiency, strength, dexterity, constitution, " +
                "intelligence, wisdom, charisma, strSave, dexSave, conSave, intSave, wisSave, chaSave, senses, languages, inventory, locationID) \n" +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Character character = (Character) characterData;

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            pstmt.setString(1, character.getName());
            pstmt.setString(2, character.getLook());
            pstmt.setString(3, character.getTitle());
            pstmt.setString(4, character.getRace());
            pstmt.setString(5, character.getVoice());
            pstmt.setString(6, character.getPersonality());
            pstmt.setString(7, character.getDesires());
            pstmt.setString(8, character.getFears());
            pstmt.setString(9, character.getBackground());
            pstmt.setString(10, character.getKnowledge());
            pstmt.setString(11, character.getOpinion());
            pstmt.setString(12, character.getDescriptor());
            pstmt.setInt(13, character.getArmorClass());
            pstmt.setString(14, character.getArmor());
            pstmt.setInt(15, character.getHitPointMax());
            pstmt.setInt(16, character.getHitPointCurrent());
            pstmt.setInt(17, character.getSpeed());
            pstmt.setInt(18, character.getProficiency());
            int[] scores = character.getStats().getScores();
            pstmt.setInt(19, scores[0]);
            pstmt.setInt(20, scores[1]);
            pstmt.setInt(21, scores[2]);
            pstmt.setInt(22, scores[3]);
            pstmt.setInt(23, scores[4]);
            pstmt.setInt(24, scores[5]);
            pstmt.setInt(25, character.getSaves().getStrengthSave());
            pstmt.setInt(26, character.getSaves().getDexteritySave());
            pstmt.setInt(27, character.getSaves().getConstitutionSave());
            pstmt.setInt(28, character.getSaves().getIntelligenceSave());
            pstmt.setInt(29, character.getSaves().getWisdomSave());
            pstmt.setInt(30, character.getSaves().getCharismaSave());
            pstmt.setString(31, character.getSenses());
            pstmt.setString(32, character.getLanguages());
            pstmt.setString(33, character.getInventory());
            pstmt.setInt(34, character.getLocationID());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("No rows affected");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    character.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating character failed, no ID obtained.");
                }
            }
        } finally {
            closeConnection();
        }
    }

    /**
     * Overrides Modifiable's method. Takes a models.Character object, finds the match in the database and deletes it. Chose
     * to use a models.Character object as the param to keep it consistent with the other Modifiable methods based around
     * inserting and deleting data.
     *
     * @param characterData models.models.Character object to retrieve the ID from.
     * @throws SQLException sql.
     */
    @Override
    public void deleteData(TableModel characterData) throws SQLException {
        connect();
        String sql = "DELETE FROM Characters WHERE characterID = ?";
        Character character = (Character) characterData;

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, character.getId());
            pstmt.executeUpdate();
            System.out.println("Entry deleted.");
        } finally {
            closeConnection();
        }
    }

    @Override
    public void updateData(TableModel characterData) throws SQLException {
        connect();
        String sql = "UPDATE Characters SET name = ?, look = ?, title = ?, race = ?, voice = ?, personality = ?, desires = ?, " +
                "fears = ?, background = ?, knowledge = ?, opinion = ?, descriptor = ?, armorClass = ?, armor = ?, " +
                "hitPointMax = ?, hitPointCurrent = ?, speed = ?, proficiency = ?, strength = ?, dexterity = ?, constitution = ?, " +
                "intelligence = ?, wisdom = ?, charisma = ?, strSave = ?, dexSave = ?, conSave = ?, intSave = ?, " +
                "wisSave = ?, chaSave = ?, senses = ?, languages = ?, inventory = ?, locationID  = ?" +
                "WHERE characterID = ?";
        Character character = (Character) characterData;

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, character.getName());
            pstmt.setString(2, character.getLook());
            pstmt.setString(3, character.getTitle());
            pstmt.setString(4, character.getRace());
            pstmt.setString(5, character.getVoice());
            pstmt.setString(6, character.getPersonality());
            pstmt.setString(7, character.getDesires());
            pstmt.setString(8, character.getFears());
            pstmt.setString(9, character.getBackground());
            pstmt.setString(10, character.getKnowledge());
            pstmt.setString(11, character.getOpinion());
            pstmt.setString(12, character.getDescriptor());
            pstmt.setInt(13, character.getArmorClass());
            pstmt.setString(14, character.getArmor());
            pstmt.setInt(15, character.getHitPointMax());
            pstmt.setInt(16, character.getHitPointCurrent());
            pstmt.setInt(17, character.getSpeed());
            pstmt.setInt(18, character.getProficiency());
            int[] scores = character.getStats().getScores();
            pstmt.setInt(19, scores[0]);
            pstmt.setInt(20, scores[1]);
            pstmt.setInt(21, scores[2]);
            pstmt.setInt(22, scores[3]);
            pstmt.setInt(23, scores[4]);
            pstmt.setInt(24, scores[5]);
            pstmt.setInt(25, character.getSaves().getStrengthSave());
            pstmt.setInt(26, character.getSaves().getDexteritySave());
            pstmt.setInt(27, character.getSaves().getConstitutionSave());
            pstmt.setInt(28, character.getSaves().getIntelligenceSave());
            pstmt.setInt(29, character.getSaves().getWisdomSave());
            pstmt.setInt(30, character.getSaves().getCharismaSave());
            pstmt.setString(31, character.getSenses());
            pstmt.setString(32, character.getLanguages());
            pstmt.setString(33, character.getInventory());
            pstmt.setInt(34, character.getLocationID());
            pstmt.setInt(35, character.getId());
            pstmt.executeUpdate();
        } finally {
            closeConnection();
        }
    }

    @Override
    public List<Character> getAllData() {
        connect();
        String sql = "SELECT * FROM Characters";
        List<Character> list = new ArrayList<>();

        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new Character(rs.getInt("characterID"), rs.getString("name"), rs.getString("look"), rs.getString("title"),
                        rs.getString("race"), rs.getString("voice"), rs.getString("personality"), rs.getString("desires"),
                        rs.getString("fears"), rs.getString("background"), rs.getString("knowledge"), rs.getString("opinion"),
                        rs.getString("descriptor"), rs.getInt("armorClass"), rs.getString("armor"), rs.getInt("hitPointMax"),
                        rs.getInt("hitPointCurrent"), rs.getInt("speed"), rs.getInt("proficiency"),
                        new int[] { rs.getInt("strength"), rs.getInt("dexterity"), rs.getInt("constitution"),
                                rs.getInt("intelligence"), rs.getInt("wisdom"), rs.getInt("charisma") },
                        new int[] { rs.getInt("strSave"), rs.getInt("dexSave"), rs.getInt("conSave"), rs.getInt("intSave"),
                                rs.getInt("wisSave"), rs.getInt("chaSave") },
                        rs.getString("senses"), rs.getString("languages"), rs.getString("inventory"),
                        rs.getInt("locationID")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        return list;
    }

    @Override
    public int getNextID() {
        connect();
        String sql = "SELECT max(characterID) \n" +
                "FROM Characters";

        try {
            ResultSet rs = stmt.executeQuery(sql);
            return rs.getInt(1) + 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            closeConnection();
        }
    }

    /**
     * I wanted the query to both return results based on name and based on location. I also wanted to set it up so
     * that you could get hierarchical results based on location. So searching "Moln" would return all hierarchical
     * results starting with Moln.
     *
     * To do this, I used a recursive CTE to first get the hierarchical list of locations starting with whatever the
     * anchor location is, then taking that set of locationIDs and left joining them with characters to retrieve any
     * character details with those locations as their locationID.
     *
     *
     * @param query The string input to query from
     * @return a List containing characters objects created from the query results.
     */
    @Override
    public List<Character> query(String query) {

        connect();
        String sql = "SELECT * FROM Characters WHERE name LIKE ('%' || ? || '%')";
        String sql2 = "WITH LocationCharacterHierarchy \n" +
                "               (locationID) \n" +
                "AS \n" +
                "( \n" +
                "SELECT locationID \n" +
                "FROM Locations \n" +
                "WHERE  Locations.name LIKE ('%' || ? || '%') \n" +
                "UNION ALL \n" +
                "SELECT  Locations.locationID \n" +
                "FROM Locations \n" +
                "INNER JOIN LocationCharacterHierarchy AS lch ON lch.locationID = Locations.parentID \n" +
                ") \n" +
                "SELECT DISTINCT c.characterID, \n" +
                "       c.name TEXT, \n" +
                "       c.look TEXT, \n" +
                "       c.title TEXT, \n" +
                "       c.race TEXT, \n" +
                "       c.voice TEXT, \n" +
                "       c.personality TEXT, \n" +
                "       c.desires TEXT, \n" +
                "       c.fears TEXT, \n" +
                "       c.background TEXT, \n" +
                "       c.knowledge TEXT, \n" +
                "       c.opinion TEXT, \n" +
                "       c.descriptor TEXT, \n" +
                "       c.armorClass INTEGER, \n" +
                "       c.armor TEXT, \n" +
                "       c.hitPointMax INTEGER, \n" +
                "       c.hitPointCurrent INTEGER, \n" +
                "       c.speed INTEGER, \n" +
                "       c.proficiency INTEGER, \n" +
                "       c.strength INTEGER, \n" +
                "       c.dexterity INTEGER, \n" +
                "       c.constitution INTEGER, \n" +
                "       c.intelligence INTEGER, \n" +
                "       c.wisdom INTEGER, \n" +
                "       c.charisma INTEGER, \n" +
                "       c.strSave INTEGER, \n" +
                "       c.dexSave INTEGER, \n" +
                "       c.conSave INTEGER, \n" +
                "       c.intSave INTEGER, \n" +
                "       c.wisSave INTEGER, \n" +
                "       c.chaSave INTEGER, \n" +
                "       c.senses INTEGER, \n" +
                "       c.languages INTEGER, \n" +
                "       c.inventory TEXT, \n" +
                "       lch.locationID INTEGER \n" +
                "FROM   LocationCharacterHierarchy AS lch \n" +
                "INNER JOIN Characters AS c ON c.locationID = lch.locationID \n" +
                "ORDER BY characterID";

        List<Character> list = new ArrayList<>();

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            pstmt.setString(1, query);
            pstmt2.setString(1, query);
            ResultSet rs = pstmt.executeQuery();
            ResultSet rs2 = pstmt2.executeQuery();

            while (rs.next()) {
                list.add(new Character(rs.getInt("characterID"), rs.getString("name"), rs.getString("look"), rs.getString("title"),
                        rs.getString("race"), rs.getString("voice"), rs.getString("personality"), rs.getString("desires"),
                        rs.getString("fears"), rs.getString("background"), rs.getString("knowledge"), rs.getString("opinion"),
                        rs.getString("descriptor"), rs.getInt("armorClass"), rs.getString("armor"), rs.getInt("hitPointMax"),
                        rs.getInt("hitPointCurrent"), rs.getInt("speed"), rs.getInt("proficiency"),
                        new int[] { rs.getInt("strength"), rs.getInt("dexterity"), rs.getInt("constitution"),
                                rs.getInt("intelligence"), rs.getInt("wisdom"), rs.getInt("charisma") },
                        new int[] { rs.getInt("strSave"), rs.getInt("dexSave"), rs.getInt("conSave"), rs.getInt("intSave"),
                                rs.getInt("wisSave"), rs.getInt("chaSave") },
                        rs.getString("senses"), rs.getString("languages"), rs.getString("inventory"),
                        rs.getInt("locationID")));
            }

            while (rs2.next()) {
                /*
                 * Because of the LEFT JOIN from the sql statement, It returns all locations, but if any of those
                 * locations don't have characters it will return a null row, so need to filter them out
                 * */
                if (rs2.getInt("characterID") == 0) {
                    continue;
                }

                /*
                 * Next block checks to make sure that the character is not already in the sql as a result from the
                 * first sql query.
                 */
                boolean check = true;
                for (Character c : list) {
                    if (c.getId() == rs2.getInt("characterID")) {
                        check = false;
                    }
                }
                if (check) {
                    list.add(new Character(rs2.getInt("characterID"), rs2.getString("name"), rs2.getString("look"), rs2.getString("title"),
                            rs2.getString("race"), rs2.getString("voice"), rs2.getString("personality"), rs2.getString("desires"),
                            rs2.getString("fears"), rs2.getString("background"), rs2.getString("knowledge"), rs2.getString("opinion"),
                            rs2.getString("descriptor"), rs2.getInt("armorClass"), rs2.getString("armor"), rs2.getInt("hitPointMax"),
                            rs2.getInt("hitPointCurrent"), rs2.getInt("speed"), rs2.getInt("proficiency"),
                            new int[] { rs2.getInt("strength"), rs2.getInt("dexterity"), rs2.getInt("constitution"),
                                    rs2.getInt("intelligence"), rs2.getInt("wisdom"), rs2.getInt("charisma") },
                            new int[] { rs2.getInt("strSave"), rs2.getInt("dexSave"), rs2.getInt("conSave"), rs2.getInt("intSave"),
                                    rs2.getInt("wisSave"), rs2.getInt("chaSave") },
                            rs2.getString("senses"), rs2.getString("languages"), rs2.getString("inventory"),
                            rs2.getInt("locationID")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        return list;
    }
}
