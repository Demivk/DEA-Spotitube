package nl.han.dea.demi.DataAccessLayer.DAO;

import nl.han.dea.demi.DataAccessLayer.Databases.MSSQL.DatabaseConnectionMSSQL;
import nl.han.dea.demi.DTO.Authentication.AuthenticationDTO;
import nl.han.dea.demi.ServiceLayer.Authentication.AuthenticationDAO;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticationDAOImpl implements AuthenticationDAO {

    @Inject
    private DatabaseConnectionMSSQL databaseMSSQL;

    /**
     * Gets token using the user
     * @param user
     * @return authentication
     */
    @Override
    public AuthenticationDTO getToken(String user) {
        AuthenticationDTO authentication = new AuthenticationDTO();

        String selectQuery = "SELECT token FROM [Authentication] WHERE [user] = '" + user + "'";

        PreparedStatement ps = databaseMSSQL.preparedStatement(selectQuery);
        ResultSet rs = databaseMSSQL.getResult(ps);

        try {
            while(rs != null && rs.next()) {
                authentication.setUser(user);
                authentication.setToken(rs.getString("token"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return authentication;
    }

    /**
     * Checks if token exists in databaseMSSQL
     * @param token
     * @return true if token exists, false if token does not exists
     */
    @Override
    public boolean checkToken(String token) {
        String selectQuery = "SELECT token FROM [Authentication] WHERE token = '" + token + "'";

        PreparedStatement ps = databaseMSSQL.preparedStatement(selectQuery);
        ResultSet rs = databaseMSSQL.getResult(ps);

        try {
            if(rs != null && rs.next()) {
                return true;
            } else {
                System.out.println("No matches");
                return false;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
