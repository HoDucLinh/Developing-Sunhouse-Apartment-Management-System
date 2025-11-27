package linh.sunhouse_apartment.services.impl;

import linh.sunhouse_apartment.dtos.request.AnswerRequest;
import linh.sunhouse_apartment.entity.Question;
import linh.sunhouse_apartment.entity.QuestionOption;
import linh.sunhouse_apartment.entity.Response;
import linh.sunhouse_apartment.entity.Survey;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.repositories.QuestionRepository;
import linh.sunhouse_apartment.repositories.ResponseRepository;
import linh.sunhouse_apartment.repositories.SurveyRepository;
import linh.sunhouse_apartment.repositories.UserRepository;
import linh.sunhouse_apartment.services.ResponseService;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResponseServiceImpl implements ResponseService {
    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveResponses(Integer surveyId, Integer userId, List<AnswerRequest> answers) {
        // Lấy survey
        Survey survey = surveyRepository.findById(surveyId);
        if (survey == null) {
            throw new IllegalArgumentException("Survey không tồn tại");
        }

        // Lấy user
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User không tồn tại");
        }

        // Lấy danh sách câu hỏi của survey
        List<Question> questions = questionRepository.findQuestionsBySurveyId(surveyId);
        if (questions.isEmpty()) {
            throw new IllegalArgumentException("Survey này chưa có câu hỏi");
        }

        // Validate: phải gửi đủ câu trả lời
        if (answers == null || answers.size() != questions.size()) {
            throw new IllegalArgumentException("Bạn phải trả lời tất cả các câu hỏi trong khảo sát");
        }

        // Map câu trả lời để dễ tìm kiếm
        Map<Integer, AnswerRequest> answerMap = answers.stream()
                .collect(Collectors.toMap(AnswerRequest::getQuestionId, a -> a));

        // Lặp qua từng câu hỏi để kiểm tra và lưu
        for (Question question : questions) {
            AnswerRequest answerReq = answerMap.get(question.getId());
            if (answerReq == null) {
                throw new IllegalArgumentException("Thiếu câu trả lời cho câu hỏi ID: " + question.getId());
            }

            Response response = new Response();
            response.setQuestionId(question);
            response.setUserId(user);
            response.setCreatedAt(new Date());

            if (survey.getType() == Survey.SurveyType.MULTIPLE_CHOICE) {
                // Câu hỏi trắc nghiệm -> phải có optionId
                if (answerReq.getOptionId() == null) {
                    throw new IllegalArgumentException("Câu hỏi ID " + question.getId() + " yêu cầu chọn đáp án");
                }
                QuestionOption option = questionRepository.findQuestionOptionById(answerReq.getOptionId());
                if (option == null || !option.getQuestionId().getId().equals(question.getId())) {
                    throw new IllegalArgumentException("Đáp án không hợp lệ cho câu hỏi ID " + question.getId());
                }
                response.setOptionId(option);
            } else {
                // Câu hỏi tự luận -> phải có answer text
                if (answerReq.getAnswer() == null || answerReq.getAnswer().trim().isEmpty()) {
                    throw new IllegalArgumentException("Câu hỏi ID " + question.getId() + " yêu cầu nhập câu trả lời");
                }
                response.setAnswer(answerReq.getAnswer().trim());
            }

            // Lưu response
            responseRepository.save(response);
        }
    }

}
