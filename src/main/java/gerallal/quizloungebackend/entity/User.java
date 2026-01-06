package gerallal.quizloungebackend.entity;

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
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendRequest> senderFriendRequests;
    @ManyToMany
    private List<Quiz> quizzes;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quiz> receivedQuizzes;
}
