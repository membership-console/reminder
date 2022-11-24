package cc.rits.membership.console.reminder.usecase.notification

import cc.rits.membership.console.reminder.domain.model.NotificationBrowsingHistoryModel
import cc.rits.membership.console.reminder.domain.model.NotificationModel
import cc.rits.membership.console.reminder.domain.model.UserModel
import cc.rits.membership.console.reminder.helper.RandomHelper
import cc.rits.membership.console.reminder.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * GetNotificationsUseCaseの単体テスト
 */
class GetNotificationsUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    GetNotificationsUseCase sut

    def "handle: お知らせリストを取得できる"() {
        given:
        final loginUser = RandomHelper.mock(UserModel)

        final notifications = [
            // 既読
            NotificationModel.builder()
                .id(1)
                .browsingHistory(NotificationBrowsingHistoryModel.builder().userId(loginUser.id).build())
                .build(),
            // 未読
            NotificationModel.builder()
                .id(2)
                .build(),
        ]

        when:
        final result = this.sut.handle(loginUser, onlyUnviewed)

        then:
        1 * this.notificationRepository.selectAll() >> notifications
        1 * this.notificationRepository.insertBrowsingHistories(_)
        result*.id == expectedNotificationIds

        where:
        onlyUnviewed || expectedNotificationIds
        false        || [1, 2]
        true         || [2]
    }

}
