package com.simao.perfumehub.docs;

import com.simao.perfumehub.docs.schemas.PerfumePaginationResponseDto;
import com.simao.perfumehub.dtos.PaginationDto;
import com.simao.perfumehub.dtos.perfume.PerfumeRequestDto;
import com.simao.perfumehub.dtos.perfume.PerfumeResponseDto;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Tag(
        name = "Perfumes",
        description = "CRUD of perfume. Reads are public. Create and update require ADMIN or EMPLOYEE. Delete requires ADMIN."
)
public interface PerfumeControllerDocs {
    @Operation(
            operationId = "createPerfume",
            summary = "Create a perfume",
            description = """
                Registers a new perfume in the catalog.

                The perfume must have a unique name, regardless of case, and must be
                associated with an existing brand identified by its ID.

                The request includes the perfume's name, description, price, stock
                quantity, genre, concentration, and brand ID.

                Returns the created perfume, including its identifier and associated
                brand information.

                Requires authentication and the ADMIN or EMPLOYEE role.
                """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Perfume created"),
            @ApiResponse(responseCode = "404", description = "Brand not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error or unreadable body", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authenticated but without ADMIN or EMPLOYEE", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Perfume name already exists", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<PerfumeResponseDto> createPerfume(PerfumeRequestDto dto);


    @Operation(
            operationId = "listPerfumes",
            summary = "List perfumes",
            description = """
                Retrieves a paginated list of perfumes with optional filtering by brand, 
                genre, concentration, price range, name, and olfactory note.

                Supports pagination and sorting through Spring Data parameters:
                - `page`: zero-based page index.
                - `size`: number of perfumes per page.
                - `sort`: sorting criteria (e.g., `sort=name,asc`).

                All filters are optional and can be combined to refine the results.
                This endpoint is publicly accessible and does not require authentication.
                """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of perfumes", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PerfumePaginationResponseDto.class)))
    })
    ResponseEntity<PaginationDto<PerfumeResponseDto>> getAll(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String concentration,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String note,
            Pageable pageable
    );


    @Operation(
            operationId = "gerPerfumeById",
            summary = "Get perfume by ID",
            description = """
                Retrieves the details of a specific perfume using its unique identifier.

                The response includes the perfume's name, description, price, available
                stock quantity, genre, concentration, associated brand, and olfactory notes.

                Returns HTTP 404 if no perfume is found with the provided ID.

                This endpoint is publicly accessible and does not require authentication.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "perfume found"),
            @ApiResponse(responseCode = "404", description = "Perfume not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<PerfumeResponseDto> findPerfumeById(Long id);

    @Operation(
            operationId = "updatePerfume",
            summary = "Update a Perfume",
            description = """
                Updates the details of an existing perfume identified by its ID.

                Updates the perfume's name, description, price, genre, concentration,
                and associated brand. The provided brand ID must correspond to an
                existing brand.

                The perfume name must be unique, regardless of case. If the name is
                already used by another perfume, the request is rejected. Keeping the
                current perfume's name is allowed.

                Returns the updated perfume details upon successful completion.

                Requires authentication and the ADMIN or EMPLOYEE role.
                """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfume updated"),
            @ApiResponse(responseCode = "400", description = "Validation error or unreadable body", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authenticated but without ADMIN or EMPLOYEE", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Perfume or associated brand not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Perfume name already exists", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<PerfumeResponseDto> updatePerfume(Long id, PerfumeRequestDto dto);


    @Operation(
            operationId = "deletePerfume",
            summary = "Delete a perfume",
            description = """
                Deletes an existing perfume identified by its ID.

                A perfume can only be deleted if it exists and has no associated stock
                movements. Deletion is rejected if stock movements are registered for
                the perfume, preserving inventory transaction history.

                Returns HTTP 404 if the perfume does not exist, or HTTP 409 if the
                perfume has associated stock movements.

                Requires authentication and role ADMIN.
                """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Brand deleted"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authenticated but without ADMIN", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Perfume not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<Void> deletePerfume(Long id);
}
