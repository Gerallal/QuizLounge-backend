package gerallal.quizloungebackend.controller.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizCreateDTO {
    String title;
    String description;
    String category;
}
