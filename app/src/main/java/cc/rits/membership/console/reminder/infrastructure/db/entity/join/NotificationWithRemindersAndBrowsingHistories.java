package cc.rits.membership.console.reminder.infrastructure.db.entity.join;

import java.util.List;

import cc.rits.membership.console.reminder.infrastructure.db.entity.Notification;
import cc.rits.membership.console.reminder.infrastructure.db.entity.NotificationBrowsingHistory;
import cc.rits.membership.console.reminder.infrastructure.db.entity.NotificationReminder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * お知らせ + リマインダーリスト + 閲覧履歴リスト
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NotificationWithRemindersAndBrowsingHistories extends Notification {

    /**
     * リマインダーリスト
     */
    List<NotificationReminder> reminders;

    /**
     * 閲覧履歴リスト
     */
    List<NotificationBrowsingHistory> browsingHistories;

}
