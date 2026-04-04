package linh.sunhouse_apartment.services.impl;

import linh.sunhouse_apartment.dtos.request.SentimentRequest;
import linh.sunhouse_apartment.dtos.response.SentimentResponse;
import linh.sunhouse_apartment.services.SentimentService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class SentimentServiceImpl implements SentimentService {
    private final String API_URL = "http://localhost:5000/predict";
    @Override
    public List<SentimentResponse.Result> analyzeTexts(List<String> texts) {
        RestTemplate restTemplate = new RestTemplate();

        SentimentRequest requestBody = new SentimentRequest();
        requestBody.setTexts(texts);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SentimentRequest> request =
                new HttpEntity<>(requestBody, headers);

        ResponseEntity<SentimentResponse> response =
                restTemplate.postForEntity(API_URL, request, SentimentResponse.class);

        return response.getBody().getResults();
    }
}
