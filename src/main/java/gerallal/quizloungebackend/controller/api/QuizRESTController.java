package gerallal.quizloungebackend.controller.api;


import gerallal.quizloungebackend.controller.api.model.*;
import gerallal.quizloungebackend.entity.Answer;
import gerallal.quizloungebackend.entity.Question;
import gerallal.quizloungebackend.entity.Quiz;
import gerallal.quizloungebackend.entity.User;
import gerallal.quizloungebackend.repository.QuizRepository;
import gerallal.quizloungebackend.service.QuizService;
import gerallal.quizloungebackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/quizlounge/api/quiz")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class QuizRESTController {
    private final QuizRepository quizRepository;
    private QuizService quizService;
    private UserService userService;

    @PostMapping("create1")
    public QuizCreateDTO createQuiz(@RequestBody QuizCreateDTO quizCreateDTO, HttpSession session) {

        String username = (String) session.getAttribute("username");
        if (username == null) throw new RuntimeException("Not logged in");

        User user = userService.getUserByUsername(session.getAttribute("username").toString());
        Quiz quiz = Quiz.builder()
                .title(quizCreateDTO.getTitle())
                .author(user)
                .description(quizCreateDTO.getDescription())
                .category(quizCreateDTO.getCategory())
                .build();

        quizService.saveQuiz(quiz);

        return new QuizCreateDTO(
                quiz.getId(),
                quiz.getTitle(),
                quiz.getDescription(),
                quiz.getCategory()
        );

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
    public void createQuizQA(@RequestBody QuizCreateQADTO quizCreateQADTO, HttpSession session) {
        Quiz quiz = quizService.getQuizById(quizCreateQADTO.getId()).orElseThrow(() -> new RuntimeException("Quiz not found"));

        if(quiz.getQuestions() == null) {
            quiz.setQuestions(new ArrayList<>());
        } else {
            quiz.getQuestions().clear();
        }

        for(QuestionDTO questionDTO : quizCreateQADTO.getQuestions()) {
            Question q = new Question();
            q.setQuestionName(questionDTO.getQuestionName());
            q.setTypeOfQuestion(questionDTO.getTypeOfQuestion());
            q.setQuiz(quiz);

            q.setAnswers(new ArrayList<>());

            for(AnswerDTO answerDTO : questionDTO.getAnswers()) {
                Answer a = new Answer();
                a.setAnswerName(answerDTO.getText());
                a.setCorrect(answerDTO.isCorrect());
                a.setQuestion(q);

                q.getAnswers().add(a);
            }

            quiz.getQuestions().add(q);
        }

        quizService.saveQuiz(quiz);
    }

    @GetMapping("/myQuiz/{id}")
    public QuizCreateQADTO showAndSolveQuiz(@PathVariable long id) {
        Quiz quiz = quizService.getQuizById(id).orElseThrow(() -> new RuntimeException("Quiz not found"));

        return new QuizCreateQADTO(
                quiz.getId(),
                quiz.getTitle(),
                quiz.getDescription(),
                quiz.getCategory(),
                quiz.getQuestions()
                        .stream()
                        .map(q -> new QuestionDTO(
                                q.getQuestionName(),
                                q.getTypeOfQuestion(),
                                q.getAnswers()
                                        .stream()
                                        .map(a -> new AnswerDTO(
                                                a.getAnswerName(),
                                                a.isCorrect()
                                        ))
                                        .toList()
                        ))
                        .toList()
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
    public Quiz updateQuiz(@PathVariable Long quizId, @RequestBody Quiz updatedQuiz) {
        return quizService.updateQuiz(quizId, updatedQuiz);
    }

}
