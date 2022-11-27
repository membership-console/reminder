package cc.rits.membership.console.reminder.usecase.notification_reminder

import cc.rits.membership.console.reminder.domain.model.NotificationModel
import cc.rits.membership.console.reminder.domain.model.UserModel
import cc.rits.membership.console.reminder.enums.Role
import cc.rits.membership.console.reminder.exception.BaseException
import cc.rits.membership.console.reminder.exception.ErrorCode
import cc.rits.membership.console.reminder.exception.ForbiddenException
import cc.rits.membership.console.reminder.exception.NotFoundException
import cc.rits.membership.console.reminder.helper.RandomHelper
import cc.rits.membership.console.reminder.infrastructure.api.request.NotificationReminderCreateRequest
import cc.rits.membership.console.reminder.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * CreateNotificationReminderUseCaseの単体テスト
 */
class CreateNotificationReminderUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    CreateNotificationReminderUseCase sut

    def "handle: 投稿者、もしくはリマインダーの管理者が リマインダーを作成"() {
        given:
        final loginUser = Spy(UserModel)
        final notification = Spy(NotificationModel)

        final requestBody = RandomHelper.mock(NotificationReminderCreateRequest)

        when:
        this.sut.handle(loginUser, notification.id, requestBody)

        then:
        noExceptionThrown()
        1 * this.notificationRepository.selectById(notification.id) >> Optional.of(notification)
        1 * loginUser.hasRole(Role.REMINDER_ADMIN) >> mockedHasRole
        (0..1) * notification.isContributed(loginUser) >> mockedIsContributed

        where:
        mockedHasRole | mockedIsContributed
        true          | true
        true          | false
        false         | true
    }

    def "handle: 投稿者でもリマインダーの管理者でもない場合は403エラー"() {
        given:
        final loginUser = Spy(UserModel)
        final notification = Spy(NotificationModel)

        final requestBody = RandomHelper.mock(NotificationReminderCreateRequest)

        when:
        this.sut.handle(loginUser, notification.id, requestBody)

        then:
        1 * this.notificationRepository.selectById(notification.id) >> Optional.of(notification)
        1 * loginUser.hasRole(Role.REMINDER_ADMIN) >> false
        1 * notification.isContributed(loginUser) >> false
        final BaseException exception = thrown()
        verifyException(exception, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "handle: お知らせが存在しない場合は404エラー"() {
        given:
        final loginUser = Spy(UserModel)
        final notification = Spy(NotificationModel)

        final requestBody = RandomHelper.mock(NotificationReminderCreateRequest)

        when:
        this.sut.handle(loginUser, notification.id, requestBody)

        then:
        1 * this.notificationRepository.selectById(notification.id) >> Optional.empty()
        final BaseException exception = thrown()
        verifyException(exception, new NotFoundException(ErrorCode.NOT_FOUND_NOTIFICATION))
    }

}
