package cc.rits.membership.console.reminder.infrastructure.repository

import cc.rits.membership.console.reminder.domain.model.NotificationReminderModel
import cc.rits.membership.console.reminder.helper.DateHelper
import cc.rits.membership.console.reminder.helper.TableHelper
import org.springframework.beans.factory.annotation.Autowired

import java.sql.Date

/**
 * NotificationReminderRepositoryの単体テスト
 */
class NotificationReminderRepository_UT extends AbstractRepository_UT {

    @Autowired
    NotificationReminderRepository sut

    def "insert: リマインダーを作成"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "notification", {
            id | title | body | contributor_id
            1  | ""    | ""   | 1
        }
        // @formatter:on

        final notificationReminder = NotificationReminderModel.builder()
            .notificationId(1)
            .scheduledDate(DateHelper.tomorrow().toLocalDate())
            .build()

        when:
        this.sut.insert(notificationReminder)

        then:
        final createdNotificationReminder = sql.firstRow("SELECT * FROM notification_reminder")
        createdNotificationReminder.notification_id == 1
        notificationReminder.scheduledDate.isEqual((createdNotificationReminder.scheduled_date as Date).toLocalDate())
    }

    def "delete: IDからリマインダーを削除"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "notification", {
            id | title | body | contributor_id
            1  | ""    | ""   | 1
        }
        TableHelper.insert sql, "notification_reminder", {
            id | notification_id | scheduled_date
            1  | 1               | "2000-01-01 00:00:00"
            2  | 1               | "2000-01-02 00:00:00"
        }
        // @formatter:on

        when:
        this.sut.deleteById(1)

        then:
        final notificationReminders = sql.rows("SELECT * FROM notification_reminder")
        notificationReminders*.id == [2]
    }

    def "deleteByIds: IDリストからリマインダーリストを削除"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "notification", {
            id | title | body | contributor_id
            1  | ""    | ""   | 1
        }
        TableHelper.insert sql, "notification_reminder", {
            id | notification_id | scheduled_date
            1  | 1               | "2000-01-01 00:00:00"
            2  | 1               | "2000-01-02 00:00:00"
            3  | 1               | "2000-01-02 00:00:00"
        }
        // @formatter:on

        when:
        this.sut.deleteByIds([1, 2])

        then:
        final notificationReminders = sql.rows("SELECT * FROM notification_reminder")
        notificationReminders*.id == [3]
    }

    def "existsById: IDからリマインダーの存在チェック"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "notification", {
            id | title | body | contributor_id
            1  | ""    | ""   | 1
        }
        TableHelper.insert sql, "notification_reminder", {
            id | notification_id | scheduled_date
            1  | 1               | "2000-01-01 00:00:00"
        }
        // @formatter:on

        when:
        final result = this.sut.existsById(inputId)

        then:
        result == expectedResult

        where:
        inputId || expectedResult
        1       || true
        2       || false
    }

}
