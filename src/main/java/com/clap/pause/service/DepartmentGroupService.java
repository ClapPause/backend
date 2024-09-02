package com.clap.pause.service;

import com.clap.pause.dto.departmentGroup.DepartmentGroupRequest;
import com.clap.pause.dto.departmentGroup.DepartmentGroupResponse;
import com.clap.pause.exception.DuplicatedException;
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

    /**
     * 학과 그룹을 생성하는 메서드
     *
     * @param departmentGroupRequest
     * @return departmentGroup
     */
    public DepartmentGroupResponse saveDepartmentGroup(DepartmentGroupRequest departmentGroupRequest) {
        nameValidation(departmentGroupRequest.name());
        var departmentGroup = saveDepartmentGroupWithDepartmentGroupRequest(departmentGroupRequest);
        return getDepartmentGroupResponse(departmentGroup);
    }

    /**
     * 학과 그룹의 이름을 수정하는 메서드
     *
     * @param id
     * @param departmentGroupRequest
     */
    public void updateDepartmentGroup(Long id, DepartmentGroupRequest departmentGroupRequest) {
        nameValidation(departmentGroupRequest.name());
        var departmentGroup = departmentGroupRepository.findById(id)
                .orElseThrow(() -> new NotFoundElementException(id + "를 가진 학과그룹이 존재하지 않습니다."));
        updateDepartmentGroupWithDepartmentGroupRequest(departmentGroup, departmentGroupRequest);
    }

    /**
     * 하나의 학과그룹을 id 를 통해 조회하는 메서드
     *
     * @param id
     * @return departmentGroupResponse
     */
    public DepartmentGroupResponse getDepartmentGroup(Long id) {
        var departmentGroup = departmentGroupRepository.findById(id)
                .orElseThrow(() -> new NotFoundElementException(id + "를 가진 학과그룹이 존재하지 않습니다."));
        return getDepartmentGroupResponse(departmentGroup);
    }

    /**
     * 여러개의 학과그룹을 모두 조회하는 메서드
     *
     * @return departmentGroups
     */
    public List<DepartmentGroupResponse> getDepartmentGroups() {
        var departmentGroups = departmentGroupRepository.findAll();
        return departmentGroups.stream()
                .map(this::getDepartmentGroupResponse)
                .toList();
    }

    /**
     * RequestDTO 를 사용해서 학과그룹을 생성하는 메서드
     *
     * @param departmentGroupRequest
     * @return departmentGroup
     */
    private DepartmentGroup saveDepartmentGroupWithDepartmentGroupRequest(DepartmentGroupRequest departmentGroupRequest) {
        var departmentGroup = new DepartmentGroup(departmentGroupRequest.name());
        return departmentGroupRepository.save(departmentGroup);
    }

    /**
     * 학과그룹과 RequestDTO 를 사용해서 학과그룹의 이름을 변경하는 메서드
     *
     * @param departmentGroup
     * @param departmentGroupRequest
     */
    private void updateDepartmentGroupWithDepartmentGroupRequest(DepartmentGroup departmentGroup, DepartmentGroupRequest departmentGroupRequest) {
        departmentGroup.updateName(departmentGroupRequest.name());
        departmentGroupRepository.save(departmentGroup);
    }


    /**
     * 중복된 학과그룹인지 검증하는 메서드
     *
     * @param name
     */
    private void nameValidation(String name) {
        if (departmentGroupRepository.existsByName(name)) {
            throw new DuplicatedException("이미 존재하는 학과그룹입니다.");
        }
    }

    /**
     * 학과그룹 엔티티를 ResponseDTO 로 변환하는 메서드
     *
     * @param departmentGroup
     * @return departmentGroupResponse
     */
    private DepartmentGroupResponse getDepartmentGroupResponse(DepartmentGroup departmentGroup) {
        return DepartmentGroupResponse.of(departmentGroup.getId(), departmentGroup.getName());
    }
}
