package linh.sunhouse_apartment.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppointmentController {

    @GetMapping("/manage-appointment")
    public String manageAppointment() {
        return "manageAppointment";
    }
}
