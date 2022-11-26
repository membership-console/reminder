package cc.rits.membership.console.reminder.domain.repository;

import java.util.List;

import cc.rits.membership.console.reminder.domain.model.NotificationBrowsingHistoryModel;
import cc.rits.membership.console.reminder.domain.model.NotificationModel;

/**
 * お知らせリポジトリ
 */
public interface INotificationRepository {

    /**
     * お知らせリストを全件取得
     * 
     * @return お知らせリスト
     */
    List<NotificationModel> selectAll();

    /**
     * 閲覧履歴リストを作成する
     * 
     * @param notificationBrowsingHistoryModels 閲覧履歴リスト
     */
    void insertBrowsingHistories(final List<NotificationBrowsingHistoryModel> notificationBrowsingHistoryModels);

}
