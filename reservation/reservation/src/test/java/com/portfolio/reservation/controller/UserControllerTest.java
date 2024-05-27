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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    
    @Test
    public void signUp_valid_username_size_테스트() throws Exception {

        UserCreateRequest request = new UserCreateRequest();
        request.setUsername("u");
        request.setPassword("Password123!");
        request.setNickname("hj0501");
        request.setAuthority(AuthorityType.USER);

        doNothing().when(userService).signUp(request);

        mockMvc.perform(post("/v1.0/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.username").value("아이디는 최소 4자, 최대 16자여야 합니다."));
    }

    @Test
    public void signUp_valid_username_pattern_테스트() throws Exception {

        UserCreateRequest request = new UserCreateRequest();
        request.setUsername("u___");
        request.setPassword("Password123!");
        request.setNickname("hj0501");
        request.setAuthority(AuthorityType.USER);

        doNothing().when(userService).signUp(request);

        mockMvc.perform(post("/v1.0/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.username").value("아이디는 영문자와 숫자만 포함할 수 있습니다."));
    }

    @Test
    public void signUp_valid_password_size_테스트() throws Exception {

        UserCreateRequest request = new UserCreateRequest();
        request.setUsername("username");
        request.setPassword("Passw1!");
        request.setNickname("hj0501");
        request.setAuthority(AuthorityType.USER);

        doNothing().when(userService).signUp(request);

        mockMvc.perform(post("/v1.0/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.password").value("비밀번호는 최소 8자, 최대 20자여야 합니다."));
    }

    @Test
    public void signUp_valid_password_pattern_테스트() throws Exception {

        UserCreateRequest request = new UserCreateRequest();
        request.setUsername("username");
        request.setPassword("Password123!");
        request.setNickname("hj0501");
        request.setAuthority(AuthorityType.USER);

        doNothing().when(userService).signUp(request);

        mockMvc.perform(post("/v1.0/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.password").value("비밀번호는 최소 하나의 대문자, 소문자, 숫자 및 특수 문자를 포함해야 합니다."));
    }

    @Test
    public void signUp_valid_nickname_size_테스트() throws Exception {

        UserCreateRequest request = new UserCreateRequest();
        request.setUsername("username");
        request.setPassword("Password123!");
        request.setNickname("n_-");
        request.setAuthority(AuthorityType.USER);

        doNothing().when(userService).signUp(request);

        mockMvc.perform(post("/v1.0/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.nickname").value("닉네임은 최소 4자, 최대 16자여야 합니다."));
    }

    @Test
    public void signUp_valid_nickname_pattern_테스트() throws Exception {

        UserCreateRequest request = new UserCreateRequest();
        request.setUsername("username");
        request.setPassword("12345678");
        request.setNickname("nick!!");
        request.setAuthority(AuthorityType.USER);

        doNothing().when(userService).signUp(request);

        mockMvc.perform(post("/v1.0/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.nickname").value("닉네임은 영문자, 숫자, 언더스코어(_) 및 하이픈(-)만 포함할 수 있습니다."));
    }
}