package cc.rits.membership.console.reminder.infrastructure.repository

import cc.rits.membership.console.reminder.domain.model.UserModel
import cc.rits.membership.console.reminder.helper.TableHelper
import org.springframework.beans.factory.annotation.Autowired

/**
 * NotificationRepositoryの単体テスト
 */
class NotificationRepository_UT extends AbstractRepository_UT {

    @Autowired
    NotificationRepository sut

    def "selectAll: お知らせリストを全件取得"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "notification", {
            id | title | body     | contributor
            1  | "A"   | "A body" | 1
            2  | "B"   | "B body" | 2
            3  | "C"   | "C body" | 3
        }
        TableHelper.insert sql, "notification_reminder", {
            id | notification_id | reminder_date
            1  | 1               | "2000-01-01 00:00:00"
            2  | 1               | "2000-01-02 00:00:00"
            3  | 2               | "2000-01-01 00:00:00"
        }
        TableHelper.insert sql, "notification_browsing_history", {
            user_id | notification_id
            1       | 1
            1       | 2
            2       | 1
        }
        // @formatter:on

        final users = [
            UserModel.builder().id(1).email("user1@example.com").build(),
            UserModel.builder().id(2).email("user2@example.com").build(),
        ]

        when:
        final result = this.sut.selectAll()

        then:
        result*.id == [1, 2, 3]
        result*.title == ["A", "B", "C"]
        result*.body == ["A body", "B body", "C body"]
        result*.reminders*.id == [[1, 2], [3], []]
        result[0].browsingHistories*.notificationId == [1, 1]
        result[0].browsingHistories*.userId == [1, 2]
        result[1].browsingHistories*.notificationId == [2]
        result[1].browsingHistories*.userId == [1]
        result[2].browsingHistories == []

        // 投稿者をIAMから取得できる
        this.iamClient.getUsers() >> users
        result[0].contributor.get().id == 1
        result[0].contributor.get().email == "user1@example.com"
        result[1].contributor.get().id == 2
        result[1].contributor.get().email == "user2@example.com"
        result[2].contributor.isEmpty()
    }

}
