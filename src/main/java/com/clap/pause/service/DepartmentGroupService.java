package com.clap.pause.service;

import com.clap.pause.dto.departmentGroup.DepartmentGroupRequest;
import com.clap.pause.dto.departmentGroup.DepartmentGroupResponse;
import com.clap.pause.model.DepartmentGroup;
import com.clap.pause.repository.DepartmentGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DepartmentGroupService {

    private final DepartmentGroupRepository departmentGroupRepository;

    public DepartmentGroupResponse saveDepartmentGroup(DepartmentGroupRequest departmentGroupRequest) {
        var departmentGroup = saveDepartmentGroupWithDepartmentGroupRequest(departmentGroupRequest);
        return getDepartmentGroupResponse(departmentGroup);
    }

    private DepartmentGroup saveDepartmentGroupWithDepartmentGroupRequest(DepartmentGroupRequest departmentGroupRequest) {
        var departmentGroup = new DepartmentGroup(departmentGroupRequest.name());
        return departmentGroupRepository.save(departmentGroup);
    }

    private DepartmentGroupResponse getDepartmentGroupResponse(DepartmentGroup departmentGroup) {
        return DepartmentGroupResponse.of(departmentGroup.getName());
    }
}
