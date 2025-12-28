package gerallal.quizloungebackend.controller.api.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class EvaluationDTO {
    private String question;
    private String answer;
}
