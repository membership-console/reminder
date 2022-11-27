package cc.rits.membership.console.reminder.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import cc.rits.membership.console.reminder.client.IamClient;
import cc.rits.membership.console.reminder.domain.model.NotificationBrowsingHistoryModel;
import cc.rits.membership.console.reminder.domain.model.NotificationModel;
import cc.rits.membership.console.reminder.domain.repository.INotificationRepository;
import cc.rits.membership.console.reminder.infrastructure.db.entity.NotificationExample;
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
        final var notifications = this.notificationMapper.selectAll();
        if (notifications.isEmpty()) {
            return List.of();
        }

        final var users = this.iamClient.getUsers();
        return notifications.stream() //
            .map(notification -> {
                final var contributor = users.stream() //
                    .filter(user -> notification.getContributorId().equals(user.getId())) //
                    .findFirst();
                return new NotificationModel(notification, contributor);
            }) //
            .collect(Collectors.toList());
    }

    @Override
    public Optional<NotificationModel> selectById(final Integer id) {
        return this.notificationMapper.selectById(id).stream() //
            .map(notification -> {
                final var contributor = this.iamClient.getUser(notification.getContributorId());
                return new NotificationModel(notification, contributor);
            }).findFirst();
    }

    @Override
    public void insert(final NotificationModel notificationModel) {
        final var notification = this.notificationFactory.createNotification(notificationModel);
        this.notificationMapper.insertSelective(notification);
    }

    @Override
    public void insertBrowsingHistories(final List<NotificationBrowsingHistoryModel> notificationBrowsingHistoryModels) {
        final var notificationBrowsingHistories = notificationBrowsingHistoryModels.stream() //
            .map(this.notificationFactory::createNotificationBrowsingHistory) //
            .collect(Collectors.toList());
        this.notificationBrowsingHistoryMapper.bulkUpsert(notificationBrowsingHistories);
    }

    @Override
    public void deleteById(final Integer id) {
        this.notificationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public boolean existsById(final Integer id) {
        final var example = new NotificationExample();
        example.createCriteria().andIdEqualTo(id);
        return this.notificationMapper.countByExample(example) != 0;
    }

}
