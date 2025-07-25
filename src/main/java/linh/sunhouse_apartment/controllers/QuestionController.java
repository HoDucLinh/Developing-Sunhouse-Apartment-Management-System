package linh.sunhouse_apartment.controllers;

import linh.sunhouse_apartment.entity.Question;
import linh.sunhouse_apartment.entity.QuestionOption;
import linh.sunhouse_apartment.entity.Survey;
import linh.sunhouse_apartment.services.QuestionService;
import linh.sunhouse_apartment.services.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("/surveys/add-question")
    public String addQuestion(@ModelAttribute Question question,
                              @RequestParam("surveyIdValue") int surveyId,
                              @RequestParam(value = "options", required = false) List<String> options,
                              RedirectAttributes model) {
        boolean success = questionService.addQuestion(question, surveyId, options);
        if (success)
            model.addFlashAttribute("message", "Thêm câu hỏi thành công!");
        else
            model.addFlashAttribute("error", "Không thể thêm câu hỏi. Vui lòng kiểm tra lại.");

        return "redirect:/surveys/view?id=" + surveyId;
    }

}
