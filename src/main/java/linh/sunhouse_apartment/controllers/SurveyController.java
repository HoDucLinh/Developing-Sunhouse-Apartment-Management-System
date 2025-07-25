package linh.sunhouse_apartment.controllers;

import jakarta.servlet.http.HttpSession;
import linh.sunhouse_apartment.auth.CustomUserDetail;
import linh.sunhouse_apartment.entity.Question;
import linh.sunhouse_apartment.entity.Survey;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.repositories.SurveyRepository;
import linh.sunhouse_apartment.services.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class SurveyController {
    @Autowired
    private SurveyService surveyService;

    @Autowired
    private HttpSession session;

    @GetMapping("/manage-survey")
    public String manageSurvey(@RequestParam(value = "title", required = false) String title,
                               Model model) {
        List<Survey> surveys = surveyService.findAllSurvey(title);
        model.addAttribute("surveys", surveys);
        model.addAttribute("title", title);
        return "manageSurvey";
    }

    @GetMapping("/create-survey")
    public String showCreateForm(Model model) {
        model.addAttribute("survey", new Survey());
        return "createSurvey";
    }

    @PostMapping("/create-survey")
    public String createSurvey(@ModelAttribute("survey") Survey survey,
                               BindingResult result,
                               @AuthenticationPrincipal CustomUserDetail userDetail,
                               RedirectAttributes redirectAttributes) {
        if (userDetail == null) {
            throw new RuntimeException("Bạn cần đăng nhập");
        }

        User currentUser = userDetail.getUser();

        try {
            surveyService.createSurvey(survey, currentUser);
            redirectAttributes.addFlashAttribute("message", "Tạo khảo sát thành công!");
            return "redirect:/manage-survey";
        } catch (SecurityException e) {
            result.reject("error", e.getMessage());
        }

        return "createSurvey";
    }
    @GetMapping("/surveys/view")
    public String viewSurvey(@RequestParam("id") int id, Model model) {
        Survey survey = surveyService.getSurveyById(id);
        model.addAttribute("survey", survey);
        model.addAttribute("question", new Question());
        return "surveyDetail"; // Tên template hiển thị form thêm câu hỏi
    }


}
