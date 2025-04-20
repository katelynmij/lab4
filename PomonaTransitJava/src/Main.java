import java.sql.*;

public class Main {
    public static void main(String[] args) {
        try {
            String dbPath = "../pomonatransit.accdb"; 
            String url = "jdbc:ucanaccess://" + dbPath;

            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM Trip");

            while (rs.next()) {
                System.out.println("Trip Number: " + rs.getInt("TripNumber"));
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
