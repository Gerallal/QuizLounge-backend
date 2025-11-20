package gerallal.quizloungebackend.service;

import gerallal.quizloungebackend.controller.api.model.FriendRequestDTO;
import gerallal.quizloungebackend.entity.FriendRequest;
import gerallal.quizloungebackend.entity.User;
import gerallal.quizloungebackend.repository.FriendRequestRepository;
import gerallal.quizloungebackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class FriendRequestService {
    UserRepository userRepository;
    FriendRequestRepository friendRequestRepository;
    @Transactional
    public void deleteFriend(Long userId, Long friendId) {
        User user = userRepository.findById(userId).orElseThrow();
        User friend = userRepository.findById(friendId).orElseThrow();
        // Entferne Freundschaft in beide Richtungen
        user.getFriends().remove(friend);
        friend.getFriends().remove(user);
        userRepository.save(user);
        userRepository.save(friend);
    }

    public void sendFriendRequest(String senderName, String receiverName){
        User sender = userRepository.findByUsername(senderName).orElse(null);
        User receiver = userRepository.findByUsername(receiverName).orElse(null);
        if(sender != null && receiver != null){
            FriendRequest friendRequest = FriendRequest.builder()
                    .sender(sender)
                    .receiver(receiver)
                    .build();
            friendRequestRepository.save(friendRequest);
        }

    }

    public List<FriendRequest> findByReceiver(User receiver){
        return friendRequestRepository.findByReceiver(receiver);
    }

    public static FriendRequestDTO map(FriendRequest friendRequest){
        FriendRequestDTO friendRequestDTO = FriendRequestDTO.builder()
                .id(friendRequest.getId())
                .sender(friendRequest.getSender().getUsername())
                .build();
        return friendRequestDTO;
    }
}
