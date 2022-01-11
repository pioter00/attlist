package com.root.attendancelist.service;

import com.root.attendancelist.model.Groups;
import com.root.attendancelist.model.User;
import com.root.attendancelist.repository.GroupRepository;
import com.root.attendancelist.repository.UserRepository;
import com.root.attendancelist.service.interfaces.GroupServiceInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class GroupService implements GroupServiceInterface {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public GroupService(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Groups> findAllGroups() {
        return groupRepository.findAll();
    }

    @Override
    public Optional<Groups> findById(Long id) {
        return groupRepository.findById(id);
    }

    @Override
    public List<User> findAllUsersById(Long id) {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getGroupID().equals(id))
                .toList();
    }

    @Override
    public Groups saveGroup(Groups group) {
        return groupRepository.save(group);
    }

    @Override
    public Groups updateGroup(Groups group) {
        return groupRepository.save(group);
    }

    @Override
    public void deleteGroup(Long id) {
        userRepository.findAll().stream()
                .filter(user -> Objects.equals(user.getGroupID(), id))
                .forEach(user -> user.setGroupID(null));
        groupRepository.deleteById(id);
    }
}
