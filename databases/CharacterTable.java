package databases;

import models.DataModel;
import models.Character;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CharacterTable extends Table implements Searchable, Modifiable {

    CharacterTable(String fileName) {
        super(fileName,
                "CREATE TABLE IF NOT EXISTS Characters (\n" +
                " characterID INTEGER PRIMARY KEY, \n" +
                " name TEXT, \n" +
                " title TEXT, \n" +
                " description TEXT, \n" +
                " strength INTEGER, \n" +
                " dexterity INTEGER, \n" +
                " constitution INTEGER, \n" +
                " intelligence INTEGER, \n" +
                " wisdom INTEGER, \n" +
                " charisma INTEGER, \n" +
                " locationID integer, \n" +
                " FOREIGN KEY(locationID) REFERENCES Locations(locationID) \n" +
                ")");
    }

    /**
     * Overrides Modifiable's method. Takes a Character object and uses its data to insert a new row into the Characters
     * table.
     *
     * @param characterData models.Character object to retrieve the data from for the row entry.
     * @throws SQLException sql throw.
     */
    @Override
    public void insertData(DataModel characterData) throws SQLException {
        connect();
        String sql = "INSERT OR IGNORE INTO Characters(name, title, description, strength, dexterity, constitution, " +
                "intelligence, wisdom, charisma, locationID) \n" +
                "VALUES(?,?,?,?,?,?,?,?,?,?)";
        Character character = (Character) characterData;

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            pstmt.setString(1, character.getName());
            pstmt.setString(2, character.getTitle());
            pstmt.setString(3, character.getDescription());
            int[] scores = character.getStats().getScores();
            pstmt.setInt(4, scores[0]);
            pstmt.setInt(5, scores[1]);
            pstmt.setInt(6, scores[2]);
            pstmt.setInt(7, scores[3]);
            pstmt.setInt(8, scores[4]);
            pstmt.setInt(9, scores[5]);
            pstmt.setInt(10, character.getLocationID());
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
     * Overrides Modifiable's method. Takes a Character object, finds the match in the database and deletes it. Chose
     * to use a Character object as the param to keep it consistent with the other Modifiable methods based around
     * inserting and deleting data.
     *
     * @param characterData models.Character object to retrieve the ID from.
     * @throws SQLException sql.
     */
    @Override
    public void deleteData(DataModel characterData) throws SQLException {
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
    public void updateData(DataModel characterData) throws SQLException {
        connect();
        String sql = "UPDATE Characters SET name = ?, title = ?, description = ?, strength = ?, dexterity = ?, " +
                "constitution = ?, intelligence = ?, wisdom = ?, charisma = ?, locationID = ? \n" +
                "WHERE characterID = ?";
        Character character = (Character) characterData;

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, character.getName());
            pstmt.setString(2, character.getTitle());
            pstmt.setString(3, character.getDescription());
            pstmt.setInt(4, character.getStats().getStrength(0));
            pstmt.setInt(5, character.getStats().getDexterity(0));
            pstmt.setInt(6, character.getStats().getConstitution(0));
            pstmt.setInt(7, character.getStats().getIntelligence(0));
            pstmt.setInt(8, character.getStats().getWisdom(0));
            pstmt.setInt(9, character.getStats().getCharisma(0));
            pstmt.setInt(10, character.getLocationID());
            pstmt.setInt(11, character.getId());
            pstmt.executeUpdate();
        } finally {
            closeConnection();
        }
    }

    @Override
    public List<Character> getAllData() {
        connect();
        String sql = "SELECT * FROM Characters ";
        List<Character> list = new ArrayList<>();

        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new Character(rs.getInt("characterID"), rs.getString("name"), rs.getString("title"),
                        new int[] { rs.getInt("strength"), rs.getInt("dexterity"), rs.getInt("constitution"),
                                rs.getInt("intelligence"), rs.getInt("wisdom"), rs.getInt("charisma") },
                        rs.getInt("locationID"), rs.getString("description")));
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
                "                c.name, \n" +
                "                c.title, \n" +
                "                c.description, \n" +
                "                c.strength, \n" +
                "                c.dexterity, \n" +
                "                c.constitution, \n" +
                "                c.intelligence, \n" +
                "                c.wisdom, \n" +
                "                c.charisma, \n" +
                "                lch.locationID \n" +       // Removes duplicates from the ResultSet
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
                list.add(new Character(rs.getInt("characterID"), rs.getString("name"), rs.getString("title"),
                        new int[] { rs.getInt("strength"), rs.getInt("dexterity"), rs.getInt("constitution"),
                                rs.getInt("intelligence"), rs.getInt("wisdom"), rs.getInt("charisma") },
                        rs.getInt("locationID"), rs.getString("description")));
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
                    list.add(new Character(rs2.getInt("characterID"), rs2.getString("name"), rs2.getString("title"),
                            new int[] { rs2.getInt("strength"), rs2.getInt("dexterity"), rs2.getInt("constitution"),
                                    rs2.getInt("intelligence"), rs2.getInt("wisdom"), rs2.getInt("charisma") },
                            rs2.getInt("locationID"), rs2.getString("description")));
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
