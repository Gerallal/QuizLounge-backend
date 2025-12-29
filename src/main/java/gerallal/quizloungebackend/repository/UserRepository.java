package gerallal.quizloungebackend.repository;

import gerallal.quizloungebackend.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RepositoryDefinition(domainClass = User.class, idClass = Long.class)
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
    void deleteFriendsById(Long id);
}
