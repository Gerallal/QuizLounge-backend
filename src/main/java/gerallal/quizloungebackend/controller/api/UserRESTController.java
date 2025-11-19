package gerallal.quizloungebackend.controller.api;

import gerallal.quizloungebackend.controller.api.model.UserDTO;
import gerallal.quizloungebackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/quizlounge/api")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class UserRESTController {
    private final UserService userService;



    @PostMapping("login")
    public UserDTO login(@RequestBody UserDTO params) {
        return userService.login(params.getUsername(), params.getPassword());
    }


}
