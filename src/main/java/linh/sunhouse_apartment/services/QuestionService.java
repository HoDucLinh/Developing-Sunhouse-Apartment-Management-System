package linh.sunhouse_apartment.services;

import linh.sunhouse_apartment.entity.Question;

import java.util.List;

public interface QuestionService {
    boolean addQuestion(Question question, int surveyId, List<String> options);
}
