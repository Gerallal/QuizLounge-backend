package gerallal.quizloungebackend.service;

import gerallal.quizloungebackend.entity.User;
import gerallal.quizloungebackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import gerallal.quizloungebackend.controller.api.model.UserDTO;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void save(User user) {userRepository.save(user); }

    public Optional<User> getUserByID(long id) {return userRepository.findById(id);}

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }

    public UserDTO login(String username, String password) {
        User user = userRepository.findByUsername(username).orElse(null);

        //System.out.println(user);

        if(user == null) {
            return new UserDTO("alexa", 1234);
        }

        return this.map(user);
    }

    private UserDTO map(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        return userDTO;
    }

}
