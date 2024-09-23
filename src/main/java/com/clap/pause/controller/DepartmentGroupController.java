package com.clap.pause.controller;

import com.clap.pause.dto.departmentGroup.DepartmentGroupRequest;
import com.clap.pause.dto.departmentGroup.DepartmentGroupResponse;
import com.clap.pause.service.DepartmentGroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/department-groups")
@RequiredArgsConstructor
public class DepartmentGroupController {

    private final DepartmentGroupService departmentGroupService;

    @PostMapping
    public ResponseEntity<Void> saveDepartmentGroup(@Valid @RequestBody DepartmentGroupRequest departmentGroupRequest) {
        var departmentGroup = departmentGroupService.saveDepartmentGroup(departmentGroupRequest);
        return ResponseEntity.created(URI.create("/api/department-groups/" + departmentGroup.id()))
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDepartmentGroup(@PathVariable Long id,
                                                      @Valid @RequestBody DepartmentGroupRequest departmentGroupRequest) {
        departmentGroupService.updateDepartmentGroup(id, departmentGroupRequest);
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentGroupResponse> getDepartmentGroup(@PathVariable Long id) {
        var departmentGroup = departmentGroupService.getDepartmentGroup(id);
        return ResponseEntity.ok(departmentGroup);
    }

    @GetMapping
    public ResponseEntity<List<DepartmentGroupResponse>> getDepartmentGroups() {
        var departmentGroups = departmentGroupService.getDepartmentGroups();
        return ResponseEntity.ok(departmentGroups);
    }
}
