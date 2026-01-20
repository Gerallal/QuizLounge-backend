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
public class QuizCreateDTO {
    private long id;
    private UserDTO author;
    private String title;
    private String description;
    private String category;
    private List<QuestionDTO> questions;
    private AttemptDTO attempt;
    private String successMessage;
    private Long quizId;

    public QuizCreateDTO(long id,
                           String title,
                           String description,
                           String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
    }

    public QuizCreateDTO(long id,
                         UserDTO author,
                         String title,
                         String description,
                         String category,
                         List<QuestionDTO> questions,
                         AttemptDTO attempt) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.description = description;
        this.category = category;
        this.questions = questions;
        this.attempt = attempt;
    }
}
