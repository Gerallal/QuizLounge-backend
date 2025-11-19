package gerallal.quizloungebackend.repository;

import gerallal.quizloungebackend.entity.Quiz;
import gerallal.quizloungebackend.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.List;

@RepositoryDefinition(domainClass = User.class, idClass = Long.class)
public interface QuizRepository extends CrudRepository<Quiz, Long> {
    List<Quiz> findByAuthor(User user);
    List<Quiz> findByCategory(String category);
}
