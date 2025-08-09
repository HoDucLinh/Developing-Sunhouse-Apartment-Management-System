package linh.sunhouse_apartment.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurveyResponse {
    private Integer id;
    private String title;
    private String description;
    private Date createdAt;
    private String type;
}
