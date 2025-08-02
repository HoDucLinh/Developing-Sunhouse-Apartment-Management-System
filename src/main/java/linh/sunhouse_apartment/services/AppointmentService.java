package linh.sunhouse_apartment.services;

import linh.sunhouse_apartment.dtos.request.AppointmentRequest;
import linh.sunhouse_apartment.entity.Appointment;

import java.util.List;

public interface AppointmentService {
    Appointment addAppointment(AppointmentRequest appointmentRequest);
    List<Appointment> getAppointments();
}
