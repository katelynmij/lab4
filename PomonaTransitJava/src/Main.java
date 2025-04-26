import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            //load jdbc driver
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

            String dbPath = "PomonaTransit1.accdb"; 
            String url = "jdbc:ucanaccess://" + dbPath;

            Connection conn = DriverManager.getConnection(url);
            Scanner scanner = new Scanner(System.in);

            System.out.print("What is the start location?: ");
            String start = scanner.nextLine();

            System.out.print("What is the destination?: ");
            String dest = scanner.nextLine();

            System.out.print("What is the date? (yyyy-mm-dd): ");
            String dateInput = scanner.nextLine();

            PreparedStatement ps = conn.prepareStatement(
                "SELECT t.TripNumber, o.Date, o.ScheduledStartTime, o.ScheduledArrivalTime, " +
                "o.DriverName, o.BusID " +
                "FROM Trip t JOIN TripOffering o ON t.TripNumber = o.TripNumber " +
                "WHERE t.StartLocationName = ? AND t.DestinationName = ? AND o.Date = ?"
            );

            ps.setString(1, start);
            ps.setString(2, dest);
            ps.setDate(3, java.sql.Date.valueOf(dateInput));

            ResultSet rs = ps.executeQuery();

            System.out.println("\n-- Trip Schedule --");
            while (rs.next()) {
                System.out.println("Trip #: " + rs.getInt("TripNumber"));
                System.out.println("Date: " + rs.getDate("Date"));
                System.out.println("Start Time: " + rs.getTime("ScheduledStartTime"));
                System.out.println("Arrival Time: " + rs.getTime("ScheduledArrivalTime"));
                System.out.println("Driver: " + rs.getString("DriverName"));
                System.out.println("Bus ID: " + rs.getString("BusID"));
                System.out.println("-----");
            }



            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
