package nl.han.dea.demi.ServiceLayer.Login;

public interface LoginDAO {
    boolean isUserValid(String user, String password);
}
