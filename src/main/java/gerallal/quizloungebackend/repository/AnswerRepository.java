package gerallal.quizloungebackend.repository;

import gerallal.quizloungebackend.entity.Answer;
import gerallal.quizloungebackend.entity.Quiz;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = Quiz.class, idClass = Long.class)
public interface AnswerRepository extends CrudRepository<Answer, Long> {

}
