package linh.sunhouse_apartment.services;

import linh.sunhouse_apartment.entity.Survey;
import linh.sunhouse_apartment.entity.User;

import java.util.List;

public interface SurveyService {
    void createSurvey(Survey survey, User currentUser);
    Survey getSurveyById(int id);
    List<Survey> findAllSurvey(String title);
}
