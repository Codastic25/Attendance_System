package DatabaseAndTables;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AdminFunctions extends Database {

    private LocalTime startWorkTime = LocalTime.of(10, 0);

    //---------------------------------------------------------------------------------------------------------------------------------------------
    //BASIC ADMINS FUNCTIONS

    // Add an employee
    public void createEmployee(String name, String code) throws SQLException {
        String sql = "INSERT INTO t_emp (name, code) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, code);
            statement.executeUpdate();
            //System.out.println("Employé créé avec succès.");
        }
    }

    // Add an admin
    public void createAdmin(int id, String username, String password) throws SQLException {
        String sql = "INSERT INTO t_admin (id, username, password) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setString(2, username);
            statement.setString(3, password);
            statement.executeUpdate();
            //System.out.println("Employé créé avec succès.");
        }
    }

    // Get all the employee
    public List<Object[]> getAllEmployee() throws SQLException {
        List<Object[]> employees = new ArrayList<>();
        String query = "SELECT id, name, code FROM t_emp";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_time_attendance", "root", "MySQL@25052004");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                employees.add(new Object[]{rs.getInt("id"), rs.getString("name"), rs.getString("code")});
            }
        }

        return employees;
    }

    // Get all the admin
    public List<Object[]> getAllAdmin() throws SQLException {
        List<Object[]> admin = new ArrayList<>();
        String query = "SELECT id, username, password FROM t_admin";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_time_attendance", "root", "MySQL@25052004");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                admin.add(new Object[]{rs.getInt("id"), rs.getString("username"), rs.getString("password")});
            }
        }

        return admin;
    }

    // Edit an employee
    public void updateEmployee(int id, String name) throws SQLException {
        String sql = "UPDATE t_emp SET name = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setInt(2, id);
            //statement.setString(2, code);
            statement.executeUpdate();
            //System.out.println("Employé mis à jour avec succès.");
        }
    }

    // Delete an employee
    public void deleteEmployee(int id) throws SQLException {
        String sql = "DELETE FROM t_emp WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            //System.out.println("Employé supprimé avec succès.");
        }
    }

    // Delete an admin
    public void deleteAdmin(int id) throws SQLException {
        String sql = "DELETE FROM t_admin WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            //System.out.println("Employé supprimé avec succès.");
        }
    }


    //---------------------------------------------------------------------------------------------------------------------------------------------
    //EMPLOYEE VERIFICATIONS FUNCTIONS


    // take the id of an employee
    public int getEmployeeIdByCode(String code) throws SQLException {
        String query = "SELECT id FROM t_emp WHERE code = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_time_attendance", "root", "MySQL@25052004");
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, code);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return -1; // the return if I don't find the employee
    }

    //Insert the time record of an employee in the summarized table
    public void logEmployeeCheckIn(int employeeId) throws SQLException {
        String query = "INSERT INTO t_lock_in_record (id, check_in_time) VALUES (?, NOW())";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_time_attendance", "root", "MySQL@25052004");
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, employeeId);
            pstmt.executeUpdate();
        }
    }


    //---------------------------------------------------------------------------------------------------------------------------------------------
    //TIME FUNCTIONS


    //Get the time when an employee log in
    public List<Object[]> timeRecordEmployeeWithStatus() throws SQLException {
        List<Object[]> employeesTime = new ArrayList<>();
        String query = "SELECT id, check_in_time FROM t_lock_in_record";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_time_attendance", "root", "MySQL@25052004");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String checkInTime = rs.getString("check_in_time");
                String status = calculateStatus(checkInTime);
                employeesTime.add(new Object[]{id, checkInTime, status});
            }
        }

        return employeesTime;
    }


    //See if the employee are present, at time or late
    private String calculateStatus(String checkInTime) {
        LocalTime checkIn = LocalTime.parse(checkInTime.substring(11));

        if (checkIn.isBefore(startWorkTime)) {
            return "Present";
        } else if (checkIn.equals(startWorkTime)) {
            return "At time";
        } else {
            return "Late";
        }
    }


    //---------------------------------------------------------------------------------------------------------------------------------------------
    //RESET FUNCTIONS

    //Reset the table employee
    public void resetEmployee (){
        String emptyTable = "DELETE FROM t_emp";
        String idToOne = "ALTER TABLE t_emp AUTO_INCREMENT = 1";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_time_attendance", "root", "MySQL@25052004");
             Statement stmt = conn.createStatement()) {

            // Exécuter la requête pour vider la table
            stmt.executeUpdate(emptyTable);

            // Réinitialiser l'auto-incrément
            stmt.executeUpdate(idToOne);

            System.out.println("Table t_emp has been reset and AUTO_INCREMENT set to 1.");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //Reset the table time records employee
    public void resetEmployeeTime (){
        String emptyTable = "DELETE FROM t_lock_in_record";
        String idToOne = "ALTER TABLE t_lock_in_record AUTO_INCREMENT = 1";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_time_attendance", "root", "MySQL@25052004");
             Statement stmt = conn.createStatement()) {

            // Exécuter la requête pour vider la table
            stmt.executeUpdate(emptyTable);

            // Réinitialiser l'auto-incrément
            stmt.executeUpdate(idToOne);

            System.out.println("Table t_lock_in_record has been reset and AUTO_INCREMENT set to 1.");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
