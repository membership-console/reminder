package cc.rits.membership.console.reminder.infrastructure.api.controller

import cc.rits.membership.console.reminder.domain.model.UserModel
import cc.rits.membership.console.reminder.enums.Role
import cc.rits.membership.console.reminder.exception.*
import cc.rits.membership.console.reminder.helper.DateHelper
import cc.rits.membership.console.reminder.helper.TableHelper
import cc.rits.membership.console.reminder.infrastructure.api.request.NotificationReminderCreateRequest
import org.springframework.http.HttpStatus

import java.time.LocalDateTime

/**
 * NotificationReminderRestControllerの統合テスト
 */
class NotificationReminderRestController_IT extends AbstractRestController_IT {

    // API PATH
    static final String BASE_PATH = "/api/notifications/%d/reminders"
    static final String CREATE_NOTIFICATION_REMINDER_PATH = BASE_PATH

    def "リマインダー作成API: 正常系 投稿者、もしくはリマインダーの管理者が リマインダーを作成"() {
        given:
        this.login(inputRoles)

        // @formatter:off
        TableHelper.insert sql, "notification", {
            id | title | body | contributor_id
            1  | ""    | ""   | 1
        }
        // @formatter:on

        final requestBody = new NotificationReminderCreateRequest(DateHelper.tomorrow())

        when:
        final request = this.postRequest(String.format(CREATE_NOTIFICATION_REMINDER_PATH, 1), requestBody)
        execute(request, HttpStatus.CREATED)

        then:
        1 * this.iamClient.getUser(_) >> Optional.ofNullable(contributor)

        final createdNotificationReminder = sql.firstRow("SELECT * FROM notification_reminder")
        createdNotificationReminder.notification_id == 1
        DateHelper.isSameMinutes(createdNotificationReminder.reminder_date as LocalDateTime, requestBody.reminderDate)

        where:
        inputRoles            | contributor
        [Role.REMINDER_ADMIN] | UserModel.builder().id(LOGIN_USER_ID).build()
        [Role.REMINDER_ADMIN] | UserModel.builder().id(LOGIN_USER_ID + 1).build()
        [Role.REMINDER_ADMIN] | null
        []                    | UserModel.builder().id(LOGIN_USER_ID).build()
    }

    def "リマインダー作成API: 異常系 投稿者でもリマインダーの管理者でもない場合は403エラー"() {
        given:
        this.login([])

        // @formatter:off
        TableHelper.insert sql, "notification", {
            id | title | body | contributor_id
            1  | ""    | ""   | 1
        }
        // @formatter:on

        final requestBody = new NotificationReminderCreateRequest(DateHelper.tomorrow())

        when:
        final request = this.postRequest(String.format(CREATE_NOTIFICATION_REMINDER_PATH, 1), requestBody)
        execute(request, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))

        then:
        1 * this.iamClient.getUser(_) >> Optional.ofNullable(contributor)

        where:
        contributor << [
            UserModel.builder().id(LOGIN_USER_ID + 1).build(),
            null,
        ]
    }

    def "リマインダー作成API: 異常系 お知らせが存在しない場合は404エラー"() {
        given:
        final loginUser = this.login([Role.REMINDER_ADMIN])

        // @formatter:off
        TableHelper.insert sql, "notification", {
            id | title | body | contributor_id
            1  | ""    | ""   | loginUser.id
        }
        // @formatter:on

        final requestBody = new NotificationReminderCreateRequest(DateHelper.tomorrow())

        expect:
        final request = this.postRequest(String.format(CREATE_NOTIFICATION_REMINDER_PATH, 2), requestBody)
        execute(request, new NotFoundException(ErrorCode.NOT_FOUND_NOTIFICATION))
    }

    def "リマインダー作成API: 異常系 リクエストボディのバリデーション"() {
        given:
        this.login([Role.REMINDER_ADMIN])

        final requestBody = new NotificationReminderCreateRequest(inputReminderDate)

        expect:
        final request = this.postRequest(String.format(CREATE_NOTIFICATION_REMINDER_PATH, 1), requestBody)
        execute(request, new BadRequestException(expectedErrorCode))

        where:
        inputReminderDate                   || expectedErrorCode
        LocalDateTime.now().minusMinutes(1) || ErrorCode.INVALID_NOTIFICATION_REMINDER_DATE
    }

    def "リマインダー作成API: 異常系 ログインしていない場合は401エラー"() {
        given:
        final requestBody = new NotificationReminderCreateRequest(DateHelper.tomorrow())

        expect:
        final request = this.postRequest(String.format(CREATE_NOTIFICATION_REMINDER_PATH, 1), requestBody)
        execute(request, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

}