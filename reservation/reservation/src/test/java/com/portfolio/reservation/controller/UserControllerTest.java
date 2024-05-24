package com.portfolio.reservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.reservation.config.TestSecurityConfig;
import com.portfolio.reservation.controller.user.UserController;
import com.portfolio.reservation.domain.user.AuthorityType;
import com.portfolio.reservation.dto.user.UserCreateRequest;
import com.portfolio.reservation.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class) // jpaMappingContext bean 에러 해결책
@Import(TestSecurityConfig.class) // 시큐리티 별도 설정
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void signUp_valid_통과_테스트() throws Exception {

        UserCreateRequest request = new UserCreateRequest();
        request.setUsername("username");
        request.setPassword("Password123!");
        request.setNickname("hj0501");
        request.setAuthority(AuthorityType.USER);

        doNothing().when(userService).signUp(request);

        mockMvc.perform(post("/v1.0/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    // TODO: signUp valid 실패 케이스 테스트
}