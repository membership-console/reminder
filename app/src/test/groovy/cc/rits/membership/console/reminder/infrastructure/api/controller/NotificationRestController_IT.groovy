package cc.rits.membership.console.reminder.infrastructure.api.controller

import cc.rits.membership.console.reminder.domain.model.UserModel
import cc.rits.membership.console.reminder.exception.ErrorCode
import cc.rits.membership.console.reminder.exception.UnauthorizedException
import cc.rits.membership.console.reminder.helper.TableHelper
import cc.rits.membership.console.reminder.infrastructure.api.response.NotificationsResponse
import org.springframework.http.HttpStatus

/**
 * NotificationRestControllerの統合テスト
 */
class NotificationRestController_IT extends AbstractRestController_IT {

    // API PATH
    static final String BASE_PATH = "/api/notifications"
    static final String GET_NOTIFICATIONS_PATH = BASE_PATH

    def "お知らせリスト取得API: 正常系 お知らせリストを取得できる"() {
        given:
        final loginUser = this.login([])

        // @formatter:off
        TableHelper.insert sql, "notification", {
            id | title | body     | contributor_id
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
            notification_id | user_id
            1               | loginUser.id
            1               | loginUser.id + 1
            2               | loginUser.id + 1
        }
        // @formatter:on

        final users = [
            UserModel.builder().id(1).email("user1@example.com").build(),
            UserModel.builder().id(2).email("user2@example.com").build(),
        ]

        when:
        final request = this.getRequest(GET_NOTIFICATIONS_PATH)
        final response = execute(request, HttpStatus.OK, NotificationsResponse)

        then:
        response.notifications*.id == [1, 2, 3]
        response.notifications*.title == ["A", "B", "C"]
        response.notifications*.body == ["A body", "B body", "C body"]
        response.notifications*.reminders*.id == [[1, 2], [3], []]
        response.notifications*.isViewed == [true, false, false]

        // 投稿者をIAMから取得できる
        1 * this.iamClient.getUsers() >> users
        response.notifications*.contributor.get(0).id == 1
        response.notifications*.contributor.get(0).email == "user1@example.com"
        response.notifications*.contributor.get(1).id == 2
        response.notifications*.contributor.get(1).email == "user2@example.com"
        response.notifications*.contributor.get(2) == null

        // 閲覧履歴が作成されている
        final notificationBrowsingHistories = sql.rows("SELECT * FROM notification_browsing_history WHERE user_id=:userId", [userId: loginUser.id])
        notificationBrowsingHistories*.notification_id == [1, 2, 3]
    }

    def "お知らせリスト取得API: 正常系 未読のみ取得できる"() {
        given:
        final loginUser = this.login([])

        // @formatter:off
        TableHelper.insert sql, "notification", {
            id | title | body | contributor_id
            1  | ""    | ""   | 1
            2  | ""    | ""   | 1
            3  | ""    | ""   | 1
        }
        TableHelper.insert sql, "notification_browsing_history", {
            notification_id | user_id
            1               | loginUser.id
            1               | loginUser.id + 1
            2               | loginUser.id + 1
        }
        // @formatter:on

        when:
        final request = this.getRequest(GET_NOTIFICATIONS_PATH + "?onlyUnviewed=${onlyUnviewed}")
        final response = execute(request, HttpStatus.OK, NotificationsResponse)

        then:
        1 * this.iamClient.getUsers() >> []
        response.notifications*.id == expectedNotificationIds

        // 閲覧履歴が作成されている
        final notificationBrowsingHistories = sql.rows("SELECT * FROM notification_browsing_history WHERE user_id=:userId", [userId: loginUser.id])
        notificationBrowsingHistories*.notification_id == [1, 2, 3]

        where:
        onlyUnviewed || expectedNotificationIds
        true         || [2, 3]
        false        || [1, 2, 3]
    }

    def "お知らせリスト取得API: 異常系 ログインしていない場合は401エラー"() {
        expect:
        final request = this.getRequest(GET_NOTIFICATIONS_PATH)
        execute(request, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

}
