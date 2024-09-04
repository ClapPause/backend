package com.clap.pause.service;

import com.clap.pause.dto.departmentGroup.DepartmentGroupRequest;
import com.clap.pause.exception.DuplicatedException;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.model.DepartmentGroup;
import com.clap.pause.repository.DepartmentGroupRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

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
        var savedDepartmentGroup = new DepartmentGroup("테스트 학과그룹");

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

    @Test
    @DisplayName("존재하지 않는 학과 그룹으로 이름을 변경 요청하면 성공한다.")
    void updateDepartmentGroup_success() {
        //given
        var departmentGroupRequest = getDepartmentGroupRequest();
        var departmentGroup = new DepartmentGroup("기존 학과그룹");

        when(departmentGroupRepository.existsByName(any(String.class)))
                .thenReturn(false);
        when(departmentGroupRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(departmentGroup));
        when(departmentGroupRepository.save(any(DepartmentGroup.class)))
                .thenReturn(departmentGroup);
        //when
        departmentGroupService.updateDepartmentGroup(1L, departmentGroupRequest);
        // then
        Assertions.assertThat(departmentGroup.getName())
                .isEqualTo("테스트 학과그룹");
    }

    @Test
    @DisplayName("존재하지 않는 학과 ID 로 이름을 변경 요청하면 실패한다.")
    void updateDepartmentGroup_fail_noExistsId() {
        //given
        var departmentGroupRequest = getDepartmentGroupRequest();

        when(departmentGroupRepository.existsByName(any(String.class)))
                .thenReturn(false);
        when(departmentGroupRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());
        //when, then
        Assertions.assertThatThrownBy(() -> departmentGroupService.updateDepartmentGroup(1L, departmentGroupRequest))
                .isInstanceOf(NotFoundElementException.class);
    }

    @Test
    @DisplayName("이미 존재하는 학과 그룹으로 이름을 변경 요청하면 실패한다.")
    void updateDepartmentGroup_fail_existsName() {
        //given
        var departmentGroupRequest = getDepartmentGroupRequest();

        when(departmentGroupRepository.existsByName(any(String.class)))
                .thenReturn(true);
        //when, then
        Assertions.assertThatThrownBy(() -> departmentGroupService.updateDepartmentGroup(1L, departmentGroupRequest))
                .isInstanceOf(DuplicatedException.class);
    }

    @Test
    @DisplayName("존재하는 학과 그룹을 ID로 조회하면 성공한다.")
    void getDepartmentGroup_success() {
        //given
        var departmentGroup = new DepartmentGroup("테스트");

        when(departmentGroupRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(departmentGroup));
        //when
        var foundedDepartmentGroup = departmentGroupService.getDepartmentGroup(1L);
        //then
        Assertions.assertThat(foundedDepartmentGroup.name())
                .isEqualTo("테스트");
    }

    @Test
    @DisplayName("존재하지 않는 학과 그룹 ID 로 조회하면 실패한다.")
    void getDepartmentGroup_fail_noExistsId() {
        //given
        when(departmentGroupRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());
        //when, then
        Assertions.assertThatThrownBy(() -> departmentGroupService.getDepartmentGroup(1L))
                .isInstanceOf(NotFoundElementException.class);
    }

    @Test
    @DisplayName("모든 학과그룹에 대한 조회 요청을 보내면 성공한다.")
    void getDepartmentGroups_success() {
        //given
        var departmentGroup = new DepartmentGroup("1");
        when(departmentGroupRepository.findAll())
                .thenReturn(List.of(departmentGroup, departmentGroup, departmentGroup));
        //when
        var departmentGroups = departmentGroupService.getDepartmentGroups();
        //then
        Assertions.assertThat(departmentGroups.size())
                .isEqualTo(3);
    }

    @Test
    @DisplayName("모든 학과그룹을 조회하면 비어있어도 성공한다.")
    void getDepartmentGroups_success_emptyList() {
        //given
        when(departmentGroupRepository.findAll())
                .thenReturn(List.of());
        //when
        var departmentGroups = departmentGroupService.getDepartmentGroups();
        //then
        Assertions.assertThat(departmentGroups.size())
                .isEqualTo(0);
    }

    private DepartmentGroupRequest getDepartmentGroupRequest() {
        return new DepartmentGroupRequest("테스트 학과그룹");
    }
}
