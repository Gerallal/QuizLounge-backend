package gerallal.quizloungebackend.service;


import gerallal.quizloungebackend.entity.Quiz;
import gerallal.quizloungebackend.entity.User;
import gerallal.quizloungebackend.repository.QuizRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class  QuizService {
    private final QuizRepository quizRepository;

    public List<Quiz> getAllQuizzes() {return (List<Quiz>) quizRepository.findAll();}

    public Optional<Quiz> getQuizById(long id) {return quizRepository.findById(id);}

    public void saveQuiz(Quiz quiz) {quizRepository.save(quiz);}

    public void deleteQuizById(long id) {quizRepository.deleteById(id);}

    public void updateQuiz(long id, Quiz updatedQuiz) {
        Quiz existing = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("not found"));
        existing.setTitle(updatedQuiz.getTitle());
        existing.setDescription(updatedQuiz.getDescription());
        existing.setCategory(updatedQuiz.getCategory());
        quizRepository.save(existing);
    }

    public List<Quiz> getQuizzesByAuthor(User author) {
        return quizRepository.findByAuthor(author);
    }

    public List<Quiz> getQuizzesByCategory(String category) { return quizRepository.findByCategory(category); }
}
