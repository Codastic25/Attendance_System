GUI:


window 1 : Attendance System
2 buttons :
•
•
log in (employee) → if the employee already exists in the database, he clicks on this button
to log in
log in (admin) → if the admin already exists in the database, he clicks on this button to log
in or if me (Aurian) decides to log in, I can click on this button to log in
Previous button : close the actual window


window 2 : Log in Page Admin
Username and Password to put
•
•
A Sign button : confirm the information and allow the admin to the next window or not
Previous button : close the actual window


window 3 : Log in Page Employee
Name and Code to put
•
•
A Log in button : confirm the information and allow the employee to get recognized or not,
this button will launch the facial recognition of the employee
Previous button : close the actual window


window 4 : For Admin
Some text, this page is dedicated to the functions that an admin can realize
•
•
•
•
•
•
Button Add Employee : will add an employee in my database
Button Delete Employee : will delete an employee in my database
Button Get Employee : will get all employee in my database
Button Edit Employee : will edit an employee in my database
Button Reset Employee : will reset all employee in my database, pictures of them and their
information
Button Check time records Employee : will display the time when all employee came at
work
•
•
• Button Add Admin : will add an admin in my database
Button Delete Admin : will delete an admin in my database
Button Get Admin : will get all admin in my database
• Previous button : close the actual window

window 5 : Page Employee (linked to the Button Add Employee)
Name and Code to put
•
•
•
Button Capture : take pictures of the employee that is creating
Button Create : create the employee and add him in the database
Previous button : close the actual window


window 6 : Page Employee (linked to the Button Delete Employee)
Id of the employee to put
•
•
Button Delete : will delete the employee in my database thanks to his Id
Previous button : close the actual window


window 7 : Employee List (linked to the Button Get Employee)
Just a summarized table of the employee in my database (3 columns : Id, Name, Code)


window 8 : Page Edit Employee (linked to the Button Edit Employee)
Id of the employee and new name to put
•
•
Button Edit : will edit the employee in my database thanks to his Id, will change his or her
name
Previous button : close the actual window


window 9 : Employee check in time list(linked to the Button Check time records Employee)
Just a summarized table of the employee checked in time in my database (3 columns : Id,
Check_in_time, Statut) statut to see if the employee is present or late


window 10 : Page Admin (linked to the Button Add Admin)
Name and Code and Id to put
•
•
Button Create : create the admin and add him in the database
Previous button : close the actual window


window 11 : Page Admin (linked to the Button Delete Admin)
Id of the employee to put
•
•
Button Delete : will delete the admin in my database thanks to his Id
Previous button : close the actual window


window 12 : Admin List (linked to the Button Get Admin)
Just a summarized table of the admin in my database (3 columns : Id, Username, Password)•
Previous button : close the actual window
CODE:
(3 scripts Python + 3 scripts Java)
PACKAGE DatabaseAndTables
•
•
AdminFunctions.java
Database.java
PACKAGE GUI
•
interfaceGraphiqueUser.java
EXTERNAL SCRIPTS
•
•
•
emptyFileNames.py
faceRecognition.py
takePictures.py

AdminFunctions :
Several functions for the admins functions

Database :
All the code to manipulate some tables of mySQL usefull for the project, everything working in the
db_time_attendance database

interfaceGraphiqueUser :
Several functions for the GUI and the events and actions, this script java will make the link between
every parts of the project.

EmptyFileNames :
This script will erase or delete the name put in the JSON file and also all the folder Dataset
containing the pictures of everyone

faceRecognition :
This script will launch the facial recognition using the pictures in Dataset to compare

takePictures :
This script will take pictures of employee when he is running, you put a name and it takes pictures
of you and after that, creates a folder of your name containing 30 pictures of you and also put your
name automatically in the JSON file


COMPONENTS:

SCRIPTS Python, Java, Json
•
•
•
AdminFunctions.java
Main.java
Database.java
interfaceGraphiqueUser.java
emptyFileNames.py
faceRecognition.py
takePictures.py
names.json


FOR PICTURES
•
Folder Dataset (stock other folder of pictures of people)


ERRORS:
For the delete button : It delete the employee in my database and table but not in the JSON file and
not deleting the pictures in foler Dataset


For the edit button : It changes the name of the employee but this employee is not recognized when
he wants to log in
