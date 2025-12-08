package gerallal.quizloungebackend.controller.api.model;

import gerallal.quizloungebackend.entity.Answer;
import gerallal.quizloungebackend.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizCreateQADTO {
    private long id;
    private String title;
    private String description;
    private String category;
    List<QuestionDTO> questions;
}
