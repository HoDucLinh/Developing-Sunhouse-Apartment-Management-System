package linh.sunhouse_apartment.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FeedBackController {
    @GetMapping("/manage-feedback")
    public String manageFeedBack(){
        return "manageFeedback";
    }
}
