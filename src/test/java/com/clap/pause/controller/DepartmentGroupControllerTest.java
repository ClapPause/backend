package com.clap.pause.controller;

import com.clap.pause.config.security.JwtAuthFilter;
import com.clap.pause.dto.departmentGroup.DepartmentGroupRequest;
import com.clap.pause.dto.departmentGroup.DepartmentGroupResponse;
import com.clap.pause.exception.ExceptionResponse;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.service.DepartmentGroupService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = DepartmentGroupController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthFilter.class)
        })
@WithMockUser
@MockBean(JpaMetamodelMappingContext.class)
class DepartmentGroupControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private DepartmentGroupService departmentGroupService;

    @Test
    @DisplayName("안내에 맞게 학과 그룹을 등록하면 성공한다")
    void saveDepartmentGroup_success() throws Exception {
        //given
        var departmentGroupRequest = new DepartmentGroupRequest("테스트");
        var departmentGroupResponse = new DepartmentGroupResponse(1L, "테스트");

        when(departmentGroupService.saveDepartmentGroup(any(DepartmentGroupRequest.class)))
                .thenReturn(departmentGroupResponse);
        //when
        var result = mockMvc.perform(post("/api/department-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(departmentGroupRequest))
                .with(csrf()));
        //then
        result.andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/department-groups/1"));
    }

    @Test
    @DisplayName("빈 이름으로 학과 그룹을 등록하면 실패한다")
    void saveDepartmentGroup_fail_emptyName() throws Exception {
        //given
        var departmentGroupRequest = new DepartmentGroupRequest("");
        //when
        var result = mockMvc.perform(post("/api/department-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(departmentGroupRequest))
                .with(csrf()));
        //then
        var postResult = result.andExpect(status().isBadRequest())
                .andReturn();
        var response = getExceptionResponseMessage(postResult);

        Assertions.assertThat(response.status())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(response.message())
                .isEqualTo("학과 그룹의 이름은 1글자 이상이어야 합니다.");
    }

    @Test
    @DisplayName("존재하는 ID 로 수정 요청을 보내면 성공한다.")
    void updateDepartmentGroup_success() throws Exception {
        //given
        var departmentGroupRequest = new DepartmentGroupRequest("테스트");

        doNothing().when(departmentGroupService)
                .updateDepartmentGroup(any(Long.class), any(DepartmentGroupRequest.class));
        //when
        var result = mockMvc.perform(put("/api/department-groups/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(departmentGroupRequest))
                .with(csrf()));
        //then
        result.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("존재하는 않는 ID 로 수정 요청을 보내면 실패한다.")
    void updateDepartmentGroup_fail_noExistsId() throws Exception {
        //given
        var departmentGroupRequest = new DepartmentGroupRequest("테스트");

        doThrow(new NotFoundElementException("1를 가진 학과그룹이 존재하지 않습니다.")).when(departmentGroupService)
                .updateDepartmentGroup(any(Long.class), any(DepartmentGroupRequest.class));
        //when
        var result = mockMvc.perform(put("/api/department-groups/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(departmentGroupRequest))
                .with(csrf()));
        //then
        var postResult = result.andExpect(status().isNotFound())
                .andReturn();
        var response = getExceptionResponseMessage(postResult);

        Assertions.assertThat(response.status())
                .isEqualTo(HttpStatus.NOT_FOUND.value());
        Assertions.assertThat(response.message())
                .isEqualTo("1를 가진 학과그룹이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("존재하는 ID 로 조회 요청을 보내면 성공한다.")
    void getDepartmentGroup_success() throws Exception {
        //given
        var departmentGroupResponse = new DepartmentGroupResponse(1L, "테스트");

        when(departmentGroupService.getDepartmentGroup(any(Long.class)))
                .thenReturn(departmentGroupResponse);
        //when
        var result = mockMvc.perform(get("/api/department-groups/1")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));
        //then
        var getResult = result.andExpect(status().isOk())
                .andReturn();
        var response = getResult.getResponse()
                .getContentAsString();
        var convertedDepartmentGroupResponse = objectMapper.readValue(response, DepartmentGroupResponse.class);

        Assertions.assertThat(convertedDepartmentGroupResponse.id())
                .isEqualTo(1L);
        Assertions.assertThat(convertedDepartmentGroupResponse.name())
                .isEqualTo("테스트");
    }

    @Test
    @DisplayName("존재하지 않는 ID 로 조회 요청을 보내면 실패한다.")
    void getDepartmentGroup_fail_noExistsId() throws Exception {
        //given
        doThrow(new NotFoundElementException("1를 가진 학과그룹이 존재하지 않습니다.")).when(departmentGroupService)
                .getDepartmentGroup(any(Long.class));
        //when
        var result = mockMvc.perform(get("/api/department-groups/1")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));
        //then
        var postResult = result.andExpect(status().isNotFound())
                .andReturn();
        var response = getExceptionResponseMessage(postResult);

        Assertions.assertThat(response.status())
                .isEqualTo(HttpStatus.NOT_FOUND.value());
        Assertions.assertThat(response.message())
                .isEqualTo("1를 가진 학과그룹이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("학과그룹 전체에 대한 요청을 보내면 성공한다.")
    void getDepartmentGroups_success() throws Exception {
        //given
        var departmentGroupResponse = new DepartmentGroupResponse(1L, "테스트");

        when(departmentGroupService.getDepartmentGroups())
                .thenReturn(List.of(departmentGroupResponse, departmentGroupResponse));
        //when
        var result = mockMvc.perform(get("/api/department-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));
        //then
        var getResult = result.andExpect(status().isOk())
                .andReturn();
        var response = getResult.getResponse()
                .getContentAsString();
        var convertedDepartmentGroupResponse = objectMapper.readValue(response, new TypeReference<List<DepartmentGroupResponse>>() {
        });

        Assertions.assertThat(convertedDepartmentGroupResponse.size())
                .isEqualTo(2);
    }

    private ExceptionResponse getExceptionResponseMessage(MvcResult result) throws Exception {
        var resultString = result.getResponse().getContentAsString();
        return objectMapper.readValue(resultString, ExceptionResponse.class);
    }
}
