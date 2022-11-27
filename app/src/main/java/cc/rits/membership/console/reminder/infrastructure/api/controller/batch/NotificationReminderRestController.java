package cc.rits.membership.console.reminder.infrastructure.api.controller.batch;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cc.rits.membership.console.reminder.usecase.batch.notification_reminder.BroadcastNotificationReminderUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * リマインダーコントローラ
 */
@Tag(name = "Notification Reminder", description = "リマインダー")
@RestController
@RequestMapping(path = "/api/batch/notification-reminders", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
public class NotificationReminderRestController {

    private final BroadcastNotificationReminderUseCase broadcastNotificationReminderUseCase;

    /**
     * リマインダー配信API
     */
    @PostMapping("/broadcast")
    @ResponseStatus(HttpStatus.OK)
    public void broadcastNotificationReminder() {
        this.broadcastNotificationReminderUseCase.handle();
    }

}
