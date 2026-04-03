package linh.sunhouse_apartment.dtos.response;

import linh.sunhouse_apartment.entity.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponseResponse {
    private Integer questionId;
    private String questionContent;
    private List<Response> responses;
}
