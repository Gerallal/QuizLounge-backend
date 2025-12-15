package gerallal.quizloungebackend.controller.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttemptResultDTO {
    private Long attemptId;
    private Long quizId;
    private boolean finished;
    private float score;
    private int numberOfRightAnswers;
    private int totalQuestions;
    private String scoreInPercent;
}
