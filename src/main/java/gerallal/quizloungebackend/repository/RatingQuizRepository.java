package gerallal.quizloungebackend.repository;

import gerallal.quizloungebackend.entity.RatingQuiz;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.List;

@RepositoryDefinition(domainClass = RatingQuiz.class, idClass = Long.class)
public interface RatingQuizRepository extends CrudRepository<RatingQuiz, Long> {
    List<RatingQuiz> findAll();
    List<RatingQuiz> findByQuizId(Long quizId);
}
