package webapp.shopmohinh.service;

import webapp.shopmohinh.dto.UserDto;
import webapp.shopmohinh.model.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    void changePass(Long id, String fullname, String email, String password);

    User findByEmail(String email);

    List<User> getAll();

    User getById(Long id);
}