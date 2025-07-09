package linh.sunhouse_apartment.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CardController {

    @GetMapping("/manage-card")
    public String manageCard(){
        return "manageCard";
    }
}
