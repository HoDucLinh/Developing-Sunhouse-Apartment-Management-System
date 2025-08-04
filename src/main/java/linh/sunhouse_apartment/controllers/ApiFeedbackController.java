package linh.sunhouse_apartment.controllers;

import linh.sunhouse_apartment.dtos.request.FeedbackRequest;
import linh.sunhouse_apartment.entity.Feedback;
import linh.sunhouse_apartment.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "*")
public class ApiFeedbackController {

    @Autowired
    FeedbackService feedbackService;

    @PostMapping("/create-feedback")
    public ResponseEntity<?> createFeedback(@RequestParam String content, @RequestParam Integer userId) {
        try {
            FeedbackRequest feedbackRequest = new FeedbackRequest(content, userId);
            Feedback f = feedbackService.createFeedback(feedbackRequest);

            if (f == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Không thể tạo feedback");
            }

            return ResponseEntity.ok(f);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi tạo feedback: " + e.getMessage());
        }
    }
    @GetMapping("/get-feedback/{id}")
    public ResponseEntity<?> getFeedback(@PathVariable Integer id) {
        try {
            List<Feedback> feedbacks = feedbackService.getFeedbackByUserId(id);
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Không thể lấy phản hồi: " + e.getMessage());
        }
    }

}
