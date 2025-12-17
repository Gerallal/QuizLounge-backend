package gerallal.quizloungebackend.controller.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {
    private Long questionId;
    private String questionText; //questionText = questionName?
    private String questionName;
    private String questionType; //questionType = typeOfQuestion
    private String typeOfQuestion;
    private Long quizId;
    private List<AnswerDTO> answers;
}
