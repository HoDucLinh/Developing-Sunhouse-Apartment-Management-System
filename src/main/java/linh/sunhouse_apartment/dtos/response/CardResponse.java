package linh.sunhouse_apartment.dtos.response;

import linh.sunhouse_apartment.entity.Relative;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardResponse {
    private Integer id;
    private Date issueDate;
    private Date expirationDate;
    private String status;
    private Integer userId;
    private Integer relativeId;
    private String relativeName;
    private Relative.EnumRelationship relationship;
}
