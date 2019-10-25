package nl.han.dea.demi.LoginTest;

import nl.han.dea.demi.DTO.Login.UserDTO;
import nl.han.dea.demi.DataAccessLayer.Databases.MSSQL.DatabaseConnectionMSSQL;
import nl.han.dea.demi.ServiceLayer.Login.LoginDAO;
import nl.han.dea.demi.ServiceLayer.Login.LoginService;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class LoginServiceTest {

    @Mock
    private DatabaseConnectionMSSQL databaseMSSQL;

    @Test
    public void testValidUserCanLoginPass() {
        // Arrange.
        LoginService sut = new LoginService();
        LoginDAO mockLoginDAO = mock(LoginDAO.class);
        sut.setLoginDAO(mockLoginDAO);

        boolean expected = true;

        String userName = "someUser";
        String password = "validPassword";

        UserDTO testUser = new UserDTO();
        testUser.setUser(userName);
        testUser.setPassword(password);

        when(mockLoginDAO.isUserValid(userName, password)).thenReturn(true);

        // Act.
        boolean actual = sut.checkLoginValues(testUser);

        // Assert.
        assertEquals(expected, actual);
    }

    @Test
    public void testValidUserCanLoginFail() {
        // Arrange.
        LoginService sut = new LoginService();
        LoginDAO mockLoginDAO = mock(LoginDAO.class);
        sut.setLoginDAO(mockLoginDAO);

        boolean expected = false;

        String userName = "someUser";
        String password = "invalidPassword";

        UserDTO testUser = new UserDTO();
        testUser.setUser(userName);
        testUser.setPassword(password);

        when(mockLoginDAO.isUserValid(userName, password)).thenReturn(false);

        // Act.
        boolean actual = sut.checkLoginValues(testUser);

        // Assert.
        assertEquals(expected, actual);
    }
}
