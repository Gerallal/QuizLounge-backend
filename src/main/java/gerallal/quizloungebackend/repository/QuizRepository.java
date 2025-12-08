package gerallal.quizloungebackend.repository;

import gerallal.quizloungebackend.entity.Quiz;
import gerallal.quizloungebackend.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.List;
import java.util.Optional;

@RepositoryDefinition(domainClass = User.class, idClass = Long.class)
public interface QuizRepository extends CrudRepository<Quiz, Long> {
    List<Quiz> findByAuthor(User user);
    List<Quiz> findByCategory(String category);
    List<Quiz> findByTitle(String title);
    Optional<Quiz> findById(long id);
    Optional<Quiz> findByIdAndAuthor(long id, User user);
}
