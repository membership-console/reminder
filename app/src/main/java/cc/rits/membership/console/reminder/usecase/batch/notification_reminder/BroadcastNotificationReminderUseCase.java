package cc.rits.membership.console.reminder.usecase.batch.notification_reminder;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.reminder.client.IamClient;
import cc.rits.membership.console.reminder.domain.model.NotificationModel;
import cc.rits.membership.console.reminder.domain.model.NotificationReminderModel;
import cc.rits.membership.console.reminder.domain.model.UserModel;
import cc.rits.membership.console.reminder.domain.repository.INotificationReminderRepository;
import cc.rits.membership.console.reminder.domain.repository.INotificationRepository;
import lombok.RequiredArgsConstructor;

/**
 * リマインダー配信ユースケース
 */
@RequiredArgsConstructor
@Component
@Transactional
public class BroadcastNotificationReminderUseCase {

    private final INotificationRepository notificationRepository;

    private final INotificationReminderRepository notificationReminderRepository;

    private final IamClient iamClient;

    /**
     * Handle UseCase
     */
    public void handle() {
        // お知らせリストを取得
        final var now = LocalDate.now();
        final var notifications = this.notificationRepository.selectAll().stream() //
            .peek(notification -> {
                final var notificationReminders = notification.getReminders().stream() //
                    .filter(notificationReminder -> notificationReminder.isScheduledFor(now)) //
                    .collect(Collectors.toList());
                notification.setReminders(notificationReminders);
            }) //
            .filter(notification -> !notification.getReminders().isEmpty()) //
            .collect(Collectors.toList());

        if (notifications.isEmpty()) {
            return;
        }

        // リマインドを配信する
        final var userIds = this.iamClient.getUsers().stream() //
            .map(UserModel::getId) //
            .collect(Collectors.toList());
        notifications.forEach(notification -> this.iamClient.sendEmail(notification.getTitle(), notification.getBody(), userIds));

        // 配信済みのリマインダーを削除する
        final var notificationReminderIds = notifications.stream() //
            .map(NotificationModel::getReminders) //
            .flatMap(Collection::stream) //
            .map(NotificationReminderModel::getId) //
            .collect(Collectors.toList());
        this.notificationReminderRepository.deleteByIds(notificationReminderIds);
    }

}
