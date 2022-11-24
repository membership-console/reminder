package cc.rits.membership.console.reminder.infrastructure.db.mapper;

import java.util.List;

import cc.rits.membership.console.reminder.infrastructure.db.entity.join.NotificationWithRemindersAndBrowsingHistories;
import cc.rits.membership.console.reminder.infrastructure.db.mapper.base.NotificationBaseMapper;

public interface NotificationMapper extends NotificationBaseMapper {

    List<NotificationWithRemindersAndBrowsingHistories> selectAll();

}
