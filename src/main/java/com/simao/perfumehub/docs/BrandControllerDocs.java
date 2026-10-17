package com.simao.perfumehub.docs;

import com.simao.perfumehub.docs.schemas.BrandPaginationResponseDto;
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

@Tag(
        name = "Brands",
        description = "CRUD of perfume brands. Reads are public. Create and update require ADMIN or EMPLOYEE. Delete requires ADMIN."
)
public interface BrandControllerDocs {
    @Operation(
            operationId = "createBrand",
            summary = "Create a brand",
            description ="""
            Registers a new perfume brand in the catalog.
    
            The brand name must be unique, regardless of case. If another brand
            already exists with the same name, the request is rejected.
    
            Returns the created brand, including its unique identifier and name.
    
            Requires authentication and the ADMIN or EMPLOYEE role.
            """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
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
            description = """
            Retrieves a paginated list of perfume brands registered in the catalog.
    
            Supports pagination and sorting through Spring Data parameters:
            - `page`: zero-based page index.
            - `size`: number of brands per page.
            - `sort`: sorting criteria (e.g., `sort=name,asc`).
    
            Returns pagination metadata along with the brands matching the
            requested page and sorting criteria.
    
            This endpoint is publicly accessible and does not require authentication.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of brands", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BrandPaginationResponseDto.class)))
    })
    ResponseEntity<PaginationDto<BrandResponseDto>> getAll(@ParameterObject Pageable pageable);


    @Operation(
            operationId = "getBrandById",
            summary = "Get brand by ID",
            description = """
            Retrieves the details of a specific perfume brand using its unique identifier.
    
            Returns the brand's identifier and name when the requested brand exists.
    
            Returns HTTP 404 if no brand is found with the provided ID.
    
            This endpoint is publicly accessible and does not require authentication.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Brand found"),
            @ApiResponse(responseCode = "404", description = "Brand not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<BrandResponseDto> findBrandById(Long id);

    @Operation(
            operationId = "updateBrand",
            summary = "Update a brand",
            description = """
            Updates the name of an existing perfume brand identified by its ID.
    
            The new name must be unique, regardless of case. Keeping the current
            brand name is allowed, provided it does not conflict with another brand.
    
            Returns the updated brand details upon successful completion.
    
            Returns HTTP 404 if the brand does not exist, or HTTP 409 if another
            brand already uses the requested name.
    
            Requires authentication and the ADMIN or EMPLOYEE role.
            """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Brand updated"),
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
            description = """
            Deletes an existing perfume brand identified by its ID.
    
            The operation permanently removes the brand from the catalog.
            Deletion may be rejected if the brand is associated with perfumes
            or if other business constraints prevent its removal.
    
            Returns a success response when the brand is deleted successfully.
    
            Returns HTTP 404 if no brand is found with the provided ID.
    
            Requires authentication and the ADMIN role.
            """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Brand deleted"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authenticated but without ADMIN", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Brand not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<Void> deleteBrand(Long id);
}
