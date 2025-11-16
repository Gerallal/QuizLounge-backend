package gerallal.quizloungebackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long id;
    private String questionName;
    private String typeOfQuestion; //bestimmt wie die Frage angezeigt und bewertet wird

//    private String answer1;
//    private String answer2;
//    private String answer3;
//    private String answer4;
//    private String rightAnswer;

    @OneToMany
    private List<Answer> answers;

    @ManyToOne
    private Quiz quiz;
}
