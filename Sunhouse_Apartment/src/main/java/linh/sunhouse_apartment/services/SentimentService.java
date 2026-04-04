package linh.sunhouse_apartment.services;

import linh.sunhouse_apartment.dtos.response.SentimentResponse;

import java.util.List;

public interface SentimentService {
    List<SentimentResponse.Result> analyzeTexts(List<String> texts);
}
