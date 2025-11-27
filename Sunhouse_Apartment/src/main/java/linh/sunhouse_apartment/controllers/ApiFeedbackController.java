package linh.sunhouse_apartment.controllers;

import linh.sunhouse_apartment.dtos.request.FeedbackRequest;
import linh.sunhouse_apartment.entity.Feedback;
import linh.sunhouse_apartment.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "*")
public class ApiFeedbackController {

    @Autowired
    FeedbackService feedbackService;

    @PostMapping("/create-feedback")
    public ResponseEntity<?> createFeedback(@RequestBody FeedbackRequest feedbackRequest) {
        try {
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
    public ResponseEntity<?> getFeedback(
            @PathVariable Integer id,
            @RequestParam(required = false) String kw) {
        try {
            Map<String, String> params = new HashMap<>();
            if (kw != null && !kw.isEmpty()) {
                params.put("kw", kw);
            }

            List<Feedback> feedbacks = feedbackService.getFeedbackByUserId(id, params);
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Không thể lấy phản hồi: " + e.getMessage());
        }
    }
    @DeleteMapping("/delete-feedback/{id}")
    public ResponseEntity<?> deleteFeedback(@PathVariable Integer id) {
        try {
            if (feedbackService.deleteFeedback(id)) {
                return ResponseEntity.ok("Đã xóa feedback");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy feedback");
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Lỗi server: " + e.getMessage()));
        }
    }
    @PutMapping("/update-feedback/{id}")
    public ResponseEntity<?> updateFeedback(@PathVariable Integer id, @RequestBody FeedbackRequest request) {
        try {
            Feedback updated = feedbackService.updateFeedback(id, request);
            if (updated != null) {
                return ResponseEntity.ok(updated);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy feedback để cập nhật");
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Lỗi server: " + e.getMessage()));
        }
    }

}
