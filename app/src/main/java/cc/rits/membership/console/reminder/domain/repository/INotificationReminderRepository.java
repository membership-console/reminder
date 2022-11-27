package cc.rits.membership.console.reminder.domain.repository;

import java.util.List;

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
     * IDリストからリマインダーリストを削除
     * 
     * @param ids リマインダーIDリスト
     */
    void deleteByIds(final List<Integer> ids);

    /**
     * IDからリマインダーの存在チェック
     * 
     * @param id リマインダーID
     * @return チェック結果
     */
    boolean existsById(final Integer id);

}
