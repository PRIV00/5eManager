package databases;

import models.DataModel;
import models.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LocationTable extends Table implements Searchable, Modifiable {

    LocationTable(String fileName) {
        super(fileName,
                "CREATE TABLE IF NOT EXISTS Locations (\n" +
                        " locationID INTEGER PRIMARY KEY, \n" +
                        " name TEXT, \n" +
                        " description TEXT, \n" +
                        " type TEXT, \n" +
                        " parentID INTEGER, \n" +
                        " FOREIGN KEY(parentID) REFERENCES Locations(locationID)" +
                        ");");
    }

    @Override
    public void insertData(DataModel locationData) throws SQLException {
        connect();
        String sql = "INSERT OR IGNORE INTO Locations(name, description, type) \n" +
                "VALUES(?,?,?)";
        Location loc = (Location) locationData;

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, loc.getName());
            pstmt.setString(2, loc.getDescription());
            pstmt.setString(3, loc.getLocType());
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
    public void deleteData(DataModel locationData) throws SQLException {
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
    public void updateData(DataModel locationData) throws SQLException {
        connect();
        String sql = "UPDATE Locations \n" +
                "SET name = ?, description = ?, type = ?, parentID = ? \n" +
                "WHERE locationID = ?";
        Location location = (Location) locationData;

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, location.getName());
            pstmt.setString(2, location.getDescription());
            pstmt.setString(3, location.getLocType());
            pstmt.setInt(4, location.getParentID());
            pstmt.setInt(5, location.getId());
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
                list.add(new Location(rs.getInt("locationID"), rs.getString("name"),
                        rs.getString("description"), rs.getString("type"), rs.getInt("parentID")));
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
                "       description, \n" +
                "       type, \n" +
                "       parentID \n" +
                "FROM   Locations \n" +
                "WHERE  name LIKE ('%' || ? || '%') \n" +
                "UNION ALL \n" +
                "SELECT Locations.locationID, \n" +
                "       Locations.name, \n" +
                "       Locations.description, \n" +
                "       Locations.type, \n" +
                "       Locations.parentID \n" +
                "FROM   Locations \n" +
                "INNER JOIN   LocationHierarchy ON Locations.parentID = LocationHierarchy.locationID \n" +
                ") \n" +
                "SELECT DISTINCT * \n" +        // Removes duplicates from the ResultSet
                "FROM   LocationHierarchy \n";
        String sql2 = "SELECT * \n" +
                "FROM Locations \n" +
                "WHERE type LIKE ('%' || ? || '%')";
        List<Location> list = new ArrayList<>();

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            pstmt.setString(1, query);
            pstmt2.setString(1, query);
            ResultSet rs = pstmt.executeQuery();
            ResultSet rs2 = pstmt2.executeQuery();

            while (rs.next()) {
                list.add(new Location(rs.getInt("locationID"), rs.getString("name"), rs.getString("description"),
                        rs.getString("type"), rs.getInt("parentID")));
            }

            /*
             * For each row in the ResultSet, it loops through the models.models.Location list created previously. If an ID match
             * occurs between the current ResultSet row and any models.models.Location in the list, the check changes to false.
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
                    list.add(new Location(rs2.getInt("locationID"), rs2.getString("name"), rs2.getString("description"),
                            rs2.getString("type"), rs2.getInt("parentID")));
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
