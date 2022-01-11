package com.root.attendancelist.service.interfaces;

import com.root.attendancelist.model.Attendance;

import java.util.List;
import java.util.Optional;

public interface AttendanceServiceInterface {
    List<Attendance> findAllAttendances();
    List<Attendance> findAllAttendancesById(Long id);
    Optional<Attendance> findById(Long id);
    List<Attendance> saveAttendance(Attendance attendance, Long groupID);
    Attendance updateStudentAttendance(Attendance attendance);
    void deleteAttendance(Long groupID);
    void deleteAttendanceByTime(Attendance attendance);
}
