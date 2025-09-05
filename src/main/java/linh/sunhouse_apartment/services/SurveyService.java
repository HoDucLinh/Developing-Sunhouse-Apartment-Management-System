package linh.sunhouse_apartment.services;

import linh.sunhouse_apartment.entity.Survey;
import linh.sunhouse_apartment.entity.User;

import java.util.List;
import java.util.Map;

public interface SurveyService {
    void createSurvey(Survey survey, User currentUser);
    Survey getSurveyById(int id);
    List<Survey> findAllSurvey(Map<String, String> params);
    List<Survey> getSurveysNotCompletedByUser(int userId, String title);
}
