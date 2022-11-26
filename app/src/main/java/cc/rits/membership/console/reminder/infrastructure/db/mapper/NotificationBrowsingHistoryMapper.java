package cc.rits.membership.console.reminder.infrastructure.db.mapper;

import java.util.List;

import cc.rits.membership.console.reminder.infrastructure.db.entity.NotificationBrowsingHistory;
import cc.rits.membership.console.reminder.infrastructure.db.mapper.base.NotificationBrowsingHistoryBaseMapper;

public interface NotificationBrowsingHistoryMapper extends NotificationBrowsingHistoryBaseMapper {

    void bulkUpsert(final List<NotificationBrowsingHistory> notificationBrowsingHistories);

}
