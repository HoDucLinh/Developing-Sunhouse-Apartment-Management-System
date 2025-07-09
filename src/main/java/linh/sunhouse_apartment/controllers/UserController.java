package linh.sunhouse_apartment.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/manage-user")
    public String manageUser(){
        return "manageUser";
    }
}
