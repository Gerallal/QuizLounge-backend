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
                return new UserDTO(user.getUsername(), user.getId());
            }
        }
        return new UserDTO(null, 0);
    }


    @PostMapping("register")
    public UserDTO register(@RequestBody LogInRequest params, HttpServletRequest request) {

        if(userService.getUserByUsername(params.getUsername()) != null) {
            return new UserDTO(null, 0);
        }
        User user = User.builder()
                .passwordHash(params.getPassword())
                .username(params.getUsername())
                .build();
        userService.save(user);

            HttpSession session = request.getSession();
            session.setAttribute("username", params.getUsername());
            session.setAttribute("loggedIn", true);
            session.setMaxInactiveInterval(60 * 60);

            return new UserDTO(user.getUsername(), user.getId());
    }

    @GetMapping("home/{id}/friends")
    public List<UserDTO> getFriends(@PathVariable Long id) {
        User user = userService.getUserByID(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getFriends().stream()
                .map(friend -> new UserDTO(friend.getUsername(), friend.getId()))
                .toList();
    }



}
