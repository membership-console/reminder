package cc.rits.membership.console.reminder.infrastructure.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import cc.rits.membership.console.reminder.client.IamClient;
import cc.rits.membership.console.reminder.domain.model.NotificationBrowsingHistoryModel;
import cc.rits.membership.console.reminder.domain.model.NotificationModel;
import cc.rits.membership.console.reminder.domain.repository.INotificationRepository;
import cc.rits.membership.console.reminder.infrastructure.db.mapper.NotificationBrowsingHistoryMapper;
import cc.rits.membership.console.reminder.infrastructure.db.mapper.NotificationMapper;
import cc.rits.membership.console.reminder.infrastructure.factory.NotificationFactory;
import lombok.RequiredArgsConstructor;

/**
 * お知らせリポジトリ
 */
@RequiredArgsConstructor
@Repository
public class NotificationRepository implements INotificationRepository {

    private final NotificationMapper notificationMapper;

    private final NotificationBrowsingHistoryMapper notificationBrowsingHistoryMapper;

    private final NotificationFactory notificationFactory;

    private final IamClient iamClient;

    @Override
    public List<NotificationModel> selectAll() {
        final var users = this.iamClient.getUsers();

        return this.notificationMapper.selectAll().stream() //
            .map(notification -> {
                final var contributor = users.stream() //
                    .filter(user -> notification.getContributor().equals(user.getId())) //
                    .findFirst();
                return new NotificationModel(notification, contributor);
            }) //
            .collect(Collectors.toList());
    }

    @Override
    public void insertBrowsingHistories(final List<NotificationBrowsingHistoryModel> notificationBrowsingHistoryModels) {
        final var notificationBrowsingHistories = notificationBrowsingHistoryModels.stream() //
            .map(this.notificationFactory::createNotificationBrowsingHistory) //
            .collect(Collectors.toList());
        this.notificationBrowsingHistoryMapper.bulkUpsert(notificationBrowsingHistories);
    }

}
