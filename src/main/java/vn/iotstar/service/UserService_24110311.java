package vn.iotstar.service;

import vn.iotstar.entity.User_24110311;

public interface UserService_24110311 {
    User_24110311 findById(int id);
    User_24110311 findByEmail(String email);
    boolean register(User_24110311 user);
    User_24110311 login(String email, String passwd);
    boolean checkEmailExist(String email);
}
