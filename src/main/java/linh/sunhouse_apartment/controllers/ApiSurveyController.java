package linh.sunhouse_apartment.controllers;


import linh.sunhouse_apartment.dtos.request.AnswerRequest;
import linh.sunhouse_apartment.dtos.request.SubmitSurveyRequest;
import linh.sunhouse_apartment.dtos.response.QuestionOptionResponse;
import linh.sunhouse_apartment.dtos.response.QuestionResponse;
import linh.sunhouse_apartment.dtos.response.SurveyResponse;
import linh.sunhouse_apartment.entity.Question;
import linh.sunhouse_apartment.entity.Survey;
import linh.sunhouse_apartment.services.DetailSurveyService;
import linh.sunhouse_apartment.services.QuestionService;
import linh.sunhouse_apartment.services.ResponseService;
import linh.sunhouse_apartment.services.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/survey")
@CrossOrigin(origins = "*")
public class ApiSurveyController {

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ResponseService responseService;

    @Autowired
    private DetailSurveyService detailSurveyService;

    @GetMapping("/get-surveys")
    public ResponseEntity<?> getAllSurveys(@RequestParam(value = "title", required = false) String title, @RequestParam("userId") Integer userId) {
        try {
            List<Survey> surveys = surveyService.getSurveysNotCompletedByUser(userId,title);

            // Chuyển Survey -> SurveyResponse DTO
            List<SurveyResponse> responseList = surveys.stream()
                    .map(s -> new SurveyResponse(
                            s.getId(),
                            s.getTitle(),
                            s.getDescription(),
                            s.getCreatedAt(),
                            s.getType().name()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(responseList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi lấy danh sách khảo sát: " + e.getMessage());
        }
    }
    // API lấy câu hỏi theo surveyId
    @GetMapping("/{surveyId}/questions")
    public ResponseEntity<?> getQuestionsBySurveyId(@PathVariable int surveyId) {
        try {
            Survey survey = surveyService.getSurveyById(surveyId);
            if (survey == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Survey không tồn tại với id = " + surveyId);
            }

            List<Question> questions = questionService.getQuestionsBySurveyId(surveyId);

            // Chuyển đổi sang DTO
            List<QuestionResponse> questionResponses = questions.stream()
                    .map(q -> {
                        List<QuestionOptionResponse> options = null;
                        if (survey.getType() == Survey.SurveyType.MULTIPLE_CHOICE) {
                            if (q.getQuestionOptionSet() != null) {
                                options = q.getQuestionOptionSet().stream()
                                        .filter(opt -> opt != null && opt.getId() != null && opt.getContent() != null)
                                        .map(opt -> new QuestionOptionResponse(opt.getId(), opt.getContent()))
                                        .collect(Collectors.toList());
                            } else {
                                options = List.of();
                            }
                        }
                        return new QuestionResponse(
                                q.getId(),
                                q.getContent() != null ? q.getContent() : "",
                                survey.getType().name(),
                                options
                        );
                    }).collect(Collectors.toList());

            return ResponseEntity.ok(questionResponses);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi lấy câu hỏi của survey: " + e.toString());
        }
    }
    @PostMapping("/submit")
    public ResponseEntity<?> submitSurvey(@RequestBody SubmitSurveyRequest request,
                                          @RequestParam Integer userId) {
        try {
            responseService.saveResponses(request.getSurveyId(), userId, request.getAnswers());
            detailSurveyService.save(request.getSurveyId(), userId);
            return ResponseEntity.ok().body("Nộp khảo sát thành công");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Có lỗi xảy ra: " + e.getMessage());
        }
    }


}
