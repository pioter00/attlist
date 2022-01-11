package com.root.attendancelist.api;

import com.root.attendancelist.model.Attendance;
import com.root.attendancelist.model.User;
import com.root.attendancelist.service.AttendanceService;
import com.root.attendancelist.service.GroupService;
import com.root.attendancelist.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class UserApi {
    private final UserService userService;
    private final AttendanceService attendanceService;
    private final GroupService groupService;
    public UserApi(UserService userService, AttendanceService attendanceService, GroupService groupService) {
        this.userService = userService;
        this.attendanceService = attendanceService;
        this.groupService = groupService;
    }

    @GetMapping
    public List<User> findAllUsers() {
        return userService.findAllUsers();
    }
    @GetMapping("/group/{id}")
    public List<User> findAllUsersByGroupId(@PathVariable("id") Long id) {
        return userService.findAllUsers()
                .stream()
                .filter(student -> Objects.equals(student.getGroupID(), id))
                .toList();
    }

    @GetMapping("attendance/{id}")
    public List<Attendance> findAllAttendancesById(@PathVariable Long id){
        return attendanceService.findAllAttendancesById(id);
    }
    @GetMapping("/{id}")
    public Optional<User> findUserById(@PathVariable("id") Long id) {
        return userService.findById(id);
    }


    @PostMapping
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user) {
        if (userService.findAllUsers().stream().anyMatch(user1 -> user1.getEmail().equals(user.getEmail())))
            return null;
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (user.getGroupID() == null)
            return userService.updateUser(user);
        if (groupService.findById(user.getGroupID()).isEmpty())
            return null;
        return userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }


}
