package gerallal.quizloungebackend.controller.api;

import gerallal.quizloungebackend.controller.api.model.*;
import gerallal.quizloungebackend.entity.User;
import gerallal.quizloungebackend.service.QuizService;
import gerallal.quizloungebackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quizlounge/api")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class FriendRESTController {

    private QuizService quizService;
    private UserService userService;

    @GetMapping("/friend/profile/{id}")
    public UserDTO getFriendProfile(@PathVariable long id) {

        User friend = userService.getUserByID(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserDTO(
                friend.getUsername(),
                friend.getId(),
                null
        );
    }

    @GetMapping("/friend/{id}/quizzes")
    public List<QuizCreateDTO> getFriendQuizzes(@PathVariable long id) {

        User friend = userService.getUserByID(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return quizService.getQuizzesByAuthor(friend)
                .stream()
                .map(q -> new QuizCreateDTO(
                        q.getId(),
                        q.getTitle(),
                        q.getDescription(),
                        q.getCategory()
                ))
                .toList();
    }

    @DeleteMapping("/friend/{id}")
    public void deleteFriend(@PathVariable long id) {
        userService.deleteFriendById(id);
    }

}
