package cc.rits.membership.console.reminder.domain.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import cc.rits.membership.console.reminder.infrastructure.db.entity.NotificationReminder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * リマインダーモデル
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationReminderModel implements Serializable {

    /**
     * リマインダーID
     */
    Integer id;

    /**
     * お知らせID
     */
    Integer notificationId;

    /**
     * リマインド日時
     */
    LocalDateTime reminderDate;

    public NotificationReminderModel(final NotificationReminder notificationReminder) {
        this.id = notificationReminder.getId();
        this.notificationId = notificationReminder.getNotificationId();
        this.reminderDate = notificationReminder.getReminderDate();
    }

}
