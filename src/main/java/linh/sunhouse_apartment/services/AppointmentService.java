package linh.sunhouse_apartment.services;

import linh.sunhouse_apartment.dtos.request.AppointmentRequest;
import linh.sunhouse_apartment.entity.Appointment;

import java.util.List;
import java.util.Map;

public interface AppointmentService {
    Appointment addAppointment(AppointmentRequest appointmentRequest);
    List<Appointment> getAppointments(Map<String, String> params);
    boolean updateStatus(int appointmentId, Appointment.AppointmentStatus status, int bodId);
}
