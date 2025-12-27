package gerallal.quizloungebackend.service;

import gerallal.quizloungebackend.controller.api.model.RatingDTO;
import gerallal.quizloungebackend.entity.Quiz;
import gerallal.quizloungebackend.entity.RatingQuiz;
import gerallal.quizloungebackend.entity.User;
import gerallal.quizloungebackend.repository.QuizRepository;
import gerallal.quizloungebackend.repository.RatingQuizRepository;
import gerallal.quizloungebackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RatingQuizService {
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final RatingQuizRepository ratingQuizRepository;

    public void saveRating(Long authorId, Long quizId, int rating) {
        User user = userRepository.findById(authorId).orElse(null);
        Quiz quiz = quizRepository.findById(quizId).orElse(null);


        RatingQuiz ratingQuiz = RatingQuiz.builder()
                .author(user)
                .quiz(quiz)
                .rating(rating)
                .build();

        ratingQuizRepository.save(ratingQuiz);
    }
}
