package linh.sunhouse_apartment.services.impl;

import linh.sunhouse_apartment.entity.DetailSurvey;
import linh.sunhouse_apartment.entity.Survey;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.repositories.DetailSurveyRepository;
import linh.sunhouse_apartment.repositories.SurveyRepository;
import linh.sunhouse_apartment.repositories.UserRepository;
import linh.sunhouse_apartment.services.DetailSurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DetailSurveyServiceImpl implements DetailSurveyService {

    @Autowired
    DetailSurveyRepository detailSurveyRepository;

    @Autowired
    SurveyRepository surveyRepository;

    @Autowired
    UserRepository userRepository;


    @Override
    public DetailSurvey save(Integer surveyId, Integer userId) {
        Survey survey = surveyRepository.findById(surveyId);
        User user = userRepository.getUserById(userId);
        if(survey == null || user == null){
            return null;
        }
        DetailSurvey detailSurvey = new DetailSurvey();
        detailSurvey.setSurvey(survey);
        detailSurvey.setUser(user);
        detailSurvey.setStatus(DetailSurvey.Status.COMPLETED);
        detailSurvey.setCompletedAt(new Date());
        return detailSurveyRepository.save(detailSurvey);
    }
}
