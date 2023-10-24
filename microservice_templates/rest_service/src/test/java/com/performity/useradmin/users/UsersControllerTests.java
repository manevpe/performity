package com.performity.myservice.users;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.performity.myservice.exceptions.AccessDeniedException;
import com.performity.myservice.utils.AuthorizationHelper;
import com.performity.myservice.validators.JsonSchemaValidator;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * @author Petar Manev - <a href="https://github.com/manevpe">GitHub</a>
 */
@WebMvcTest(controllers = UsersController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class UsersControllerTests {
  static User user;
  static User defaultUser;
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private UsersService usersService;
  @MockBean
  private AuthorizationHelper authorizationHelper;
  @MockBean
  private JsonSchemaValidator jsonSchemaValidator;
  @InjectMocks
  private UsersController usersController;
  @Autowired
  private ObjectMapper objectMapper;

  @BeforeAll
  static void setup() {
    user = User.builder()
        .firstName("Michael")
        .lastName("Scott")
        .email("michael.scott@dundermifflin.com")
        .teams(Arrays.asList("admin", "users"))
        .vacationDays(20)
        .build();

    defaultUser = User.builder()
        .firstName("Jim")
        .lastName("Halpert")
        .email("jim.halpert@dundermifflin.com")
        .build();
  }

  @Test
  void usersController_Get_All_Users() throws Exception {
    when(authorizationHelper.checkAdminPermission("Admin")).thenReturn(true);

    ResultActions response = mockMvc.perform(
        get("/myservice/v1/users")
            .requestAttr("user_roles", "Admin")
    );

    response.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    verify(usersService, times(1)).allUsers();
    verify(authorizationHelper, times(1)).checkAdminPermission("Admin");
  }

  @Test
  void usersController_Get_User_Details() throws Exception {
    when(authorizationHelper.checkAdminPermission("Admin")).thenReturn(true);

    ResultActions response =
        mockMvc.perform(
            get("/myservice/v1/users/michael.scott@dundermifflin.com")
                .requestAttr("user_email", "admin@dundermifflin.com")
                .requestAttr("user_roles", "Admin")
        );

    response.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    verify(usersService, times(1))
        .getUserDetails("michael.scott@dundermifflin.com");
    verify(authorizationHelper, times(1)).checkAdminPermission("Admin");
  }

  @Test
  void usersController_Get_User_Own_Details() throws Exception {
    when(authorizationHelper.checkAdminPermission(null)).thenReturn(true);

    ResultActions response = mockMvc.perform(
        get("/myservice/v1/users/michael.scott@dundermifflin.com")
            .requestAttr("user_email", "michael.scott@dundermifflin.com")
    );

    response.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    verify(usersService, times(1)).getUserDetails("michael.scott@dundermifflin.com");
    verify(authorizationHelper, times(0)).checkAdminPermission(null);
  }

  @Test
  void usersController_Create_User() throws Exception {
    when(authorizationHelper.checkAdminPermission(Mockito.any(String.class))).thenReturn(true);
    doNothing().when(jsonSchemaValidator)
        .validate(Mockito.any(String.class), Mockito.any(String.class));

    String payload = objectMapper.writeValueAsString(user);
    ResultActions response = mockMvc.perform(
        post("/myservice/v1/users")
            .requestAttr("user_roles", "Admin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload)
    );

    response.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    verify(usersService, times(1)).createUser(user);
    verify(jsonSchemaValidator, times(1))
        .validate("model/user.schema.json", payload);
  }

  @Test
  void usersController_Create_User_Invalid_Permissions() throws Exception {
    when(authorizationHelper.checkAdminPermission("RolesList")).thenThrow(
        new AccessDeniedException("Access denied"));

    ResultActions response = mockMvc.perform(
        post("/myservice/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user))
            .requestAttr("user_roles", "RolesList")
    );

    response.andExpect(MockMvcResultMatchers.status().is4xxClientError());
    verify(usersService, times(0)).createUser(user);
  }

  @Test
  void usersController_Update_User() throws Exception {
    when(authorizationHelper.checkAdminPermission(Mockito.any(String.class))).thenReturn(true);
    doNothing().when(jsonSchemaValidator)
        .validate(Mockito.any(String.class), Mockito.any(String.class));

    String payload = objectMapper.writeValueAsString(user);
    ResultActions response = mockMvc.perform(
        put("/myservice/v1/users/michael.scott@dundermifflin.com")
            .requestAttr("user_roles", "Admin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload)
    );

    response.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    verify(usersService, times(1)).updateByEmail("michael.scott@dundermifflin.com", user);
    verify(jsonSchemaValidator, times(1))
        .validate("model/user.schema.json", payload);
  }

  @Test
  void usersController_Delete_User() throws Exception {
    when(authorizationHelper.checkAdminPermission(Mockito.any(String.class))).thenReturn(true);

    ResultActions response = mockMvc.perform(
        delete("/myservice/v1/users/michael.scott@dundermifflin.com")
            .requestAttr("user_roles", "Admin")
    );

    response.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    verify(usersService, times(1)).deleteByEmail("michael.scott@dundermifflin.com");
  }

}
