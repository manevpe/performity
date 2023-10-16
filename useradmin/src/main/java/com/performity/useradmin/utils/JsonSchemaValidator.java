package com.performity.useradmin.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import com.performity.useradmin.UserController;

import java.io.InputStream;
import java.util.Set;

public class JsonSchemaValidator {
    public static void validate(String jsonSchema, String requestStr) throws JsonProcessingException, InvalidRequestDataException {
        ObjectMapper om = new ObjectMapper();
        om.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
        JsonNode jsonNode = om.readTree(requestStr);

        InputStream schemaAsStream = UserController.class.getClassLoader().getResourceAsStream(jsonSchema);
        JsonSchema schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(schemaAsStream);
        Set<ValidationMessage> errors = schema.validate(jsonNode);
        String errorsCombined = "";
        for (ValidationMessage error : errors) {
            //log.error("Validation Error: {}", error);
            System.out.println("Validation Error: ");
            System.out.println(error);
            errorsCombined += error.toString() + "\n";
        }

        if (errors.size() > 0)
            throw new InvalidRequestDataException("Invalid json: " + errorsCombined);
    }
}
