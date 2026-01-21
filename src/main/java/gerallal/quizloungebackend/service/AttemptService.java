package gerallal.quizloungebackend.service;

import gerallal.quizloungebackend.entity.*;
import gerallal.quizloungebackend.repository.AttemptRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AttemptService {
    private final AttemptRepository attemptRepository;
    private final EvaluationService evaluationService;

    public Attempt save(Attempt attempt) {
        return attemptRepository.save(attempt);
    }

    public Optional<Attempt> findAttemptById(Long id) {
        return attemptRepository.findById(id);
    }

    public List<Attempt> findAllAttemptsByQuizId(Long quizId) {
        return attemptRepository.findByQuizId(quizId);
    }

    public List<String> getUsernamesOfFinishedAttemptsForQuiz(Long quizId) {
        return attemptRepository.findByQuizIdAndFinishedTrue(quizId)
                .stream()
                .map(a -> a.getUser().getUsername())
                .toList();
    }

    public List<Attempt> getAllQuizzesForUser(Long userId) {
        return attemptRepository.findByUserIdAndFinishedTrue(userId)
                .stream()
                .filter(a -> a.getUser().getId().equals(userId))
                .toList();

    }

    public Optional<Attempt> getLatestAttemptByUserAndQuiz(Long userId, Long quizId) {
        return attemptRepository.findTopByUserIdAndQuizIdOrderByEndTimeDesc(userId, quizId);
    }
    public void evaluateAttempt(Attempt attempt, Map<String,String> answers) {
        int correctAnswers = 0;
        for (Question question : attempt.getQuiz().getQuestions()) {
            String userAnswer = answers.get(String.valueOf(question.getId()));
            if (userAnswer == null) continue;
            boolean result = false;

            switch (question.getTypeOfQuestion()) {
                case SingleAnswerQuestion:
                    result = question.getAnswers().stream()
                            .anyMatch(a -> a.isCorrect() && a.getAnswerName().equals(userAnswer));
                    break;
                case MultipleAnswerQuestion:
                    Set<String> userAnswers = Arrays.stream(userAnswer.split(","))
                            .map(String::trim)
                            .collect(Collectors.toSet());

                    Set<String> correctAnswer = question.getAnswers().stream()
                            .filter(Answer::isCorrect)
                            .map(Answer::getAnswerName)
                            .collect(Collectors.toSet());

                    result = userAnswers.equals(correctAnswer);
                    break;
                case UserInputQuestion:
                    result = evaluationService.evaluate(question.getAnswers().get(0).getAnswerName(), userAnswer);

                    System.out.println(result);
                    break;
            }
            if (result) {
                correctAnswers++;
            }
        }

        attempt.setNumberOfRightAnswers(correctAnswers);
        attempt.setScore(((float) attempt.getNumberOfRightAnswers()) / attempt.getQuiz().getQuestions().size());
        attempt.setFinished(true);
        attempt.setEndTime();
        calculateDuration(attempt);
        attemptRepository.save(attempt);
    }

    public void calculateDuration(Attempt attempt) {
        if (attempt.getStartTime() != null && attempt.getEndTime() != null) {
            attempt.setDuration(Duration.between(attempt.getStartTime(), attempt.getEndTime()));
        }
    }

    public Attempt startAttempt(Quiz quiz, User user) {
        Attempt attempt = new Attempt();
        attempt.setQuiz(quiz);
        attempt.setUser(user);
        attempt.setFinished(false);
        attempt.setStartTime();

        return this.save(attempt);
    }
}
