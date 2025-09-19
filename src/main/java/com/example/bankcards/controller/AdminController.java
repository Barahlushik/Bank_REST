package com.example.bankcards.controller;

import com.example.bankcards.dto.admin.*;
import com.example.bankcards.dto.admin.create.CreateCardRequest;
import com.example.bankcards.dto.admin.create.CreateCardResponse;
import com.example.bankcards.dto.admin.update.UpdateCardRequest;
import com.example.bankcards.dto.admin.update.UpdateCardResponse;
import com.example.bankcards.dto.common.CardDto;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.service.api.card.AdminCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/cards")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Cards", description = "Ручки для администратора")
public class AdminController {

    private final AdminCardService cardService;

    @Operation(
            summary = "Получить список карт",
            description = "Возвращает страницу карт с возможностью фильтрации и пагинации",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список карт получен",
                            content = @Content(schema = @Schema(implementation = CardDto.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные параметры фильтра/пагинации"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            }
    )
    @GetMapping
    public ResponseEntity<Page<CardDto>> getAllCards(
            @ParameterObject CardFilter filter,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(cardService.findAll(filter, pageable));
    }

    @Operation(
            summary = "Создать новую карту",
            description = "Создает новую карту на основе входных данных",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Карта успешно создана",
                            content = @Content(schema = @Schema(implementation = CreateCardResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
                    @ApiResponse(responseCode = "409", description = "Карта с таким номером уже существует"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            }
    )
    @PostMapping
    public ResponseEntity<CreateCardResponse> create(@RequestBody @Valid CreateCardRequest req) {
        CreateCardResponse created = cardService.create(req);
        return ResponseEntity
                .created(URI.create(String.format("/admin/cards/%d", created.id())))
                .body(created);
    }

    @Operation(
            summary = "Обновить карту",
            description = "Обновляет существующую карту по её ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Карта обновлена",
                            content = @Content(schema = @Schema(implementation = UpdateCardResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные"),
                    @ApiResponse(responseCode = "404", description = "Карта не найдена"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<UpdateCardResponse> update(
            @PathVariable @Min(1) Long id,
            @RequestBody @Valid UpdateCardRequest req) {
        return ResponseEntity.ok(cardService.update(req));
    }

    @Operation(
            summary = "Изменить статус карты",
            description = "Меняет статус карты (например: ACTIVE, BLOCKED, EXPIRED)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Статус карты изменен",
                            content = @Content(schema = @Schema(implementation = SetCardStatusResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректный статус"),
                    @ApiResponse(responseCode = "404", description = "Карта не найдена")
            }
    )
    @PatchMapping("/{id}/status/{status}")
    public ResponseEntity<SetCardStatusResponse> setCardStatus(
            @Parameter(description = "Card ID", example = "1")
            @PathVariable @Min(1) Long id,
            @Parameter(description = "Card status")
            @PathVariable CardStatus status) {
        return ResponseEntity.ok(cardService.setStatus(id, status));
    }

    @Operation(
            summary = "Удалить карту",
            description = "Удаляет карту по её ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Карта удалена"),
                    @ApiResponse(responseCode = "404", description = "Карта не найдена"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Card ID", example = "1")
            @PathVariable @Min(1) Long id) {
        cardService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
