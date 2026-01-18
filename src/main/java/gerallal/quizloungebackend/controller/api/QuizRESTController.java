package gerallal.quizloungebackend.controller.api;


import gerallal.quizloungebackend.controller.api.model.*;
import gerallal.quizloungebackend.entity.*;
import gerallal.quizloungebackend.service.QuizService;
import gerallal.quizloungebackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/quizlounge/api/quiz")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class QuizRESTController {

    private QuizService quizService;
    private UserService userService;

    @PostMapping("create1")
    public QuizCreateDTO createQuiz(@RequestBody QuizCreateDTO quizCreateDTO, HttpSession session) {

        if(session.getAttribute("username") == null) {
            return QuizCreateDTO.builder()
                    .quizId(-999L)
                    .successMessage("fail").build();
        }
        User user = userService.getUserByUsername(session.getAttribute("username").toString());
        Quiz quiz = Quiz.builder()
                .title(quizCreateDTO.getTitle())
                .author(user)
                .description(quizCreateDTO.getDescription())
                .category(quizCreateDTO.getCategory())
                .build();

        Quiz q = quizService.saveQuiz(quiz);

        return QuizCreateDTO.builder()
                .quizId(q.getId())
                .successMessage("succes!").build();
    }

    @GetMapping("home/{id}/create1")
    public List<QuizCreateDTO> getMyQuiz(@PathVariable long id) {
        User user = userService.getUserByID(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return quizService.getQuizzesByAuthor(user)
                .stream()
                .map(myQuiz -> new QuizCreateDTO(
                        myQuiz.getId(),
                        myQuiz.getTitle(),
                        myQuiz.getDescription(),
                        myQuiz.getCategory()
                )).toList();
    }

    @PostMapping("create2")
    public void createQuizQA(@RequestBody QuizCreateDTO quizCreateQADTO) {
        Quiz quiz = quizService.getQuizById(quizCreateQADTO.getId()).orElseThrow(() -> new RuntimeException("Quiz not found"));

        if(quiz.getQuestions() == null) {
            quiz.setQuestions(new ArrayList<>());
        } else {
            quiz.getQuestions().clear();
        }

        for(QuestionDTO questionDTO : quizCreateQADTO.getQuestions()) {
            Question q = new Question();
            q.setQuestionName(questionDTO.getQuestionText());
            q.setTypeOfQuestion(questionDTO.getQuestionType());
            q.setQuiz(quiz);
            q.setAnswers(new ArrayList<>());

            for(AnswerDTO answerDTO : questionDTO.getAnswers()) {
                Answer a = new Answer();
                a.setAnswerName(answerDTO.getAnswerText());
                a.setCorrect(answerDTO.isCorrect());
                a.setQuestion(q);

                q.getAnswers().add(a);
            }

            validateQuestion(q);

            quiz.getQuestions().add(q);
        }

        quizService.saveQuiz(quiz);
    }

    @GetMapping("/myQuiz/{id}")
    public QuizCreateDTO showAndSolveMyQuiz(@PathVariable long id) {

        Quiz quiz = quizService.getQuizById(id).orElseThrow(() -> new RuntimeException("Quiz not found"));
        User author = quiz.getAuthor();

        AttemptDTO attemptDTO = quiz.getAttempts().isEmpty()
                ? null
                : new AttemptDTO(
                quiz.getAttempts().get(quiz.getAttempts().size() - 1).getId(),
                quiz.getId(),
                null
        );

        return new QuizCreateDTO(
                quiz.getId(),
                new UserDTO(
                        author.getUsername(),
                        author.getId(),
                        author.getReceivedQuizzes()
                                .stream()
                                .map(q -> new QuizCreateDTO(
                                        q.getId(),
                                        q.getTitle(),
                                        q.getDescription(),
                                        q.getCategory()
                                ))
                                .toList()),
                quiz.getTitle(),
                quiz.getDescription(),
                quiz.getCategory(),
                quiz.getQuestions()
                        .stream()
                        .map(q -> new QuestionDTO(
                                q.getId(),
                                q.getQuestionName(),
                                q.getTypeOfQuestion(),
                                quiz.getId(),
                                q.getAnswers()
                                        .stream()
                                        .map(a -> new AnswerDTO(
                                                a.getId(),
                                                a.getAnswerName(),
                                                false
                                        ))
                                        .toList()
                        ))
                        .toList(),
                attemptDTO
        );
    }

    @PostMapping("/myQuiz/share/{quizId}/{friendId}")
    public void shareQuiz(@PathVariable Long quizId, @PathVariable Long friendId) {
        userService.shareQuizWithFriend(quizId, friendId);
    }

    @DeleteMapping("/myQuiz/{quizId}")
    public void deleteQuiz(@PathVariable Long quizId) {
        quizService.deleteQuizById(quizId);
    }

    @PutMapping("/myQuiz/edit/{quizId}")
    public void updateQuiz(@PathVariable Long quizId, @RequestBody QuizCreateDTO updatedQuiz, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) throw new RuntimeException("Ausgeloggt");
        Quiz quiz = quizService.getQuizById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found"));

        quiz.setTitle(updatedQuiz.getTitle());
        quiz.setDescription(updatedQuiz.getDescription());
        quiz.setCategory(updatedQuiz.getCategory());

        quiz.getQuestions().clear();

        for(QuestionDTO questionDTO : updatedQuiz.getQuestions()) {
            Question q = new Question();
            q.setQuestionName(questionDTO.getQuestionText());
            q.setTypeOfQuestion(questionDTO.getQuestionType());
            q.setQuiz(quiz);
            q.setAnswers(new ArrayList<>());

            for(AnswerDTO answerDTO : questionDTO.getAnswers()) {
                Answer a = new Answer();
                a.setAnswerName(answerDTO.getAnswerText());
                a.setCorrect(answerDTO.isCorrect());
                a.setQuestion(q);
                q.getAnswers().add(a);
            }

            validateQuestion(q);

            quiz.getQuestions().add(q);
        }
        quizService.saveQuiz(quiz);
    }

    private void validateQuestion(Question q) {

        long correctCount = q.getAnswers().stream()
                .filter(Answer::isCorrect)
                .count();

        switch (q.getTypeOfQuestion()) {

            case SingleAnswerQuestion -> {
                if (correctCount != 1) {
                    throw new IllegalArgumentException(
                            "Single answer question must have exactly one correct answer"
                    );
                }
            }

            case MultipleAnswerQuestion -> {
                if (correctCount < 1) {
                    throw new IllegalArgumentException(
                            "Multiple answer question must have at least one correct answer"
                    );
                }
            }

            case UserInputQuestion -> {
                if (q.getAnswers().size() != 1) {
                    throw new IllegalArgumentException(
                            "User input question must have exactly one answer"
                    );
                }
                q.getAnswers().get(0).setCorrect(true);
            }
        }
    }

}
