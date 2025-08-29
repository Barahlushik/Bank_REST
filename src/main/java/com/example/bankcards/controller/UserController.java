package com.example.bankcards.controller;


import com.example.bankcards.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base-path}/user/cards")
@PreAuthorize("hasRole('USER')")
public class UserController {

    @Operation(summary = "List my cards")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "Cards retrieved successfully")
    public ResponseEntity<Page<CardDto>> listMyCards(
            @AuthenticationPrincipal UserDetails principal, Pageable pageable) {
    }

    @Operation(summary = "Get balance of my card")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Balance retrieved"),
            @ApiResponse(responseCode = "404", description = "Card not found or not owned by user")
    })

    @GetMapping("/{cardId}/balance")
    public ResponseEntity<BalanceResponse> getMyCardBalance(
            @AuthenticationPrincipal UserDetails principal,
            @Parameter(description = "Card ID", example = "100")
            @PathVariable @Min(1) Long cardId) {
    }

    @Operation(summary = "Transfer between my cards")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transfer completed"),
            @ApiResponse(responseCode = "400", description = "Validation error or same card"),
            @ApiResponse(responseCode = "403", description = "Card does not belong to the user"),
            @ApiResponse(responseCode = "409", description = "Idempotency conflict or concurrency issue"),
    })
    @PostMapping("/transfer")
    public ResponseEntity<TransferBetweenMyCardsResponse> transferBetweenMyCards(
            @AuthenticationPrincipal UserDetails principal,
            @RequestHeader(name = "Idempotency-Key", required = false)
            String idempotencyKey,
            @RequestBody @Valid TransferBetweenMyCardsRequest req) {
    }


    @Operation(summary = "Request card block")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Block request accepted"),
            @ApiResponse(responseCode = "404", description = "Card not found or not owned by user")
    })
    @PostMapping("/{cardId}/block-request")
    public ResponseEntity<BlockRequestResponse> createCardBlockRequest(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable @Min(1) Long cardId,
            @RequestBody @Valid CardBlockRequest req) {}
}
                                                                                                              ){}


