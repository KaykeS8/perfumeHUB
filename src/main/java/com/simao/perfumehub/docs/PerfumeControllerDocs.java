package com.simao.perfumehub.docs;

import com.simao.perfumehub.dtos.PaginationDto;
import com.simao.perfumehub.dtos.brand.BrandRequestDto;
import com.simao.perfumehub.dtos.brand.BrandResponseDto;
import com.simao.perfumehub.dtos.perfume.PerfumeRequestDto;
import com.simao.perfumehub.dtos.perfume.PerfumeResponseDto;
import com.simao.perfumehub.exceptions.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Tag(
        name = "Perfumes",
        description = "CRUD of perfume. Reads are public. Create and update require ADMIN or EMPLOYEE. Delete requires ADMIN."
)
public interface PerfumeApi {
    @Operation(
            operationId = "createPerfume",
            summary = "Create a perfume",
            description = "Registers a new perfume. The name must be unique (case-insensitive). Requires role ADMIN or EMPLOYEE.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Perfume created", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PerfumeResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error or unreadable body", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authenticated but without ADMIN or EMPLOYEE", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Perfume name already exists", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<PerfumeResponseDto> createPerfume(PerfumeRequestDto dto);


    @Operation(
            operationId = "listPerfumes",
            summary = "List perfumes",
            description = "Returns a paginated list of perfumes. Public endpoint. Supports Spring Data query params `page`, `size` and `sort` (e.g. `sort=name,asc`)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of perfumes")
    })
    ResponseEntity<PaginationDto<PerfumeResponseDto>> getAll(@ParameterObject Pageable pageable);


    @Operation(
            operationId = "gerPerfumeById",
            summary = "Get perfume by ID",
            description = "Returns a single perfume. Public endpoint."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "perfume found"),
            @ApiResponse(responseCode = "404", description = "Perfume not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<PerfumeResponseDto> findPerfumeById(Long id);

    @Operation(
            operationId = "updateBrand",
            summary = "Update a brand",
            description = "Replaces the brand name. The new name must be unique (case-insensitive), unless it is unchanged. Requires role ADMIN or EMPLOYEE.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Brand updated",
//                    content = @Content(
//                            mediaType = MediaType.APPLICATION_JSON_VALUE,
//                            schema = @Schema(implementation = BrandResponseDto.class),
//                            examples = @ExampleObject(value = "{\"id\":1,\"name\":\"Christian Dior\"}")
//                    )
//            ),
//            @ApiResponse(responseCode = "400", description = "Validation error or unreadable body", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
//            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
//            @ApiResponse(responseCode = "403", description = "Authenticated but without ADMIN or EMPLOYEE", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
//            @ApiResponse(responseCode = "404", description = "Brand not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
//            @ApiResponse(responseCode = "409", description = "Brand name already exists", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class)))
//    })
//    ResponseEntity<BrandResponseDto> updateBrand(Long id, BrandRequestDto dto);
//
//
//    @Operation(
//            operationId = "deleteBrand",
//            summary = "Delete a brand",
//            description = "Removes the brand by ID. Requires role ADMIN.",
//            security = @SecurityRequirement(name = "bearerAuth")
//    )
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Brand deleted",
//                    content = @Content(
//                            mediaType = MediaType.APPLICATION_JSON_VALUE,
//                            examples = @ExampleObject(value = "{\"Message\":\"Brand with ID: 1 was successfuly deleted\"}")
//                    )
//            ),
//            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
//            @ApiResponse(responseCode = "403", description = "Authenticated but without ADMIN", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
//            @ApiResponse(responseCode = "404", description = "Brand not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class)))
//    })
//    ResponseEntity<Map<String, String>> deleteBrand(Long id);
}
