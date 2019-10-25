package nl.han.dea.demi.ServiceLayer.Authentication;

import nl.han.dea.demi.DTO.Authentication.AuthenticationDTO;

public interface AuthenticationDAO {
    AuthenticationDTO getToken(String user);
    boolean checkToken(String token);
}
