package gerallal.quizloungebackend.controller.api.model;

import gerallal.quizloungebackend.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerDTO {
    Long answerId;
    String answerText;
    boolean correct;
}
