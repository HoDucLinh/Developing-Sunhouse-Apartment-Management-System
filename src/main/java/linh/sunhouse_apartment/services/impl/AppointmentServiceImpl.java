package linh.sunhouse_apartment.services.impl;

import linh.sunhouse_apartment.dtos.request.AppointmentRequest;
import linh.sunhouse_apartment.entity.Appointment;
import linh.sunhouse_apartment.repositories.AppointmentRepository;
import linh.sunhouse_apartment.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Override
    public Appointment addAppointment(AppointmentRequest appointmentRequest) {
        if(appointmentRequest != null){
            Appointment appointment = new Appointment();
            appointment.setFullName(appointmentRequest.getFullName());
            appointment.setEmail(appointmentRequest.getEmail());
            appointment.setPhone(appointmentRequest.getPhone());
            appointment.setAppointmentTime(appointmentRequest.getAppointmentTime());
            appointment.setNote(appointmentRequest.getNote());
            appointment.setStatus(Appointment.AppointmentStatus.PENDING);
            appointment.setCreatedAt(LocalDateTime.now());
            appointment.setBod(null);
            return appointmentRepository.addAppointment(appointment);
        }
        return null;
    }

    @Override
    public List<Appointment> getAppointments() {
        return List.of();
    }
}
