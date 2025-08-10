package linh.sunhouse_apartment.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitSurveyRequest {
    private Integer surveyId;
    private List<AnswerRequest> answers;
}
