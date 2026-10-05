package vn.iotstar.repository;

import vn.iotstar.entity.User_24110311;

public interface UserRepository_24110311 {
    User_24110311 findById(int id);
    User_24110311 findByEmail(String email);
    void save(User_24110311 user);
    void update(User_24110311 user);
    boolean checkEmailExist(String email);
}
