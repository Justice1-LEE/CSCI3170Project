import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;

// work to do:
// 0. several problems need to be sovled in createTables
// 1.finish the implementation related database
// 2.finish the work related to error handling
// 3.test
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
            System.out.println(e);
        }

        // The sketch of the project
        System.out.println("Welcome to Car Renting System!\n");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("-----Main menu-----");
            System.out.println("What kinds of operations would you like to perform?");
            System.out.println("1. Operations for Administrator");
            System.out.println("2. Operations for User");
            System.out.println("3. Operations for Manager");
            System.out.println("4. Exit this program");

            System.out.print("Enter Your Choice: ");
            int choice = scanner.nextInt();

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
                    int choice1 = scanner.nextInt();

                    if (choice1 == 5) {
                        break;
                    } else if (choice1 == 1) {
                        // create table schemas in the database
                        System.out.print("Processing...");
                        createTables(con);
                        System.out.println("Done. Database is initialized.");
                    } else if (choice1 == 2) {
                        System.out.print("Processing...");
                        deleteTables(con);
                        System.out.println("Done. Database is removed");
                    } else if (choice1 == 3) {
                        System.out.print("Type in the Source Data Folder Path: ");
                        scanner.nextLine();
                        String path = scanner.nextLine();
                        System.out.print("Processing...");
                        loadData(con, path);
                        System.out.println("Done. Data is inputted to the database.");
                    } else if (choice1 == 4) {
                        System.out.println("Number of records in each table:");
                        showRecords(con);
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
                    int choice2 = scanner.nextInt();
                    if (choice2 == 3) {
                        break;
                    } else if (choice2 == 1) {
                        System.out.println("Choose the Search criterion:");
                        System.out.println("1. call number");
                        System.out.println("2. name");
                        System.out.println("3. company");
                        System.out.print("Choose the Search criterion: ");
                        int choice21 = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Type in the Search keyword:");
                        String keyword = scanner.nextLine();
                        if (choice21 == 1) {
                            searchCar(1, con, keyword);
                        } else if (choice21 == 2) {
                            searchCar(2, con, keyword);
                        } else if (choice21 == 3) {
                            searchCar(3, con, keyword);
                        }
                    } else if (choice2 == 2) {
                        System.out.print("Enter The cuser ID: ");
                        String userID = scanner.nextLine();
                        System.out.println("Loan Record:");
                        showLoan(con, userID);
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
                    int choice3 = scanner.nextInt();
                    if (choice3 == 4) {
                        break;
                    } else if (choice3 == 1) {
                        System.out.print("Enter The User ID: ");
                        String userID = scanner.nextLine();
                        System.out.print("Enter The Call Number: ");
                        String callNum = scanner.nextLine();
                        System.out.print("Enter The Copy Number: ");
                        int copyNum = scanner.nextInt();
                        if (rentCar(con, userID, callNum, copyNum)) {
                            System.out.println("car renting performed \u001B[32msuccessfully\u001B[0m.");
                        }
                    } else if (choice3 == 2) {
                        System.out.print("Enter The User ID: ");
                        String userID = scanner.nextLine();
                        System.out.print("Enter The Call Number: ");
                        String callNum = scanner.nextLine();
                        System.out.print("Enter The Copy Number: ");
                        int copyNum = scanner.nextInt();
                        if (returnCar(con, userID, callNum, copyNum)) {
                            System.out.println("car returning performed \u001B[32msuccessfully\u001B[0m.");
                        }
                    } else if (choice3 == 3) {
                        System.out.print("Type in the \u001B[36mstarting\u001B[0m date [dd/mm/yyyy]: ");
                        String startDate = scanner.nextLine();
                        System.out.print("Type in the ending date [dd/mm/yyyy]: ");
                        String endDate = scanner.nextLine();
                        System.out.println("List of UnReturned Cars:");
                        listUnreturned(con, startDate, endDate);
                        System.out.println("End of Query");
                    }
                }
            }
        }
        scanner.close();
    }

    // create table schemas in the database
    public static void createTables(Connection con) {
        Statement stmt = null;
        try {
            // haven't add foreign key constrains
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
                    // "check (length(uid) = 12 and age > 10 and age < 100 and ucid > 0 and ucid <
                    // 10)" +
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
                    // "check (length(callnum) = 8 and time_rent >= 0 and time_rent < 100 and ccid >
                    // 0 and ccid < 10)" +
                    "check (time_rent >= 0 and time_rent < 100 and ccid > 0 and ccid < 10)" +
                    ")";
            stmt.executeUpdate(sql);

            sql = "create table copy (" +
                    "callnum varchar(8) not null," +
                    "copynum integer not null," +
                    "primary key (callnum, copynum)," +
                    // "check (length(callnum) = 8 and copynum > 0 and copynum < 10)" +
                    "check (copynum > 0 and copynum < 10)" +
                    ")";
            stmt.executeUpdate(sql);

            // 'return' is a reserved word
            sql = "create table rent (" +
                    "uid varchar(12) not null," +
                    "callnum varchar(8) not null," +
                    "copynum integer not null," +
                    "checkout date not null," +
                    "return_date date," +
                    "primary key (uid, callnum, copynum, checkout)," +
                    // "check (length(uid) = 12 and length(callnum) = 8 and copynum > 0 and copynum
                    // < 10)" +
                    "check (copynum > 0 and copynum < 10)" +
                    ")";
            stmt.executeUpdate(sql);

            sql = "create table produce (" +
                    "cname varchar(25) not null," +
                    "callnum varchar(8) not null," +
                    "primary key (cname, callnum)" +
                    // "check(length(callnum) = 8)" +
                    ")";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    // delete all table in the database
    public static void deleteTables(Connection con) {
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.executeUpdate("drop table if exists user_category, user, car_category, car, copy, rent, produce");

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    // load data from a dataset to the database
    public static void loadData(Connection con, String path) {
        Scanner file = null;
        Statement stmt = null;
        // load from user_category.txt
        try {
            file = new Scanner(new File(path + "/user_category.txt"));
        } catch (IOException e) {
            System.out.println(e);
        }
        while (file.hasNextLine()) {
            String line = file.nextLine();
            // System.out.println(line);
            String[] attributes = line.split("\t");
            String temp = attributes[0] + ", " + attributes[1] + ", " + attributes[2];
            try {
                stmt = con.createStatement();
                stmt.executeUpdate("insert into user_category values (" + temp + ")");
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
        // load from user.txt
        try {
            file = new Scanner(new File(path + "/user.txt"));
        } catch (IOException e) {
            System.out.println(e);
        }
        while (file.hasNextLine()) {
            String line = file.nextLine();
            // System.out.println(line);
            String[] attributes = line.split("\t");
            String temp = "'" + attributes[0] + "', '" + attributes[1] + "', " + attributes[2] + ", '" + attributes[3]
                    + "', "
                    + attributes[4];
            // System.out.println(temp);
            try {
                stmt = con.createStatement();
                stmt.executeUpdate("insert into user values (" + temp + ")");
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
        // load from car_category.txt
        try {
            file = new Scanner(new File(path + "/car_category.txt"));
        } catch (IOException e) {
            System.out.println(e);
        }
        while (file.hasNextLine()) {
            String line = file.nextLine();
            // System.out.println(line);
            String[] attributes = line.split("\t");
            String temp = attributes[0] + ", '" + attributes[1] + "'";
            try {
                stmt = con.createStatement();
                stmt.executeUpdate("insert into car_category values (" + temp + ")");
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
        // load from rent.txt
        try {
            file = new Scanner(new File(path + "/rent.txt"));
        } catch (IOException e) {
            System.out.println(e);
        }
        while (file.hasNextLine()) {
            String line = file.nextLine();
            // System.out.println(line);
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
            try {
                stmt = con.createStatement();
                stmt.executeUpdate("insert into rent values (" + temp + ")");
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
        // load from car.txt
        try {
            file = new Scanner(new File(path + "/car.txt"));
        } catch (IOException e) {
            System.out.println(e);
        }
        while (file.hasNextLine()) {
            String line = file.nextLine();
            // System.out.println(line);
            String[] attributes = line.split("\t");
            String carTemp = "'" + attributes[0] + "', '" + attributes[2] + "', '" + attributes[4] + "', "
                    + attributes[5] + ", "
                    + attributes[6];
            String copyTemp = "'" + attributes[0] + "', " + attributes[1];
            String produceTemp = "'" + attributes[2] + "', '" + attributes[3] + "'";
            try {
                stmt = con.createStatement();
                stmt.executeUpdate("insert into car values (" + carTemp + ")");
                stmt.executeUpdate("insert into copy values (" + copyTemp + ")");
                stmt.executeUpdate("insert into produce values (" + produceTemp + ")");
            } catch (SQLException e) {
                System.out.println(e);
            }
        }

    }

    // show the number of records in each table
    public static void showRecords(Connection con) {
        String[] tableList = { "user_category", "user", "car_category", "car", "rent", "copy", "produce" };
        try {
            Statement stmt = con.createStatement();
            for (int i = 0; i < tableList.length; i++) {
                String query = "SELECT COUNT(*) FROM " + tableList[i];
                ResultSet rs = stmt.executeQuery(query);
                rs.next();
                System.out.println(tableList[i] + ": " + rs.getString(1));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    // search car according to different search criterion
    public static void searchCar(int choice, Connection con, String keyword) {
        System.out.println("|Call Num|Name|Car Category|Company|Available No. of Copy|");
        try {
            Statement stmt = con.createStatement();
            // String query = "SELECT a.callnum, a.name, a.ccname, a.cname, b.total_copies -
            // c.rented_copies AS available_copies "
            // +
            // "FROM " +
            // "(SELECT car.callnum, car.name, car_category.ccname, produce.cname " +
            // "FROM car, produce, car_category " +
            // "WHERE car.callnum = produce.callnum AND car.ccid = car_category.ccid) a " +
            // "JOIN " +
            // "(SELECT callnum, COUNT(copynum) AS total_copies " +
            // "FROM copy " +
            // "GROUP BY callnum) b " +
            // "ON a.callnum = b.callnum " +
            // "JOIN " +
            // "(SELECT callnum, COUNT(*) AS rented_copies " +
            // "FROM rent " +
            // "WHERE return_date = NULL " +
            // "GROUP BY callnum) c " +
            // "ON a.callnum = c.callnum " +
            // "ORDER BY a.callnum ASC";
            String query = "select * from car";
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                System.out.println(result.toString());
                // String callnum = result.getString("callnum");
                // String name = result.getString("name");
                // String carCategory = result.getString("ccname");
                // String company = result.getString("cname");
                // int copynum = result.getInt("copynum");
                // if (choice == 1) {
                // if (callnum.equals(keyword)) {
                // System.out.println(
                // "|" + callnum + "|" + name + "|" + carCategory + "|" + company + "|" +
                // copynum + "|");
                // }
                // } else if (choice == 2) {
                // if (name.contains(keyword)) {
                // System.out.println("|" + callnum + "|" + name + "|" + carCategory + "|" +
                // company + "|"
                // + copynum + "|");
                // }
                // } else if (choice == 3) {
                // if (company.contains(keyword)) {
                // System.out.println("|" + callnum + "|" + name + "|" + carCategory + "|" +
                // company + "|"
                // + copynum + "|");
                // }
                // }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    // show loan record of a user
    public static void showLoan(Connection con, String userID) {
        System.out.println("|CallNum|CopyNum|Name|Company|Check-out|Returned?|");
        try {
            Statement stmt = con.createStatement();
            String query = "SELECT C.callnum, P.copynum, C.name, D.cname, R.checkout, R.return_date " +
                    "FROM car C, copy P, produce D, rent R " +
                    "WHERE C.callnum = P.callnum AND C.callnum = D.callnum AND P.copynum = R.copynum AND R.uid = "
                    + userID +
                    " ORDER by R.checkout DESC";
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
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    // rent a car copy
    public static boolean rentCar(Connection con, String userID, String callNum, int copyNum) {
        try {
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM rent WHERE return_date = NULL AND callnum = " + callNum + " AND copynum = "
                    + copyNum;
            ResultSet result = stmt.executeQuery(query);
            if (result.next()) {
                return false;
            } else {
                java.util.Date checkout = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String qry = "insert into rent values (" + userID + ", " + callNum + ", " + copyNum + ", "
                        + formatter.format(checkout) + ", NULL)";
                stmt.executeUpdate(qry);
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    // return a car
    public static boolean returnCar(Connection con, String userID, String callNum, int copyNum) {
        try {
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM rent WHERE return_date = NULL AND callnum = " + callNum + " AND copynum = "
                    + copyNum + " AND uid = " + userID;
            ResultSet result = stmt.executeQuery(query);
            if (!result.next()) {
                return false;
            } else {
                java.util.Date return_date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String qry = "UPDATE rent SET return_date = " + formatter.format(return_date)
                        + " WHERE return_date = NULL AND callnum = " + callNum + " AND copynum = " + copyNum
                        + " AND uid = " + userID;
                stmt.executeUpdate(qry);
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    // list all un-returned car copies which are checked-out within a period
    public static void listUnreturned(Connection con, String startDate, String endDate) {
        System.out.println("|UID|CallNum|CopyNum|Checkout|");
        try {
            Statement stmt = con.createStatement();
            String query = "SELECT uid, callnum, copynum, checkout " +
                    "FROM rent " +
                    "WHERE return_date = NULL AND checkout >= " + startDate + " AND checkout <= " + endDate;
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                String uid = result.getString("uid");
                String callNum = result.getString("callnum");
                int copyNum = result.getInt("copynum");
                java.sql.Date checkout = result.getDate("checkout");
                System.out.println("|" + uid + "|" + callNum + "|" + copyNum + "|" + checkout.toString() + "|");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
