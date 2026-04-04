package linh.sunhouse_apartment.controllers;

import linh.sunhouse_apartment.dtos.request.SentimentRequest;
import linh.sunhouse_apartment.dtos.response.QuestionResponseResponse;
import linh.sunhouse_apartment.dtos.response.SentimentResponse;
import linh.sunhouse_apartment.entity.Question;
import linh.sunhouse_apartment.entity.Response;
import linh.sunhouse_apartment.entity.Survey;
import linh.sunhouse_apartment.repositories.ResponseRepository;
import linh.sunhouse_apartment.services.ResponseService;
import linh.sunhouse_apartment.services.SentimentService;
import linh.sunhouse_apartment.services.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@Controller
public class ResponseController {
    @Autowired
    private ResponseService responseService;

    @Autowired
    private SurveyService surveyService;

    @GetMapping("/get-responses/{surveyId}")
    public String manageResponses(@PathVariable Integer surveyId, Model model) {

        Survey survey = surveyService.getSurveyById(surveyId);

        List<QuestionResponseResponse> data = responseService.getResponsesBySurveyId(surveyId);

        int totalResponses = data.stream()
                .mapToInt(d -> d.getResponses().size())
                .sum();

        model.addAttribute("survey", survey);
        model.addAttribute("surveyTitle", survey.getTitle());
        model.addAttribute("surveyType",
                survey.getType() == Survey.SurveyType.MULTIPLE_CHOICE ? "Trắc nghiệm" : "Tự luận");
        model.addAttribute("totalResponses", totalResponses);
        model.addAttribute("totalQuestions", data.size());
        model.addAttribute("data", data);

        return "manageResponse";
    }
}
