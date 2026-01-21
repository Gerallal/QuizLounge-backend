package gerallal.quizloungebackend.service;


import gerallal.quizloungebackend.entity.Quiz;
import gerallal.quizloungebackend.entity.User;
import gerallal.quizloungebackend.repository.QuizRepository;
import gerallal.quizloungebackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class  QuizService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;

    public Optional<Quiz> getQuizById(long id) {return quizRepository.findById(id);}

    public Quiz saveQuiz(Quiz quiz) { return quizRepository.save(quiz);}

    @Transactional
    public void deleteQuizById(long quizId) {
        userRepository.deleteQuizFromUserQuizzes(quizId);
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));
        quizRepository.delete(quiz);
    }

    public  List<Quiz> getQuizzesByAuthor(User author) {
        return quizRepository.findByAuthor(author);
    }
}
