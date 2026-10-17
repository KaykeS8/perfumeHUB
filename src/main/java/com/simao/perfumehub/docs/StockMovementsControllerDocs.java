package com.simao.perfumehub.docs;

import com.simao.perfumehub.docs.schemas.StockMovementPaginationResponseDto;
import com.simao.perfumehub.dtos.PaginationDto;
import com.simao.perfumehub.dtos.stockMovement.StockMovementRequestDto;
import com.simao.perfumehub.dtos.stockMovement.StockMovementResponseDto;
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


@Tag(
        name = "Stock Movements",
        description = "Endpoints for managing and querying perfume stock movements, including inbound, outbound, and stock adjustment operations."
)
public interface StockMovementsControllerDocs {


    @Operation(
            operationId = "registerStockMovement",
            summary = "Register a stock movement",
            description = """
                Registers a stock movement for an existing perfume.

                Supports three movement types:
                - INBOUND: increases the current stock by the requested quantity.
                - OUTBOUND: decreases the current stock by the requested quantity.
                - ADJUSTMENT: sets the stock to the requested quantity.

                The perfume must exist and is identified by the perfume ID provided
                in the request body.

                The resulting stock balance cannot be negative. If an OUTBOUND
                movement would result in a negative balance, the request is rejected
                with HTTP 409 Conflict.

                Updates the perfume's stock quantity and records the movement,
                including its type, quantity, and resulting stock balance.

                Returns the registered stock movement, including its identifier
                and resulting balance.

                Requires authentication and the ADMIN or EMPLOYEE role.
                """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "register movement successfully"),
            @ApiResponse(responseCode = "404", description = "Perfume not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error or unreadable body", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authenticated but without ADMIN or EMPLOYEE", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Insufficient stock for this operation", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<StockMovementResponseDto> registerMovement(StockMovementRequestDto dto);

    @Operation(
            operationId = "list stockmovements",
            summary = "List of stock movements",
            description = """
                Retrieves a paginated list of stock movements with optional filtering
                by perfume ID and movement type.

                Supports the following filters:
                - `perfumeId`: filters movements associated with a specific perfume.
                - `type`: filters movements by type, such as INBOUND, OUTBOUND, or ADJUSTMENT.

                Supports pagination and sorting through Spring Data parameters:
                - `page`: zero-based page index.
                - `size`: number of stock movements per page.
                - `sort`: sorting criteria (e.g., `sort=quantity,asc`).

                Filters are optional and can be combined to refine the results.

                Returns a paginated response containing the stock movements and
                pagination metadata.

                This endpoint is publicly accessible and does not require authentication.
                """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of stock movements", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = StockMovementPaginationResponseDto.class)))
    })
    ResponseEntity<PaginationDto<StockMovementResponseDto>> getAllMovements(Long perfumeId, String type, Pageable pageable);
}
