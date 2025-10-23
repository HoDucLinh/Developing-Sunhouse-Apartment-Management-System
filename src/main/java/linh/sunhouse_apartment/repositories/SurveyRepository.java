package linh.sunhouse_apartment.repositories;

import linh.sunhouse_apartment.entity.Survey;

import java.util.List;
import java.util.Map;

public interface SurveyRepository {
    void save(Survey survey);
    Survey findById(int id);
    List<Survey> findAll(Map<String, String> params);
    List<Survey> getSurveysNotCompletedByUser(int userId, String title);
    boolean deleteSurvey(Survey survey);
}
