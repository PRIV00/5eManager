package databases;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CalendarTable extends Table {

    CalendarTable(String fileName) {
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

    private void insertData(CalendarDate calendarDate) {
        connect();
        String sql = "INSERT OR IGNORE INTO FantasyCalendar \n" +
                "(dayNum, dayName, monthNum, monthName, year) \n" +
                "VALUES \n" +
                "(?, ?, ?, ?, ?)";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, calendarDate.getDayNum());
            pstmt.setString(2, calendarDate.getDayName());
            pstmt.setInt(3, calendarDate.getMonthNum());
            pstmt.setString(4, calendarDate.getMonthName());
            pstmt.setInt(5, calendarDate.getYear());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    calendarDate.setId(generatedKeys.getInt(1));
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

    public List<String> getAllData() {
        return new ArrayList<String>();
    }
}
