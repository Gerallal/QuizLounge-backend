package gerallal.quizloungebackend.service;

import gerallal.quizloungebackend.controller.api.model.LogInRequest;
import gerallal.quizloungebackend.entity.Quiz;
import gerallal.quizloungebackend.entity.User;
import gerallal.quizloungebackend.repository.QuizRepository;
import gerallal.quizloungebackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import gerallal.quizloungebackend.controller.api.model.UserDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final QuizRepository quizRepository;
    private final PasswordEncoder encoder;

    public Optional<User> getUserByID(long id) {
        return userRepository.findById(id);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }

    public boolean login(String username, String password) {
        User user = userRepository.findByUsername(username).orElse(null);
        if(user == null) {
            return false;
        }

        return encoder.matches(password, user.getPasswordHash());
    }

    private UserDTO map(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        return userDTO;
    }

    public boolean register(LogInRequest request) {
        if(userRepository.findByUsername(request.getUsername()).isPresent()
        || request.getPassword() == null || request.getPassword().isEmpty()) {
            return false;
        }

        User user = User.builder()
                .passwordHash(encoder.encode(request.getPassword()))
                .username(request.getUsername())
                .build();
        userRepository.save(user);

        return true;
    }

    public void shareQuizWithFriend(Long quizId, Long friendId) {
        User receiver = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        if (receiver.getQuizzes().contains(quiz)) {
            throw new RuntimeException("User already has Quiz");
        }

        receiver.getQuizzes().add(quiz);
        userRepository.save(receiver);
    }

    @Transactional
    public void deleteFriendById(User user, User friend) {
        user.getFriends().remove(friend);
        friend.getFriends().remove(user);
        userRepository.save(user);
        userRepository.save(friend);
//        userRepository.deleteFriendsById(friendId);
    }

}
