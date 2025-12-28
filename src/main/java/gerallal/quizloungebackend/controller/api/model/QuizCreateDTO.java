package gerallal.quizloungebackend.controller.api.model;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizCreateDTO {
    private long id;
    private String title;
    private String description;
    private String category;
}
