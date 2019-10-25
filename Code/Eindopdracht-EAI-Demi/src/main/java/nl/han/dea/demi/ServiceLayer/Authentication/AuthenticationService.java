package nl.han.dea.demi.ServiceLayer.Authentication;

import javax.inject.Inject;

public class AuthenticationService {

    private AuthenticationDAO authenticationDAO;

    @Inject
    public void setAuthenticationDAO(AuthenticationDAO authenticationDAO) {
        this.authenticationDAO = authenticationDAO;
    }

    public boolean isTokenValid(String token) {
        return authenticationDAO.checkToken(token);
    }
}
