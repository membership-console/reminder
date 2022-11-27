package cc.rits.membership.console.reminder.infrastructure.repository

import cc.rits.membership.console.reminder.domain.model.NotificationBrowsingHistoryModel
import cc.rits.membership.console.reminder.domain.model.NotificationModel
import cc.rits.membership.console.reminder.domain.model.UserModel
import cc.rits.membership.console.reminder.helper.RandomHelper
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
            id | title | body     | contributor_id
            1  | "A"   | "A body" | 1
            2  | "B"   | "B body" | 2
            3  | "C"   | "C body" | 3
        }
        TableHelper.insert sql, "notification_reminder", {
            id | notification_id | scheduled_date
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

    def "selectById: IDからお知らせを取得"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "notification", {
            id | title | body     | contributor_id
            1  | "A"   | "A body" | 1
        }
        TableHelper.insert sql, "notification_reminder", {
            id | notification_id | scheduled_date
            1  | 1               | "2000-01-01 00:00:00"
            2  | 1               | "2000-01-02 00:00:00"
        }
        TableHelper.insert sql, "notification_browsing_history", {
            user_id | notification_id
            1       | 1
            2       | 1
        }
        // @formatter:on

        final user = UserModel.builder().id(1).email("user1@example.com").build()

        when:
        final result = this.sut.selectById(1)

        then:
        result.get().id == 1
        result.get().title == "A"
        result.get().body == "A body"
        result.get().reminders*.id == [1, 2]
        result.get().browsingHistories*.notificationId == [1, 1]
        result.get().browsingHistories*.userId == [1, 2]

        // 投稿者をIAMから取得できる
        this.iamClient.getUser(1) >> Optional.of(user)
        result.get().contributor.get().id == 1
    }

    def "selectById: 存在しない場合はOptional.empty()を返す"() {
        when:
        final result = this.sut.selectById(1)

        then:
        result.isEmpty()
    }

    def "insert: お知らせを作成"() {
        given:
        final user = UserModel.builder()
            .id(1)
            .build()
        final notification = new NotificationModel(RandomHelper.alphanumeric(10), RandomHelper.alphanumeric(10), user)

        when:
        this.sut.insert(notification)

        then:
        final createdNotification = sql.firstRow("SELECT * FROM notification")
        createdNotification.title == notification.title
        createdNotification.body == notification.body
        createdNotification.contributor_id == user.id
    }

    def "insertBrowsingHistories: 閲覧履歴リストを作成"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "notification", {
            id | title | body | contributor_id
            1  | ""    | ""   | 1
            2  | ""    | ""   | 1
            3  | ""    | ""   | 1
        }
        // @formatter:on

        final notificationBrowsingHistories = [
            NotificationBrowsingHistoryModel.builder().notificationId(1).userId(1).build(),
            NotificationBrowsingHistoryModel.builder().notificationId(2).userId(1).build(),
            NotificationBrowsingHistoryModel.builder().notificationId(2).userId(2).build(),
        ]

        when:
        this.sut.insertBrowsingHistories(notificationBrowsingHistories)

        then:
        final createdNotificationBrowsingHistories = sql.rows("SELECT * FROM notification_browsing_history")
        createdNotificationBrowsingHistories*.notification_id == [1, 2, 2]
        createdNotificationBrowsingHistories*.user_id == [1, 1, 2]
    }

    def "deleteById: IDからお知らせを削除"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "notification", {
            id | title | body | contributor_id
            1  | ""    | ""   | 1
            2  | ""    | ""   | 1
        }
        // @formatter:on

        when:
        this.sut.deleteById(1)

        then:
        final notifications = sql.rows("SELECT * FROM notification")
        notifications*.id == [2]
    }

    def "existsById: IDからお知らせの存在チェック"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "notification", {
            id | title | body | contributor_id
            1  | ""    | ""   | 1
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
