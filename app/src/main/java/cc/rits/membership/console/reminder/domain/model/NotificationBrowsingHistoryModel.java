package cc.rits.membership.console.reminder.domain.model;

import java.time.LocalDateTime;

import cc.rits.membership.console.reminder.infrastructure.db.entity.NotificationBrowsingHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 閲覧履歴モデル
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationBrowsingHistoryModel {

    /**
     * お知らせID
     */
    Integer notificationId;

    /**
     * ユーザID
     */
    Integer userId;

    /**
     * 閲覧日
     */
    @Builder.Default
    LocalDateTime viewedDate = LocalDateTime.now();

    public NotificationBrowsingHistoryModel(final NotificationBrowsingHistory notificationBrowsingHistory) {
        this.notificationId = notificationBrowsingHistory.getNotificationId();
        this.userId = notificationBrowsingHistory.getUserId();
        this.viewedDate = notificationBrowsingHistory.getViewedDate();
    }

}
