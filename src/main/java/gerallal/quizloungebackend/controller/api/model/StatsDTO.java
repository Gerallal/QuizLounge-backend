package gerallal.quizloungebackend.controller.api.model;

import lombok.*;

import java.time.LocalDateTime;

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
    private String quizTitle;
    private LocalDateTime finishedAt;

}
