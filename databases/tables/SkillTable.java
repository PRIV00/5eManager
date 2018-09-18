package main.databases.tables;

import main.models.Character;
import main.models.characterfields.Skill;
import main.models.TableModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SkillTable extends Table implements CharacterSubTable {

    public SkillTable(String fileName) {
        super(fileName,
                "CREATE TABLE IF NOT EXISTS Skills (\n" +
                        "skillID INTEGER PRIMARY KEY, \n" +
                        "name TEXT, \n" +
                        "ability TEXT, \n" +
                        "bonus INTEGER, \n" +
                        "characterID INTEGER, \n" +
                        "FOREIGN KEY (characterID) REFERENCES Characters(characterID)  \n" +
                        ")");
    }

    public void deleteAll() {
        connect();
        String sql = "DELETE FROM Skills";

        try {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    @Override
    public void insertData(TableModel skillData) {
        connect();
        String sql = "INSERT OR IGNORE INTO Skills (name, ability, bonus, characterID) " +
                "VALUES (?,?,?,?)";
        Skill skill = (Skill) skillData;

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, skill.getName());
            pstmt.setString(2, skill.getAbility());
            pstmt.setInt(3, skill.getBonus());
            pstmt.setInt(4, skill.getCharacterID());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    skill.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("No keys generated");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

    }

    @Override
    public void deleteData(TableModel skillData) {
        connect();
        String sql = "DELETE FROM Skills WHERE skillID = ?";
        Skill skill = (Skill) skillData;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, skill.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    @Override
    public void updateData(TableModel skillData) {
        connect();
        String sql = "UPDATE Skills SET name = ?, ability = ?, bonus = ?, characterID = ? \n" +
                "WHERE skillID = ?";
        Skill skill = (Skill) skillData;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, skill.getName());
            pstmt.setString(2, skill.getAbility());
            pstmt.setInt(3, skill.getBonus());
            pstmt.setInt(4, skill.getCharacterID());
            pstmt.setInt(5, skill.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    @Override
    public int getNextID() {
        connect();
        String sql = "SELECT max(skillID) \n" +
                "FROM Skills";

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


    @Override
    public List<Skill> getDataByCharacter(Character character) {
        connect();
        String sql = "SELECT * FROM Skills \n" +
                "WHERE characterID = ?";
        List<Skill> list = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, character.getId());

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Skill(rs.getInt("skillID"), rs.getString("name"), rs.getString("ability"),
                        rs.getInt("bonus"), rs.getInt("characterID")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection();
        }

        return list;
    }

    @Override
    public void deleteDataByCharacter(Character character) {
        connect();
        String sql = "DELETE FROM Skills WHERE characterID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, character.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }
}
