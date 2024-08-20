package com.clap.pause.service;

import com.clap.pause.dto.departmentGroup.DepartmentGroupRequest;
import com.clap.pause.dto.departmentGroup.DepartmentGroupResponse;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.model.DepartmentGroup;
import com.clap.pause.repository.DepartmentGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DepartmentGroupService {

    private final DepartmentGroupRepository departmentGroupRepository;

    public DepartmentGroupResponse saveDepartmentGroup(DepartmentGroupRequest departmentGroupRequest) {
        var departmentGroup = saveDepartmentGroupWithDepartmentGroupRequest(departmentGroupRequest);
        return getDepartmentGroupResponse(departmentGroup);
    }

    public void updateDepartmentGroup(Long id, DepartmentGroupRequest departmentGroupRequest) {
        var departmentGroup = departmentGroupRepository.findById(id)
                .orElseThrow(() -> new NotFoundElementException(id + "를 가진 학과그룹이 존재하지 않습니다."));
        updateDepartmentGroupWithDepartmentGroupRequest(departmentGroup, departmentGroupRequest);
    }

    public DepartmentGroupResponse getDepartmentGroup(Long id) {
        var departmentGroup = departmentGroupRepository.findById(id)
                .orElseThrow(() -> new NotFoundElementException(id + "를 가진 학과그룹이 존재하지 않습니다."));
        return getDepartmentGroupResponse(departmentGroup);
    }

    public List<DepartmentGroupResponse> getDepartmentGroups() {
        var departmentGroups = departmentGroupRepository.findAll();
        return departmentGroups.stream()
                .map(this::getDepartmentGroupResponse)
                .toList();
    }

    private DepartmentGroup saveDepartmentGroupWithDepartmentGroupRequest(DepartmentGroupRequest departmentGroupRequest) {
        var departmentGroup = new DepartmentGroup(departmentGroupRequest.name());
        return departmentGroupRepository.save(departmentGroup);
    }

    private void updateDepartmentGroupWithDepartmentGroupRequest(DepartmentGroup departmentGroup, DepartmentGroupRequest departmentGroupRequest) {
        departmentGroup.updateName(departmentGroupRequest.name());
        departmentGroupRepository.save(departmentGroup);
    }

    private DepartmentGroupResponse getDepartmentGroupResponse(DepartmentGroup departmentGroup) {
        return DepartmentGroupResponse.of(departmentGroup.getId(), departmentGroup.getName());
    }
}
