package gerallal.quizloungebackend.controller.api.model;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizCreateQADTO {
    private long id;
    private UserDTO author;
    private String title;
    private String description;
    private String category;
    List<QuestionDTO> questions;
}
