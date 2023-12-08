package app;

import java.sql.*;

public class DatabaseManipulation {
    private static final AppConfig config = new AppConfig();
    private static final DummyData dataGen = new DummyData();
    private final String jdbcDriver = "com.mysql.cj.jdbc.Driver";
    private final String url = config.getDb_url();
    private final String user = config.getUsername();
    private final String password = config.getPassword();
    private final int numbersOfRows = 250;
    public void createTable (){
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS employees ( " +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(100), " +
                    "position TEXT, " +
                    "salary INT)";

            statement.executeUpdate(sql);
            System.out.println("Table created successfully");
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());

        }
    }
    public void createConnectionForEachReq () throws ClassNotFoundException, SQLException {
        Class.forName(jdbcDriver);
        for(int i = 0; i <= numbersOfRows; i++){
            Connection connection = DriverManager.getConnection(url, user, password);
            this.addRow(connection);
        }
    };

    public void usingOneConnection () throws ClassNotFoundException, SQLException {
        Class.forName(jdbcDriver);
        Connection connection = DriverManager.getConnection(url, user, password);
        for(int i = 0; i <= numbersOfRows; i++){
            this.addRow(connection);
        }
    };

    public void usingOneConnectionUsingBatch(int batchSize) throws ClassNotFoundException, SQLException {
        Class.forName(jdbcDriver);
        Connection connection = DriverManager.getConnection(url, user, password);
        final String sql = "INSERT INTO employees (name, position, salary) VALUES (?, ?, ?)";
        connection.setAutoCommit(false);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i <= numbersOfRows; i++) {
                String dataGenName = DatabaseManipulation.dataGen.getAlphaNumericString(5);
                String dataGenPosition = DatabaseManipulation.dataGen.getAlphaNumericString(10);
                Integer dataGenInt = DatabaseManipulation.dataGen.getRandomInt();

                preparedStatement.setString(1, dataGenName);
                preparedStatement.setString(2, dataGenPosition);
                preparedStatement.setInt(3, dataGenInt);

                preparedStatement.addBatch();

                // Execute batch after reaching the specified batch size
                if ((i + 1) % batchSize == 0 || (i + 1) == numbersOfRows) {
                    preparedStatement.executeBatch();
                    preparedStatement.clearBatch();
                }
            }

            connection.commit();
            System.out.println("Batch operations executed successfully.");
        } catch (SQLException e) {
            connection.rollback();
            System.out.println("Batch operations failed: " + e.getMessage());
        } finally {
            // Rest of the code remains the same...
        }
    };


    public void usingOneConnectionWithoutAutoCommit() throws ClassNotFoundException, SQLException  {
        Class.forName(jdbcDriver);
        Connection connection = DriverManager.getConnection(url,user,password);
        connection.setAutoCommit(false);
        try {
            for (int i = 0; i <=  numbersOfRows; i++){
                this.addRow(connection);
            }

            connection.commit();
            System.out.println("Operations executed successfully without auto-commit.");
        } catch (SQLException e) {
            connection.rollback();
            System.out.println("Operations failed without auto-commit: " + e.getMessage());
        }finally {
            connection.setAutoCommit(true);
            connection.close();
        }
    };

    private void addRow(Connection connection) {
        final String sql = "INSERT INTO employees (name, position, salary) VALUES (?, ?, ?)";
        String dataGenName = DatabaseManipulation.dataGen.getAlphaNumericString(5);
        String dataGenPosition = DatabaseManipulation.dataGen.getAlphaNumericString(10);
        Integer dataGenInt = DatabaseManipulation.dataGen.getRandomInt();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, dataGenName);
            preparedStatement.setString(2, dataGenPosition);
            preparedStatement.setInt(3, dataGenInt.intValue());

            int rowsAffected = preparedStatement.executeUpdate();
            if (0 < rowsAffected) {
                System.out.println("Row added successfully.");
            } else {
                System.out.println("Failed to add row.");
            }
        }catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
    }
}
