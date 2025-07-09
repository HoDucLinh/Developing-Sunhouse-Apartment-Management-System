package linh.sunhouse_apartment.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LockerController {

    @GetMapping("/manage-locker")
    public String manageLocker(){
        return "manageLocker";
    }
}
