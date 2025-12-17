package gerallal.quizloungebackend.service;

import gerallal.quizloungebackend.controller.api.model.AnswerDTO;
import gerallal.quizloungebackend.entity.Answer;

public class AnswerService {

    public static Answer map(AnswerDTO answerDTO) {
        return Answer.builder()
                .answerName(answerDTO.getAnswerText())
                .correct(answerDTO.isCorrect())
                .build();
    }
}
