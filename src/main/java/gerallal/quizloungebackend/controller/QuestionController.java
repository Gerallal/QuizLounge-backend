package gerallal.quizloungebackend.controller;

import gerallal.quizloungebackend.controller.api.model.QuestionDTO;
import gerallal.quizloungebackend.service.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quizlounge/api/question")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    public void addQuestion(@RequestBody QuestionDTO questionDTO) {
        System.out.println(questionDTO.getQuestionText());
        questionService.addQuestion(questionDTO);
    }
}
