package gerallal.quizloungebackend.repository;

import gerallal.quizloungebackend.entity.FriendRequest;
import gerallal.quizloungebackend.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends CrudRepository<FriendRequest, Long> {
    List<FriendRequest> findByReceiver(User receiver);
    List<FriendRequest> findByReceiverAndAccepted(User receiver, Boolean accepted);
    Long id(Long id);
    Optional<FriendRequest> findByReceiverAndSender(User receiver, User sender);
}
