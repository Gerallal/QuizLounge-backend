package gerallal.quizloungebackend.controller.api.model;

import gerallal.quizloungebackend.entity.Quiz;
import gerallal.quizloungebackend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserDTO {
    private String username;
    private long id;
    private QuizCreateQADTO[] receivedQuizzes;

}
