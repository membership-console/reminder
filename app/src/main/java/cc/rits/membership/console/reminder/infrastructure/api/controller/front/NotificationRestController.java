package cc.rits.membership.console.reminder.infrastructure.api.controller.front;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cc.rits.membership.console.reminder.auth.LoginUserDetails;
import cc.rits.membership.console.reminder.infrastructure.api.request.NotificationCreateRequest;
import cc.rits.membership.console.reminder.infrastructure.api.response.NotificationResponse;
import cc.rits.membership.console.reminder.infrastructure.api.response.NotificationsResponse;
import cc.rits.membership.console.reminder.infrastructure.api.validation.RequestValidated;
import cc.rits.membership.console.reminder.usecase.front.notification.CreateNotificationUseCase;
import cc.rits.membership.console.reminder.usecase.front.notification.DeleteNotificationUseCase;
import cc.rits.membership.console.reminder.usecase.front.notification.GetNotificationsUseCase;
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

    private final CreateNotificationUseCase createNotificationUseCase;

    private final DeleteNotificationUseCase deleteNotificationUseCase;

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
            .toList();
        return new NotificationsResponse(notifications);
    }

    /**
     * お知らせ作成API
     *
     * @param loginUser ログインユーザ
     * @param requestBody お知らせ作成リクエスト
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createNotification( //
        @AuthenticationPrincipal final LoginUserDetails loginUser, //
        @RequestValidated @RequestBody final NotificationCreateRequest requestBody //
    ) {
        this.createNotificationUseCase.handle(loginUser, requestBody);
    }

    /**
     * お知らせ削除API
     *
     * @param loginUser ログインユーザ
     * @param notificationId お知らせID
     */
    @DeleteMapping("/{notification_id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteNotification( //
        @AuthenticationPrincipal final LoginUserDetails loginUser, //
        @PathVariable("notification_id") final Integer notificationId //
    ) {
        this.deleteNotificationUseCase.handle(loginUser, notificationId);
    }

}
