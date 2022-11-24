package cc.rits.membership.console.reminder.usecase.notification;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import cc.rits.membership.console.reminder.domain.model.NotificationBrowsingHistoryModel;
import cc.rits.membership.console.reminder.domain.model.NotificationModel;
import cc.rits.membership.console.reminder.domain.model.UserModel;
import cc.rits.membership.console.reminder.domain.repository.INotificationRepository;
import lombok.RequiredArgsConstructor;

/**
 * お知らせリスト取得ユースケース
 */
@RequiredArgsConstructor
@Component
public class GetNotificationsUseCase {

    private final INotificationRepository notificationRepository;

    /**
     * Handle UseCase
     * 
     * @param loginUser ログインユーザ
     * @param onlyUnviewed 未読のみ取得するか
     * @return お知らせリスト
     */
    public List<NotificationModel> handle(final UserModel loginUser, final boolean onlyUnviewed) {
        // お知らせリストを取得
        final var notifications = this.notificationRepository.selectAll();
        final var unViewedNotifications = notifications.stream() //
            .filter(notification -> !notification.isViewed(loginUser.getId())) //
            .collect(Collectors.toList());

        // 全てのお知らせを既読にする
        final var notificationBrowsingHistories = unViewedNotifications.stream() //
            .map(notification -> NotificationBrowsingHistoryModel.builder() //
                .notificationId(notification.getId()) //
                .userId(loginUser.getId()) //
                .build() //
            ).collect(Collectors.toList());
        this.notificationRepository.insertBrowsingHistories(notificationBrowsingHistories);

        return onlyUnviewed ? unViewedNotifications : notifications;
    }

}
