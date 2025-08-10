package linh.sunhouse_apartment.dtos.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerRequest {
    private Integer questionId;
    private String answer;     // Dùng cho câu hỏi tự luận
    private Integer optionId;  // Dùng cho câu hỏi trắc nghiệm
}
