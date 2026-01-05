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

    public List<Quiz> getAllQuizzes() {return (List<Quiz>) quizRepository.findAll();}

    public Optional<Quiz> getQuizById(long id) {return quizRepository.findById(id);}

    public Quiz saveQuiz(Quiz quiz) { return quizRepository.save(quiz);}

    @Transactional
    public void deleteQuizById(long quizId) {
        userRepository.deleteQuizFromUserQuizzes(quizId);
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));
        quizRepository.delete(quiz);
    }

    public Quiz updateQuiz(long id, Quiz updatedQuiz) {
        Quiz existing = quizRepository.findById(id).orElseThrow(() -> new RuntimeException("Quiz not found"));
        existing.setTitle(updatedQuiz.getTitle());
        existing.setDescription(updatedQuiz.getDescription());
        existing.setCategory(updatedQuiz.getCategory());
        existing.setQuestions(updatedQuiz.getQuestions());

        return quizRepository.save(updatedQuiz);
    }

    public  List<Quiz> getQuizzesByAuthor(User author) {
        return quizRepository.findByAuthor(author);
    }

    public List<Quiz> getQuizzesByCategory(String category) {
        return quizRepository.findByCategory(category);
    }

    public void shareQuizWithFriend(Long quizId, Long friendId, User sender) {
        User receiver = userRepository.findById(friendId).orElseThrow(() -> new RuntimeException("User not found"));
        Quiz quiz = getQuizById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found"));

        if (receiver.getQuizzes().contains(quiz)) {
            throw new RuntimeException("Receiver already has Quiz");
        }

        receiver.getQuizzes().add(quiz);
        userRepository.save(receiver);
    }
}
