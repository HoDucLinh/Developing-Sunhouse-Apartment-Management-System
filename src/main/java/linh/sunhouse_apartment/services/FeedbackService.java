package linh.sunhouse_apartment.services;

import linh.sunhouse_apartment.dtos.request.FeedbackRequest;
import linh.sunhouse_apartment.entity.Feedback;
import linh.sunhouse_apartment.entity.User;

import java.util.List;
import java.util.Map;

public interface FeedbackService {
    Feedback createFeedback(FeedbackRequest feedbackRequest);
    List<Feedback> getFeedbackByUserId(Integer userId, Map<String, String> params);
    List<Feedback> getAllFeedback(Map<String, String> params);
    boolean deleteFeedback(Integer id);
    Feedback updateFeedback(Integer id, FeedbackRequest request);
    void updateStatus(int feedbackId, Feedback.FeedbackStatus status, User handler);
}
