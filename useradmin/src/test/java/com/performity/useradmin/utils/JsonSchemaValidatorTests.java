package com.performity.useradmin.utils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.performity.useradmin.exceptions.InvalidRequestDataException;
import com.performity.useradmin.users.User;
import com.performity.useradmin.validators.JsonSchemaValidator;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class JsonSchemaValidatorTests {

  private final JsonSchemaValidator jsonSchemaValidator = new JsonSchemaValidator();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void jsonSchemaValidator_Validate_Correct_Payload() throws JsonProcessingException {
    User user = User.builder()
        .firstName("Michael")
        .lastName("Scott")
        .email("michael.scott@dundermifflin.com")
        .teams(Arrays.asList("admin", "users"))
        .vacationDays(20)
        .build();
    String payload = objectMapper.writeValueAsString(user);
    jsonSchemaValidator.validate("model/user.schema.json", payload);
  }

  @Test
  void jsonSchemaValidator_Validate_Invalid_Payload() throws JsonProcessingException {
    User invalidUser = User.builder()
        .firstName("Jim")
        .lastName("Halpert")
        .build();
    String payload = objectMapper.writeValueAsString(invalidUser);
    assertThatThrownBy(() ->
        jsonSchemaValidator.validate("model/user.schema.json", payload)
    ).isInstanceOf(InvalidRequestDataException.class);
  }
}
