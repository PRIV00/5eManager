package main.databases.tables;

import main.model.modeldata.ModelData;
import main.model.characterfields.Attack;
import main.model.modeldata.Character;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AttackTable extends Table implements CharacterSubTable{

    public AttackTable(String fileName) {
        super(fileName,
                "CREATE TABLE IF NOT EXISTS Attacks (\n" +
                        "attackID INTEGER PRIMARY KEY, \n" +
                        "name TEXT, \n" +
                        "category TEXT, \n" +
                        "ability TEXT, \n" +
                        "attackBonus INTEGER, \n" +
                        "range INTEGER, \n" +
                        "numDice INTEGER, \n" +
                        "damageDice INTEGER, \n" +
                        "damageType TEXT, \n" +
                        "characterID INTEGER, \n" +
                        "FOREIGN KEY (characterID) REFERENCES Characters(characterID)" +
                        ")");
    }

    public void deleteAll() {
        connect();
        String sql = "DELETE FROM Attacks";

        try {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    @Override
    public void insertData(ModelData attackData) {
        connect();
        String sql = "INSERT OR IGNORE INTO Attacks(name, category, ability, attackBonus, range, numDice, damageDice, damageType," +
                "characterID) VALUES (?,?,?,?,?,?,?,?,?)";
        Attack attack = (Attack) attackData;

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            pstmt.setString(1, attack.getName());
            pstmt.setString(2, attack.getCategory());
            pstmt.setString(3, attack.getAbility());
            pstmt.setInt(4, attack.getAttackBonus());
            pstmt.setInt(5, attack.getRange());
            pstmt.setInt(6, attack.getNumDice());
            pstmt.setInt(7, attack.getDamageDice());
            pstmt.setString(8, attack.getDamageType());
            pstmt.setInt(9, attack.getCharacterID());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()){
                if (generatedKeys.next()) {
                    attack.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("No keys generated");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection();
        }
    }

    @Override
    public void deleteData(ModelData modelData){
        connect();
        String sql = "DELETE FROM Attacks WHERE attackID = ?";
        Attack attack = (Attack) modelData;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, attack.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection();
        }
    }



    @Override
    public void updateData(ModelData modelData) {
        connect();
        String sql = "UPDATE Attacks SET name = ?, category = ?, ability = ?, attackBonus = ?, range = ?, numDice = ?, damageDice = ?," +
                " damageType = ?, characterID = ? \n" +
                "WHERE attackID = ?";
        Attack attack = (Attack) modelData;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, attack.getName());
            pstmt.setString(2, attack.getCategory());
            pstmt.setString(3, attack.getAbility());
            pstmt.setInt(4, attack.getAttackBonus());
            pstmt.setInt(5, attack.getRange());
            pstmt.setInt(6, attack.getNumDice());
            pstmt.setInt(7, attack.getDamageDice());
            pstmt.setString(8, attack.getDamageType());
            pstmt.setInt(9, attack.getCharacterID());
            pstmt.setInt(10, attack.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection();
        }
    }

    @Override
    public int getNextID() {
        connect();
        String sql = "SELECT max(attackID) \n" +
                "FROM Attacks";

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
    public List<Attack> getDataByCharacter(Character character) {
        connect();
        String sql = "SELECT * FROM Attacks \n" +
                "WHERE characterID = ?";
        List<Attack> list = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, character.getId());

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Attack(rs.getInt("attackID"), rs.getString("name"), rs.getString("category"), rs.getString("ability"),
                        rs.getInt("attackBonus"), rs.getInt("range"), rs.getInt("numDice"),
                        rs.getInt("damageDice"), rs.getString("damageType"), rs.getInt("characterID")));
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
    public void deleteDataByCharacter(Character c) {
        connect();
        String sql = "DELETE FROM Attacks WHERE characterID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, c.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }
}
