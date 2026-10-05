package vn.iotstar.service;

import vn.iotstar.entity.User_24110311;
import vn.iotstar.repository.UserRepository_24110311;
import vn.iotstar.repository.UserRepositoryImpl_24110311;

import java.time.LocalDateTime;

public class UserServiceImpl_24110311 implements UserService_24110311 {

    private final UserRepository_24110311 userRepository = new UserRepositoryImpl_24110311();

    @Override
    public User_24110311 findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public User_24110311 findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        return userRepository.findByEmail(email.trim());
    }

    @Override
    public boolean register(User_24110311 user) {
        if (user == null || user.getEmail() == null || checkEmailExist(user.getEmail())) {
            return false;
        }
        userRepository.save(user);
        return true;
    }

    @Override
    public User_24110311 login(String email, String passwd) {
        if (email == null || passwd == null) {
            return null;
        }
        User_24110311 user = findByEmail(email.trim());
        if (user != null && passwd.equals(user.getPasswd())) {
            user.setLast_login(LocalDateTime.now());
            userRepository.update(user);
            return user;
        }
        return null;
    }

    @Override
    public boolean checkEmailExist(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return userRepository.checkEmailExist(email.trim());
    }
}
