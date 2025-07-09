package linh.sunhouse_apartment.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SurveyController {

    @GetMapping("/manage-survey")
    public String manageSurvey(){
        return "manageSurvey";
    }
}
