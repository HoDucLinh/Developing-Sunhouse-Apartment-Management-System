package linh.sunhouse_apartment.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponse {
    private Integer id;
    private String content;
    private String type; // "SHORT_ANSWER" hoặc "MULTIPLE_CHOICE"
    private List<QuestionOptionResponse> options; // null nếu short answer
}
