package gerallal.quizloungebackend.controller.api;


import gerallal.quizloungebackend.controller.api.model.FriendRequestDTO;
import gerallal.quizloungebackend.entity.FriendRequest;
import gerallal.quizloungebackend.entity.User;
import gerallal.quizloungebackend.service.FriendRequestService;
import gerallal.quizloungebackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/quizlounge/api/friends")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class FriendRequestRESTController {

    private final UserService userService;
    FriendRequestService friendRequestService;

    @GetMapping("friend-request")
    Map<String, String> sendFriendRequest(@RequestParam Map<String, String> params, HttpServletRequest request){
        String senderName =request.getSession().getAttribute("username").toString();
        String friendName = params.get("friend");
        friendRequestService.sendFriendRequest(senderName, friendName);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return response;
    }

    @GetMapping("/requests-for-user")
    public ResponseEntity<Map<String, Object>> retrieveFriendRequests(HttpServletRequest request){
        String username = request.getSession().getAttribute("username").toString();
        User user = userService.getUserByUsername(username);
        List<FriendRequest> friendRequests = friendRequestService.findByReceiver(user);
        List<FriendRequestDTO> friendRequestDTOS =  friendRequests.stream().map(FriendRequestService::map).toList();
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("data", friendRequestDTOS);
        return new ResponseEntity<> (response, HttpStatus.OK);
    }
}
