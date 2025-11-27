package linh.sunhouse_apartment.controllers;


import linh.sunhouse_apartment.dtos.request.AppointmentRequest;
import linh.sunhouse_apartment.entity.Appointment;
import linh.sunhouse_apartment.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/appointment")
@CrossOrigin(origins = "*")
public class ApiAppointmentController {
    @Autowired
    AppointmentService appointmentService;

    @PostMapping("/create-appointment")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentRequest appointmentRequest) {
        try {
            Appointment appointment = appointmentService.addAppointment(appointmentRequest);
            if (appointment != null) {
                return ResponseEntity.ok(appointment);
            } else {
                return ResponseEntity.badRequest().body("Tạo lịch hẹn thất bại!");
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Lỗi server: " + e.getMessage()));
        }
    }
}
