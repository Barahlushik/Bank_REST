package com.example.bankcards.controller;

import com.example.bankcards.dto.common.CardDto;
import com.example.bankcards.dto.user.*;
import com.example.bankcards.service.api.card.UserCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/user/cards")
@PreAuthorize("hasRole('USER')")
@Tag(name = "User Cards", description = "Операции пользователя с собственными картами")
public class UserController {

    private final UserCardService cardService;

    @Operation(
            summary = "Список карт пользователя",
            description = "Возвращает все карты, принадлежащие текущему пользователю (по principal), с поддержкой пагинации.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Карты успешно получены",
                            content = @Content(schema = @Schema(implementation = CardDto.class))),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            }
    )
    @GetMapping
    public ResponseEntity<Page<CardDto>> listCards(
            @AuthenticationPrincipal UserDetails principal,
            Pageable pageable) {
        return ResponseEntity.ok(cardService.getCardsByUsername(principal.getUsername(), pageable));
    }

    @Operation(
            summary = "Получить баланс карты",
            description = "Возвращает текущий баланс указанной карты, если она принадлежит пользователю.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Баланс успешно получен",
                            content = @Content(schema = @Schema(implementation = BalanceResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Карта не найдена или не принадлежит пользователю"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            }
    )
    @GetMapping("/{cardId}/balance")
    public ResponseEntity<BalanceResponse> getMyCardBalance(
            @AuthenticationPrincipal UserDetails principal,
            @Parameter(description = "ID карты", example = "100")
            @PathVariable @Min(1) Long cardId) {
        return ResponseEntity.ok(cardService.getCardBalance(principal.getUsername(), cardId));
    }

    @Operation(
            summary = "Перевод между своими картами",
            description = """
                    Выполняет перевод средств между двумя картами пользователя.
                    Поддерживает идемпотентность (повторный запрос с тем же ключом может вернуть 409).
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Перевод успешно выполнен",
                            content = @Content(schema = @Schema(implementation = TransferBetweenMyCardsResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Ошибка валидации или одинаковая карта-отправитель/получатель"),
                    @ApiResponse(responseCode = "403", description = "Карта не принадлежит пользователю"),
                    @ApiResponse(responseCode = "409", description = "Конфликт идемпотентности или параллельного перевода"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            }
    )
    @PostMapping("/transfer")
    public ResponseEntity<TransferBetweenMyCardsResponse> transferBetweenMyCards(
            @AuthenticationPrincipal UserDetails principal,
            @RequestBody(
                    description = "Данные для перевода между картами (ID источника, ID получателя, сумма)",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TransferBetweenMyCardsRequest.class))
            )
            @Valid @org.springframework.web.bind.annotation.RequestBody TransferBetweenMyCardsRequest req) {
        return ResponseEntity.ok(cardService.transfer(principal.getUsername(), req));
    }

    @Operation(
            summary = "Запрос на блокировку карты",
            description = """
                    Отправляет запрос на блокировку карты (например, при утере).
                    Запрос принимается асинхронно (HTTP 202 Accepted).
                    """,
            responses = {
                    @ApiResponse(responseCode = "202", description = "Запрос на блокировку принят",
                            content = @Content(schema = @Schema(implementation = CardBlockResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Карта не найдена или не принадлежит пользователю"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            }
    )
    @PostMapping("/{cardId}/block-request")
    public ResponseEntity<CardBlockResponse> createCardBlockRequest(
            @AuthenticationPrincipal UserDetails principal,
            @Parameter(description = "ID карты", example = "150")
            @PathVariable @Min(1) Long cardId,
            @RequestBody(
                    description = "Причина блокировки карты",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CardBlockRequest.class))
            )
            @Valid @org.springframework.web.bind.annotation.RequestBody CardBlockRequest req) {
        return ResponseEntity.accepted()
                .body(cardService.requestBlock(principal.getUsername(), cardId, req));
    }
}
