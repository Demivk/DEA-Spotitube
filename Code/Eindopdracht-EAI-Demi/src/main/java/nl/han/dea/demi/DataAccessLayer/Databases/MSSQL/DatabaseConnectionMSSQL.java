package nl.han.dea.demi.DataAccessLayer.Databases.MSSQL;

import nl.han.dea.demi.DataAccessLayer.Databases.IDatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DatabaseConnectionMSSQL implements IDatabaseConnection {

    private static DatabaseConnectionMSSQL instance;
    private Connection connection;
    private Properties properties;

    /**
     * Generates an instance of the database
     * @return instance of database
     */
    public static DatabaseConnectionMSSQL getInstance() {
        if(instance == null) {
            instance = new DatabaseConnectionMSSQL();
        }
        return instance;
    }

    /**
     * Loads properties from properties file and connects with the database
     */
    public DatabaseConnectionMSSQL() {
        properties = new Properties();

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch(ClassNotFoundException e) {
            System.out.println("Could not find class: " + e);
        }

        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("database.properties"));
        } catch(IOException e) {
            System.out.println("Could not load file: " + e);
        }

        try {
            connect();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Makes the database connection
     * @throws SQLException
     */
    @Override
    public void connect() throws SQLException {
        try {
            connection = DriverManager.getConnection(properties.getProperty("connectionString"));
        } catch(SQLException e) {
            System.out.println("Could not connect to database: " + e);
        }
    }

    /**
     * Returns a prepared statement using the query
     * @param query
     * @return PreparedStatement connection.prepareStatement(query)
     */
    @Override
    public PreparedStatement preparedStatement(String query) {
        connection = this.getConnection();

        try {
            return connection.prepareStatement(query);
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns a result using the prepared statement
     * @param ps
     * @return ResultSet rs
     */
    @Override
    public ResultSet getResult(PreparedStatement ps) {
        ResultSet rs = null;

        try {
            rs = ps.executeQuery();
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    // Getters & setters
    public static void setInstance(DatabaseConnectionMSSQL instance) {
        DatabaseConnectionMSSQL.instance = instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
