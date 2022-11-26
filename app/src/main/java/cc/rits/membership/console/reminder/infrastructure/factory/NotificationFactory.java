package cc.rits.membership.console.reminder.infrastructure.factory;

import org.springframework.stereotype.Component;

import cc.rits.membership.console.reminder.domain.model.NotificationBrowsingHistoryModel;
import cc.rits.membership.console.reminder.domain.model.NotificationModel;
import cc.rits.membership.console.reminder.infrastructure.db.entity.Notification;
import cc.rits.membership.console.reminder.infrastructure.db.entity.NotificationBrowsingHistory;

/**
 * お知らせファクトリ
 */
@Component
public class NotificationFactory {

    /**
     * Notificationを作成
     *
     * @param notificationModel model
     * @return entity
     */
    public Notification createNotification(final NotificationModel notificationModel) {
        final var contributorId = notificationModel.getContributor().isPresent() //
            ? notificationModel.getContributor().get().getId() //
            : null;
        return Notification.builder() //
            .id(notificationModel.getId()) //
            .title(notificationModel.getTitle()) //
            .body(notificationModel.getBody()) //
            .contributorId(contributorId) //
            .postedDate(notificationModel.getPostedDate()) //
            .build();
    }

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
