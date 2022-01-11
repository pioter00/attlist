package com.root.attendancelist.service;

import com.root.attendancelist.model.Attendance;
import com.root.attendancelist.model.User;
import com.root.attendancelist.repository.AttendanceRepository;
import com.root.attendancelist.repository.UserRepository;
import com.root.attendancelist.service.interfaces.AttendanceServiceInterface;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceService implements AttendanceServiceInterface {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    public AttendanceService(AttendanceRepository attendanceRepository, UserRepository userRepository) {
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Attendance> findAllAttendances() {
        return attendanceRepository.findAll().stream()
                .sorted(Comparator.comparing(Attendance::getAttendanceFrom).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Attendance> findAllAttendancesById(Long id) {
        return attendanceRepository.findAll().stream()
                .filter(attendance -> attendance.getStudentID().equals(id))
                .sorted(Comparator.comparing(Attendance::getAttendanceFrom).reversed())
                .toList();
    }

    @Override
    public Optional<Attendance> findById(Long id) {
        return attendanceRepository.findById(id);
    }

    @Override
    public List<Attendance> saveAttendance(Attendance attendance, Long groupID) {
        System.out.println("id" + groupID);
        List<Long> usersID = userRepository.findAll().stream()
                .filter(user -> user.getGroupID() != null)
                .filter(user -> user.getGroupID().equals(groupID))
                .map(User::getID).toList();

        List<Attendance> attendances = new ArrayList<>();
        for (var userID: usersID) {
            attendances.add(new Attendance(attendance.getID(), userID,
                    attendance.getAttendanceFrom(), attendance.getAttendanceTo(), attendance.getAttendanceStatus(), attendance.getNameOfSubject()));
        }
        System.out.println(attendances);
        attendanceRepository.saveAll(attendances);
        return attendances;
    }

    @Override
    public Attendance updateStudentAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    @Override
    public void deleteAttendance(Long groupID) {
        List<Long> users = userRepository.findAll().stream()
                .filter(user -> user.getGroupID() != null)
                .filter(user -> user.getGroupID().equals(groupID))
                .map(User::getID).toList();
        List<Long> toDelete = attendanceRepository.findAll().stream()
                .filter(attendance -> users.contains(attendance.getStudentID()))
                .map(Attendance::getID).toList();
        for (var id: toDelete) {
            attendanceRepository.deleteById(id);
        }
    }
    @Override
    public void deleteAttendanceByTime(Attendance attendance) {
        List<Attendance> attendances = attendanceRepository.findAll().stream()
                        .filter(el -> el.getAttendanceTo().equals(attendance.getAttendanceTo()) &&
                                el.getAttendanceFrom().equals(attendance.getAttendanceFrom()) &&
                                el.getNameOfSubject().equals(attendance.getNameOfSubject())
                        ).toList();
        attendanceRepository.deleteAll(attendances);
    }
}
