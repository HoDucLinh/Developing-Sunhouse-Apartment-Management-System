package linh.sunhouse_apartment.repositories;

import linh.sunhouse_apartment.entity.Appointment;

import java.util.List;
import java.util.Map;

public interface AppointmentRepository {
    Appointment addAppointment(Appointment appointment);
    List<Appointment> getAppointments(Map<String, String> params);
    Appointment getAppointmentById(int id);
    boolean updateAppointment(Appointment appointment);
}
