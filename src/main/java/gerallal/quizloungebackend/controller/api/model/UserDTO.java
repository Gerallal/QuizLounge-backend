package gerallal.quizloungebackend.controller.api.model;

import gerallal.quizloungebackend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserDTO {
    private String username;
    private long id;
}
