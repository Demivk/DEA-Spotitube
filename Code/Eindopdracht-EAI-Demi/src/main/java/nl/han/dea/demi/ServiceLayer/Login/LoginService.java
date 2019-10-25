package nl.han.dea.demi.ServiceLayer.Login;

import nl.han.dea.demi.DTO.Login.UserDTO;

import javax.inject.Inject;

public class LoginService {

    private LoginDAO loginDAO;

    @Inject
    public void setLoginDAO(LoginDAO loginDAO) {
        this.loginDAO = loginDAO;
    }

    public boolean checkLoginValues(UserDTO user) {
        return loginDAO.isUserValid(user.getUser(), user.getPassword());
    }
}
