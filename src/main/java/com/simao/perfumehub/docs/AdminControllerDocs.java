package com.simao.perfumehub.docs;

import com.simao.perfumehub.dtos.user.ChangeUserRoleRequestDto;
import com.simao.perfumehub.dtos.user.UserRoleResponseDto;
import com.simao.perfumehub.exceptions.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(
        name = "Administration",
        description = "Endpoints for administrative operations, including user role management."
)
public interface AdminControllerDocs {


    @Operation(
            summary = "Change user's role",
            description = """
            Updates the role assigned to a specific user.
            This operation is restricted to authenticated administrators.

            Administrators cannot change their own roles, and the role of the
            last remaining administrator cannot be downgraded. The requested
            role must be a valid role supported by the application.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Change role user with successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or unsupported role.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Authentication is required.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "The authenticated user does not have administrator privileges.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "The specified user was not found.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = """
                The role change conflicts with a business rule.
                For example, the administrator is attempting to change their
                own role, downgrade the last administrator, or assign the
                role the user already has.
                """, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    ResponseEntity<UserRoleResponseDto> changeRoleUser(Long userId, ChangeUserRoleRequestDto dto);
}
