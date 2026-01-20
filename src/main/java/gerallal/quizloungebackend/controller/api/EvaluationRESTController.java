package gerallal.quizloungebackend.controller.api;

import gerallal.quizloungebackend.controller.api.model.EvaluationDTO;
import gerallal.quizloungebackend.entity.User;
import gerallal.quizloungebackend.service.EvaluationService;
import gerallal.quizloungebackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping("/quizlounge/api")
@CrossOrigin(
        origins = "http://localhost:4200",
        allowCredentials = "true",
        allowedHeaders = "*",
        methods = {RequestMethod.POST, RequestMethod.OPTIONS}
)

public class EvaluationRESTController {

    private final UserService userService;
    private final EvaluationService evaluationService;

    @PostMapping("/solveQuiz/evaluateAnswer")
    public boolean getAnswer(@RequestBody EvaluationDTO params, HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");

        if (username == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not logged in");
        }

        User user = userService.getUserByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }

        return evaluationService.evaluate(params.getQuestion(), params.getAnswer());

    }
}
