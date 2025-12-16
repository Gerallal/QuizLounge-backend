package gerallal.quizloungebackend.controller.api.model;

import gerallal.quizloungebackend.entity.Question;
import gerallal.quizloungebackend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizCreateDTO {
    private long id;
    String title;
    String description;
    String category;
}
