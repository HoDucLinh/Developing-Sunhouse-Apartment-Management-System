package linh.sunhouse_apartment.services;

import linh.sunhouse_apartment.dtos.request.AnswerRequest;

import java.util.List;

public interface ResponseService {
    void saveResponses(Integer surveyId, Integer userId, List<AnswerRequest> answers);
}
