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
import java.util.Optional;

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

    public boolean sendFriendRequest(String senderName, String receiverName){
        User sender = userRepository.findByUsername(senderName).orElse(null);
        User receiver = userRepository.findByUsername(receiverName).orElse(null);
        if(sender != null && receiver != null
        && friendRequestRepository.findByReceiverAndSender(receiver, sender).isEmpty()
        && !sender.getFriends().contains(receiver)){

            FriendRequest friendRequest = FriendRequest.builder()
                    .sender(sender)
                    .receiver(receiver)
                    .build();
            friendRequestRepository.save(friendRequest);
            return true;
        }
    return false;
    }

    public List<FriendRequest> findByReceiver(User receiver){
        return friendRequestRepository.findByReceiver(receiver);
    }

    public List<FriendRequest> findByReceiverAndNotAccepted(User receiver){
        return friendRequestRepository.findByReceiverAndAccepted(receiver, false);
    }

    public static FriendRequestDTO map(FriendRequest friendRequest){
        return FriendRequestDTO.builder()
                .id(friendRequest.getId())
                .sender(friendRequest.getSender().getUsername())
                .build();
    }

    public boolean acceptFriendRequest(String receiverName, Long frqID) {

        User receiver = userRepository.findByUsername(receiverName).orElse(null);
        User sender = null;
        if(friendRequestRepository.findById(frqID).isPresent()){
            sender = friendRequestRepository.findById(frqID).get().getSender();
        }
        if(receiver == null || sender == null){
            return false;
        }

        FriendRequest frq = friendRequestRepository.findByReceiverAndSender(receiver, sender)
                .orElse(null);
        if(frq == null){
            System.out.println("frq is null");
            return false;
        }

        frq.setAccepted(true);
        friendRequestRepository.save(frq);

        receiver.getFriends().add(sender);
        sender.getFriends().add(receiver);
        userRepository.save(receiver);
        userRepository.save(sender);
        friendRequestRepository.deleteById(frq.getId());
        return true;
    }

    public Optional<FriendRequest> getFriendRequestByID(Long id){
        return this.friendRequestRepository.findById(id);
    }

    public void deleteFriendRequest(Long requestID) {
        friendRequestRepository.deleteById(requestID);
    }
}
