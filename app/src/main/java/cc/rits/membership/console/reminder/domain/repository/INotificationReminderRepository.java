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

    /**
     * IDからリマインダーを削除
     * 
     * @param id リマインダーID
     */
    void deleteById(final Integer id);

    /**
     * IDからリマインダーの存在チェック
     * 
     * @param id リマインダーID
     * @return チェック結果
     */
    boolean existsById(final Integer id);

}
