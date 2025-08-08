package linh.sunhouse_apartment.controllers;

import linh.sunhouse_apartment.entity.Feedback;
import linh.sunhouse_apartment.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
public class FeedBackController {

    @Autowired
    FeedbackService feedbackService;

    @GetMapping("/manage-feedback")
    public String manageFeedBack(Model model, @RequestParam Map<String,String> params){
        List<Feedback> feedbackList = feedbackService.getAllFeedback(params);
        model.addAttribute("feedbackList",feedbackList);
        model.addAttribute("params",params);
        return "manageFeedback";
    }

    @PostMapping("/change-status")
    public String changeStatus(@RequestParam("id") int feedbackId,
                               @RequestParam("status") Feedback.FeedbackStatus status,
                               RedirectAttributes redirectAttributes) {
        feedbackService.updateStatus(feedbackId, status);
        redirectAttributes.addFlashAttribute("message", "Đã cập nhật trạng thái phản ánh.");
        return "redirect:/manage-feedback";
    }

}
