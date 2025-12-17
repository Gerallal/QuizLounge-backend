package gerallal.quizloungebackend.controller.api.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizCreateAnswerDTO {
    private String successMessage;
    private Long quizId;
}
