package com.clap.pause.service;

import com.clap.pause.dto.departmentGroup.DepartmentGroupRequest;
import com.clap.pause.exception.DuplicatedException;
import com.clap.pause.model.DepartmentGroup;
import com.clap.pause.repository.DepartmentGroupRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentGroupServiceTest {

    @Mock
    private DepartmentGroupRepository departmentGroupRepository;
    @InjectMocks
    private DepartmentGroupService departmentGroupService;

    @Test
    @DisplayName("안내에 맞게 학과 그룹 등록을 요청하면 성공한다.")
    void saveDepartmentGroup_success() {
        //given
        var departmentGroupRequest = getDepartmentGroupRequest();
        var savedDepartmentGroup = getSavedDepartmentGroup();

        when(departmentGroupRepository.existsByName(any(String.class)))
                .thenReturn(false);
        when(departmentGroupRepository.save(any(DepartmentGroup.class)))
                .thenReturn(savedDepartmentGroup);
        //when
        var departmentGroupResponse = departmentGroupService.saveDepartmentGroup(departmentGroupRequest);
        // then
        Assertions.assertThat(departmentGroupResponse.name())
                .isEqualTo("테스트 학과그룹");
    }

    @Test
    @DisplayName("이미 존재하는 학과 그룹을 등록 요청하면 실패한다.")
    void saveDepartmentGroup_fail_existsName() {
        //given
        var departmentGroupRequest = getDepartmentGroupRequest();

        when(departmentGroupRepository.existsByName(any(String.class)))
                .thenReturn(true);
        //when, then
        Assertions.assertThatThrownBy(() -> departmentGroupService.saveDepartmentGroup(departmentGroupRequest))
                .isInstanceOf(DuplicatedException.class);
    }

    private DepartmentGroupRequest getDepartmentGroupRequest() {
        return new DepartmentGroupRequest("테스트 학과그룹");
    }

    private DepartmentGroup getSavedDepartmentGroup() {
        return new DepartmentGroup("테스트 학과그룹");
    }
}
