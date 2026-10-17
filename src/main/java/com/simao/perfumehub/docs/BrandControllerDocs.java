package com.simao.perfumehub.docs.contracts;

import com.simao.perfumehub.docs.responses.CommonApiResponses;
import com.simao.perfumehub.dtos.PaginationDto;
import com.simao.perfumehub.dtos.brand.BrandRequestDto;
import com.simao.perfumehub.dtos.brand.BrandResponseDto;
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
        name = "Brands",
        description = "CRUD of perfume brands. Reads are public. Create and update require ADMIN or EMPLOYEE. Delete requires ADMIN."
)
public interface BrandControllerDocs {
    @Operation(
            operationId = "createBrand",
            summary = "Create a brand",
            description = "Registers a new brand. The name must be unique (case-insensitive). Requires role ADMIN or EMPLOYEE.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @CommonApiResponses
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Brand created", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BrandResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error or unreadable body", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authenticated but without ADMIN or EMPLOYEE", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Brand name already exists", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<BrandResponseDto> createBrand(BrandRequestDto dto);


    @Operation(
            operationId = "listBrands",
            summary = "List brands",
            description = "Returns a paginated list of brands. Public endpoint. Supports Spring Data query params `page`, `size` and `sort` (e.g. `sort=name,asc`)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of brands")
    })
    ResponseEntity<PaginationDto<BrandResponseDto>> getAll(@ParameterObject Pageable pageable);


    @Operation(
            operationId = "getBrandById",
            summary = "Get brand by ID",
            description = "Returns a single brand. Public endpoint."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Brand found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BrandResponseDto.class),
                            examples = @ExampleObject(value = "{\"id\":1,\"name\":\"Dior\"}")
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Brand not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<BrandResponseDto> findBrandById(Long id);

    @Operation(
            operationId = "updateBrand",
            summary = "Update a brand",
            description = "Replaces the brand name. The new name must be unique (case-insensitive), unless it is unchanged. Requires role ADMIN or EMPLOYEE.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Brand updated",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BrandResponseDto.class),
                            examples = @ExampleObject(value = "{\"id\":1,\"name\":\"Christian Dior\"}")
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Validation error or unreadable body", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authenticated but without ADMIN or EMPLOYEE", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Brand not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Brand name already exists", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<BrandResponseDto> updateBrand(Long id, BrandRequestDto dto);


    @Operation(
            operationId = "deleteBrand",
            summary = "Delete a brand",
            description = "Removes the brand by ID. Requires role ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Brand deleted",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "{\"Message\":\"Brand with ID: 1 was successfuly deleted\"}")
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authenticated but without ADMIN", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Brand not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<Map<String, String>> deleteBrand(Long id);
}
