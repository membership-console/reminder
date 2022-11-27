package cc.rits.membership.console.reminder.infrastructure.api.controller.front

import cc.rits.membership.console.reminder.domain.model.UserModel
import cc.rits.membership.console.reminder.enums.Role
import cc.rits.membership.console.reminder.exception.*
import cc.rits.membership.console.reminder.helper.RandomHelper
import cc.rits.membership.console.reminder.helper.TableHelper
import cc.rits.membership.console.reminder.infrastructure.api.controller.AbstractRestController_IT
import cc.rits.membership.console.reminder.infrastructure.api.request.NotificationCreateRequest
import cc.rits.membership.console.reminder.infrastructure.api.response.NotificationsResponse
import org.springframework.http.HttpStatus

/**
 * NotificationRestControllerの統合テスト
 */
class NotificationRestController_IT extends AbstractRestController_IT {

    // API PATH
    static final String BASE_PATH = "/api/notifications"
    static final String GET_NOTIFICATIONS_PATH = BASE_PATH
    static final String CREATE_NOTIFICATIONS_PATH = BASE_PATH
    static final String DELETE_NOTIFICATION_PATH = BASE_PATH + "/%d"

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
            id | notification_id | scheduled_date
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

    def "お知らせ作成API: 正常系 お知らせを作成すると全ユーザにメール配信される"() {
        given:
        final loginUser = this.login([])

        final requestBody = NotificationCreateRequest.builder()
            .title(inputTitle)
            .body(inputBody)
            .build()

        final users = [
            RandomHelper.mock(UserModel),
            RandomHelper.mock(UserModel),
        ]

        when:
        final request = this.postRequest(CREATE_NOTIFICATIONS_PATH, requestBody)
        execute(request, HttpStatus.CREATED)

        then:
        1 * this.iamClient.getUsers() >> users
        1 * this.iamClient.sendEmail(requestBody.title, requestBody.body, users*.id) >> {}

        final notification = sql.firstRow("SELECT * FROM notification")
        notification.title == requestBody.title
        notification.body == requestBody.body
        notification.contributor_id == loginUser.id

        where:
        inputTitle                     | inputBody
        RandomHelper.alphanumeric(1)   | RandomHelper.alphanumeric(1)
        RandomHelper.alphanumeric(100) | RandomHelper.alphanumeric(1000)
    }

    def "お知らせ作成API: 異常系 リクエストボディのバリデーション"() {
        given:
        this.login([])

        final requestBody = NotificationCreateRequest.builder()
            .title(inputTitle)
            .body(inputBody)
            .build()

        expect:
        final request = this.postRequest(CREATE_NOTIFICATIONS_PATH, requestBody)
        execute(request, new BadRequestException(expectedErrorCode))

        where:
        inputTitle                     | inputBody                       || expectedErrorCode
        RandomHelper.alphanumeric(0)   | RandomHelper.alphanumeric(1)    || ErrorCode.INVALID_NOTIFICATION_TITLE
        RandomHelper.alphanumeric(101) | RandomHelper.alphanumeric(1)    || ErrorCode.INVALID_NOTIFICATION_TITLE
        RandomHelper.alphanumeric(1)   | RandomHelper.alphanumeric(0)    || ErrorCode.INVALID_NOTIFICATION_BODY
        RandomHelper.alphanumeric(1)   | RandomHelper.alphanumeric(1001) || ErrorCode.INVALID_NOTIFICATION_BODY
    }

    def "お知らせ作成API: 異常系 ログインしていない場合は401エラー"() {
        given:
        final requestBody = NotificationCreateRequest.builder()
            .title(RandomHelper.alphanumeric(10))
            .body(RandomHelper.alphanumeric(10))
            .build()

        expect:
        final request = this.postRequest(CREATE_NOTIFICATIONS_PATH, requestBody)
        execute(request, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

    def "お知らせ削除API: 正常系 投稿者、もしくはリマインダーの管理者がお知らせを削除"() {
        given:
        final loginUser = this.login(inputRoles)

        // @formatter:off
        TableHelper.insert sql, "notification", {
            id | title | body | contributor_id
            1  | ""    | ""   | 1
            2  | ""    | ""   | 1
        }
        // @formatter:on

        when:
        final request = this.deleteRequest(String.format(DELETE_NOTIFICATION_PATH, 1))
        execute(request, HttpStatus.OK)

        then:
        1 * this.iamClient.getUser(_) >> Optional.ofNullable(contributor)

        final notifications = sql.rows("SELECT * FROM notification")
        notifications*.id == [2]

        where:
        inputRoles            | contributor
        [Role.REMINDER_ADMIN] | UserModel.builder().id(LOGIN_USER_ID).build()
        [Role.REMINDER_ADMIN] | UserModel.builder().id(LOGIN_USER_ID + 1).build()
        [Role.REMINDER_ADMIN] | null
        []                    | UserModel.builder().id(LOGIN_USER_ID).build()
    }

    def "お知らせ削除API: 異常系 投稿者でもリマインダーの管理者でもない場合は403エラー"() {
        given:
        final loginUser = this.login([])

        // @formatter:off
        TableHelper.insert sql, "notification", {
            id | title | body | contributor_id
            1  | ""    | ""   | loginUser.id + 1
        }
        // @formatter:on

        when:
        final request = this.deleteRequest(String.format(DELETE_NOTIFICATION_PATH, 1))
        execute(request, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))

        then:
        1 * this.iamClient.getUser(_) >> Optional.ofNullable(contributor)

        where:
        contributor << [
            UserModel.builder().id(LOGIN_USER_ID + 1).build(),
            null,
        ]
    }

    def "お知らせ削除API: 異常系 お知らせが存在しない場合は404エラー"() {
        given:
        this.login([])

        expect:
        final request = this.deleteRequest(String.format(DELETE_NOTIFICATION_PATH, 1))
        execute(request, new NotFoundException(ErrorCode.NOT_FOUND_NOTIFICATION))
    }

    def "お知らせ削除API: 異常系 ログインしていない場合は401エラー"() {
        expect:
        final request = this.deleteRequest(String.format(DELETE_NOTIFICATION_PATH, 1))
        execute(request, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

}
