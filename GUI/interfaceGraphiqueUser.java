package GUI;

import DatabaseAndTables.AdminFunctions;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.sql.*;
import java.util.List;

//importations pour executer le code python
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class interfaceGraphiqueUser {


    public static String videoPython() {

        String recognizedName = null;


        try {

            Process process = Runtime.getRuntime().exec("python3 /home/cytech/id3/java/projets/attendance_system/attendance_projet/src/faceRecognition.py");

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            recognizedName = reader.readLine();  // assum your py code only print one line.

            process.waitFor();

        } catch (Exception ex) {

            ex.printStackTrace();

        }


        return recognizedName;

    }


    public static void photoPython(String name) {
        new Thread(() -> {
            try {
                String pythonScriptPath = "/home/cytech/id3/java/projets/attendance_system/attendance_projet/src/takePictures.py";

                // Passer le nom comme argument
                ProcessBuilder processBuilder = new ProcessBuilder("python3", pythonScriptPath, name);
                Process process = processBuilder.start();

                // Lire la sortie du script Python
                try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                     BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

                    String line;
                    System.out.println("Output from the Python script:");
                    while ((line = stdInput.readLine()) != null) {
                        System.out.println(line);
                    }

                    System.out.println("Python script errors (if any):");
                    while ((line = stdError.readLine()) != null) {
                        System.err.println(line);
                    }
                }

                int exitCode = process.waitFor();
                System.out.println("Python script exited with code " + exitCode);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }



    public static void emptyPhotos() {
        try {
            // Exécuter le script Python pour vider le fichier JSON et supprimer les fichiers dans le dossier Dataset
            Process process = Runtime.getRuntime().exec("python3 /home/cytech/id3/java/projets/attendance_system/attendance_projet/src/emptyFileNames.py");

            // Lire la sortie standard du processus
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            // Afficher la sortie standard pour surveiller l'exécution
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Attendre la fin du processus
            process.waitFor();
            System.out.println("Script Python exécuté avec succès.");

        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Erreur lors de l'exécution du script Python.");
        }
    }



    public static void logInEmployee (){

        JFrame window = new JFrame ( " Login Page Employee" ) ;
        window . setBounds (100 , 200 , 400 , 400) ;
        window . setLayout ( null ) ;
        JLabel label2 = new JLabel ( " Enter your code :" );
        JLabel label1 = new JLabel ( " Enter your name :" );

        label1 . setBounds (50 , 60 , 150 , 25) ;
        window . add ( label1 ) ;
        JPasswordField textFieldEmployee = new JPasswordField (20) ;
        textFieldEmployee . setBounds (200 , 20 , 130 , 25) ;
        window . add ( textFieldEmployee ) ;

        label2 . setBounds (50 , 20 , 150 , 25) ;
        window . add ( label2 ) ;
        JTextField textFieldEmployee2 = new JTextField (20) ;
        textFieldEmployee2 . setBounds (200 , 60 , 130 , 25) ;
        window . add ( textFieldEmployee2 ) ;

        // Add a submit button
        JButton jb = new JButton("Log in");
        jb.setBounds(130, 100, 130, 25);
        window.add(jb);
        jb.setBackground(new Color(0, 255, 0));

        // Event of Sign button
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminFunctions admin = new AdminFunctions();
                String enteredCode = textFieldEmployee.getText().trim();  // Code de l'employé
                String enteredName = textFieldEmployee2.getText().trim();  // Nom de l'employé saisi

                try {
                    int employeeId = admin.getEmployeeIdByCode(enteredCode);
                    if (employeeId > 0) {
                        // Appel de la reconnaissance faciale Python et récupérer le nom reconnu
                        String recognizedName = videoPython();

                        // Vérification que le nom reconnu est le même que celui saisi
                        if (recognizedName != null && enteredName.trim().equalsIgnoreCase(recognizedName.trim())) {
                            admin.logEmployeeCheckIn(employeeId);
                            JOptionPane.showMessageDialog(window, "Employee Checked!");
                        } else {
                            System.out.println("Nom saisi : " + enteredName);
                            System.out.println("Nom reconnu : " + recognizedName);
                            JOptionPane.showMessageDialog(window, "Face recognition did not match the entered name.");
                        }

                    } else {
                        // Afficher un message d'erreur si l'employé n'est pas trouvé
                        JOptionPane.showMessageDialog(window, "No employee found!");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(window, "Error during the check-in process.");
                }
            }
        });





        JButton goBack = new JButton("Previous");
        goBack.setBounds(150, 300, 100, 50);
        window.add(goBack);
        goBack.setBackground(new Color(255, 99, 71));

        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.dispose(); // Ferme la fenêtre actuelle
            }
        });


        // Set JFrame properties
        window . setVisible ( true ) ;
        window . setDefaultCloseOperation( JFrame . EXIT_ON_CLOSE ); ;
        window . setResizable ( false ) ;
    }

    public static void logInAdmin (){
        JFrame window = new JFrame ( " Login Page Admin" ) ;
        window . setBounds (100 , 200 , 400 , 400) ;
        window . setLayout ( null ) ;
        JLabel label1 = new JLabel ( " Username " );
        JLabel label2 = new JLabel ( " Password " );

        label1 . setBounds (50 , 20 , 150 , 25) ;
        window . add ( label1 ) ;
        JTextField textField = new JTextField (20) ;
        textField . setBounds (200 , 20 , 130 , 25) ;
        window . add ( textField ) ;

        label2.setBounds(50, 50, 150, 25);
        window.add(label2);
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setBounds(200, 50, 130, 25);
        window.add(passwordField);


        // Add a submit button
        JButton jb = new JButton ( " Sign " ) ;
        jb . setBounds (130 , 100 , 130 , 25) ;
        window . add ( jb ) ;
        jb.setBackground(new Color(0, 255, 0));

        //error of input
        JLabel errorLabel = new JLabel();
        errorLabel.setBounds(50, 250, 300, 25);
        errorLabel.setForeground(Color.RED);
        window.add(errorLabel);

        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Récupérer les valeurs saisies
                String username = textField.getText();
                String password = new String(passwordField.getPassword());

                // Cas spécial pour "Aurian" avec mot de passe "2505"
                if ("Aurian".equals(username) && "2505".equals(password)) {
                    errorLabel.setText("");
                    ForAdmin(textField, passwordField); // Accès administrateur pour "Aurian"
                    return;
                }

                // Vérification dans la base de données pour les autres administrateurs
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_time_attendance", "root", "MySQL@25052004");
                     PreparedStatement stmt = conn.prepareStatement("SELECT * FROM t_admin WHERE username = ? AND password = ?")) {

                    stmt.setString(1, username);
                    stmt.setString(2, password);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        errorLabel.setText("");
                        ForAdmin(textField, passwordField); // Accès autorisé pour les admins de la base de données
                    } else {
                        errorLabel.setText("Wrong Username or Password!");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    errorLabel.setText("Database error!");
                }
            }
        });


        JButton goBack = new JButton("Previous");
        goBack.setBounds(150, 300, 100, 50);
        window.add(goBack);
        goBack.setBackground(new Color(255, 99, 71));

        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.dispose(); // Ferme la fenêtre actuelle
            }
        });

        // Set JFrame properties
        window . setVisible ( true ) ;
        window . setDefaultCloseOperation( JFrame . EXIT_ON_CLOSE ); ;
        window . setResizable ( false ) ;
    }

    public static void ForAdmin (JTextField textFieldEmployee, JPasswordField passwordField) {
        JFrame window = new JFrame ( " For Admin" ) ;
        window . setBounds (500,200,600,700) ;
        window . setLayout ( null ) ;

        JLabel introEmployee = new JLabel("HERE ARE THE FUNCTIONS OF THE ADMIN (EMPLOYEES)");
        introEmployee.setBounds(40, 40, 600, 50);
        introEmployee.setHorizontalAlignment(SwingConstants.CENTER); // Centrer le texte
        window.add(introEmployee);


        JLabel introAdmin = new JLabel("HERE ARE THE FUNCTIONS OF THE ADMIN (ADMINS)");
        introAdmin.setBounds(40, 390, 600, 50);
        introAdmin.setHorizontalAlignment(SwingConstants.CENTER); // Centrer le texte
        window.add(introAdmin);

        //button for adding employee
        JButton addEmployee = new JButton ( "Add Employee" ) ;
        addEmployee . setBounds (50 , 100 , 300 , 25) ;
        window . add ( addEmployee ) ;
        addEmployee.setBackground(new Color(70, 130, 180));

        addEmployee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //creation of the employee in the database
                CreationEmployee();
            }
        });

        //button for adding employee
        JButton addAdmin = new JButton ( "Add Admin" ) ;
        addAdmin . setBounds (50 , 450 , 300 , 25) ;
        window . add ( addAdmin ) ;
        addAdmin.setBackground(new Color(186, 85, 211));

        addAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //the function for adding admin
                CreationAdmin();
            }
        });

        //button for deleting employee
        JButton deleteEmployee = new JButton ( "Delete Employee" ) ;
        deleteEmployee . setBounds (50 , 150 , 300 , 25) ;
        window . add ( deleteEmployee ) ;
        deleteEmployee.setBackground(new Color(70, 130, 180));

        deleteEmployee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Deletion of the employee in the database
                DeleteEmployee();
            }
        });

        //button for deleting employee
        JButton deleteAdmin = new JButton ( "Delete Admin" ) ;
        deleteAdmin . setBounds (50 , 500 , 300 , 25) ;
        window . add ( deleteAdmin ) ;
        deleteAdmin.setBackground(new Color(186, 85, 211));

        deleteAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Deletion of the admin in the database
                DeleteAdmin();
            }
        });

        //button for getting employee
        JButton getAllEmployee = new JButton ( "Get Employee" ) ;
        getAllEmployee . setBounds (50 , 200 , 300 , 25) ;
        window . add ( getAllEmployee ) ;
        getAllEmployee.setBackground(new Color(70, 130, 180));

        getAllEmployee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminFunctions admin = new AdminFunctions();

                try {
                    // Récupérer les données des employés
                    List<Object[]> employeeData = admin.getAllEmployee();

                    // Afficher les données dans le terminal pour vérifier (facultatif)
                    employeeData.forEach(row -> System.out.println(
                            "ID: " + row[0] + ", Name: " + row[1] + ", Code: " + row[2]
                    ));

                    // Passer les données au tableau GUI
                    AffichageEmployee(employeeData);

                    //System.out.println(employeeData);

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(window, "Error retrieving employees: " + ex.getMessage());
                }
            }
        });

        //button for getting admin
        JButton getAllAdmin = new JButton ( "Get Admin" ) ;
        getAllAdmin . setBounds (50 , 550 , 300 , 25) ;
        window . add ( getAllAdmin ) ;
        getAllAdmin.setBackground(new Color(186, 85, 211));

        getAllAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminFunctions admin = new AdminFunctions();

                try {
                    // Récupérer les données des employés
                    List<Object[]> adminData = admin.getAllAdmin();

                    // Passer les données au tableau GUI
                    AffichageAdmin(adminData);

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(window, "Error retrieving admin: " + ex.getMessage());
                }
            }
        });


        //button for getting Check in time of employee
        JButton CheckInTimeEmployee = new JButton ( "Check time Records Employee" ) ;
        CheckInTimeEmployee . setBounds (50 , 350 , 300 , 25) ;
        window . add ( CheckInTimeEmployee ) ;
        CheckInTimeEmployee.setBackground(new Color(70, 130, 180));

        CheckInTimeEmployee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminFunctions admin = new AdminFunctions();

                try {
                    // Récupérer les données temps des employés
                    List<Object[]> employeeDataTime = admin.timeRecordEmployeeWithStatus();

                    // Passer les données au tableau GUI
                    AffichageTimeRecordEmployee(employeeDataTime);

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(window, "Error retrieving check in time employees: " + ex.getMessage());
                }
            }
        });


        //button for editing employee
        JButton updateEmployee = new JButton ( "Edit Employee" ) ;
        updateEmployee . setBounds (50 , 250 , 300 , 25) ;
        window . add ( updateEmployee ) ;
        updateEmployee.setBackground(new Color(70, 130, 180));

        updateEmployee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //call the edit function
                EditEmployee();
            }
        });

        //button for reseting employee
        JButton resetEmployee = new JButton ( "Reset Employee" ) ;
        resetEmployee . setBounds (50 , 300 , 300 , 25) ;
        window . add ( resetEmployee ) ;
        resetEmployee.setBackground(new Color(70, 130, 180));

        resetEmployee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminFunctions admin = new AdminFunctions();

                // reset all employee in the table and their time log in
                admin.resetEmployee();
                admin.resetEmployeeTime();

                emptyPhotos();

                // Afficher un message de confirmation
                JOptionPane.showMessageDialog(window, "Employee tables and photos have been reset!");
                //System.out.println(employeeData);

            }
        });


        JButton goBack = new JButton("Previous");
        goBack.setBounds(450, 525, 100, 50);
        window.add(goBack);
        goBack.setBackground(new Color(255, 99, 71));

        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.dispose(); // Ferme la fenêtre actuelle
            }
        });


        // Set JFrame properties
        window . setVisible ( true ) ;
        window . setDefaultCloseOperation( JFrame . EXIT_ON_CLOSE ); ;
        window . setResizable ( false ) ;
    }


    public static void CreationAdmin () {

        JFrame window = new JFrame("Page Admin");
        window.setBounds(200, 200, 400, 400);
        window.setLayout(null);
        JLabel label1 = new JLabel(" Password for admin : ");
        JLabel label2 = new JLabel(" Name for admin : ");
        JLabel label3 = new JLabel(" ID for admin : ");


        label2.setBounds(40, 60, 200, 25);
        window.add(label2);
        JTextField textFieldEmployee2 = new JTextField(20);
        textFieldEmployee2.setBounds(240, 60, 130, 25);
        window.add(textFieldEmployee2);

        label1.setBounds(40, 20, 200, 25);
        window.add(label1);
        JPasswordField textFieldEmployee = new JPasswordField(20);
        textFieldEmployee.setBounds(240, 20, 130, 25);
        window.add(textFieldEmployee);

        label3.setBounds(40, 100, 200, 25);
        window.add(label3);
        JTextField textFieldAdmin = new JTextField(20);
        textFieldAdmin.setBounds(240, 100, 130, 25);
        window.add(textFieldAdmin);

        // Add a submit button
        JButton jb = new JButton(" Create ");
        jb.setBounds(130, 150, 130, 25);
        window.add(jb);
        jb.setBackground(new Color(0, 255, 0));

        //event of Sign button
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Récupérer le id admin et le nom
                String mdp = textFieldEmployee.getText();
                String name = textFieldEmployee2.getText();
                String id = textFieldAdmin.getText();
                int idAdmin = Integer.valueOf(id);

                //creation of the admin in the database
                AdminFunctions newAdmin = new AdminFunctions();

                try {
                    newAdmin.createAdmin(idAdmin, name, mdp);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                System.out.println("ID admin " + id + "Password : " + mdp + " ; Name : " + name);
                JOptionPane.showMessageDialog(window, "Admin added successfully!");

            }
        });

        JButton goBack = new JButton("Previous");
        goBack.setBounds(250, 300, 100, 50);
        window.add(goBack);
        goBack.setBackground(new Color(255, 99, 71));

        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.dispose(); // Ferme la fenêtre actuelle
            }
        });

        // Set JFrame properties
        window . setVisible ( true ) ;
        window . setDefaultCloseOperation( JFrame . EXIT_ON_CLOSE ); ;
        window . setResizable ( false ) ;
    }


    public static void CreationEmployee () {

        JFrame window = new JFrame("Page Employee");
        window.setBounds(200, 200, 400, 400);
        window.setLayout(null);
        JLabel label1 = new JLabel(" Password for employee : ");
        JLabel label2 = new JLabel(" Name for employee : ");


        label2.setBounds(40, 60, 200, 25);
        window.add(label2);
        JTextField textFieldEmployee2 = new JTextField(20);
        textFieldEmployee2.setBounds(240, 60, 130, 25);
        window.add(textFieldEmployee2);

        label1.setBounds(40, 20, 200, 25);
        window.add(label1);
        JPasswordField textFieldEmployee = new JPasswordField(20);
        textFieldEmployee.setBounds(240, 20, 130, 25);
        window.add(textFieldEmployee);



        // Add a capture button
        JButton capture = new JButton ( " Capture" ) ;
        capture . setBounds (130 , 100 , 130 , 25) ;
        window . add ( capture ) ;
        capture.setBackground(new Color(192, 192, 192)); // Gris perle



        //event of Capture button
        capture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = textFieldEmployee2.getText(); // Récupérer le nom saisi
                if (!name.isEmpty()) {
                    photoPython(name); // Passer le nom au script Python
                } else {
                    JOptionPane.showMessageDialog(window, "Veuillez entrer un nom.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });



        // Add a create button
        JButton jb = new JButton(" Create ");
        jb.setBounds(130, 150, 130, 25);
        window.add(jb);
        jb.setBackground(new Color(0, 255, 0));

        //event of Create button
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Récupérer le id employee et le nom
                String mdp = textFieldEmployee.getText();
                String name = textFieldEmployee2.getText();

                //creation of the employee in the database
                AdminFunctions newEmployee = new AdminFunctions();

                try {
                    newEmployee.createEmployee(name, mdp);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                System.out.println("Password : " + mdp + " ; Name : " + name);
                JOptionPane.showMessageDialog(window, "Employee added successfully!");

            }
        });


        JButton goBack = new JButton("Previous");
        goBack.setBounds(250, 300, 100, 50);
        window.add(goBack);
        goBack.setBackground(new Color(255, 99, 71));

        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.dispose(); // Ferme la fenêtre actuelle
            }
        });

        // Set JFrame properties
        window . setVisible ( true ) ;
        window . setDefaultCloseOperation( JFrame . EXIT_ON_CLOSE ); ;
        window . setResizable ( false ) ;
    }


    public static void EditEmployee () {

        JFrame window = new JFrame("Page Edit Employee");
        window.setBounds(200, 200, 400, 400);
        window.setLayout(null);
        JLabel label2 = new JLabel(" New name of employee : ");
        JLabel label1 = new JLabel(" ID of employee : ");


        label2.setBounds(40, 60, 200, 25);
        window.add(label2);
        JTextField textFieldEmployee2 = new JTextField(20);
        textFieldEmployee2.setBounds(240, 60, 130, 25);
        window.add(textFieldEmployee2);

        label1.setBounds(40, 20, 200, 25);
        window.add(label1);
        JTextField textFieldEmployee = new JTextField(20);
        textFieldEmployee.setBounds(240, 20, 130, 25);
        window.add(textFieldEmployee);

        // Add a submit button
        JButton jb = new JButton(" Edit ");
        jb.setBounds(130, 100, 130, 25);
        window.add(jb);
        jb.setBackground(new Color(0, 255, 0));

        //event of Sign button
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Récupérer le id employee et le nom
                String id = textFieldEmployee.getText();
                String name = textFieldEmployee2.getText();

                if (id.matches("\\d+")) { // Validation de l'ID
                    int idForEdit = Integer.valueOf(id);
                    AdminFunctions newEmployee = new AdminFunctions();
                    try {
                        newEmployee.updateEmployee(idForEdit, name);
                        JOptionPane.showMessageDialog(window, "Employee edited successfully!");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(window, "Error updating employee: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(window, "Veuillez entrer un ID valide (chiffres uniquement).");
                }

                System.out.println("id changed : " + id + " ; Name : " + name);
                //JOptionPane.showMessageDialog(window, "Employee edited successfully!");

            }
        });

        JButton goBack = new JButton("Previous");
        goBack.setBounds(250, 300, 100, 50);
        window.add(goBack);
        goBack.setBackground(new Color(255, 99, 71));

        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.dispose(); // Ferme la fenêtre actuelle
            }
        });

        // Set JFrame properties
        window . setVisible ( true ) ;
        window . setDefaultCloseOperation( JFrame . EXIT_ON_CLOSE ); ;
        window . setResizable ( false ) ;
    }

    public static void DeleteAdmin () {
        JFrame window = new JFrame("Page Admin");
        window.setBounds(200, 200, 400, 400);
        window.setLayout(null);
        JLabel label1 = new JLabel(" ID of admin : ");

        label1.setBounds(40, 20, 200, 25);
        window.add(label1);
        JTextField textFieldAdmin = new JTextField(20);
        textFieldAdmin.setBounds(240, 20, 130, 25);
        window.add(textFieldAdmin);

        // Add a submit button
        JButton jb = new JButton(" Delete ");
        jb.setBounds(130, 100, 130, 25);
        window.add(jb);
        jb.setBackground(new Color(255, 0, 0));


        //event of Sign button
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Récupérer le id employee et le nom
                String id = textFieldAdmin.getText();
                int idAdmin = Integer.valueOf(id);

                //creation of the employee in the database
                AdminFunctions newAdmin = new AdminFunctions();

                try {
                    newAdmin.deleteAdmin(idAdmin);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                System.out.println("ID deleted : " + idAdmin);
                JOptionPane.showMessageDialog(window, "Admin deleted successfully!");

            }
        });

        JButton goBack = new JButton("Previous");
        goBack.setBounds(250, 300, 100, 50);
        window.add(goBack);
        goBack.setBackground(new Color(255, 99, 71));

        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.dispose(); // Ferme la fenêtre actuelle
            }
        });

        // Set JFrame properties
        window . setVisible ( true ) ;
        window . setDefaultCloseOperation( JFrame . EXIT_ON_CLOSE ); ;
        window . setResizable ( false ) ;
    }


    public static void DeleteEmployee () {
        JFrame window = new JFrame("Page Employee");
        window.setBounds(200, 200, 400, 400);
        window.setLayout(null);
        JLabel label1 = new JLabel(" ID of employee : ");

        label1.setBounds(40, 20, 200, 25);
        window.add(label1);
        JTextField textFieldEmployee = new JTextField(20);
        textFieldEmployee.setBounds(240, 20, 130, 25);
        window.add(textFieldEmployee);

        // Add a submit button
        JButton jb = new JButton(" Delete ");
        jb.setBounds(130, 100, 130, 25);
        window.add(jb);
        jb.setBackground(new Color(255, 0, 0));

        //event of Sign button
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Récupérer le id employee et le nom
                String id = textFieldEmployee.getText();
                int idEmployee = Integer.valueOf(id);

                //creation of the employee in the database
                AdminFunctions newEmployee = new AdminFunctions();

                try {
                    newEmployee.deleteEmployee(idEmployee);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                System.out.println("ID deleted : " + idEmployee);
                JOptionPane.showMessageDialog(window, "Employee deleted successfully!");

            }
        });

        JButton goBack = new JButton("Previous");
        goBack.setBounds(250, 300, 100, 50);
        window.add(goBack);
        goBack.setBackground(new Color(255, 99, 71));

        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.dispose(); // Ferme la fenêtre actuelle
            }
        });

        // Set JFrame properties
        window . setVisible ( true ) ;
        window . setDefaultCloseOperation( JFrame . EXIT_ON_CLOSE ); ;
        window . setResizable ( false ) ;
    }


    public static void AffichageEmployee(List<Object[]> employeeData) {
        String[] columnNames = {"ID", "Name", "Code"};
        Object[][] data = employeeData.toArray(new Object[0][]);

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        JFrame frame = new JFrame("Employee List");
        frame.setBounds(1100, 200, 400, 400);
        frame.add(scrollPane);
        frame.setVisible(true);
    }


    public static void AffichageTimeRecordEmployee(List<Object[]> employeeDataTime) {
        String[] columnNames = {"ID", "Check_in_time", "Statut"}; //statut to see if the employee is present, late or absent
        Object[][] data = employeeDataTime.toArray(new Object[0][]);

        DefaultTableModel tableTimeModel = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(tableTimeModel);

        JScrollPane scrollPane = new JScrollPane(table);
        JFrame frame = new JFrame("Employee Check in time List");
        frame.setBounds(1100, 600, 600, 400);
        frame.add(scrollPane);
        frame.setVisible(true);
    }

    public static void AffichageAdmin(List<Object[]> adminData) {
        String[] columnNames = {"ID", "Username", "Password"};
        Object[][] data = adminData.toArray(new Object[0][]);

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        JFrame frame = new JFrame("Admin List");
        frame.setBounds(1500, 200, 400, 400);
        frame.add(scrollPane);
        frame.setVisible(true);
    }



    public static void main(String[] args) {


        // Creating the frame
        JFrame window = new JFrame("Attendance System");
        window.setBounds(500,200,600,700);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(null);
        window.setResizable(false);


        //JButton employee
        JButton employee = new JButton("Log in (Employee)");
        employee.setBounds(50,200,500,60);
        window.add(employee);
        employee.setEnabled(true);
        employee.setBackground(new Color(70, 130, 180));
        employee.setFocusPainted(false);

        // Ajout de l'ActionListener
        employee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logInEmployee();
            }
        });


        //JButton admin
        JButton admin = new JButton("Log in (Admin)");
        admin.setBounds(50,300,500,60);
        window.add(admin);
        admin.setEnabled(true);
        admin.setBackground(new Color(186, 85, 211)); // Violet clair
        admin.setFocusPainted(false);

        // Ajout de l'ActionListener
        admin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logInAdmin();
            }
        });

        JButton goBack = new JButton("Previous");
        goBack.setBounds(450, 550, 100, 50);
        window.add(goBack);
        goBack.setBackground(new Color(255, 99, 71));

        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.dispose(); // Ferme la fenêtre actuelle
            }
        });

        window.setVisible(true);
    }
}

/*
Arranger et ranger les boutons dans la page For Admin, ceux pour modif les employee et ceux pour modif les admin
Sur chaque page, ranger et trier les boutons au mieux pour que ce soit propre et compréhensible à la lecture
Ajouter du texte sur certaines pages si nécessaire
*/
