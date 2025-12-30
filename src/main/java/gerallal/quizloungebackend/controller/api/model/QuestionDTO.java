package gerallal.quizloungebackend.controller.api.model;

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
    String questionType;
    Long quizId;
    List<AnswerDTO> answers;
}
