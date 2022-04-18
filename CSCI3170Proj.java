import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;

// work to do:
// 3.more test
class CSCI3170Proj {
    public static void main(String[] args) {
        // This part is for getting connection from the database
        String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db15";
        String dbUsername = "Group15";
        String dbPassword = "CSCI3170";

        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
        } catch (ClassNotFoundException e) {
            System.out.println("[Error]: Java MySQL DB Driver not found!!");
            System.exit(0);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        // The sketch of the project
        System.out.println("Welcome to Car Renting System!");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n-----Main menu-----");
            System.out.println("What kinds of operations would you like to perform?");
            System.out.println("1. Operations for Administrator");
            System.out.println("2. Operations for User");
            System.out.println("3. Operations for Manager");
            System.out.println("4. Exit this program");
            System.out.print("Enter Your Choice: ");
            int choice = -1;
            try {
                choice = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("INVALID INPUT.");
                continue;
            }

            // Exit this program
            if (choice == 4) {
                break;
            }
            // Operations for Administrator
            else if (choice == 1) {
                while (true) {
                    System.out.println();
                    System.out.println("-----Operations for administrator menu-----");
                    System.out.println("What kind of operation would you like to perform?");
                    System.out.println("1. Create all tables");
                    System.out.println("2. Delete all tables");
                    System.out.println("3. Load from datafile");
                    System.out.println("4. Show number of records in each table");
                    System.out.println("5. Return to the main menu");
                    System.out.print("Enter Your Choice: ");
                    int choice1 = -1;
                    try {
                        choice1 = scanner.nextInt();
                    } catch (Exception e) {
                        System.out.println("INVALID INPUT.");
                        continue;
                    }

                    if (choice1 == 5) {
                        break;
                    } else if (choice1 == 1) {
                        // create table schemas in the database
                        System.out.print("Processing...");
                        try {
                            createTables(con);
                        } catch (Exception e) {
                            System.out.println("fail to create tables.");
                            continue;
                        }
                        System.out.println("Done. Database is initialized.");
                    } else if (choice1 == 2) {
                        System.out.print("Processing...");
                        try {
                            deleteTables(con);
                        } catch (Exception e) {
                            System.out.println("fail to delete tables");
                            continue;
                        }
                        System.out.println("Done. Database is removed");
                    } else if (choice1 == 3) {
                        System.out.print("Type in the Source Data Folder Path: ");
                        scanner.nextLine();
                        String path = scanner.nextLine();
                        System.out.print("Processing...");
                        try {
                            loadData(con, path);
                        } catch (Exception e) {
                            System.out.println("Error: fail to load data.");
                            continue;
                        }
                        System.out.println("Done. Data is inputted to the database.");
                    } else if (choice1 == 4) {
                        System.out.println("Number of records in each table:");
                        try {
                            showRecords(con);
                        } catch (Exception e) {
                            System.out.println("fail to show records.");
                        }
                    } else {
                        System.out.println("INVALID INPUT.");
                    }
                }
            }
            // Operations for User
            else if (choice == 2) {
                while (true) {
                    System.out.println();
                    System.out.println("-----Operations for usermenu-----");
                    System.out.println("What kind of operation would you like to perform?");
                    System.out.println("1. Search for Cars");
                    System.out.println("2. Show loan record of a user");
                    System.out.println("3. Return to the main menu");
                    System.out.print("Enter Your Choice: ");
                    int choice2 = -1;
                    try {
                        choice2 = scanner.nextInt();
                    } catch (Exception e) {
                        System.out.println("INVALID INPUT.");
                        continue;
                    }
                    if (choice2 == 3) {
                        break;
                    } else if (choice2 == 1) {
                        while (true) {
                            System.out.println("Choose the Search criterion:");
                            System.out.println("1. call number");
                            System.out.println("2. name");
                            System.out.println("3. company");
                            System.out.print("Choose the Search criterion: ");
                            int choice21 = -1;
                            try {
                                choice21 = scanner.nextInt();
                                scanner.nextLine();
                            } catch (Exception e) {
                                System.out.println("INVALID INPUT.");
                                continue;
                            }
                            System.out.print("Type in the Search keyword:");
                            String keyword = scanner.nextLine();
                            if (choice21 == 1) {
                                try {
                                    searchCar(1, con, keyword);
                                } catch (Exception e) {
                                    System.out.println("fail to search cars.");
                                    continue;
                                }
                                break;
                            } else if (choice21 == 2) {
                                try {
                                    searchCar(2, con, keyword);
                                } catch (Exception e) {
                                    System.out.println("fail to search cars.");
                                    continue;
                                }
                                break;
                            } else if (choice21 == 3) {
                                try {
                                    searchCar(3, con, keyword);
                                } catch (Exception e) {
                                    System.out.println("fail to search cars.");
                                    continue;
                                }
                                break;
                            } else {
                                System.out.println("INVALID INPUT.");
                            }
                        }
                    } else if (choice2 == 2) {
                        System.out.print("Enter The cuser ID: ");
                        scanner.nextLine();
                        String userID = scanner.nextLine();
                        System.out.println("Loan Record:");
                        try {
                            showLoan(con, userID);
                        } catch (Exception e) {
                            System.out.println("fail to show loan record.");
                            continue;
                        }
                    }
                    System.out.println("End of Query");
                }
            }
            // Operations for manager
            else if (choice == 3) {
                while (true) {
                    System.out.println();
                    System.out.println("-----Operations for manager menu-----");
                    System.out.println("What kind of operation would you like to perform?");
                    System.out.println("1. Car Renting");
                    System.out.println("2. Car Returning");
                    System.out.println("3. List all un-returned car copies which are checked-out within a period");
                    System.out.println("4. Return to the main menu");
                    System.out.print("Enter Your Choice: ");
                    int choice3 = -1;
                    try {
                        choice3 = scanner.nextInt();
                    } catch (Exception e) {
                        System.out.println("INVALID INPUT.");
                        continue;
                    }
                    if (choice3 == 4) {
                        break;
                    } else if (choice3 == 1) {
                        System.out.print("Enter The User ID: ");
                        scanner.nextLine();
                        String userID = scanner.nextLine();
                        System.out.print("Enter The Call Number: ");
                        String callNum = scanner.nextLine();
                        int copyNum = -1;
                        while (true) {
                            System.out.print("Enter The Copy Number: ");
                            try {
                                copyNum = scanner.nextInt();
                                break;
                            } catch (Exception e) {
                                System.out.println("INVALID INPUT.");
                            }
                        }
                        if (rentCar(con, userID, callNum, copyNum)) {
                            System.out.println("car renting performed \u001B[32msuccessfully\u001B[0m.");
                        }
                    } else if (choice3 == 2) {
                        System.out.print("Enter The User ID: ");
                        scanner.nextLine();
                        String userID = scanner.nextLine();
                        System.out.print("Enter The Call Number: ");
                        String callNum = scanner.nextLine();
                        int copyNum = -1;
                        while (true) {
                            System.out.print("Enter The Copy Number: ");
                            try {
                                copyNum = scanner.nextInt();
                                break;
                            } catch (Exception e) {
                                System.out.println("INVALID INPUT.");
                            }
                        }
                        if (returnCar(con, userID, callNum, copyNum)) {
                            System.out.println("car returning performed \u001B[32msuccessfully\u001B[0m.");
                        }
                    } else if (choice3 == 3) {
                        System.out.print("Type in the \u001B[36mstarting\u001B[0m date [dd/mm/yyyy]: ");
                        scanner.nextLine();
                        String startDate = scanner.nextLine();
                        System.out.print("Type in the ending date [dd/mm/yyyy]: ");
                        String endDate = scanner.nextLine();
                        System.out.println("List of UnReturned Cars:");
                        try {
                            listUnreturned(con, startDate, endDate);
                        } catch (Exception e) {
                            System.out.println("fail to list unreturned cars");
                            continue;
                        }

                        System.out.println("End of Query");
                    }
                }
            } else {
                System.out.println("INVALID INPUT.");
            }
        }
        scanner.close();
    }

    // create table schemas in the database
    public static void createTables(Connection con) throws Exception {
        Statement stmt = null;

        stmt = con.createStatement();

        String sql = "create table user_category (" +
                "ucid integer not null," +
                "max integer not null," +
                "period integer not null," +
                "primary key (ucid)," +
                "check (ucid > 0 and ucid < 10 and max > 0 and max < 10 and period > 0 and period < 100)" +
                ")";
        stmt.executeUpdate(sql);

        sql = "create table user (" +
                "uid varchar(12) not null," +
                "name varchar(25) not null," +
                "age integer not null," +
                "occupation varchar(20) not null," +
                "ucid integer not null," +
                "primary key (uid)," +
                "foreign key (ucid) references user_category(ucid)," +
                "check (age > 10 and age < 100 and ucid > 0 and ucid < 10)" +
                ")";
        stmt.executeUpdate(sql);

        sql = "create table car_category (" +
                "ccid integer not null," +
                "ccname varchar(20) not null," +
                "primary key (ccid)," +
                "check (ccid > 0 and ccid < 10 )" +
                ")";
        stmt.executeUpdate(sql);

        sql = "create table car (" +
                "callnum varchar(8) not null," +
                "name varchar(10) not null," +
                "manufacture date not null," +
                "time_rent integer(2) not null," +
                "ccid integer not null," +
                "primary key (callnum)," +
                "foreign key (ccid) references car_category(ccid)," +
                "check (time_rent >= 0 and time_rent < 100 and ccid > 0 and ccid < 10)" +
                ")";
        stmt.executeUpdate(sql);

        sql = "create table copy (" +
                "callnum varchar(8) not null," +
                "copynum integer not null," +
                "primary key (callnum, copynum)," +
                "foreign key (callnum) references car(callnum)," +
                "check (copynum > 0 and copynum < 10)" +
                ")";
        stmt.executeUpdate(sql);

        sql = "create table produce (" +
                "cname varchar(25) not null," +
                "callnum varchar(8) not null," +
                "primary key (cname, callnum)," +
                "foreign key (callnum) references car(callnum)" +
                ")";
        stmt.executeUpdate(sql);

        sql = "create table rent (" +
                "uid varchar(12) not null," +
                "callnum varchar(8) not null," +
                "copynum integer not null," +
                "checkout date not null," +
                "return_date date," +
                "primary key (uid, callnum, copynum, checkout)," +
                "foreign key (uid) references user(uid)," +
                "foreign key (callnum, copynum) references copy(callnum, copynum)," +
                "check (copynum > 0 and copynum < 10)" +
                ")";
        stmt.executeUpdate(sql);

    }

    // delete all table in the database
    public static void deleteTables(Connection con) throws Exception {
        Statement stmt = null;
        stmt = con.createStatement();
        stmt.executeUpdate("drop table if exists rent, produce, copy, car, car_category, user, user_category");

    }

    // load data from a dataset to the database
    public static void loadData(Connection con, String path) throws Exception {
        Scanner file = null;
        Statement stmt = null;
        // load from user_category.txt
        file = new Scanner(new File(path + "/user_category.txt"));
        while (file.hasNextLine()) {
            String line = file.nextLine();
            String[] attributes = line.split("\t");
            String temp = attributes[0] + ", " + attributes[1] + ", " + attributes[2];
            stmt = con.createStatement();
            stmt.executeUpdate("insert into user_category values (" + temp + ")");

        }

        // load from user.txt into user
        file = new Scanner(new File(path + "/user.txt"));
        while (file.hasNextLine()) {
            String line = file.nextLine();
            String[] attributes = line.split("\t");
            String temp = "'" + attributes[0] + "', '" + attributes[1] + "', " + attributes[2] + ", '" + attributes[3]
                    + "', "
                    + attributes[4];
            stmt = con.createStatement();
            stmt.executeUpdate("insert into user values (" + temp + ")");

        }

        // load from car_category.txt into car_category
        file = new Scanner(new File(path + "/car_category.txt"));
        while (file.hasNextLine()) {
            String line = file.nextLine();
            String[] attributes = line.split("\t");
            String temp = attributes[0] + ", '" + attributes[1] + "'";
            stmt = con.createStatement();
            stmt.executeUpdate("insert into car_category values (" + temp + ")");

        }

        // load from car.txt into car, copy & produce
        file = new Scanner(new File(path + "/car.txt"));
        while (file.hasNextLine()) {
            String line = file.nextLine();
            String[] attributes = line.split("\t");
            String carTemp = "'" + attributes[0] + "', '" + attributes[2] + "', '" + attributes[4] + "', "
                    + attributes[5] + ", " + attributes[6];
            String copyTemp = "";
            String produceTemp = "'" + attributes[3] + "', '" + attributes[0] + "'";

            stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO car VALUES (" + carTemp + ")");

            for (int i = 1; i <= Integer.parseInt(attributes[1]); i++) {
                copyTemp = "'" + attributes[0] + "', " + i;
                stmt.executeUpdate("INSERT INTO copy VALUES (" + copyTemp + ")");
            }

            stmt.executeUpdate("INSERT INTO produce VALUES (" + produceTemp + ")");

        }

        // load from rent.txt into rent
        file = new Scanner(new File(path + "/rent.txt"));
        while (file.hasNextLine()) {
            String line = file.nextLine();
            String[] attributes = line.split("\t");
            String temp = null;
            if (attributes[4].equals("NULL")) {
                temp = "'" + attributes[2] + "', '" + attributes[0] + "', " + attributes[1] + ", '" + attributes[3]
                        + "', "
                        + attributes[4];
            } else {
                temp = "'" + attributes[2] + "', '" + attributes[0] + "', " + attributes[1] + ", '" + attributes[3]
                        + "', '"
                        + attributes[4] + "'";
            }

            stmt = con.createStatement();
            stmt.executeUpdate("insert into rent values (" + temp + ")");

        }
    }

    // show the number of records in each table
    public static void showRecords(Connection con) throws Exception {
        String[] tableList = { "user_category", "user", "car_category", "car", "rent", "copy", "produce" };
        Statement stmt = con.createStatement();
        for (int i = 0; i < tableList.length; i++) {
            String query = "SELECT COUNT(*) FROM " + tableList[i];
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            System.out.println(tableList[i] + ": " + rs.getString(1));
        }

    }

    // search car according to different search criterion
    public static void searchCar(int choice, Connection con, String keyword) throws Exception {
        System.out.println("|Call Num|Name|Car Category|Company|Available No. of Copy|");
        Statement stmt = con.createStatement();
        String query = "SELECT a.callnum, a.name, a.ccname, a.cname, if(c.rented_copies is null, b.total_copies, b.total_copies - c.rented_copies) AS copynum "
                +
                "FROM " +
                "(SELECT car.callnum, car.name, car_category.ccname, produce.cname " +
                "FROM car, produce, car_category " +
                "WHERE car.callnum = produce.callnum AND car.ccid = car_category.ccid) a " +
                "INNER JOIN " +
                "(SELECT callnum, COUNT(copynum) AS total_copies " +
                "FROM copy " +
                "GROUP BY callnum) b " +
                "ON a.callnum = b.callnum " +
                "LEFT JOIN " +
                "(SELECT callnum, COUNT(copynum) AS rented_copies " +
                "FROM rent " +
                "WHERE return_date is NULL " +
                "GROUP BY callnum) c " +
                "ON a.callnum = c.callnum " +
                "ORDER BY a.callnum ASC";

        ResultSet result = stmt.executeQuery(query);
        while (result.next()) {
            String callnum = result.getString("callnum");
            String name = result.getString("name");
            String carCategory = result.getString("ccname");
            String company = result.getString("cname");
            int copynum = result.getInt("copynum");
            if (choice == 1) {
                if (callnum.toLowerCase().equals(keyword.toLowerCase())) {
                    System.out.println(
                            "|" + callnum + "|" + name + "|" + carCategory + "|" + company + "|" +
                                    copynum + "|");
                }
            } else if (choice == 2) {
                if (name.toLowerCase().contains(keyword.toLowerCase())) {
                    System.out.println("|" + callnum + "|" + name + "|" + carCategory + "|" +
                            company + "|"
                            + copynum + "|");
                }
            } else if (choice == 3) {
                if (company.toLowerCase().contains(keyword.toLowerCase())) {
                    System.out.println("|" + callnum + "|" + name + "|" + carCategory + "|" +
                            company + "|"
                            + copynum + "|");
                }
            }
        }

    }

    // show loan record of a user
    public static void showLoan(Connection con, String userID) throws Exception {
        System.out.println("|CallNum|CopyNum|Name|Company|Check-out|Returned?|");

        Statement stmt = con.createStatement();
        String query = "SELECT C.callnum, R.copynum, C.name, D.cname, R.checkout, R.return_date " +
                "FROM car C, produce D, rent R " +
                "WHERE C.callnum = R.callnum AND R.callnum = D.callnum AND R.uid = '"
                + userID +
                "' ORDER by R.checkout DESC";
        ResultSet result = stmt.executeQuery(query);
        while (result.next()) {
            String callnum = result.getString("callnum");
            int copynum = result.getInt("copynum");
            String name = result.getString("name");
            String company = result.getString("cname");
            java.sql.Date checkout = result.getDate("checkout");
            java.sql.Date return_date = result.getDate("return_date");
            String returnOrNot = null;
            if (return_date == null) {
                returnOrNot = "No";
            } else {
                returnOrNot = "Yes";
            }
            System.out.println("|" + callnum + "|" + copynum + "|" + name + "|" + company + "|"
                    + checkout.toString() + "|" + returnOrNot + "|");
        }

    }

    // rent a car copy
    public static boolean rentCar(Connection con, String userID, String callNum, int copyNum) {
        try {
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM rent WHERE return_date is NULL AND callnum = '" + callNum + "' AND copynum = "
                    + copyNum;
            ResultSet result = stmt.executeQuery(query);
            if (result.next()) {
                System.out.println("[\u001B[31mError\u001B[0m]: \u001B[31mNo\u001B[0m Matching car copy found.");
                return false;
            } else {
                String qry = "SELECT max FROM user, user_category WHERE user.ucid = user_category.ucid AND user.uid = " + userID;
                ResultSet max = stmt.executeQuery(qry);
                qry = "SELECT COUNT(*) AS car_rented FROM rent WHERE return_date is NULL GROUP BY uid HAVING uid = " + userID;
                ResultSet car_rented = stmt.executeQuery(qry);
                if (car_rented.next() && max.next() && car_rented.getString("car_rented") == max.getString("max")) {
                    System.out.println("[\u001B[31mError\u001B[0m]: \u001B[31mNo\u001B[0m This user has rented the maximum number of cars.");
                    return false;
                }
                java.util.Date checkout = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                qry = "insert into rent values ('" + userID + "', '" + callNum + "', " + copyNum + ", '"
                        + formatter.format(checkout) + "', NULL)";
                stmt.executeUpdate(qry);
                qry = "UPDATE car SET time_rent = time_rent + 1 where callnum = '" + callNum + "'";
                stmt.executeUpdate(qry);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("[\u001B[31mError\u001B[0m]: \u001B[31mNo\u001B[0m Matching car copy found.");
            return false;
        }
    }
    
    // return a car
    public static boolean returnCar(Connection con, String userID, String callNum, int copyNum) {
        try {
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM rent WHERE return_date is NULL AND callnum = '" + callNum + "' AND copynum = "
                    + copyNum + " AND uid = '" + userID + "'";
            ResultSet result = stmt.executeQuery(query);
            if (!result.next()) {
                System.out.println("[\u001B[31mError\u001B[0m]: \u001B[31mNo\u001B[0m Matching car copy found.");
                return false;
            } else {
                java.util.Date return_date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String qry = "UPDATE rent SET return_date = '" + formatter.format(return_date)
                        + "' WHERE return_date is NULL AND callnum = '" + callNum + "' AND copynum = " + copyNum
                        + " AND uid = '" + userID + "'";
                stmt.executeUpdate(qry);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("[\u001B[31mError\u001B[0m]: \u001B[31mNo\u001B[0m Matching car copy found.");
            return false;
        }
    }
    
    // list all un-returned car copies which are checked-out within a period
    public static void listUnreturned(Connection con, String startDate, String endDate) throws Exception {
        System.out.println("|UID|CallNum|CopyNum|Checkout|");

        Statement stmt = con.createStatement();
        String query = "SELECT uid, callnum, copynum, checkout " +
                "FROM rent " +
                "where return_date is NULL AND DATE(checkout) between DATE_FORMAT(STR_TO_DATE('" + startDate +
                "','%d/%m/%Y'), '%Y-%m-%d') and DATE_FORMAT(STR_TO_DATE('" + endDate +
                "','%d/%m/%Y'), '%Y-%m-%d')";
        ResultSet result = stmt.executeQuery(query);
        while (result.next()) {
            String uid = result.getString("uid");
            String callNum = result.getString("callnum");
            int copyNum = result.getInt("copynum");
            java.sql.Date checkout = result.getDate("checkout");
            System.out.println("|" + uid + "|" + callNum + "|" + copyNum + "|" + checkout.toString() + "|");
        }

    }
}
