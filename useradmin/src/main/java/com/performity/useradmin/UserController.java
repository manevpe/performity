package com.performity.useradmin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.performity.useradmin.utils.AccessDeniedException;
import com.performity.useradmin.utils.JsonSchemaValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/useradmin/v1/users")
public class UserController {

    @Autowired
    private UsersService usersService;

    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    @Operation(
            summary = "Get all users",
            description = "Returns a list of all users for the tenant.",
            parameters = {@Parameter(
                    name = "userRoles",
                    in = ParameterIn.HEADER,
                    example = "Admin"
            )},
            responses = @ApiResponse(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = User.class))
                    )
            )
    )
    public ResponseEntity<List<User>> getAllUsers(HttpServletRequest request) throws AccessDeniedException {
        checkAdminPermission(request.getHeader("userRoles"));
        return new ResponseEntity<List<User>>(usersService.allUsers(), HttpStatus.OK);
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
                            description = "User with requested email not found.",
                            content = @Content(schema = @Schema(implementation = Void.class))
                    ),
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserByEmail(HttpServletRequest request, @PathVariable("id") String email) throws AccessDeniedException {
        // Allow user to read their own data, but only admins get read other users data.
        if (!email.equals(request.getHeader("userEmail"))) {
            checkAdminPermission(request.getHeader("userRoles"));
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
            parameters = {@Parameter(
                    name = "userRoles",
                    in = ParameterIn.HEADER,
                    example = "Admin"
            )},
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
                            description = "Request data failed validation.",
                            content = @Content(schema = @Schema(implementation = Void.class))
                    )
            }
    )
    public ResponseEntity<User> createUser(HttpServletRequest request, @RequestBody String payload) throws AccessDeniedException, JsonProcessingException {
        checkAdminPermission(request.getHeader("userRoles"));
        JsonSchemaValidator.validate("model/user.schema.json", payload);
        return new ResponseEntity<User>(usersService.createUser(payload), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update user",
            description = "Update a particular user, by the email. Replaces all existing data for the user.",
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
                            description = "Request data failed validation.",
                            content = @Content(schema = @Schema(implementation = Void.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User with requested email was not found.",
                            content = @Content(schema = @Schema(implementation = Void.class))
                    )
            }
    )
    public ResponseEntity<User> updateUser(HttpServletRequest request, @RequestBody String payload, @PathVariable("id") String email) throws AccessDeniedException, JsonProcessingException {
        checkAdminPermission(request.getHeader("userRoles"));
        JsonSchemaValidator.validate("model/user.schema.json", payload);
        return new ResponseEntity<>(usersService.updateByEmail(email, payload), HttpStatus.OK);
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
                            description = "User successfully deleted.",
                            content = @Content(schema = @Schema(implementation = Void.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User with requested email was not found.",
                            content = @Content(schema = @Schema(implementation = Void.class)))
            }
    )
    public ResponseEntity<HttpStatus> deleteUser(HttpServletRequest request, @PathVariable("id") String email) throws AccessDeniedException {
        checkAdminPermission(request.getHeader("userRoles"));
        usersService.deleteByEmail(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private boolean checkAdminPermission(String roles) throws AccessDeniedException {
        if (roles == null) {
            throw new AccessDeniedException("Access denied");
        }
        String[] userRoles = roles.split(", ");
        boolean isAdmin = Arrays.stream(userRoles).anyMatch(x -> "Admin".equals(x));
        if (!isAdmin) {
            throw new AccessDeniedException("Access denied");
        }
        return true;
    }
}
