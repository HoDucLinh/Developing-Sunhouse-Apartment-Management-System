package linh.sunhouse_apartment.services.impl;

import linh.sunhouse_apartment.entity.Question;
import linh.sunhouse_apartment.entity.QuestionOption;
import linh.sunhouse_apartment.entity.Survey;
import linh.sunhouse_apartment.repositories.QuestionRepository;
import linh.sunhouse_apartment.repositories.SurveyRepository;
import linh.sunhouse_apartment.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private SurveyRepository surveyRepository;

    @Override
    public boolean addQuestion(Question question, int surveyId, List<String> options) {
        Survey survey = surveyRepository.findById(surveyId);
        question.setSurveyId(survey);

        if (survey.getType() == Survey.SurveyType.MULTIPLE_CHOICE) {
            if (options == null || options.stream().allMatch(String::isBlank)) {
                return false;
            }

            List<QuestionOption> optionList = new ArrayList<>();
            for (String opt : options) {
                if (!opt.trim().isEmpty()) {
                    QuestionOption option = new QuestionOption();
                    option.setContent(opt.trim());
                    option.setQuestionId(question);
                    optionList.add(option);
                }
            }
            question.setQuestionOptionSet(optionList);
        }

        questionRepository.save(question);
        return true;
    }
}
