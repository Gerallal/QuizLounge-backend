package gerallal.quizloungebackend.repository;

import gerallal.quizloungebackend.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RepositoryDefinition(domainClass = User.class, idClass = Long.class)
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Modifying
    @Transactional
    @Query(
            value = "DELETE FROM user_quizzes WHERE quizzes_id = :quizId",
            nativeQuery = true
    )
    void deleteQuizFromUserQuizzes(@Param("quizId") Long quizId);

    void deleteFriendsById(Long id);
}
