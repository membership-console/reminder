package cc.rits.membership.console.reminder.infrastructure.api.controller.batch

import cc.rits.membership.console.reminder.domain.model.UserModel
import cc.rits.membership.console.reminder.helper.DateHelper
import cc.rits.membership.console.reminder.helper.RandomHelper
import cc.rits.membership.console.reminder.helper.TableHelper
import cc.rits.membership.console.reminder.infrastructure.api.controller.AbstractRestController_IT
import org.springframework.http.HttpStatus

/**
 * NotificationReminderRestControllerの統合テスト
 */
class NotificationReminderRestController_IT extends AbstractRestController_IT {

    // API PATH
    static final String BASE_PATH = "/api/batch/notification-reminders"
    static final String BROADCAST_NOTIFICATION_REMINDER_PATH = BASE_PATH + "/broadcast"

    def "リマインダー配信API: 正常系 リマインド予定日が現在のリマインダーを配信し、削除する"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "notification", {
            id | title | body     | contributor_id
            1  | "A"   | "A body" | 1
            2  | "B"   | "B body" | 1
            3  | "C"   | "C body" | 1
        }
        TableHelper.insert sql, "notification_reminder", {
            id | notification_id | scheduled_date
            1  | 1               | DateHelper.today()     // 配信される
            2  | 1               | DateHelper.yesterday() // 配信される
            3  | 1               | DateHelper.tomorrow()  // 配信されない
            4  | 2               | DateHelper.yesterday() // 配信される
            5  | 2               | DateHelper.tomorrow()  // 配信されない
            6  | 2               | DateHelper.tomorrow()  // 配信されない
            7  | 3               | DateHelper.tomorrow()  // 配信されない
            8  | 3               | DateHelper.tomorrow()  // 配信されない
            9  | 3               | DateHelper.tomorrow()  // 配信されない
        }
        // @formatter:on

        final users = [
            RandomHelper.mock(UserModel),
            RandomHelper.mock(UserModel),
        ]

        when:
        final request = this.postRequest(BROADCAST_NOTIFICATION_REMINDER_PATH)
        execute(request, HttpStatus.OK)

        then:
        2 * this.iamClient.getUsers() >> users

        // 本日に予定されたリマインドを持つお知らせがメール配信される
        // 同じ日時のリマインダーが複数ある場合でも、1度だけ配信されることを期待
        1 * this.iamClient.sendEmail("A", "A body", users*.id)
        1 * this.iamClient.sendEmail("B", "B body", users*.id)
        0 * this.iamClient.sendEmail("C", "C body", users*.id)

        // 本日に予定されたリマインドが削除される
        final notificationReminders = sql.rows("SELECT * FROM notification_reminder")
        notificationReminders*.id == [3, 5, 6, 7, 8, 9]
    }

    def "リマインダー配信API: 正常系 リマインド予定日が現在のリマインダーが存在しない場合、何もしない"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "notification", {
            id | title | body | contributor_id
            1  | ""    | ""   | 1
        }
        TableHelper.insert sql, "notification_reminder", {
            id | notification_id | scheduled_date
            1  | 1               | DateHelper.tomorrow()
        }
        // @formatter:on

        when:
        final request = this.postRequest(BROADCAST_NOTIFICATION_REMINDER_PATH)
        execute(request, HttpStatus.OK)

        then:
        1 * this.iamClient.getUsers() >> []
        0 * this.iamClient.sendEmail(_, _, _)
    }

}
