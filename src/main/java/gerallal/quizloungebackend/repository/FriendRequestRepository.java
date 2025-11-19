package gerallal.quizloungebackend.repository;

import gerallal.quizloungebackend.entity.FriendRequest;
import gerallal.quizloungebackend.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FriendRequestRepository extends CrudRepository<FriendRequest, Long> {
    List<FriendRequest> findByReceiver(User receiver);
    Long id(Long id);
}
