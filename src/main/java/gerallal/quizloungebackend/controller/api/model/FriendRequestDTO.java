package gerallal.quizloungebackend.controller.api.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendRequestDTO {
    private Long id;
    private String sender;
}
