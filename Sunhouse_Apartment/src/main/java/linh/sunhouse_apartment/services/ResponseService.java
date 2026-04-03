package linh.sunhouse_apartment.services;

import linh.sunhouse_apartment.dtos.request.AnswerRequest;
import linh.sunhouse_apartment.dtos.response.QuestionResponseResponse;
import linh.sunhouse_apartment.entity.Question;
import linh.sunhouse_apartment.entity.Response;

import java.util.List;
import java.util.Map;

public interface ResponseService {
    void saveResponses(Integer surveyId, Integer userId, List<AnswerRequest> answers);
    List<Response> getResponses(Integer questionId);
    List<QuestionResponseResponse> getResponsesBySurveyId(Integer surveyId);
}
