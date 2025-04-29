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
    
                        PreparedStatement ps1 = conn.prepareStatement(
                            "SELECT t.TripNumber, o.Date, o.ScheduledStartTime, o.ScheduledArrivalTime, " +
                            "o.DriverName, o.BusID " +
                            "FROM Trip t JOIN TripOffering o ON t.TripNumber = o.TripNumber " +
                            "WHERE t.StartLocationName = ? AND t.DestinationName = ? AND o.Date = ?"
                        );
    
                        ps1.setString(1, start);
                        ps1.setString(2, dest);
                        ps1.setDate(3, java.sql.Date.valueOf(dateInput));
    
                        ResultSet rs1 = ps1.executeQuery();
    
                        System.out.println("\n-- Trip Schedule --");
                        while (rs1.next()) {
                            System.out.println("Trip #: " + rs1.getInt("TripNumber"));
                            System.out.println("Date: " + rs1.getDate("Date"));
                            System.out.println("Start Time: " + rs1.getTime("ScheduledStartTime"));
                            System.out.println("Arrival Time: " + rs1.getTime("ScheduledArrivalTime"));
                            System.out.println("Driver: " + rs1.getString("DriverName"));
                            System.out.println("Bus ID: " + rs1.getString("BusID"));
                            System.out.println("-----");
                            }

                        rs1.close();
                        ps1.close();
                        break;
                        
                    case 2:
                        // WIP
                        System.out.println("Choose an option: ");
                        System.out.println("1.Delete a Trip Offering.");
                        System.out.println("2.Add a Trip Offering.");
                        System.out.println("3:Change the Driver for a Trip Offering.");
                        System.out.println("4:Change the Bus for a Trip Offering.");
                        System.out.print("Enter a number: ");
                        int choice = scanner.nextInt();
                        scanner.nextLine();

                        //preparing parameters
                        Integer tripNumber = null;
                        String date = null;
                        String startTime = null;
                        String arrivalTime = null;
                        String driverName = null;
                        String busID = null;

                        if (choice == 1 || choice == 3 || choice == 4) {
                            System.out.print("Enter the Trip Number: ");
                            tripNumber = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Enter the Date (YYYY-MM-DD): ");
                            date = scanner.nextLine();
                            System.out.print("Enter the Scheduled Start Time (HH:MM:SS): ");
                            startTime = scanner.nextLine();
                        }
                        if (choice == 2) {
                            System.out.print("Enter the Trip Number: ");
                            tripNumber = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Enter the Date (YYYY-MM-DD): ");
                            date = scanner.nextLine();
                            System.out.print("Enter the Scheduled Start Time (HH:MM:SS): ");
                            startTime = scanner.nextLine();
                            System.out.print("Enter the Scheduled Arrival Time (HH:MM:SS): ");
                            arrivalTime = scanner.nextLine();
                            System.out.print("Enter the Driver's Name: ");
                            driverName = scanner.nextLine();
                            System.out.print("Enter the Bus ID: ");
                            busID = scanner.nextLine();
                        }
                        if (choice == 3) {
                            System.out.print("Enter the New Driver's Name: ");
                            driverName = scanner.nextLine();
                        }
                        if (choice == 4) {
                            System.out.print("Enter the New Bus ID: ");
                            busID = scanner.nextLine();
                        }
                        editTripOffering(conn, choice, tripNumber, date, startTime, arrivalTime, driverName, busID);
                        break;
                    case 3:
                        // WIP
                        System.out.print("Enter the trip number: ");
                        int tripNumForStops = scanner.nextInt();
                        displayTripStops(conn, tripNumForStops);
                        break;
                    case 4:
                        // WIP
                        System.out.print("Enter the driver's name: ");
                        String driverForWeeklySchedule = scanner.nextLine();
                        System.out.print("Etner the date (YYYY-MM-DD): ");
                        String weeklyDateInput = scanner.nextLine();
                        displayWeeklySchedule(conn, driverForWeeklySchedule, weeklyDateInput);
                        break;
                    case 5:
                        // kenneth testing adding a driver
                        System.out.println("What is the Driver's name: ");
                        String newDriverName = scanner.nextLine();

                        System.out.println("What is the Driver's phone number: (xxx-xxx-xxxx)");
                        String phone = scanner.nextLine();

                        addDriver(conn, newDriverName, phone);
                        break;
                    case 6:
                        // adding a bus
                        System.out.println("Enter BusID: ");
                        int newBusID = scanner.nextInt();
                        scanner.nextLine();

                        System.out.println("Enter the bus model: ");
                        String model = scanner.nextLine();

                        System.out.println("Enter the bus year: ");
                        int year = scanner.nextInt();
                        scanner.nextLine();

                        addBus(conn, newBusID, model, year);
                        break;
                    case 7:
                        //deleting a bus
                        System.out.println("Enter BusID: ");
                        int busIDToDelete = scanner.nextInt();
                        scanner.nextLine();

                        deleteBus(conn, busIDToDelete);
                        break;
                    case 8:
                        //inserting actual trip information
                        System.out.println("Enter tripNumber: ");
                        int tripNumberInput = scanner.nextInt();
                        scanner.nextLine();

                        System.out.println("Enter date of the trip (YYYY-MM-DD): ");
                        Date tripDate = Date.valueOf(scanner.nextLine());   

                        System.out.println("Enter a scheduled start time (HH:MM:SS): ");
                        Time scheduledStart = Time.valueOf(scanner.nextLine()); 

                        System.out.println("Enter a stop number: ");
                        int stopNumber = scanner.nextInt();
                        scanner.nextLine();

                        System.out.println("Enter a scheduled arrival time (HH:MM:SS): ");
                        Time scheduledArrival= Time.valueOf(scanner.nextLine());

                        System.out.println("Enter a actual start time (HH:MM:SS): ");
                        Time actualStart = Time.valueOf(scanner.nextLine());

                        System.out.println("Enter a actual arrival time (HH:MM:SS): ");
                        Time actualArrival = Time.valueOf(scanner.nextLine());

                        System.out.println("Enter amount of passengers in: ");
                        int passengersIn = scanner.nextInt();
                        scanner.nextLine();

                        System.out.println("Enter amount of passengers out: ");
                        int passengersOut = scanner.nextInt();
                        scanner.nextLine();

                        recordActualTripStopInfo(conn, tripNumberInput, tripDate, scheduledStart, stopNumber, scheduledArrival, actualStart, actualArrival, passengersIn, passengersOut);
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


    
    //task 2
    static void editTripOffering(
        Connection conn,
        int choice,
        Integer tripNumber,
        String date,
        String startTime,
        String arrivalTime,
        String driverName,
        String busID
    ) throws SQLException {
        /* 
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose.");
        System.out.println("1: Delete a Trip Offering");
        System.out.println("2: Add a Trip Offering");
        System.out.println("3: Change the Driver for a Trip Offering");
        System.out.println("4. Change the bus for a Trip Offering");
        System.out.print("Enter a number: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        */

        //delete
        if (choice == 1) {
            String sql = "DELETE FROM TripOffering WHERE TripNumber = ? AND Date = ? AND ScheduledStartTime = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, tripNumber);
            ps.setDate(2, java.sql.Date.valueOf(date));
            ps.setTime(3, java.sql.Time.valueOf(startTime));
            int rows = ps.executeUpdate();
            System.out.println(rows + " atrip offering(s) deleted.");
            ps.close();
        } else if (choice == 2) { //add
            String sql = "INSERT INTO TripOffering (TripNumber, Date, ScheduledStartTime, ScheduledArrivalTime, DriverName, BusID) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, tripNumber);
            ps.setDate(2, java.sql.Date.valueOf(date));
            ps.setTime(3, java.sql.Time.valueOf(startTime));
            ps.setTime(4, java.sql.Time.valueOf(arrivalTime));
            ps.setString(5, driverName);
            ps.setString(6, busID);
            ps.executeUpdate();
            System.out.println("Trip offering added.");
            ps.close();

        } else if (choice == 3) { // changing driver
            String sql = "UPDATE TripOffering SET DriverName = ? WHERE TripNumber = ? AND Date = ? AND ScheduledStartTime = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, driverName);
            ps.setInt(2, tripNumber);
            ps.setDate(3, java.sql.Date.valueOf(date));
            ps.setTime(4, java.sql.Time.valueOf(startTime));
            int rows = ps.executeUpdate();
            System.out.println(rows + " trip offering(s) updated with the new driver.");
            ps.close();
        } else if (choice == 4) { // changing bus
            String sql = "UPDATE TripOffering SET BusID = ? WHERE TripNumber = ? AND Date = ? AND ScheduledStartTime = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, busID);
            ps.setInt(2, tripNumber);
            ps.setDate(3, java.sql.Date.valueOf(date));
            ps.setTime(4, java.sql.Time.valueOf(startTime));
            int rows = ps.executeUpdate();
            System.out.println(rows + " trip offering(s) updated with new bus.");
            ps.close();
        } else {
            System.out.println("Enter a number 1 through 4.");
        }
    }


    //task 3
    static void displayTripStops(Connection conn, int tripNumber) throws SQLException {
        /* 
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the trip number: ");
        int tripNumber = scanner.nextInt();
        */
        String query = """
            SELECT StopNumber, SequenceNumber, DrivingTime
            FROM TripStopInfo
            WHERE TripNumber = ?
            ORDER BY SequenceNumber
            """;

        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, tripNumber);

        ResultSet rs = ps.executeQuery();

        System.out.println("\n -- trip stops for #" + tripNumber + " -- ");
        while (rs.next()) {
            System.out.println("Stop #: " + rs.getInt("StopNumber"));
            System.out.println("Sequence #: " + rs.getInt("SequenceNumber"));
            System.out.println("Driving Time: " + rs.getInt("DrivingTime") + " minutes");
            System.out.println("---------");
        }

        rs.close();
        ps.close();
    }

    //task 4
    static void displayWeeklySchedule(Connection conn, String driverName, String dateInput) throws SQLException {
        Date date = Date.valueOf(dateInput);
        /* 
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the driver's name: ");
        String driverName = scanner.nextLine();

        System.out.print("Enter the date (YYYY-MM-DD): ");
        String dateInput = scanner.nextLine();
        */
        //monday
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        cal.set(java.util.Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        Date monday = new Date(cal.getTimeInMillis());

        //sunday
        cal.add(java.util.Calendar.DATE, 6);
        Date sunday = new Date(cal.getTimeInMillis());

        String query = """
        SELECT TripNumber, Date, ScheduledStartTime, ScheduledArrivalTime, BusID
        FROM TripOffering
        WHERE DriverName = ? AND Date BETWEEN ? AND ?
        ORDER BY Date, ScheduledStartTime
        """;

        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, driverName);
        ps.setDate(2, monday);
        ps.setDate(3, sunday);

        ResultSet rs = ps.executeQuery();

        System.out.println("\n--- Weekly schedule for " + driverName + " ---");
        while (rs.next()) {
            System.out.println("Trip #: " + rs.getInt("TripNumber"));
            System.out.println("Date: " + rs.getDate("Date"));
            System.out.println("Start Time: " + rs.getTime("ScheduledStartTime"));
            System.out.println("Arrival Time: " + rs.getTime("ScheduledArrivalTime"));
            System.out.println("Bus ID: " + rs.getString("BusID"));
            System.out.println("--------");
        }

        rs.close();
        ps.close();
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

    static void addBus(Connection conn, int busID, String model, int year) throws SQLException {
        String sql = "INSERT INTO Bus (BusID, Model, Year) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, busID);
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

    static void deleteBus(Connection conn, int busID) throws SQLException {
        String sql = "DELETE FROM Bus WHERE BusID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, busID);
            int affectedRow = stmt.executeUpdate();
            System.out.println("Deleted " + affectedRow + " row from the Bus table");
        }
    }

    static void recordActualTripStopInfo(
    Connection conn, int tripNumber, Date date, Time scheduledStartTime,
    int stopNumber, Time scheduledArrivalTime, Time actualStartTime, Time actualArrivalTime,
    int passengersIn, int passengersOut) throws SQLException {

    String sql = "INSERT INTO ActualTripStopInfo " +
        "(TripNumber, Date, ScheduledStartTime, StopNumber, ScheduledArrivalTime, " +
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
        } catch (SQLException e) {
            if (e.getMessage().contains("REFERENCE constraint")) {
                System.out.println("Foreign key constraint violation: " + e.getMessage());
            } else {
                throw e;
            }
        }
    }
}
