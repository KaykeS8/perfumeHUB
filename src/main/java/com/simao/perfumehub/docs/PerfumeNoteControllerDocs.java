package com.simao.perfumehub.docs;

import com.simao.perfumehub.dtos.perfumeNote.PerfumeNoteRequestDto;
import com.simao.perfumehub.dtos.perfumeNote.PerfumeNoteResponseDto;
import com.simao.perfumehub.exceptions.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(
        name = "Perfume Notes",
        description = "Operations for managing the associations between perfumes and olfactory notes."
)
public interface PerfumeNoteControllerDocs {
    @Operation(
            operationId = "createPerfumeNote",
            summary = "Associate an olfactory note with a perfume",
            description = """
                Associates an existing olfactory note with a specific perfume.

                The perfume must exist and is identified by its ID. The olfactory
                note must also exist and is identified by the note ID provided
                in the request body.

                The association includes a note type, which defines the role of
                the olfactory note in the perfume's composition.

                The same olfactory note can be associated with the same perfume
                using different note types. However, duplicate associations with
                the same perfume, olfactory note, and note type are not allowed.

                Returns the created association, including its identifier,
                associated perfume, olfactory note, and note type.

                Requires authentication and the ADMIN or EMPLOYEE role.
                """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Perfume Note created"),
            @ApiResponse(responseCode = "404", description = "Perfume or olfactory note was not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error or unreadable body", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authenticated but without ADMIN or EMPLOYEE", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "note with this type is already associated with the perfume", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<PerfumeNoteResponseDto> createPerfumeNote(Long perfumeId, PerfumeNoteRequestDto dto);


    @Operation(
            operationId = "listPerfumeNotes",
            summary = "List olfactory notes associated with a perfume",
            description = """
                Retrieves all olfactory notes associated with a specific perfume,
                identified by its ID.

                Each result contains the association details, including the
                olfactory note and its type in the perfume's composition.

                Returns an empty list if the perfume has no associated olfactory
                notes.

                Requires the perfume to exist. This endpoint is publicly
                accessible and does not require authentication.
                """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of perfumes notes", content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = PerfumeNoteResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "Perfume not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<List<PerfumeNoteResponseDto>> getAll(Long perfumeId);


    @Operation(
            operationId = "deletePerfumeNote",
            summary = "Delete a perfume note",
            description = """
            Deletes an existing association between a perfume and an olfactory note,
            identified by the association ID.
    
            The deletion removes the perfume note association without deleting the
            perfume or the olfactory note itself.
    
            Returns HTTP 404 if the perfume note association does not exist.
    
            Requires authentication and the ADMIN role.
            """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfume note deleted"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authenticated but without ADMIN", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Perfume not found or perfume note", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<Void> deletePerfumeNote(Long perfumeId, Long id);
}
