package gerallal.quizloungebackend.repository;

import gerallal.quizloungebackend.entity.Attempt;
import gerallal.quizloungebackend.entity.Quiz;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.Optional;

@RepositoryDefinition(domainClass = Quiz.class, idClass = Long.class)
public interface AttemptRepository extends CrudRepository<Attempt, Long> {
    Optional<Attempt> findTopByUserIdAndQuizIdOrderByEndTimeDesc(Long userId, Long quizId);
}
