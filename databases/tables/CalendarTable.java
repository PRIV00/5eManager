package main.databases.tables;

import main.model.modeldata.CalendarDate;
import main.model.modeldata.ModelData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CalendarTable extends Table {

    public CalendarTable(String fileName) {
        super(fileName,
                "CREATE TABLE IF NOT EXISTS FantasyCalendar \n" +
                        "(\n" +
                        "dateID INTEGER PRIMARY KEY,\n" +
                        "dayNum INTEGER,\n" +
                        "dayName TEXT,\n" +
                        "monthNum INTEGER,\n" +
                        "monthName TEXT,\n" +
                        "year INTEGER \n" +
                        ")");
    }

    private void setup() {

    }

    @Override
    public void insertData(ModelData calendarDate) {
        connect();
        String sql = "INSERT OR IGNORE INTO FantasyCalendar \n" +
                "(dayNum, dayName, monthNum, monthName, year) \n" +
                "VALUES \n" +
                "(?, ?, ?, ?, ?)";

        CalendarDate cd = (CalendarDate) calendarDate;

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, cd.getDayNum());
            pstmt.setString(2, cd.getDayName());
            pstmt.setInt(3, cd.getMonthNum());
            pstmt.setString(4, cd.getMonthName());
            pstmt.setInt(5, cd.getYear());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cd.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating character failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    @Override
    public void deleteData(ModelData calendarDate) {

    }

    @Override
    public void updateData(ModelData modelData) {

    }

    @Override
    public int getNextID() {
        return 0;
    }
}
