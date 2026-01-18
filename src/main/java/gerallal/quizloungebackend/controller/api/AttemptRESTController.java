package gerallal.quizloungebackend.controller.api;

import gerallal.quizloungebackend.controller.api.model.AnswerDTO;
import gerallal.quizloungebackend.controller.api.model.AttemptDTO;
import gerallal.quizloungebackend.controller.api.model.AttemptResultDTO;
import gerallal.quizloungebackend.controller.api.model.QuestionDTO;
import gerallal.quizloungebackend.entity.Attempt;
import gerallal.quizloungebackend.entity.Quiz;
import gerallal.quizloungebackend.entity.User;
import gerallal.quizloungebackend.service.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/quizlounge/api/solve")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AttemptRESTController {

    private final QuizService quizService;
    private final AttemptService attemptService;
    private final UserService userService;
    private final RatingQuizService ratingQuizService;
    private final QuestionService questionService;

    @GetMapping("/{id}")
    public AttemptDTO solve(@PathVariable long id, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            throw new RuntimeException("Ausgeloggt");
        }


        User user = userService.getUserByUsername(session.getAttribute("username").toString());
        Quiz quiz = quizService.getQuizById(id).orElse(null);
        if (quiz == null) {
            throw new RuntimeException("Quiz not found");
        }
        if (quiz.getQuestions().isEmpty()) {
            throw new RuntimeException("Question not found");
        }

        Attempt attempt = attemptService.startAttempt(quiz, user);
        return AttemptDTO.builder()
                .attemptId(attempt.getId())
                .quizId(quiz.getId())
                //.quizTitle(quiz.getTitle())
                .questions(
                        quiz.getQuestions().stream()
                                .map(q -> QuestionDTO.builder()
                                        .questionId(q.getId())
                                        .questionText(q.getQuestionName())
                                        .questionType(q.getTypeOfQuestion())
                                        .quizId(quiz.getId())
                                        .answers(
                                                q.getAnswers().stream()
                                                        .map(a -> AnswerDTO.builder()
                                                                .answerId(a.getId())
                                                                .answerText(a.getAnswerName())
                                                                .correct(a.isCorrect())
                                                                .build()
                                                        ).toList()
                                        )
                                        .build()
                                ).toList()
                )
                .build();

    }

    @GetMapping("/attempts/{id}")
    public AttemptDTO solveQuiz(@PathVariable long id, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            throw new RuntimeException("Ausgeloggt");
        }


        User user = userService.getUserByUsername(session.getAttribute("username").toString());
        Attempt attempt = attemptService.findAttemptById(id).orElse(null);
        if (attempt == null) {
            throw new RuntimeException("Attempt not found");
        }
        if (attempt.isFinished()) {
            throw new RuntimeException("Attempt is finished");
        }
        if (!user.equals(attempt.getUser())) {
            throw new RuntimeException("User does not belong to Attempt");
        }

        Quiz quiz = attempt.getQuiz();
        if (quiz.getQuestions().isEmpty()) {
            throw new RuntimeException("Question not found");
        }

        return AttemptDTO.builder()
                .attemptId(attempt.getId())
                .quizId(quiz.getId())
                //.quizTitle(quiz.getTitle())
                .questions(
                        quiz.getQuestions().stream()
                                .map(q -> QuestionDTO.builder()
                                        .questionId(q.getId())
                                        .questionText(q.getQuestionName()) // wichtig!
                                        .questionType(q.getTypeOfQuestion())
                                        .quizId(q.getQuiz().getId())
                                        .answers(
                                                q.getAnswers().stream()
                                                        .map(a -> AnswerDTO.builder()
                                                                .answerId(a.getId())
                                                                .answerText(a.getAnswerName()) // wichtig!
                                                                .correct(a.isCorrect())        // wichtig!
                                                                .build()
                                                        ).toList()
                                        )
                                        .build()
                                )
                                .toList()
                )
                .build();
    }

    @PostMapping("/attempts/{id}/evaluate")
    public AttemptResultDTO evaluateAttempt(
            @PathVariable long id,
            @RequestBody Map<String, String> allParams,
            HttpSession session) {

        String username = (String) session.getAttribute("username");
        if (username == null) {
            throw new RuntimeException("Ausgeloggt");
        }


        Attempt attempt = attemptService.findAttemptById(id).orElse(null);
        if (attempt == null) {
            throw new RuntimeException("Attempt not found");
        }
        if (allParams == null || allParams.isEmpty()) {
            throw new RuntimeException("No answers sent");
        }

        attemptService.evaluateAttempt(attempt, allParams);
        Quiz quiz = attempt.getQuiz();
        quiz.getAttempts().add(attempt);
        quizService.saveQuiz(quiz);

        return AttemptResultDTO.builder()
                .attemptId(attempt.getId())
                .quizId(attempt.getQuiz().getId())
                .finished(attempt.isFinished())
                .score(attempt.getScore())
                .numberOfRightAnswers(attempt.getNumberOfRightAnswers())
                .totalQuestions(attempt.getQuiz().getQuestions().size())
                .scoreInPercent(attempt.getScoreInPercent())
                .build();

    }

    @PostMapping("/{id}/rating")
    public Map<String, String> ratingQuiz(@PathVariable long id, HttpSession session, @RequestBody Map<String, Integer> allParams) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            throw new RuntimeException("Ausgeloggt");
        }

        if (allParams == null || allParams.isEmpty()) {
            throw new RuntimeException("No rating sent");
        }

        User user = userService.getUserByUsername(session.getAttribute("username").toString());
        Quiz quiz = quizService.getQuizById(id).orElseThrow(() -> new RuntimeException("Quiz not found"));
        Integer rating = allParams.get("rating");

        if (rating < 1 || rating > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        ratingQuizService.saveRating(user.getId(), quiz.getId(), rating);

        return Map.of("message", "Rating saved");
    }

    @GetMapping("/{id}/results")
    public AttemptResultDTO getResults(@PathVariable long id, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            throw new RuntimeException("Attempt not found");
        }


        User user = userService.getUserByUsername(session.getAttribute("username").toString());
        Quiz quiz = quizService.getQuizById(id).orElse(null);
        int numberOfQuestions = questionService.getNumberOfQuestions(quiz.getId());
        Optional<Attempt> attempt = attemptService.getLatestAttemptByUserAndQuiz(user.getId(), quiz.getId());

        return AttemptResultDTO.builder()
                .quizId(attempt.get().getQuiz().getId())
                .numberOfRightAnswers(attempt.get().getNumberOfRightAnswers())
                .totalQuestions(numberOfQuestions)
                .build();

    }
}
