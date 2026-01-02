package gerallal.quizloungebackend.controller.api;

import gerallal.quizloungebackend.controller.api.model.RatingDTO;
import gerallal.quizloungebackend.controller.api.model.StatsDTO;
import gerallal.quizloungebackend.entity.Attempt;
import gerallal.quizloungebackend.entity.Quiz;
import gerallal.quizloungebackend.entity.User;
import gerallal.quizloungebackend.service.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/quizlounge/api/stats")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class StatsRestController {

    private final RatingQuizService ratingQuizService;
    private final UserService userService;
    private final QuizService quizService;
    private final AttemptService attemptService;
    private final QuestionService questionService;

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
    @GetMapping("/myStats")
    public List<StatsDTO> getMyStats(HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not logged in");
        }

        User user = userService.getUserByUsername(username);
        List<Attempt> getAllOfMyAttempts = attemptService.getAllQuizzesForUser(user.getId());

        List<Quiz> quiz = getAllOfMyAttempts.stream()
                .map(Attempt::getQuiz)
                .distinct()
                .toList();


        Map<Long, Integer> questionCountByQuizId =
                quiz.stream()
                        .collect(Collectors.toMap(
                                Quiz::getId,
                                q -> questionService.getNumberOfQuestions(q.getId())
                        ));

        List<StatsDTO> results =
                getAllOfMyAttempts.stream()
                        .map(attempt -> new StatsDTO(
                                attempt.getQuiz().getId(),
                                user.getUsername(),
                                attempt.getNumberOfRightAnswers(),
                                questionCountByQuizId.get(attempt.getQuiz().getId()),
                                attempt.getQuiz().getTitle(),
                                attempt.getEndTime()
                        ))
                        .toList();

        return results;


    }

    @GetMapping("/statsOfMyQuizzes")
    public List<StatsDTO> getStatsOfMyQuizzes(HttpSession session){
        String username = "Basti";

        if (username == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not logged in");
        }

        User user = userService.getUserByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }

        List<Quiz> quiz = quizService.getQuizzesByAuthor(user);

        Map<Long, Integer> questionCountByQuizId =
                quiz.stream()
                        .collect(Collectors.toMap(
                                Quiz::getId,
                                q -> questionService.getNumberOfQuestions(q.getId())
                        ));

        List<StatsDTO> results =
                quiz.stream()
                        .flatMap(q ->
                                attemptService
                                        .findAllAttemptsByQuizId(q.getId())
                                        .stream()
                        )
                        .filter(Attempt::isFinished)
                        .map(attempt -> new StatsDTO(
                                attempt.getQuiz().getId(),
                                attempt.getUser().getUsername(),
                                attempt.getNumberOfRightAnswers(),
                                questionCountByQuizId.get(attempt.getQuiz().getId()),
                                attempt.getQuiz().getTitle(),
                                attempt.getEndTime()
                        ))
                        .toList();

         return results;
    }
}
