package linh.sunhouse_apartment.services.impl;

import linh.sunhouse_apartment.entity.Survey;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.repositories.SurveyRepository;
import linh.sunhouse_apartment.repositories.UserRepository;
import linh.sunhouse_apartment.services.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SurveyServiceImpl implements SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;

    @Override
    public void createSurvey(Survey survey, User currentUser) {
        if (survey != null) {
            if (currentUser != null && currentUser.getRole() == User.Role.BOD) {
                survey.setCreatedAt(new Date());
                survey.setAdminId(currentUser);
                surveyRepository.save(survey);
            } else {
                throw new SecurityException("Bạn không có quyền tạo khảo sát");
            }
        }
    }

    @Override
    public Survey getSurveyById(int id) {
        return surveyRepository.findById(id);
    }

    @Override
    public List<Survey> findAllSurvey(String title) {
        return surveyRepository.findAll(title);
    }
}
