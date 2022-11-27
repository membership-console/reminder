package cc.rits.membership.console.reminder.infrastructure.api.controller.front;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cc.rits.membership.console.reminder.auth.LoginUserDetails;
import cc.rits.membership.console.reminder.infrastructure.api.request.NotificationReminderCreateRequest;
import cc.rits.membership.console.reminder.infrastructure.api.validation.RequestValidated;
import cc.rits.membership.console.reminder.usecase.front.notification_reminder.CreateNotificationReminderUseCase;
import cc.rits.membership.console.reminder.usecase.front.notification_reminder.DeleteNotificationReminderUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * リマインダーコントローラ
 */
@Tag(name = "Notification Reminder", description = "リマインダー")
@RestController
@RequestMapping(path = "/api/notifications/{notification_id}/reminders", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
public class NotificationReminderRestController {

    private final CreateNotificationReminderUseCase createNotificationReminderUseCase;

    private final DeleteNotificationReminderUseCase deleteNotificationReminderUseCase;

    /**
     * リマインダー作成API
     *
     * @param loginUser ログインユーザ
     * @param notificationId お知らせID
     * @param requestBody リマインダー作成リクエスト
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createNotificationReminder( //
        @AuthenticationPrincipal final LoginUserDetails loginUser, //
        @PathVariable("notification_id") final Integer notificationId, //
        @RequestValidated @RequestBody final NotificationReminderCreateRequest requestBody //
    ) {
        this.createNotificationReminderUseCase.handle(loginUser, notificationId, requestBody);
    }

    /**
     * リマインダー削除API
     *
     * @param loginUser ログインユーザ
     * @param notificationId お知らせID
     * @param notificationReminderId リマインダーID
     */
    @DeleteMapping("/{notification_reminder_id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteNotificationReminder( //
        @AuthenticationPrincipal final LoginUserDetails loginUser, //
        @PathVariable("notification_id") final Integer notificationId, //
        @PathVariable("notification_reminder_id") final Integer notificationReminderId //
    ) {
        this.deleteNotificationReminderUseCase.handle(loginUser, notificationId, notificationReminderId);
    }

}
