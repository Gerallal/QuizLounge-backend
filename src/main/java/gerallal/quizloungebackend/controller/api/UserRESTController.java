package gerallal.quizloungebackend.controller.api;

import gerallal.quizloungebackend.controller.api.model.LogInRequest;
import gerallal.quizloungebackend.controller.api.model.QuizCreateDTO;
import gerallal.quizloungebackend.controller.api.model.UserDTO;
import gerallal.quizloungebackend.entity.User;
import gerallal.quizloungebackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
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
    public Map<String, Object> login(@RequestBody LogInRequest params, HttpServletRequest request) {

        boolean success = userService.login(params.getUsername(), params.getPassword());
        Map<String, Object> loginResponse = new HashMap<>();
        if(success) {
            HttpSession session = request.getSession();
            session.setAttribute("username", params.getUsername());
            session.setAttribute("loggedIn", true);
            session.setMaxInactiveInterval(60 * 60);
            loginResponse.put("success", true);

            return loginResponse;
        }
        loginResponse.put("success", false);
        return loginResponse;
    }


    @GetMapping("current-user")
    public Map<String, String> getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            return Map.of("username", (String) session.getAttribute("username"));
            //return (String) session.getAttribute("username");
        }
        return Map.of("username", null);
        //return null;
    }

    @GetMapping("current-user/zwei")
    public UserDTO getCurrentUser2(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            User user = userService.getUserByUsername((String) session.getAttribute("username"));
            if (user != null) {
                return new UserDTO(
                        user.getUsername(),
                        user.getId(),
                        user.getReceivedQuizzes()
                                .stream()
                                .map(q -> new QuizCreateDTO(
                                        q.getId(),
                                        q.getTitle(),
                                        q.getDescription(),
                                        q.getCategory()
                                ))
                                .toList());
            }
        }
        return new UserDTO(null, 0, null);
    }


    @PostMapping("register")
    public Map<String, Object> register(@RequestBody LogInRequest params, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        if(userService.register(params)) {
            HttpSession session = request.getSession();
            session.setAttribute("username", params.getUsername());
            session.setAttribute("loggedIn", true);
            session.setMaxInactiveInterval(60 * 60);
            response.put("success", true);
            return response;

        }
        response.put("success", false);
        return response;
    }

    @GetMapping("home/{id}/friends")
    public List<UserDTO> getFriends(@PathVariable Long id) {
        User user = userService.getUserByID(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getFriends().stream()
                .map(friend -> new UserDTO(
                        friend.getUsername(),
                        friend.getId(),
                        friend.getReceivedQuizzes()
                                .stream()
                                .map(q -> new QuizCreateDTO(
                                        q.getId(),
                                        q.getTitle(),
                                        q.getDescription(),
                                        q.getCategory()
                                ))
                                .toList()
                ))
                .toList();
    }

    @GetMapping("/home/received/{userId}")
    public List<QuizCreateDTO> getReceivedQuizzes(@PathVariable Long userId) {

        User user = userService.getUserByID(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getQuizzes()
                .stream()
                .map(q -> new QuizCreateDTO(
                        q.getId(),
                        q.getTitle(),
                        q.getDescription(),
                        q.getCategory()
                ))
                .toList();
    }

}
