package gerallal.quizloungebackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gerallal.quizloungebackend.controller.api.model.QuizCreateQADTO;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String passwordHash;
    @ManyToMany
    private List<User> friends;
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendRequest> friendRequests;
    @ManyToMany
    private List<Quiz> quizzes;
    @OneToMany
    private List<Quiz> receivedQuizzes;

}
