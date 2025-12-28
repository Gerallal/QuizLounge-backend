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

        boolean success = friendRequestService.sendFriendRequest(senderName, friendName);

        Map<String, String> response = new HashMap<>();
        if(success){
            response.put("status", "success");
        }
        else{
            response.put("status", "failed");
        }
        return response;
    }

    @GetMapping("/requests-for-user")
    public ResponseEntity<Map<String, Object>> retrieveFriendRequests(HttpServletRequest request){
        String username = request.getSession().getAttribute("username").toString();
        User user = userService.getUserByUsername(username);
        List<FriendRequest> friendRequests = friendRequestService.findByReceiverAndNotAccepted(user);
        List<FriendRequestDTO> friendRequestDTOS =  friendRequests.stream().map(FriendRequestService::map).toList();
        Map<String, Object> response = new HashMap<>();
        response.put("data", friendRequestDTOS);
        return new ResponseEntity<> (response, HttpStatus.OK);
    }

    @GetMapping("/accept-friend-request/{senderID}")
    public Map<String, Object> acceptFriendRequest(@PathVariable Long senderID, HttpServletRequest request){

        String receiverName = request.getSession().getAttribute("username").toString();

        System.out.println(receiverName);

        boolean success = friendRequestService.acceptFriendRequest(receiverName, senderID);

        Map<String, Object> response = new HashMap<>();
        if(success){
            response.put("status", "success");
        }
        else{
            response.put("status", "failed");
        }
        return response;
    }

    @GetMapping("decline-friend-request/{requestID}")
    public Map<String, Object>  declineFriendRequest(@PathVariable Long requestID, HttpServletRequest request){
        Map<String, Object> response = new HashMap<>();

        FriendRequest frq = friendRequestService.getFriendRequestByID(requestID).orElse(null);
        if(frq == null){
            response.put("status", "fail");
            return response;
        }
        String username = request.getSession().getAttribute("username").toString();
        System.out.println(username);

        if(!username.equals(frq.getReceiver().getUsername())){
            response.put("status", "fail");
            return response;
        }
        friendRequestService.deleteFriendRequest(requestID);
            response.put("status", "success");

            return response;
    }
}
