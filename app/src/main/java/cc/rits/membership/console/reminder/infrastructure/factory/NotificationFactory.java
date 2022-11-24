package cc.rits.membership.console.reminder.infrastructure.factory;

import org.springframework.stereotype.Component;

import cc.rits.membership.console.reminder.domain.model.NotificationBrowsingHistoryModel;
import cc.rits.membership.console.reminder.infrastructure.db.entity.NotificationBrowsingHistory;

/**
 * お知らせファクトリ
 */
@Component
public class NotificationFactory {

    /**
     * NotificationBrowsingHistoryを作成
     * 
     * @param notificationBrowsingHistoryModel model
     * @return entity
     */
    public NotificationBrowsingHistory createNotificationBrowsingHistory(
        final NotificationBrowsingHistoryModel notificationBrowsingHistoryModel) {
        return NotificationBrowsingHistory.builder() //
            .notificationId(notificationBrowsingHistoryModel.getNotificationId()) //
            .userId(notificationBrowsingHistoryModel.getUserId()) //
            .viewedDate(notificationBrowsingHistoryModel.getViewedDate()) //
            .build();
    }

}
