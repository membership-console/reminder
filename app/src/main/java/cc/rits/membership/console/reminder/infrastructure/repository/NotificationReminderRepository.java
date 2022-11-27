package cc.rits.membership.console.reminder.infrastructure.repository;

import org.springframework.stereotype.Repository;

import cc.rits.membership.console.reminder.domain.model.NotificationReminderModel;
import cc.rits.membership.console.reminder.domain.repository.INotificationReminderRepository;
import cc.rits.membership.console.reminder.infrastructure.db.mapper.NotificationReminderMapper;
import cc.rits.membership.console.reminder.infrastructure.factory.NotificationReminderFactory;
import lombok.RequiredArgsConstructor;

/**
 * お知らせリポジトリ
 */
@RequiredArgsConstructor
@Repository
public class NotificationReminderRepository implements INotificationReminderRepository {

    private final NotificationReminderMapper notificationReminderMapper;

    private final NotificationReminderFactory notificationReminderFactory;

    @Override
    public void insert(final NotificationReminderModel notificationReminderModel) {
        final var notificationReminder = this.notificationReminderFactory.createNotificationReminder(notificationReminderModel);
        this.notificationReminderMapper.insertSelective(notificationReminder);
    }

}