package nl.han.dea.demi.AuthenticationTest;

import nl.han.dea.demi.DTO.Authentication.AuthenticationDTO;
import nl.han.dea.demi.DataAccessLayer.Databases.MSSQL.DatabaseConnectionMSSQL;
import nl.han.dea.demi.ServiceLayer.Authentication.AuthenticationDAO;
import nl.han.dea.demi.ServiceLayer.Authentication.AuthenticationService;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthenticationServiceTest {

    @Mock
    private DatabaseConnectionMSSQL databaseMSSQL;

    @Test
    public void testTokenIsValidPass() {
        // Arrange.
        AuthenticationService sut = new AuthenticationService();
        AuthenticationDAO mockAuthDAO = mock(AuthenticationDAO.class);
        sut.setAuthenticationDAO(mockAuthDAO);

        boolean expected = true;

        String userName = "someUser";
        String token = "1234-5678";

        AuthenticationDTO testAuth = new AuthenticationDTO();
        testAuth.setUser(userName);
        testAuth.setToken(token);

        when(mockAuthDAO.checkToken(token)).thenReturn(true);

        // Act.
        boolean actual = sut.isTokenValid(testAuth.getToken());

        // Assert.
        assertEquals(expected, actual);
    }

    @Test
    public void testTokenIsValidFail() {
        // Arrange.
        AuthenticationService sut = new AuthenticationService();
        AuthenticationDAO mockAuthDAO = mock(AuthenticationDAO.class);
        sut.setAuthenticationDAO(mockAuthDAO);

        boolean expected = false;

        String userName = "someUser";
        String incorrectToken = "0000-0000";

        AuthenticationDTO testAuth = new AuthenticationDTO();
        testAuth.setUser(userName);
        testAuth.setToken(incorrectToken);

        when(mockAuthDAO.checkToken(incorrectToken)).thenReturn(false);

        // Act.
        boolean actual = sut.isTokenValid(testAuth.getToken());

        // Assert.
        assertEquals(expected, actual);
    }
}
