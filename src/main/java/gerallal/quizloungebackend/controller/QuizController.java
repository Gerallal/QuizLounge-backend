package gerallal.quizloungebackend.controller;


import gerallal.quizloungebackend.controller.api.model.QuizCreateAnswerDTO;
import gerallal.quizloungebackend.controller.api.model.QuizCreateDTO;
import gerallal.quizloungebackend.entity.Quiz;
import gerallal.quizloungebackend.entity.User;
import gerallal.quizloungebackend.service.QuizService;
import gerallal.quizloungebackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/quizlounge/api/quiz")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class QuizController {
    private QuizService quizService;
    private UserService userService;

    @PostMapping("create")
    public QuizCreateAnswerDTO createQuiz(@RequestBody QuizCreateDTO quizCreateDTO, HttpSession session) {

        if(session.getAttribute("username") == null) {
            return QuizCreateAnswerDTO.builder()
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

        return QuizCreateAnswerDTO.builder()
                .quizId(q.getId())
                .successMessage("succes!").build();


    }
}
