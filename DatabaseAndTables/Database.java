package DatabaseAndTables;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/db_time_attendance";
        String user = "root";
        String password = "MySQL@25052004";
        return DriverManager.getConnection(url, user, password);
    }

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/";
        String user = "root";
        String password = "MySQL@25052004";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();//Mon objet statement

            String createDatabase = "CREATE DATABASE IF NOT EXISTS db_time_attendance;";//Créer la database
            stmt.executeUpdate(createDatabase);
            stmt.executeUpdate("USE db_time_attendance");//Enclenche la database


            //Show all the databases
            /*
            ResultSet result = stmt.executeQuery("SHOW DATABASES");
            while (result.next()) {
                System.out.println(result.getString(1));
            }
             */

            //je crée les tables de ma base de données

            String createTable_t_emp = """
                    CREATE TABLE IF NOT EXISTS t_emp (
                        id INT PRIMARY KEY AUTO_INCREMENT, 
                        name VARCHAR(20) NOT NULL,
                        code CHAR(36) UNIQUE NOT NULL,
                        UNIQUE (name, code)
                    );
                    """;
            stmt.executeUpdate(createTable_t_emp);

            String createTable_t_lock_in_record = """
                    CREATE TABLE IF NOT EXISTS t_lock_in_record (
                        id INT,
                        check_in_time DATETIME,
                        FOREIGN KEY (id) REFERENCES t_emp (id)
                    );
                    """;
            stmt.executeUpdate(createTable_t_lock_in_record);

            String createTable_t_admin = """
                    CREATE TABLE IF NOT EXISTS t_admin (
                        id INT,
                        username VARCHAR(20) NOT NULL,
                        password VARCHAR(20) NOT NULL,
                        FOREIGN KEY (id) REFERENCES t_emp (id)
                    );
                    """;
            stmt.executeUpdate(createTable_t_admin);

            String createTable_t_work_time = """
                    CREATE TABLE IF NOT EXISTS t_work_time (
                        start TIME
                    );
                    """;
            stmt.executeUpdate(createTable_t_work_time);


            // je réinitialise mon tableau t_emp
            String resetTable= "DELETE FROM t_emp";
            stmt.executeUpdate(resetTable);

            //reset l'ID à 1
            String resetAutoIncrement = "ALTER TABLE t_emp AUTO_INCREMENT = 1";
            stmt.executeUpdate(resetAutoIncrement);

            // je réinitialise mon tableau t_admin
            String resetTableAdmin= "DELETE FROM t_admin";
            stmt.executeUpdate(resetTableAdmin);


            //je ferme les ressources à la fin
            stmt.close();
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
