package cc.rits.membership.console.reminder.usecase.batch.notification_reminder

import cc.rits.membership.console.reminder.domain.model.NotificationModel
import cc.rits.membership.console.reminder.domain.model.NotificationReminderModel
import cc.rits.membership.console.reminder.domain.model.UserModel
import cc.rits.membership.console.reminder.helper.RandomHelper
import cc.rits.membership.console.reminder.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * BroadcastNotificationReminderUseCaseの単体テスト
 */
class BroadcastNotificationReminderUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    BroadcastNotificationReminderUseCase sut

    def "handle: リマインド予定日が現在のリマインダーを配信し、削除する"() {
        given:
        final notifications = [
            RandomHelper.mock(NotificationModel),
            RandomHelper.mock(NotificationModel),
            RandomHelper.mock(NotificationModel),
        ]
        notifications[0].reminders = [
            Spy(NotificationReminderModel),
            Spy(NotificationReminderModel),
            Spy(NotificationReminderModel),
        ]
        notifications[1].reminders = [
            Spy(NotificationReminderModel),
            Spy(NotificationReminderModel),
            Spy(NotificationReminderModel),
        ]
        notifications[2].reminders = [
            Spy(NotificationReminderModel),
            Spy(NotificationReminderModel),
            Spy(NotificationReminderModel),
        ]

        final users = [
            RandomHelper.mock(UserModel),
            RandomHelper.mock(UserModel),
        ]

        when:
        this.sut.handle()

        then:
        1 * this.notificationRepository.selectAll() >> notifications

        1 * notifications[0].reminders[0].isScheduledFor(_) >> true
        1 * notifications[0].reminders[1].isScheduledFor(_) >> true
        1 * notifications[0].reminders[2].isScheduledFor(_) >> false

        1 * notifications[1].reminders[0].isScheduledFor(_) >> true
        1 * notifications[1].reminders[1].isScheduledFor(_) >> false
        1 * notifications[1].reminders[2].isScheduledFor(_) >> false

        1 * notifications[2].reminders[0].isScheduledFor(_) >> false
        1 * notifications[2].reminders[1].isScheduledFor(_) >> false
        1 * notifications[2].reminders[2].isScheduledFor(_) >> false

        1 * this.iamClient.getUsers() >> users

        // 本日に予定されたリマインドを持つお知らせがメール配信される
        // 同じ日時のリマインダーが複数ある場合でも、1度だけ配信されることを期待
        1 * this.iamClient.sendEmail(notifications[0].title, notifications[0].body, users*.id)
        1 * this.iamClient.sendEmail(notifications[1].title, notifications[1].body, users*.id)
        0 * this.iamClient.sendEmail(notifications[2].title, notifications[2].body, users*.id)

        // 本日に予定されたリマインドが削除される
        1 * this.notificationReminderRepository.deleteByIds([
            notifications[0].reminders[0].id,
            notifications[0].reminders[1].id,
            notifications[1].reminders[0].id,
        ])
    }

    def "handle: リマインド予定日が現在のリマインダーが存在しない場合、何もしない"() {
        given:
        final notifications = []

        when:
        this.sut.handle()

        then:
        1 * this.notificationRepository.selectAll() >> notifications
        0 * this.iamClient.getUsers()
        0 * this.iamClient.sendEmail(_, _, _)
    }

}
