package cc.rits.membership.console.reminder.infrastructure.factory;

import org.springframework.stereotype.Component;

import cc.rits.membership.console.reminder.domain.model.NotificationReminderModel;
import cc.rits.membership.console.reminder.infrastructure.db.entity.NotificationReminder;

/**
 * リマインダーファクトリ
 */
@Component
public class NotificationReminderFactory {

    /**
     * NotificationReminderを作成
     *
     * @param notificationReminderModel model
     * @return entity
     */
    public NotificationReminder createNotificationReminder(final NotificationReminderModel notificationReminderModel) {
        return NotificationReminder.builder() //
            .id(notificationReminderModel.getId()) //
            .notificationId(notificationReminderModel.getNotificationId()) //
            .reminderDate(notificationReminderModel.getReminderDate()) //
            .build();
    }

}
