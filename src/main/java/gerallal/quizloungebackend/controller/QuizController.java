package gerallal.quizloungebackend.controller;


import gerallal.quizloungebackend.controller.api.model.*;
import gerallal.quizloungebackend.entity.Answer;
import gerallal.quizloungebackend.entity.Question;
import gerallal.quizloungebackend.entity.Quiz;
import gerallal.quizloungebackend.entity.User;
import gerallal.quizloungebackend.repository.QuizRepository;
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
public class QuizController {
    private final QuizRepository quizRepository;
    private QuizService quizService;
    private UserService userService;

    @PostMapping("create1")
    public void createQuiz(@RequestBody QuizCreateDTO quizCreateDTO, HttpSession session) {

        if(session.getAttribute("username") == null) {
            return;
        }
        User user = userService.getUserByUsername(session.getAttribute("username").toString());
        Quiz quiz = Quiz.builder()
                .title(quizCreateDTO.getTitle())
                .author(user)
                .description(quizCreateDTO.getDescription())
                .category(quizCreateDTO.getCategory())
                .build();

        quizService.saveQuiz(quiz);

    }

    @GetMapping("home/{id}/create1")
    public List<QuizCreateDTO> getMyQuiz(@PathVariable long id) {
        User user = userService.getUserByID(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return quizService.getQuizzesByAuthor(user).stream().map(myQuiz -> new QuizCreateDTO(myQuiz.getTitle(), myQuiz.getDescription(), myQuiz.getCategory())).toList();
    }

    @PostMapping("create2")
    public void createQuizQA(@RequestBody QuizCreateQADTO quizCreateQADTO, HttpSession session) {
        if(session.getAttribute("username") == null) {
            return;
        }
        User user = userService.getUserByUsername(session.getAttribute("username").toString());
        Quiz quiz = new Quiz();
        quiz.setAuthor(user);
        quiz.setTitle(quizCreateQADTO.getTitle());
        quiz.setDescription(quizCreateQADTO.getDescription());
        quiz.setCategory(quizCreateQADTO.getCategory());

        quiz.setQuestions(new ArrayList<>());

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

    @GetMapping("home/myQuiz/{id}")
    public List<QuizCreateQADTO> showAndSolveQuiz(@PathVariable long id) {
        User user = userService.getUserByID(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return quizService.getQuizByIdAndAuthor(id, user)
                .stream()
                .map(myQuiz -> new QuizCreateQADTO(
                        myQuiz.getTitle(),
                        myQuiz.getDescription(),
                        myQuiz.getCategory(),
                        myQuiz.getQuestions()
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
                ))
                .toList();
    }

}
