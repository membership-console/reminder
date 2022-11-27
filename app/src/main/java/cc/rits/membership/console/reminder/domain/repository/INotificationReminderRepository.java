package cc.rits.membership.console.reminder.domain.repository;

import cc.rits.membership.console.reminder.domain.model.NotificationReminderModel;

/**
 * リマインダーリポジトリ
 */
public interface INotificationReminderRepository {

    /**
     * リマインダーを作成
     * 
     * @param notificationReminderModel リマインダー
     */
    void insert(final NotificationReminderModel notificationReminderModel);

}
