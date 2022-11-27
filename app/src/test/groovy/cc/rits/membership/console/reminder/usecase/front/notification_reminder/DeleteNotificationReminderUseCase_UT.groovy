package cc.rits.membership.console.reminder.usecase.front.notification_reminder

import cc.rits.membership.console.reminder.domain.model.NotificationModel
import cc.rits.membership.console.reminder.domain.model.NotificationReminderModel
import cc.rits.membership.console.reminder.domain.model.UserModel
import cc.rits.membership.console.reminder.enums.Role
import cc.rits.membership.console.reminder.exception.BaseException
import cc.rits.membership.console.reminder.exception.ErrorCode
import cc.rits.membership.console.reminder.exception.ForbiddenException
import cc.rits.membership.console.reminder.exception.NotFoundException
import cc.rits.membership.console.reminder.helper.RandomHelper
import cc.rits.membership.console.reminder.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * CreateNotificationReminderUseCaseの単体テスト
 */
class DeleteNotificationReminderUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    DeleteNotificationReminderUseCase sut

    def "handle: 投稿者、もしくはリマインダーの管理者がリマインダーを削除"() {
        given:
        final loginUser = Spy(UserModel)
        final notification = Spy(NotificationModel)
        final notificationReminder = RandomHelper.mock(NotificationReminderModel)

        when:
        this.sut.handle(loginUser, notification.id, notificationReminder.id)

        then:
        noExceptionThrown()
        1 * this.notificationRepository.selectById(notification.id) >> Optional.of(notification)
        1 * this.notificationReminderRepository.existsById(notificationReminder.id) >> true
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
        final notificationReminder = RandomHelper.mock(NotificationReminderModel)

        when:
        this.sut.handle(loginUser, notification.id, notificationReminder.id)

        then:
        1 * this.notificationRepository.selectById(notification.id) >> Optional.of(notification)
        1 * loginUser.hasRole(Role.REMINDER_ADMIN) >> false
        1 * notification.isContributed(loginUser) >> false
        final BaseException exception = thrown()
        verifyException(exception, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "handle: お知らせ、もしくはリマインダーが存在しない場合は404エラー"() {
        given:
        final loginUser = Spy(UserModel)
        final notification = Spy(NotificationModel)
        final notificationReminder = RandomHelper.mock(NotificationReminderModel)

        when:
        this.sut.handle(loginUser, notification.id, notificationReminder.id)

        then:
        1 * this.notificationRepository.selectById(notification.id) >> Optional.ofNullable(mockedNotification)
        (0..1) * this.notificationReminderRepository.existsById(notificationReminder.id) >> isNotificationReminderExists
        (0..1) * loginUser.hasRole(Role.REMINDER_ADMIN) >> true
        final BaseException exception = thrown()
        verifyException(exception, new NotFoundException(expectedErrorCode))

        where:
        mockedNotification                   | isNotificationReminderExists || expectedErrorCode
        null                                 | true                         || ErrorCode.NOT_FOUND_NOTIFICATION
        RandomHelper.mock(NotificationModel) | false                        || ErrorCode.NOT_FOUND_NOTIFICATION_REMINDER
    }

}
