package gerallal.quizloungebackend.service;

import gerallal.quizloungebackend.entity.User;
import gerallal.quizloungebackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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


}
