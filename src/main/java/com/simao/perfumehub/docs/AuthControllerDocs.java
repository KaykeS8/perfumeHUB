package com.simao.perfumehub.docs;

import com.simao.perfumehub.dtos.auth.LoginRequestDto;
import com.simao.perfumehub.dtos.auth.LoginResponseDto;
import com.simao.perfumehub.dtos.auth.RegisterRequestDto;
import com.simao.perfumehub.dtos.auth.UserResponseDto;
import com.simao.perfumehub.exceptions.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(
        name = "Authentication",
        description = "Endpoints for user registration and authentication using JWT."
)
public interface AuthControllerDocs {

    @Operation(
            operationId = "registerUser",
            summary = "Register a new user",
            description = """
                    Registers a new user account.

                    The request includes the user's name, email, and password.
                    The email must not already be registered.

                    Email addresses are normalized to lowercase when checking
                    for existing accounts. New users are assigned the CUSTOMER
                    role by default, and their passwords are securely hashed
                    before being stored.

                    Returns the created user's information, including their
                    identifier, name, email, and role.

                    This endpoint is publicly accessible and does not require
                    authentication.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error or unreadable request body", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class)             )),
            @ApiResponse(responseCode = "409", description = "Email address already registered", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class)             ))
    })
    ResponseEntity<UserResponseDto> register(
            @RequestBody RegisterRequestDto dto
    );

    @Operation(
            operationId = "loginUser",
            summary = "Authenticate a user",
            description = """
                    Authenticates a user using their email address and password.

                    The email address is normalized to lowercase before
                    authentication. If the credentials are valid, the endpoint
                    generates and returns a JWT access token.

                    The response includes the access token, its token type
                    (Bearer), and its expiration time in seconds.

                    This endpoint is publicly accessible and does not require
                    authentication.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authentication successful", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = LoginResponseDto.class)             )),
            @ApiResponse(responseCode = "400", description = "Validation error or unreadable request body", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid email or password", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginRequestDto dto
    );
}