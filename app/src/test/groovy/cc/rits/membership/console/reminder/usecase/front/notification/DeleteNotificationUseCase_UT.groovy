package cc.rits.membership.console.reminder.usecase.front.notification

import cc.rits.membership.console.reminder.domain.model.NotificationModel
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
 * DeleteNotificationUseCaseの単体テスト
 */
class DeleteNotificationUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    DeleteNotificationUseCase sut

    def "handle: 投稿者、もしくはリマインダーの管理者がお知らせを削除"() {
        given:
        final loginUser = Spy(UserModel)
        final notification = Spy(NotificationModel)

        when:
        this.sut.handle(loginUser, notification.id)

        then:
        1 * this.notificationRepository.selectById(notification.id) >> Optional.of(notification)
        1 * loginUser.hasRole(Role.REMINDER_ADMIN) >> mockedHasRole
        (0..1) * notification.isContributed(loginUser) >> mockedIsContributed
        1 * this.notificationRepository.deleteById(notification.id)

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

        when:
        this.sut.handle(loginUser, notification.id)

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
        final notification = RandomHelper.mock(NotificationModel)

        when:
        this.sut.handle(loginUser, notification.id)

        then:
        1 * this.notificationRepository.selectById(notification.id) >> Optional.empty()
        final BaseException exception = thrown()
        verifyException(exception, new NotFoundException(ErrorCode.NOT_FOUND_NOTIFICATION))
    }

}
