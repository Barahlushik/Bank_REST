package com.example.bankcards.controller;

import com.example.bankcards.dto.*;
import com.example.bankcards.entity.enums.CardStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base-path}/admin/cards")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {


    @Operation(summary = "Get all cards with filtering")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "Cards retrieved successfully")
    public ResponseEntity<Page<CardDto>> getAllCards(
            @ParameterObject CardFilter filter,
            @ParameterObject Pageable pageable){}


    @Operation(summary = "Create card")
    @PostMapping("/create")
    @ApiResponse(responseCode = "201", description = "Card created")
    public ResponseEntity<CreateCardResponse> create(@RequestBody @Valid CreateCardRequest req) {

    }

    @Operation(summary = "Update card")
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody @Valid UpdateCardRequest req) {

    }

    @Operation(summary = "Set card status")
    @PatchMapping("/{id}/status/{status}")
    public ResponseEntity<SetCardStatusResponse> setCardStatus(
            @Parameter(description = "Card ID", example = "1")
            @PathVariable @Min(1) Long id,
            @Parameter(description = "Card status")
            @PathVariable CardStatus status
                                                              ) {
    }

    @Operation(summary = "Delete card")
    @ApiResponse(responseCode = "204", description = "Card deleted")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Card ID", example = "1")
            @PathVariable Long id) {
    }

}
