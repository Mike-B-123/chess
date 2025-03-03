package services;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.User;
import responses.errors.Unauthorized401;
import responses.errors.UniqueError500;

public class loginService {
    public static AuthData login(User inputUser) throws Unauthorized401, UniqueError500 {
        try{
            if(getUser(inputUser) != null){
                if(getUser(inputUser).password() != inputUser.password()){
                    throw new Unauthorized401() ;
                }
                return createAuth(inputUser);
            }
            throw new UniqueError500() ; // should this be unauthorized?
        }
        catch(Exception ex){
            throw new UniqueError500() ;
            }
    }

    public static User getUser(User inputUser){
        UserDAO userDao = MemoryUserDAO.getInstance() ;
        return userDao.findUser(inputUser) ;
    }

    public static AuthData createAuth(User user){
        AuthDAO authDao = MemoryAuthDAO.getInstance() ;
        return authDao.createAuth(user) ;
    }
}
