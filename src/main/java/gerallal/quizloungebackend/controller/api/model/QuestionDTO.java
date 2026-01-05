package gerallal.quizloungebackend.controller.api.model;

import gerallal.quizloungebackend.entity.QuestionType;
import lombok.*;

import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionDTO {
    Long questionId;
    String questionText;
    QuestionType questionType;
    Long quizId;
    List<AnswerDTO> answers;
}
