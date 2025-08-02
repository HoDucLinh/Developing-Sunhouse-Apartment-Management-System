package linh.sunhouse_apartment.controllers;


import linh.sunhouse_apartment.entity.Appointment;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.services.AppointmentService;
import linh.sunhouse_apartment.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserService userService;

    @GetMapping("/manage-appointment")
    public String manageAppointment(
            @RequestParam(required = false) String kw,
            @RequestParam(required = false) String email,
            Model model
    ) {
        Map<String, String> params = new HashMap<>();
        if (kw != null && !kw.isEmpty()) {
            params.put("kw", kw);
        }
        if (email != null && !email.isEmpty()) {
            params.put("email", email);
        }

        List<Appointment> appointments = appointmentService.getAppointments(params);
        model.addAttribute("appointments", appointments);
        model.addAttribute("kw", kw);
        model.addAttribute("email", email);

        return "manageAppointment";
    }
    @PostMapping("/appointments/update-status")
    public String updateAppointmentStatus(
            @RequestParam("appointmentId") int appointmentId,
            @RequestParam("status") String statusStr,
            Principal principal,
            Model model
    ) {
        try {
            Appointment.AppointmentStatus status = Appointment.AppointmentStatus.valueOf(statusStr.toUpperCase());

            // Lấy user hiện tại theo email đăng nhập
            String username = principal.getName();
            User bod = userService.getUserByUsername(username);  // Cần viết hàm này trong UserService

            boolean updated = appointmentService.updateStatus(appointmentId, status, bod.getId());

            if (!updated) {
                model.addAttribute("errorMessage", "Không tìm thấy cuộc hẹn hoặc cập nhật thất bại");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
        }

        return "redirect:/manage-appointment";
    }

}
