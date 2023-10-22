package com.performity.useradmin.users;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.performity.useradmin.keycloak.KeycloakService;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UsersServiceTests {

  static User user;
  static User defaultUser;
  @Mock
  private static UsersRepository usersRepository;
  @Mock
  private KeycloakService keycloakService;
  @InjectMocks
  private UsersServiceImpl usersService;

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
  void usersService_Create_User() {
    when(usersRepository.save(Mockito.any(User.class))).thenReturn(user);
    when(usersRepository.findByEmail(Mockito.any(String.class))).thenReturn(null);
    User savedUser = usersService.createUser(new User(user));
    Assertions.assertThat(savedUser).usingRecursiveComparison().isEqualTo(user);
    verify(keycloakService, times(1)).addUser(user);
  }

  @Test
  void usersService_Create_User_Throws_Exception_If_User_Already_Exists() {
    when(usersRepository.findByEmail(Mockito.any(String.class))).thenReturn(user);
    User newUser = new User(user);
    assertThatThrownBy(() -> usersService.createUser(newUser))
        .isInstanceOf(UserExistsException.class);
  }

  @Test
  void usersService_Get_All_Users() {
    List<User> mockedData = Arrays.asList(user, defaultUser);
    when(usersRepository.findAll()).thenReturn(mockedData);
    List<User> returnedUsers = usersService.allUsers();
    Assertions.assertThat(returnedUsers)
        .usingRecursiveComparison()
        .ignoringCollectionOrder()
        .isEqualTo(mockedData);
  }

  @Test
  void usersService_Get_User_Details() {
    when(usersRepository.findByEmail(user.getEmail())).thenReturn(user);
    User returnedUser = usersService.getUserDetails(user.getEmail());
    Assertions.assertThat(returnedUser)
        .usingRecursiveComparison()
        .isEqualTo(user);
  }

  @Test
  void usersService_Get_User_Details_Throws_Exception_If_User_Not_Found() {
    assertThatThrownBy(() ->
        usersService.getUserDetails("nonexisting.user@dundermifflin.com")
    ).isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void usersService_Update_User() {
    User mockedUser = new User(user);
    mockedUser.setFirstName("NewFirstName");
    mockedUser.setLastName("NewLastName");
    mockedUser.setVacationDays((float) 5);
    mockedUser.setTeams(Arrays.asList("teamA", "teamB"));
    mockedUser.setDateCreated(new Timestamp(System.currentTimeMillis()));
    when(usersRepository.findByEmail(user.getEmail())).thenReturn(user);
    when(usersRepository.save(Mockito.any(User.class))).thenReturn(mockedUser);
    User returnedUser = usersService.updateByEmail(user.getEmail(), mockedUser);
    Assertions.assertThat(returnedUser)
        .usingRecursiveComparison()
        .isEqualTo(mockedUser);
  }

  @Test
  void usersService_Update_User_New_Email() {
    User mockedUser = new User(user);
    mockedUser.setFirstName("NewFirstName");
    mockedUser.setLastName("NewLastName");
    mockedUser.setEmail("new@dundermifflin.com");
    mockedUser.setVacationDays((float) 5);
    mockedUser.setTeams(Arrays.asList("teamA", "teamB"));
    when(usersRepository.findByEmail(user.getEmail())).thenReturn(user);
    when(usersRepository.save(Mockito.any(User.class))).thenReturn(mockedUser);
    User returnedUser = usersService.updateByEmail(user.getEmail(), mockedUser);
    Assertions.assertThat(returnedUser)
        .usingRecursiveComparison()
        .isEqualTo(mockedUser);
  }

  @Test
  void usersService_Update_User_New_Email_Duplicate_Error() {
    User mockedUser = new User(user);
    mockedUser.setEmail("duplicate@dundermifflin.com");
    when(usersRepository.findByEmail(user.getEmail())).thenReturn(user);
    when(usersRepository.findByEmail("duplicate@dundermifflin.com")).thenReturn(user);
    assertThatThrownBy(() ->
        usersService.updateByEmail(user.getEmail(), mockedUser)
    ).isInstanceOf(UserExistsException.class);
  }

  @Test
  void usersService_Delete_User() {
    when(usersRepository.findByEmail(user.getEmail())).thenReturn(user);
    String email = user.getEmail();
    usersService.deleteByEmail(email);
    verify(usersRepository, times(1)).deleteByEmail(email);
    verify(keycloakService, times(1)).deleteUser(email);
  }
}
