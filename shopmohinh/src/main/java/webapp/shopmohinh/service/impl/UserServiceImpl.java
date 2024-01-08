package webapp.shopmohinh.service.impl;

import webapp.shopmohinh.dto.UserDto;
import webapp.shopmohinh.model.Role;
import webapp.shopmohinh.model.User;
import webapp.shopmohinh.repository.RoleRepository;
import webapp.shopmohinh.repository.UserRepository;
import webapp.shopmohinh.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setFullname(userDto.getFullname());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role role = roleRepository.findByName("ROLE_USER");
        if(role == null){
            role = checkRoleExist();
        }
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public void changePass(Long id, String fullname, String email, String password) {
        User user = new User();
        user.setId(id);
        user.setFullname(fullname);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAll() {
        List<User> users = userRepository.findAll();
        return users;
    }

    @Override
    public User getById(Long id) {
        User user = null;

        Optional<User> optional = userRepository.findById(id);

        if (optional.isPresent()) {
            user = optional.get();
        } else {
            throw new RuntimeException("Không tìm thấy user!");
        }

        return user;
    }

    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_USER");
        return roleRepository.save(role);
    }
}