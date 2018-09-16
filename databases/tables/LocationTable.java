package main.databases.tables;

import main.models.TableModel;
import main.models.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LocationTable extends Table implements Searchable, TopLevelTable {

    public LocationTable(String fileName) {
        super(fileName,
                "CREATE TABLE IF NOT EXISTS Locations (\n" +
                        " locationID INTEGER PRIMARY KEY, \n" +
                        " name TEXT, \n" +
                        " locType TEXT, \n" +
                        " flavour TEXT, \n" +
                        " songLink TEXT, \n" +
                        " description TEXT, \n" +
                        " culture TEXT, \n" +
                        " government TEXT, \n" +
                        " crime TEXT, \n" +
                        " demographic TEXT, \n" +
                        " religion TEXT, \n" +
                        " history TEXT, \n" +
                        " parentID INTEGER, \n" +
                        " FOREIGN KEY(parentID) REFERENCES Locations(locationID)" +
                        ");");
    }

    @Override
    public void insertData(TableModel locationData) throws SQLException {
        connect();
        String sql = "INSERT OR IGNORE INTO Locations(name, locType, flavour, songLink, description, culture, " +
                "government, crime, demographic, religion, history, parentID) \n" +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        Location loc = (Location) locationData;

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, loc.getName());
            pstmt.setString(2, loc.getLocType());
            pstmt.setString(3, loc.getFlavour());
            pstmt.setString(4, loc.getSongLink());
            pstmt.setString(5, loc.getDescription());
            pstmt.setString(6, loc.getCulture());
            pstmt.setString(7, loc.getGovernment());
            pstmt.setString(8, loc.getCrime());
            pstmt.setString(9, loc.getDemographic());
            pstmt.setString(10, loc.getReligion());
            pstmt.setString(11, loc.getHistory());
            pstmt.setInt(12, loc.getParentID());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    loc.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating location failed, no ID obtained.");
                }
            }
        } finally {
            closeConnection();
        }
    }

    @Override
    public void deleteData(TableModel locationData) throws SQLException {
        connect();
        String sql = "DELETE \n" +
                "FROM Locations \n" +
                "WHERE locationID = ?";
        Location location = (Location) locationData;

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, location.getId());
            pstmt.executeUpdate();
            System.out.println("Entry deleted.");
        } finally {
            closeConnection();
        }
    }

    @Override
    public void updateData(TableModel locationData) throws SQLException {
        connect();
        String sql = "UPDATE Locations \n" +
                "SET name = ?, locType = ?, flavour = ?, songLink = ?, description = ?, culture = ?, " +
                "government = ?, crime = ?, demographic = ?, religion = ?, history = ?, parentID  = ?\n" +
                "WHERE locationID = ?";
        Location location = (Location) locationData;

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, location.getName());
            pstmt.setString(2, location.getLocType());
            pstmt.setString(3, location.getFlavour());
            pstmt.setString(4, location.getSongLink());
            pstmt.setString(5, location.getDescription());
            pstmt.setString(6, location.getCulture());
            pstmt.setString(7, location.getGovernment());
            pstmt.setString(8, location.getCrime());
            pstmt.setString(9, location.getDemographic());
            pstmt.setString(10, location.getReligion());
            pstmt.setString(11, location.getHistory());
            pstmt.setInt(12, location.getParentID());
            pstmt.setInt(13, location.getId());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No entry updated");
            } else {
                System.out.println("Entry updated.");
            }
        } finally {
            closeConnection();
        }
    }

    @Override
    public int getNextID() {
        connect();
        String sql = "SELECT max(locationID) \n" +
                "FROM Locations";

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
    public List<Location> getAllData() {
        connect();
        String sql = "SELECT * \n" +
                "FROM Locations";
        List<Location> list = new ArrayList<>();

        try (ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Location(rs.getInt("locationID"), rs.getString("name"), rs.getString("locType"),
                        rs.getString("flavour"), rs.getString("songLink"), rs.getString("description"),
                        rs.getString("culture"), rs.getString("government"), rs.getString("crime"), rs.getString("demographic"),
                        rs.getString("religion"), rs.getString("history"), rs.getInt("parentID")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        return list;
    }

    @Override
    public List<Location> query(String query) {
        connect();
        String sql = "WITH LocationHierarchy \n" +
                "AS \n" +
                "( \n" +
                "SELECT locationID, \n" +
                "       name, \n" +
                "       locType, \n" +
                "       flavour, \n" +
                "       songLink, \n" +
                "       description, \n" +
                "       culture, \n" +
                "       government, \n" +
                "       crime, \n" +
                "       demographic, \n" +
                "       religion, \n" +
                "       history, \n" +
                "       parentID \n" +
                "FROM   Locations \n" +
                "WHERE  name LIKE ('%' || ? || '%') \n" +
                "UNION ALL \n" +
                "SELECT Locations.locationID, \n" +
                "       Locations.name, \n" +
                "       Locations.locType, \n" +
                "       Locations.flavour, \n" +
                "       Locations.songLink, \n" +
                "       Locations.description, \n" +
                "       Locations.culture, \n" +
                "       Locations.government, \n" +
                "       Locations.crime, \n" +
                "       Locations.demographic, \n" +
                "       Locations.religion, \n" +
                "       Locations.history, \n" +
                "       Locations.parentID \n" +
                "FROM   Locations \n" +
                "INNER JOIN   LocationHierarchy ON Locations.parentID = LocationHierarchy.locationID \n" +
                ") \n" +
                "SELECT DISTINCT * \n" +        // Removes duplicates from the ResultSet
                "FROM   LocationHierarchy \n";
        String sql2 = "SELECT * \n" +
                "FROM Locations \n" +
                "WHERE locType LIKE ('%' || ? || '%')";
        List<Location> list = new ArrayList<>();

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            pstmt.setString(1, query);
            pstmt2.setString(1, query);
            ResultSet rs = pstmt.executeQuery();
            ResultSet rs2 = pstmt2.executeQuery();

            while (rs.next()) {
                list.add(new Location(rs.getInt("locationID"), rs.getString("name"), rs.getString("locType"),
                        rs.getString("flavour"), rs.getString("songLink"), rs.getString("description"),
                        rs.getString("culture"), rs.getString("government"), rs.getString("crime"), rs.getString("demographic"),
                        rs.getString("religion"), rs.getString("history"), rs.getInt("parentID")));
            }

            /*
             * For each row in the ResultSet, it loops through the models.models.models.Location list created previously. If an ID match
             * occurs between the current ResultSet row and any models.models.models.Location in the list, the check changes to false.
             *
             * This ensure that only rows with unique IDs are added to the list, preventing duplicate objects.
             */
            while (rs2.next()) {
                boolean check = true;
                for (Location i : list) {
                    if (i.getId() == rs2.getInt("locationID")) {
                        check = false;
                    }
                }
                if (check) {
                    list.add(new Location(rs2.getInt("locationID"), rs2.getString("name"), rs2.getString("locType"),
                            rs2.getString("flavour"), rs2.getString("songLink"), rs2.getString("description"),
                            rs2.getString("culture"), rs2.getString("government"), rs2.getString("crime"), rs2.getString("demographic"),
                            rs2.getString("religion"), rs2.getString("history"), rs2.getInt("parentID")));
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
