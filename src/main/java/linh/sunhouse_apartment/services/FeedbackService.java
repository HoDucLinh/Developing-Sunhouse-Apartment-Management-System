package linh.sunhouse_apartment.services;

import linh.sunhouse_apartment.dtos.request.FeedbackRequest;
import linh.sunhouse_apartment.entity.Feedback;

import java.util.List;
import java.util.Map;

public interface FeedbackService {
    Feedback createFeedback(FeedbackRequest feedbackRequest);
    List<Feedback> getFeedbackByUserId(Integer userId);
    List<Feedback> getAllFeedback(Map<String, String> params);
}
