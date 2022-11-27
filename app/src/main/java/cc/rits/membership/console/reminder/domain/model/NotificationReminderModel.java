package cc.rits.membership.console.reminder.domain.model;

import java.io.Serializable;
import java.time.LocalDate;

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
     * リマインド予定日
     */
    LocalDate scheduledDate;

    public NotificationReminderModel(final NotificationReminder notificationReminder) {
        this.id = notificationReminder.getId();
        this.notificationId = notificationReminder.getNotificationId();
        this.scheduledDate = notificationReminder.getScheduledDate();
    }

    /**
     * 〜にリマインド予定されているかチェック
     * 
     * @param date リマインド予定日
     * @return チェック結果
     */
    public boolean isScheduledFor(final LocalDate date) {
        // 障害で同じ日にリマインドできない場合を考慮して、過去のものもtrue判定する
        return this.getScheduledDate().isEqual(date) || this.getScheduledDate().isBefore(date);
    }

}
