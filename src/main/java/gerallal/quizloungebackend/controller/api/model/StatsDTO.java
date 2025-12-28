package gerallal.quizloungebackend.controller.api.model;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatsDTO {
    private long quizId;
    private String username;
    private int numberOfRightAnswers;
    private int totalQuestions;

}
