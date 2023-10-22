package com.performity.useradmin.users;

import com.performity.useradmin.UseradminApplication;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(
    classes = UseradminApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {"spring.datasource.url=jdbc:tc:postgresql:latest:///performity"}
)
@ActiveProfiles("test")
@TestConfiguration(proxyBeanMethods = false)
@Testcontainers
class UsersRepositoryTests {

  // will be started before and stopped after each test method
  @Container
  private static final PostgreSQLContainer postgresqlContainer =
      new PostgreSQLContainer("postgres:latest");
  //            .withDatabaseName("performity")
  //            .withUsername("foo")
  //            .withPassword("secret");
  static User user;
  static User defaultUser;
  @Autowired
  UsersRepository usersRepository;
  private User savedUser;
  private User savedDefaultUser;

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

  @BeforeEach
  void init() {
    savedUser = usersRepository.save(user);
    savedDefaultUser = usersRepository.save(defaultUser);
  }

  @Test
  void usersRepository_Create_User_With_All_Fields() {
    Assertions.assertThat(savedUser)
        .usingRecursiveComparison()
        .ignoringFields("id", "dateCreated", "dateUpdated")
        .isEqualTo(user);
  }

  @Test
  void usersRepository_Create_User_With_Mandatory_Fields_Only() {
    User expectedDefaultUser = defaultUser;
    expectedDefaultUser.setVacationDays((0));
    Assertions.assertThat(savedDefaultUser)
        .usingRecursiveComparison()
        .ignoringFields("id", "dateCreated", "dateUpdated")
        .isEqualTo(expectedDefaultUser);
  }

  @Test
  void usersRepository_Date_Created_and_Date_Updated_Are_Set() {
    Assertions.assertThat(savedUser)
        .extracting("dateCreated")
        .isInstanceOfAny(Timestamp.class)
        .isNotNull();
    Assertions.assertThat(savedUser)
        .extracting("dateUpdated")
        .isNull();
  }

  @Test
  void usersRepository_Find_All() {
    List<User> allUsers = usersRepository.findAll();
    User expectedDefaultUser = defaultUser;
    expectedDefaultUser.setVacationDays((0));
    Assertions.assertThat(allUsers)
        .usingRecursiveComparison()
        .ignoringCollectionOrder()
        .ignoringFields("id", "dateCreated", "dateUpdated")
        .isEqualTo(Arrays.asList(user, expectedDefaultUser));
  }

  @Test
  void usersRepository_Find_By_Email() {
    User returnedUser = usersRepository.findByEmail(user.getEmail());
    Assertions.assertThat(returnedUser)
        .usingRecursiveComparison()
        .ignoringFields("id", "dateCreated", "dateUpdated")
        .isEqualTo(user);
  }

  @Test
  void usersRepository_Find_By_Email_Returns_Null_If_Not_Found() {
    User returnedUser = usersRepository.findByEmail("nonexisting.user@dundermifflin.com");
    Assertions.assertThat(returnedUser).isNull();
  }

  @Test
  void usersRepository_Update_User() {
    User updatedUser = user;
    updatedUser.setVacationDays((25));
    usersRepository.save(updatedUser);
    User returnedUser = usersRepository.findByEmail(updatedUser.getEmail());
    Assertions.assertThat(returnedUser)
        .usingRecursiveComparison()
        .ignoringFields("id", "dateCreated", "dateUpdated")
        .isEqualTo(updatedUser);
    Assertions.assertThat(returnedUser)
        .extracting("dateCreated")
        .isEqualTo(savedUser.getDateCreated());
    Assertions.assertThat(returnedUser)
        .extracting("dateUpdated")
        .isNotNull();
  }

  @Test
  void usersRepository_Delete_By_Email() {
    usersRepository.deleteByEmail(defaultUser.getEmail());
    List<User> allUsers = usersRepository.findAll();
    Assertions.assertThat(allUsers)
        .usingRecursiveComparison()
        .ignoringFields("id", "dateCreated", "dateUpdated")
        .ignoringCollectionOrder()
        .isEqualTo(List.of(user));
  }
}