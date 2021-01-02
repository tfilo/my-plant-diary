package sk.filo.plantdiary.service;

import org.springframework.stereotype.Service;
import sk.filo.plantdiary.service.so.CreateUserSO;
import sk.filo.plantdiary.service.so.UserSO;

@Service
public class UserService {

    // register new user, set enabled to false
    public UserSO register(CreateUserSO createUserSO) {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }

    // activate new user by link from email, set enabled to true
    public UserSO activate(String token) {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }

    // allow update only logged user
    public UserSO updateOwnUser(UserSO userSO) {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }

    // allow get only logged user
    public UserSO getOwnUser() {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }

    // allow delete only own logged user
    public void deleteOwnUser() {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }
}
