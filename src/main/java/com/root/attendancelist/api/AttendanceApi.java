package com.root.attendancelist.api;

import com.root.attendancelist.model.Attendance;
import com.root.attendancelist.service.AttendanceService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/attendance")
public class AttendanceApi {
    private final AttendanceService attendanceService;

    public AttendanceApi(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }
    @GetMapping
    public List<Attendance> findAllUsers() {
        return attendanceService.findAllAttendances();
    }
    @GetMapping("/{id}")
    public Optional<Attendance> findAttendanceById(@PathVariable("id") Long id) {
        return attendanceService.findById(id);
    }

    @PostMapping("/{id}")
    public List<Attendance> saveAttendance(@Valid @RequestBody Attendance attendance, @PathVariable("id") Long id) {
        return attendanceService.saveAttendance(attendance, id);
    }
    @PutMapping("/{id}")
    public List<Attendance> updateAttendance(@Valid @RequestBody Attendance attendance, @PathVariable("id") Long id) {
        return attendanceService.saveAttendance(attendance, id);
    }
    @PutMapping
    public Attendance updateStudentAttendance(@Valid @RequestBody Attendance attendance) {
        return attendanceService.updateStudentAttendance(attendance);
    }
    @DeleteMapping("/{id}")
    public void deleteAttendance(@PathVariable("id") Long id) {
        attendanceService.deleteAttendance(id);
    }
    @DeleteMapping
    public void deleteAttendanceByTime(@RequestBody Attendance attendance) {
        attendanceService.deleteAttendanceByTime(attendance);
    }
}
