package linh.sunhouse_apartment.repositories;

import linh.sunhouse_apartment.entity.Feedback;

import java.util.List;
import java.util.Map;

public interface FeedBackRepository {
    Feedback createFeedback(Feedback feedback);
    List<Feedback> findAllFeedback(Map<String, String> params);
    List<Feedback> findAllFeedbackByUserId(Integer userId);
    boolean deleteFeedbackById(Integer id);
    Feedback updateFeedback(Feedback feedback);
}
