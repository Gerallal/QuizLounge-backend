package gerallal.quizloungebackend.repository;

import gerallal.quizloungebackend.entity.Question;
import org.springframework.data.repository.CrudRepository;

public interface QuestionRepository extends CrudRepository<Question, Long> {
}
