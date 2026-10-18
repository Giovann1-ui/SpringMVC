package app.service;

import app.model.UserModel;
import app.repository.UserRepository;
import org.springframework.stereotype.Service;
import annotation.Injection;

@Service
public class UserService {
    @Injection
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserModel createUser(String username, String email) {
        UserModel user = new UserModel();
        user.setUsername(username);
        return userRepository.save(user);
    }

    public UserModel getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
