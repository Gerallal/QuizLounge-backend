package gerallal.quizloungebackend.service;


import gerallal.quizloungebackend.controller.api.model.QuestionDTO;
import gerallal.quizloungebackend.entity.Question;
import gerallal.quizloungebackend.entity.Quiz;
import gerallal.quizloungebackend.repository.AnswerRepository;
import gerallal.quizloungebackend.repository.QuestionRepository;
import gerallal.quizloungebackend.repository.QuizRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class QuestionService {
    private QuestionRepository questionRepository;
    private QuizRepository quizRepository;
    private AnswerRepository answerRepository;

    public void addQuestion(QuestionDTO questionDTO) {
        Quiz quiz = quizRepository.findById(questionDTO.getQuizId()).get();
        Question question = Question.builder()
                .quiz(quiz)
                .typeOfQuestion(questionDTO.getQuestionType())
                .answers(questionDTO.getAnswers().stream()
                        .map(AnswerService::map)
                        .toList())
                .questionName(questionDTO.getQuestionText())
                .build();
        question.getAnswers().forEach(answer -> answer.setQuestion(question));

        quiz.getQuestions().add(question);
        questionRepository.save(question);
        quizRepository.save(quiz);
        answerRepository.saveAll(question.getAnswers());

    }

    public int getNumberOfQuestions(Long quizId) {
        return questionRepository.countByQuizId(quizId);
    }
}
