package com.root.attendancelist.service.interfaces;

import com.root.attendancelist.model.Groups;
import com.root.attendancelist.model.User;

import java.util.List;
import java.util.Optional;

public interface GroupServiceInterface {
    List<Groups> findAllGroups();
    Optional<Groups> findById(Long id);
    List<User> findAllUsersById(Long id);
    Groups saveGroup(Groups group);
    Groups updateGroup(Groups group);
    void deleteGroup(Long id);
}
