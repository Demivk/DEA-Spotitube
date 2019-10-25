package nl.han.dea.demi.DataAccessLayer.DAO;

import nl.han.dea.demi.DataAccessLayer.Databases.MSSQL.DatabaseConnectionMSSQL;
import nl.han.dea.demi.ServiceLayer.Login.LoginDAO;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class LoginDAOImpl implements LoginDAO {

    @Inject
    private DatabaseConnectionMSSQL databaseMSSQL;

    private Map<String, String> mapUserPassword = new HashMap<>();

    /**
     * Checks if the combination of user and password exists in the databaseMSSQL
     * @param user
     * @param password
     * @return true if combination exists, false if combination is wrong
     */
    @Override
    public boolean isUserValid(String user, String password) {
        // Checks if user and password exist in hashmap
        if(mapUserPassword.get(user) != null) {
            if(mapUserPassword.get(user).equals(password)) {
                System.out.println("User and password are the same and exist in hashmap.");
                return true;
            } else {
                System.out.println("User and password are not the same, try again or create an account.");
                return false;
            }
        }

        System.out.println("Hasmap with user is null, checking user...");

        String dbUser = "";
        String dbPassword = "";

        String selectQuery = "SELECT [user], [password] " +
                             "FROM [User] " +
                             "WHERE [user] = '" + user + "' AND [password] = '" + password + "'";

        PreparedStatement ps = databaseMSSQL.preparedStatement(selectQuery);
        ResultSet rs = databaseMSSQL.getResult(ps);

        try {
            while(rs != null && rs.next()) {
                dbUser = rs.getString("user");
                dbPassword = rs.getString("password");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        if(user.equals(dbUser) && password.equals(dbPassword)) {
            System.out.println("User and password are the same, they will be added to the hashmap.");
            mapUserPassword.put(dbUser, dbPassword);
            return true;
        }

        System.out.println("User and password combination does not exist in database. Try again or create an account.");
        return false;
    }
}
