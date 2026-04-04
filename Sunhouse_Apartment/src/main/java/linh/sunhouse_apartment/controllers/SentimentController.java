package linh.sunhouse_apartment.controllers;

import linh.sunhouse_apartment.dtos.request.SentimentRequest;
import linh.sunhouse_apartment.dtos.response.SentimentResponse;
import linh.sunhouse_apartment.services.SentimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SentimentController {
    @Autowired
    private SentimentService sentimentService;

    @PostMapping("/sentiment")
    public SentimentResponse analyze(@RequestBody SentimentRequest request) {

        List<SentimentResponse.Result> results =
                sentimentService.analyzeTexts(request.getTexts());

        SentimentResponse res = new SentimentResponse();
        res.setResults(results);

        return res;
    }
}
