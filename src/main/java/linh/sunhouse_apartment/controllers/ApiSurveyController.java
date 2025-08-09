package linh.sunhouse_apartment.controllers;


import linh.sunhouse_apartment.dtos.response.SurveyResponse;
import linh.sunhouse_apartment.entity.Survey;
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

    @GetMapping("/get-surveys")
    public ResponseEntity<?> getAllSurveys(@RequestParam(value = "title", required = false) String title) {
        try {
            List<Survey> surveys = surveyService.findAllSurvey(title);

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
}
