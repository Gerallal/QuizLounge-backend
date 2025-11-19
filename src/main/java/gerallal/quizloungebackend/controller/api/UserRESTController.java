package gerallal.quizloungebackend.controller.api;

import gerallal.quizloungebackend.controller.api.model.LogInRequest;
import gerallal.quizloungebackend.controller.api.model.UserDTO;
import gerallal.quizloungebackend.entity.User;
import gerallal.quizloungebackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/quizlounge/api")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class UserRESTController {
    private final UserService userService;



/*    @PostMapping("loginOld")
    public UserDTO loginOld(@RequestBody UserDTO params) {
        return userService.login(params.getUsername(), params.getPassword());
    }*/

    @PostMapping("login")
    public UserDTO login(@RequestBody LogInRequest params, HttpServletRequest request) {

        User possibleUser = userService.getUserByUsername(params.getUsername());
        if(possibleUser != null) {
            HttpSession session = request.getSession();
            session.setAttribute("username", params.getUsername());
            session.setAttribute("loggedIn", true);
            session.setMaxInactiveInterval(60 * 60);

            return new UserDTO(possibleUser.getUsername(),possibleUser.getId().longValue());
        }
        return new UserDTO(null, 0);
    }


    @GetMapping("current-user")
    public String getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            return (String) session.getAttribute("username");
        }

        return null;
    }


}
