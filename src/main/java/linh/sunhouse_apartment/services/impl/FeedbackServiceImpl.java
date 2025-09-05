package linh.sunhouse_apartment.services.impl;

import linh.sunhouse_apartment.dtos.request.FeedbackRequest;
import linh.sunhouse_apartment.entity.Feedback;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.repositories.FeedBackRepository;
import linh.sunhouse_apartment.repositories.UserRepository;
import linh.sunhouse_apartment.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    FeedBackRepository feedBackRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public Feedback createFeedback(FeedbackRequest feedbackRequest) {
        User user = userRepository.getUserById(feedbackRequest.getUserId());
        if(user==null){
            return null;
        }
        Feedback feedback = new Feedback();
        feedback.setContent(feedbackRequest.getContent());
        feedback.setUserId(user);
        feedback.setStatus(Feedback.FeedbackStatus.PENDING);
        feedback.setCreatedAt(new Date());
        return feedBackRepository.createFeedback(feedback);
    }

    @Override
    public List<Feedback> getFeedbackByUserId(Integer userId, Map<String, String> params) {
        User user = userRepository.getUserById(userId);
        if(user == null){
            throw new RuntimeException("Không tìm thấy người dùng");
        }
        return feedBackRepository.findAllFeedbackByUserId(userId, params);
    }

    @Override
    public List<Feedback> getAllFeedback(Map<String, String> params) {
        return feedBackRepository.findAllFeedback(params);
    }
    @Override
    public boolean deleteFeedback(Integer id) {
        return feedBackRepository.deleteFeedbackById(id);
    }

    @Override
    public Feedback updateFeedback(Integer id, FeedbackRequest request) {
        Feedback feedback = feedBackRepository.updateFeedback(new Feedback() {{
            setId(id);
            setContent(request.getContent());
        }});
        return feedback;
    }

    @Override
    public void updateStatus(int feedbackId, Feedback.FeedbackStatus status, User handler) {
        Feedback feedback = feedBackRepository.findFeedbackById(feedbackId);
        if (feedback != null) {
            feedback.setStatus(status);// Cập nhật status mới từ người dùng
            feedback.setHandlerId(handler);
            feedBackRepository.update(feedback);
        }
    }
}
