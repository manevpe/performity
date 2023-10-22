package com.performity.useradmin.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.performity.useradmin.exceptions.AccessDeniedException;
import com.performity.useradmin.utils.AuthorizationHelper;
import com.performity.useradmin.validators.JsonSchemaValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Petar Manev - <a href="https://github.com/manevpe">GitHub</a>
 */
@RestController
@RequestMapping("/useradmin/v1/users")
public class UsersController {

  private static final String USER_ROLES = "userRoles";
  @Autowired
  private UsersService usersService;
  @Autowired
  private AuthorizationHelper authorizationHelper;
  @Autowired
  private JsonSchemaValidator jsonSchemaValidator;

  @GetMapping
  @Operation(
      summary = "Get all users",
      description = "Returns a list of all users for the tenant.",
      parameters = {
          @Parameter(
              name = "userRoles",
              in = ParameterIn.HEADER,
              example = "Admin"
          )
      },
      responses = @ApiResponse(
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = User.class))
          )
      )
  )
  public ResponseEntity<List<User>> getAllUsers(HttpServletRequest request)
      throws AccessDeniedException {
    authorizationHelper.checkAdminPermission(request.getHeader(USER_ROLES));
    return new ResponseEntity<>(usersService.allUsers(), HttpStatus.OK);
  }

  // TODO - add filterable get all users

  @Operation(
      summary = "Get User details",
      description = "Returns all the data for a particular user by email exact match.",
      parameters = {
          @Parameter(name = "id", description = "The email of the user"),
          @Parameter(
              name = "userEmail",
              in = ParameterIn.HEADER,
              example = "admin@example.com"
          ),
          @Parameter(
              name = "userRoles",
              in = ParameterIn.HEADER,
              example = "Admin"
          )
      },
      responses = {
          @ApiResponse(
              content = @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = User.class)
              ),
              responseCode = "200"
          ),
          @ApiResponse(
              responseCode = "404",
              description = "User with requested email not found."
          ),
      }
  )
  @GetMapping("/{id}")
  public ResponseEntity<User> getUserByEmail(HttpServletRequest request,
                                             @PathVariable("id") String email)
      throws AccessDeniedException {
    // Allow user to read their own data, but only admins get read other users data.
    if (!email.equals(request.getHeader("userEmail"))) {
      authorizationHelper.checkAdminPermission(request.getHeader(USER_ROLES));
    }
    return new ResponseEntity<>(usersService.getUserDetails(email), HttpStatus.OK);
  }

  @PostMapping
  @Operation(
      summary = "Create User",
      description = "Create a new user.",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          content = @Content(schema = @Schema(implementation = User.class))
      ),
      parameters = {
          @Parameter(
              name = "userRoles",
              in = ParameterIn.HEADER,
              example = "Admin"
          )
      },
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "User successfully created.",
              content = @Content(
                  schema = @Schema(implementation = User.class),
                  mediaType = MediaType.APPLICATION_JSON_VALUE
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Request data failed validation."
          )
      }
  )
  public ResponseEntity<User> createUser(HttpServletRequest request, @RequestBody String payload)
      throws AccessDeniedException, JsonProcessingException {
    authorizationHelper.checkAdminPermission(request.getHeader(USER_ROLES));
    jsonSchemaValidator.validate("model/user.schema.json", payload);
    ObjectMapper objectMapper = new ObjectMapper();
    User newUser = objectMapper.readValue(payload, User.class);
    return new ResponseEntity<>(usersService.createUser(newUser), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update user",
      description = "Update a particular user. Replaces all existing data for the user.",
      parameters = {
          @Parameter(name = "id", description = "The email of the user"),
          @Parameter(
              name = "userRoles",
              in = ParameterIn.HEADER,
              example = "Admin"
          )
      },
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          content = @Content(schema = @Schema(implementation = User.class))
      ),
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "User successfully updated.",
              content = @Content(
                  schema = @Schema(implementation = User.class),
                  mediaType = MediaType.APPLICATION_JSON_VALUE
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Request data failed validation."
          ),
          @ApiResponse(
              responseCode = "404",
              description = "User with requested email was not found."
          )
      }
  )
  public ResponseEntity<User> updateUser(HttpServletRequest request, @RequestBody String payload,
                                         @PathVariable("id") String email)
      throws AccessDeniedException, JsonProcessingException {
    authorizationHelper.checkAdminPermission(request.getHeader(USER_ROLES));
    jsonSchemaValidator.validate("model/user.schema.json", payload);
    ObjectMapper objectMapper = new ObjectMapper();
    User newUser = objectMapper.readValue(payload, User.class);
    return new ResponseEntity<>(usersService.updateByEmail(email, newUser), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  @Operation(
      summary = "Delete user",
      description = "Delete a particular user, by the email.",
      parameters = {
          @Parameter(name = "id", description = "The email of the user"),
          @Parameter(
              name = "userRoles",
              in = ParameterIn.HEADER,
              example = "Admin"
          )
      },
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "User successfully deleted."
          ),
          @ApiResponse(
              responseCode = "404",
              description = "User with requested email was not found."
          )
      }
  )
  public ResponseEntity<HttpStatus> deleteUser(HttpServletRequest request,
                                               @PathVariable("id") String email)
      throws AccessDeniedException {
    authorizationHelper.checkAdminPermission(request.getHeader(USER_ROLES));
    usersService.deleteByEmail(email);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
