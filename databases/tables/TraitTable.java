package main.databases.tables;

import main.model.modeldata.Character;
import main.model.modeldata.ModelData;
import main.model.characterfields.Trait;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TraitTable extends Table implements CharacterSubTable {

    public TraitTable(String fileName) {
        super(fileName,
                "CREATE TABLE IF NOT EXISTS Traits (\n" +
                        "traitID INTEGER PRIMARY KEY, \n" +
                        "name TEXT, \n" +
                        "description TEXT, \n" +
                        "characterID INTEGER, \n" +
                        "FOREIGN KEY (characterID) REFERENCES Characters(characterID) \n" +
                        ")");
    }

    public void deleteAll() {
        connect();
        String sql = "DELETE FROM Traits";

        try {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    @Override
    public void insertData(ModelData traitData) {
        connect();
        String sql = "INSERT OR IGNORE INTO Traits (name, description, characterID) VALUES (?,?,?)";
        Trait trait = (Trait) traitData;

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, trait.getName());
            pstmt.setString(2, trait.getDescription());
            pstmt.setInt(3, trait.getCharacterID());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    trait.setId(generatedKeys.getInt(1));
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
    public void deleteData(ModelData traitData) {
        connect();
        String sql = "DELETE FROM Traits WHERE traitID = ?";
        Trait trait = (Trait) traitData;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trait.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    @Override
    public void updateData(ModelData traitData) {
        connect();
        String sql = "UPDATE Traits SET name = ?, description = ?, characterID = ? \n" +
                "WHERE traitID = ?";
        Trait trait = (Trait) traitData;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, trait.getName());
            pstmt.setString(2, trait.getDescription());
            pstmt.setInt(3, trait.getCharacterID());
            pstmt.setInt(4, trait.getId());

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
        String sql = "SELECT max(traitID) FROM Traits";

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
    public List<Trait> getDataByCharacter(Character character) {
        connect();
        String sql = "SELECT * FROM Traits \n" +
                "WHERE characterID = ?";
        List<Trait> list = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, character.getId());

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Trait(rs.getInt("traitID"), rs.getString("name"), rs.getString("description"),
                        rs.getInt("characterID")));
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
        String sql = "DELETE FROM Traits WHERE characterID = ?";

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
