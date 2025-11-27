package linh.sunhouse_apartment.services.impl;

import linh.sunhouse_apartment.dtos.request.AppointmentRequest;
import linh.sunhouse_apartment.entity.Appointment;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.repositories.AppointmentRepository;
import linh.sunhouse_apartment.repositories.UserRepository;
import linh.sunhouse_apartment.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

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
    public List<Appointment> getAppointments(Map<String, String> params) {
        return appointmentRepository.getAppointments(params);
    }

    @Override
    public boolean updateStatus(int appointmentId, Appointment.AppointmentStatus status, int bodId) {
        Appointment appointment = appointmentRepository.getAppointmentById(appointmentId);
        if (appointment == null)
            return false;

        appointment.setStatus(status);

        if (status == Appointment.AppointmentStatus.APPROVED || status == Appointment.AppointmentStatus.REJECTED) {
            User bod = userRepository.getUserById(bodId);
            appointment.setBod(bod);
        }

        return appointmentRepository.updateAppointment(appointment);
    }
}
