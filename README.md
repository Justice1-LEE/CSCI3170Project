## CSCI3170 Group Project

Group Number:   15

Group Members:  Ji Yi,      1155141508
                Li Hangji,  1155141449
                Yang Boyu,  1155178392

List of files:  CSCI3170proj.java    -   The main program to communicate with MySQL server
              

Methods of compilation and execution:

Before compilation:
    - Modify CSCI3170proj.java to work on other MySQL server environment
    - In Line 17-19,
	String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db15";
	String dbUsername = "Group15";
	String dbPassword = "CSCI3170";
      Change the above parameters to other environment

For compilation:
    - The jdbc.jar file should be placed in the same directory

For execution:
    - java -cp .:jdbc.jar CSCI3170proj

### Operations ###

1. Operations for administrator menu
	- [x] 1.1 Create all tables
	- [x] 1.2 Delete all tables
	- [x] 1.3 Load from datafile
	- [x] 1.4 Show number of records in each table
	- [x] 1.5 Return to the main menu

2. Operations for user menu
	- [x] 2.1 Search for Cars
	- [x] 2.2 Show loan records of a user
	- [x] 2.3 Return to the main menu

3. Operations for manager menu
	- [x] 3.1 Car renting
	- [x] 3.2 Car returning
	- [x] 3.3 List all un-returned car copies which are checked out within a period
	- [x] 3.4 Return to the main menu



