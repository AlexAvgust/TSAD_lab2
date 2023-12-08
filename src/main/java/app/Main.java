package app;

import java.sql.*;
import java.util.Scanner;

public class Main {
    private static void showUserInputVariants(){
        System.out.println("What would you like to do? \n");
        System.out.println("1. Add row adding connection for each request");
        System.out.println("2. Add row using one connection");
        System.out.println("3: Add row using one connection with batch ");
        System.out.println("4. Add row using one connection without autocommit");
        System.out.println("5. Add table to DB");
        System.out.println("0. Exit \n");
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DatabaseManipulation dbManipulation = new DatabaseManipulation();
        TimeForOperationsDecoratorInterface timedOperations = null;
        boolean continueChoice = true;
        System.out.println("Connecting to the database...");
        Main.showUserInputVariants();
        while(continueChoice) {
            int userChoice = scanner.nextInt();

            switch (userChoice) {
                case 1:
                    timedOperations = new TimeDecorator(() -> {
                        try {
                            dbManipulation.createConnectionForEachReq();
                        } catch (ClassNotFoundException | SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    timedOperations.performOperation();
                    break;
                case 2:
                    timedOperations = new TimeDecorator(() -> {
                        try {
                            dbManipulation.usingOneConnection();
                        } catch (ClassNotFoundException | SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    timedOperations.performOperation();
                    break;
                case 3:
                    timedOperations = new TimeDecorator(() -> {
                        try {
                            dbManipulation.usingOneConnectionUsingBatch(100);
                        } catch (ClassNotFoundException | SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    timedOperations.performOperation();
                    break;
                case 4:
                    timedOperations = new TimeDecorator(() -> {
                        try {
                            dbManipulation.usingOneConnectionWithoutAutoCommit();
                        } catch (ClassNotFoundException | SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    timedOperations.performOperation();
                    break;
                case 5:
                    dbManipulation.createTable();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    continueChoice = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
            Main.showUserInputVariants();
        }

    }



}