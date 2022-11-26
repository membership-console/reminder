package cc.rits.membership.console.reminder.domain.repository;

import java.util.List;
import java.util.Optional;

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
     * IDからお知らせを取得
     *
     * @param id お知らせID
     * @return お知らせ
     */
    Optional<NotificationModel> selectById(final Integer id);

    /**
     * お知らせを作成
     * 
     * @param notificationModel お知らせ
     */
    void insert(final NotificationModel notificationModel);

    /**
     * 閲覧履歴リストを作成
     * 
     * @param notificationBrowsingHistoryModels 閲覧履歴リスト
     */
    void insertBrowsingHistories(final List<NotificationBrowsingHistoryModel> notificationBrowsingHistoryModels);

    /**
     * IDからお知らせを削除
     * 
     * @param id お知らせID
     */
    void deleteById(final Integer id);

    /**
     * IDからお知らせの存在チェック
     * 
     * @param id お知らせID
     * @return チェック結果
     */
    boolean existsById(final Integer id);

}
