package linh.sunhouse_apartment.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SentimentResponse {
    private List<Result> results;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Result {
        private String text;
        private String sentiment;
        private double confidence;
    }
}
