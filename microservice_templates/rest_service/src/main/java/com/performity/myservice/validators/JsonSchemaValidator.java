package com.performity.myservice.validators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import com.performity.myservice.exceptions.InvalidRequestDataException;
import com.performity.myservice.users.UsersController;
import java.io.InputStream;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Petar Manev - <a href="https://github.com/manevpe">GitHub</a>
 */
@Component
public class JsonSchemaValidator {
  private static final Logger logger = LoggerFactory.getLogger(JsonSchemaValidator.class);

  public void validate(String jsonSchema, String requestStr)
      throws JsonProcessingException, InvalidRequestDataException {
    ObjectMapper om = new ObjectMapper();
    om.setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE);
    JsonNode jsonNode = om.readTree(requestStr);

    InputStream schemaAsStream =
        UsersController.class.getClassLoader().getResourceAsStream(jsonSchema);
    JsonSchema schema =
        JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(schemaAsStream);
    Set<ValidationMessage> errors = schema.validate(jsonNode);
    StringBuilder errorsCombined = new StringBuilder();
    for (ValidationMessage error : errors) {
      logger.error("Validation Error: {}", error);
      errorsCombined.append(error.toString()).append("\n");
    }

    if (!errors.isEmpty()) {
      throw new InvalidRequestDataException("Invalid json: " + errorsCombined);
    }
  }
}
