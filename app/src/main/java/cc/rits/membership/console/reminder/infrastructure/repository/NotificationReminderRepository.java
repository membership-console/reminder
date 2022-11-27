package cc.rits.membership.console.reminder.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import cc.rits.membership.console.reminder.domain.model.NotificationReminderModel;
import cc.rits.membership.console.reminder.domain.repository.INotificationReminderRepository;
import cc.rits.membership.console.reminder.infrastructure.db.entity.NotificationReminderExample;
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

    @Override
    public void deleteById(final Integer id) {
        this.notificationReminderMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteByIds(final List<Integer> ids) {
        if (ids.isEmpty()) {
            return;
        }

        final var example = new NotificationReminderExample();
        example.createCriteria().andIdIn(ids);
        this.notificationReminderMapper.deleteByExample(example);
    }

    @Override
    public boolean existsById(final Integer id) {
        final var example = new NotificationReminderExample();
        example.createCriteria().andIdEqualTo(id);
        return this.notificationReminderMapper.countByExample(example) != 0;
    }

}
