package gerallal.quizloungebackend.controller.api;

import gerallal.quizloungebackend.controller.api.model.QuizCreateDTO;
import gerallal.quizloungebackend.controller.api.model.UserDTO;
import gerallal.quizloungebackend.entity.User;
import gerallal.quizloungebackend.service.QuizService;
import gerallal.quizloungebackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping("/quizlounge/api")
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
                friend.getReceivedQuizzes()
                        .stream()
                        .map(rq -> new QuizCreateDTO(
                                rq.getId(),
                                rq.getTitle(),
                                rq.getDescription(),
                                rq.getCategory()
                        )).toList()
        );
    }

    @GetMapping("/friend/{id}/quizzes")
    public List<QuizCreateDTO> getFriendQuizzes(@PathVariable long id) {

        User friend = userService.getUserByID(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return friend.getQuizzes()
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
