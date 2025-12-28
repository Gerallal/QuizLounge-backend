package gerallal.quizloungebackend.controller.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingDTO {
    private int rating;
    private Long authorId;
    private Long quizId;
    private double averageRating;
}
