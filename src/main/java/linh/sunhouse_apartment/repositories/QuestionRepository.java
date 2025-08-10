package linh.sunhouse_apartment.repositories;

import linh.sunhouse_apartment.entity.Question;
import linh.sunhouse_apartment.entity.QuestionOption;

import java.util.List;

public interface QuestionRepository {
    void save(Question question);
    List<Question> findQuestionsBySurveyId(int surveyId);
    Question findById(int id);
    QuestionOption findQuestionOptionById(Integer id);
}
