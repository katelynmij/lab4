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
            int UserTask = 0;
            
            while (UserTask != 9) {
                // basic menu
                System.out.println("Enter a number between 1 and 9 for a given task");
                System.out.println("1. Display the schedule of all trips for a given StartLocationName, Destination Name, and Date.");
                System.out.println("2. Edit the schedule");
                System.out.println("3. Display the stops of a given trip");
                System.out.println("4. Display the weekly schedule of a given driver and date.");
                System.out.println("5. Add a driver.");
                System.out.println("6. Add a bus.");
                System.out.println("7. Delete a bus.");
                System.out.println("8. Record (insert) the actual data of a given trip offering specified by its key. ");
                System.out.println("9. Exit the program");
                UserTask = scanner.nextInt();
                scanner.nextLine();

                switch(UserTask) {
                    case 1:
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
                        break;
                        
                    case 2:
                        // WIP
                        break;
                    case 3:
                        // WIP
                        break;
                    case 4:
                        // WIP
                        break;
                    case 5:
                        // kenneth testing adding a driver
                        System.out.println("What is the Driver's name: ");
                        String name = scanner.nextLine();

                        System.out.println("What is the Driver's phone number: (xxx-xxx-xxxx)");
                        String phone = scanner.nextLine();

                        addDriver(conn, name, phone);
                        break;
                    case 6:
                        // adding a bus
                        System.out.println("Enter BusID: ");
                        String BusID = scanner.nextLine();

                        System.out.println("Enter the bus model: ");
                        String model = scanner.nextLine();

                        System.out.println("Enter the bus: ");
                        int year = scanner.nextInt();
                        scanner.nextLine();

                        addBus(conn, BusID, model, year);
                        break;
                    case 7:
                        //deleting a bus
                        System.out.println("Enter BusID: ");
                        String busID = scanner.nextLine();

                        deleteBus(conn, busID);
                        break;
                    case 8:
                        //inserting actual trip information will add later
                        break;
                    case 9:
                        System.out.println("Exiting");
                        break;
                    default:
                        System.out.println("Please enter a valid value.");               
                }
            }
            

            conn.close();
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void addDriver(Connection conn, String name, String phone) throws SQLException {
        String sql = "INSERT INTO Driver (DriverName, DriverTelephoneNumber) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, phone);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Driver " + name + " was successfully added.");
            } else {
                System.out.println("No driver was added.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Could not add driver: A driver with the name " + name + " already exists.");
        } catch (Exception e) {
            System.out.println("Error adding a driver, check inputs.");
        }
    }

    static void addBus(Connection conn, String busID, String model, int year) throws SQLException {
        String sql = "INSERT INTO Bus (BusID, Model, Year) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, busID);
            stmt.setString(2, model);
            stmt.setInt(3, year);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Bus " + busID + " was successfully added.");
            } else {
                System.out.println("No bus was added.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Could not add bus: A bus with the busID " + busID + " already exists.");
        } catch (Exception e) {
            System.out.println("Error adding a bus, check inputs.");
        }
    }

    static void deleteBus(Connection conn, String busID) throws SQLException {
        String sql = "DELETE FROM Bus WHERE BusID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, busID);
            int affectedRow = stmt.executeUpdate();
            System.out.println("Deleted " + affectedRow + " row from the Bus table");
        }
    }

    static void recordActualTripStopInfo(
    Connection conn, int tripNumber, Date date, Time scheduledStartTime,
    int stopNumber, Time scheduledArrivalTime, Time actualStartTime, Time actualArrivalTime,
    int passengersIn, int passengersOut) throws SQLException {

    String sql = "INSERT INTO ActualTripStopInfo " +
        "(TripNumber, Date, ScheduledStartTime, StopNumber, SecheduledArrivalTime, " +
        "ActualStartTime, ActualArrivalTime, NumberOfPassengerIn, NumberOfPassengerOut) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, tripNumber);
        stmt.setDate(2, date);
        stmt.setTime(3, scheduledStartTime);
        stmt.setInt(4, stopNumber);
        stmt.setTime(5, scheduledArrivalTime);
        stmt.setTime(6, actualStartTime);
        stmt.setTime(7, actualArrivalTime);
        stmt.setInt(8, passengersIn);
        stmt.setInt(9, passengersOut);
        stmt.executeUpdate();
    }
}
}
