package com.root.attendancelist.api;

import com.root.attendancelist.model.Groups;
import com.root.attendancelist.model.User;
import com.root.attendancelist.service.GroupService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/group")
public class GroupApi {
    private final GroupService groupService;

    public GroupApi(GroupService groupService) {
        this.groupService = groupService;
    }
    @GetMapping
    public List<Groups> findAllGroups() {
        return groupService.findAllGroups();
    }
    @GetMapping("/{id}")
    public Optional<Groups> findGroupById(@PathVariable("id") Long id) {
        return groupService.findById(id);
    }
    @GetMapping("user/{id}")
    public List<User> findAllUsersById(@PathVariable("id") Long id) {
        return groupService.findAllUsersById(id);
    }

    @PostMapping
    public Groups saveGroup(@Valid @RequestBody Groups group) {
        return groupService.saveGroup(group);
    }

    @PutMapping
    public Groups updateGroup(@Valid @RequestBody Groups group) {
        return groupService.updateGroup(group);
    }

    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable("id") Long id) {
        groupService.deleteGroup(id);
    }
}
