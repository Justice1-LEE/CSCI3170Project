### Connect to CSE CUHK environment

The connection is only establishable in `linux1.cse.cuhk.edu.hk`

1. Copy whole file to cse machine
```bash
scp -r CSCI3170Proj/ {CSE USERNAME}@gw.cse.cuhk.edu.hk:{DIRECTORY}
```

2. Modify `CSCI3170Proj.java`

```java
String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db15";
String dbUsername = "Group15";
String dbPassword = "CSCI3170";
```

3. `make`

4. Run Java application
```bash
java -cp .:jdbc.jar CSCI3170Proj
```

### Create local MySQL environment

Using Docker and create own MySQL server for testing

1. Install Docker
2. Pull Docker image

```bash
docker pull centurylink/mysql
```

3. Run Docker and create container


### Run Java in local environment

1. Modify `CSCI3170proj.java`

```java
String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db15";
String dbUsername = "Group15";
String dbPassword = "CSCI3170";
```

2. `make`
3. `java -cp .:jdbc.jar CSCI3170proj`


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



