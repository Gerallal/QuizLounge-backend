package gerallal.quizloungebackend.controller.api;

import com.openai.models.beta.realtime.sessions.Session;
import gerallal.quizloungebackend.controller.api.model.RatingDTO;
import gerallal.quizloungebackend.entity.Quiz;
import gerallal.quizloungebackend.entity.User;
import gerallal.quizloungebackend.service.QuizService;
import gerallal.quizloungebackend.service.RatingQuizService;
import gerallal.quizloungebackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/quizlounge/api")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class StatsRestController {

    private final RatingQuizService ratingQuizService;
    private final UserService userService;
    private final QuizService quizService;

    @GetMapping("/stats")
    public List<RatingDTO> getStats(HttpSession session){
        String username = (String) session.getAttribute("username");

        if (username == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not logged in");
        }

        User user = userService.getUserByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }

        List<Quiz> quiz = quizService.getQuizzesByAuthor(user);

        List<RatingDTO> result = new ArrayList<>();

        for(Quiz q : quiz){
            double average;
            List<RatingDTO> ratings = ratingQuizService.getRatingQuizzesByQuizID(q.getId());
            average = ratingQuizService.getAverageRatingByQuizID(q.getId());
            ratings.forEach(rating -> {rating.setAverageRating(average);});
            result.addAll(ratings);
        }

        return result;
    }
}
