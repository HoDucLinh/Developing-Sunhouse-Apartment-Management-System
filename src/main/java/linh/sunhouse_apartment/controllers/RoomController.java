package linh.sunhouse_apartment.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoomController {

    @GetMapping("/manage-room")
    public String manageRoom() {
        return "manageRoom";
    }
}
