package linh.sunhouse_apartment.repositories;

import linh.sunhouse_apartment.entity.Survey;

import java.util.List;

public interface SurveyRepository {
    void save(Survey survey);
    Survey findById(int id);
    List<Survey> findAll(String title);
}
