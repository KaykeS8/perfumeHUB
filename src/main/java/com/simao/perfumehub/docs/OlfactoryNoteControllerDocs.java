package com.simao.perfumehub.docs;

import com.simao.perfumehub.docs.schemas.OlfactoryNotePaginationResponseDto;
import com.simao.perfumehub.dtos.PaginationDto;
import com.simao.perfumehub.dtos.olfactoryNote.OlfactoryNoteRequestDto;
import com.simao.perfumehub.dtos.olfactoryNote.OlfactoryNoteResponseDto;
import com.simao.perfumehub.exceptions.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Olfactory Note", description = "CRUD of olfactory note. Reads are public. Create and update require ADMIN or EMPLOYEE. Delete requires ADMIN.")
public interface OlfactoryNoteControllerDocs {

    @Operation(
            operationId = "createOlfactoryNote",
            summary = "Create a olfactory note",
            description = """
            Registers a new olfactory note in the catalog.
    
            The olfactory note name must be unique, regardless of case.
            If an olfactory note with the same name already exists, the request
            is rejected with HTTP 409 Conflict.
    
            Returns the created olfactory note, including its identifier
            and registered information.
    
            Requires authentication and the ADMIN or EMPLOYEE role.
            """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Olfactory note created"),
            @ApiResponse(responseCode = "400", description = "Validation error or unreadable body", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authenticated but without ADMIN or EMPLOYEE", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Olfactory note name already exists", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<OlfactoryNoteResponseDto> createOlfactoryNote(OlfactoryNoteRequestDto dto);

    @Operation(
            operationId = "listOlfactoryNote",
            summary = "List olfactory notes",
            description ="""
                Retrieves a paginated list of olfactory notes registered in the catalog.
        
                Supports pagination and sorting through Spring Data parameters:
                - `page`: zero-based page index.
                - `size`: number of olfactory notes per page.
                - `sort`: sorting criteria (e.g., `sort=name,asc`).
        
                Returns a paginated response containing the olfactory notes and
                pagination metadata, including the total number of elements and pages.
        
                This endpoint is publicly accessible and does not require authentication.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of olfactory notes", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = OlfactoryNotePaginationResponseDto.class)))
    })
    ResponseEntity<PaginationDto<OlfactoryNoteResponseDto>> getAll(Pageable pageable);

    @Operation(
            operationId = "getOlfactoryNoteById",
            summary = "Get olfactory note by ID",
            description = """
                Retrieves the details of a specific olfactory note using its unique identifier.

                Returns the olfactory note information mapped to the response DTO.

                Returns HTTP 404 if no olfactory note is found with the provided ID.

                This endpoint is publicly accessible and does not require authentication.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Olfactory note found successfully"),
            @ApiResponse(responseCode = "404", description = "Olfactory note not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            )
    })
    ResponseEntity<OlfactoryNoteResponseDto> findOlfactoryNoteById(Long id);


    @Operation(
            operationId = "updateOlfactoryNote",
            summary = "Update an olfactory note",
            description = """
                Updates an existing olfactory note identified by its ID.

                The request provides the olfactory note's name. The name must be
                unique, regardless of case. Keeping the current name is allowed,
                but using the name of another existing olfactory note is not.

                Returns the updated olfactory note upon successful completion.

                Returns HTTP 404 if no olfactory note is found with the provided ID,
                or HTTP 409 if another olfactory note already uses the requested name.

                Requires authentication and the ADMIN or EMPLOYEE role.
                """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Olfactory note updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error or unreadable request body", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authenticated but without ADMIN or EMPLOYEE role", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Olfactory note not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Another olfactory note already uses the requested name", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    ResponseEntity<OlfactoryNoteResponseDto> updateOlfactoryNote(Long id, OlfactoryNoteRequestDto dto);


    @Operation(
            operationId = "deleteOlfactoryNote",
            summary = "Delete an olfactory note",
            description = """
                Deletes an existing olfactory note identified by its ID.

                Returns HTTP 404 if no olfactory note is found with the provided ID.

                Requires authentication and the ADMIN role.
                """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Olfactory note deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authenticated but without ADMIN role", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Olfactory note not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<Void> deleteOlfactoryNote(Long id);
}
