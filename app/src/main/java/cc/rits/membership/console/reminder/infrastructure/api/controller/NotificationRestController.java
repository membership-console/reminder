package cc.rits.membership.console.reminder.infrastructure.api.controller;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cc.rits.membership.console.reminder.auth.LoginUserDetails;
import cc.rits.membership.console.reminder.infrastructure.api.response.NotificationResponse;
import cc.rits.membership.console.reminder.infrastructure.api.response.NotificationsResponse;
import cc.rits.membership.console.reminder.usecase.notification.GetNotificationsUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * お知らせコントローラ
 */
@Tag(name = "Notification", description = "お知らせ")
@RestController
@RequestMapping(path = "/api/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
public class NotificationRestController {

    private final GetNotificationsUseCase getNotificationsUseCase;

    /**
     * お知らせリスト取得API
     *
     * @param loginUser ログインユーザ
     * @param onlyUnviewed 未読のみ取得するか
     * @return お知らせリスト
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public NotificationsResponse getNotifications( //
        @AuthenticationPrincipal final LoginUserDetails loginUser, //
        @RequestParam(defaultValue = "false") final Boolean onlyUnviewed //
    ) {
        final var notifications = this.getNotificationsUseCase.handle(loginUser, onlyUnviewed).stream() //
            .map(notification -> new NotificationResponse(notification, loginUser)) //
            .collect(Collectors.toList());
        return new NotificationsResponse(notifications);
    }

}
