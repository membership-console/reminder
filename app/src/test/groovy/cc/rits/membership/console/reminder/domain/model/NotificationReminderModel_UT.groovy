package cc.rits.membership.console.reminder.domain.model

import cc.rits.membership.console.reminder.AbstractSpecification
import cc.rits.membership.console.reminder.helper.DateHelper

/**
 * NotificationReminderModelの単体テスト
 */
class NotificationReminderModel_UT extends AbstractSpecification {

    def "isScheduledFor: 〜にリマインド予定されているかチェック"() {
        given:
        final notificationReminder = NotificationReminderModel.builder()
            .scheduledDate(DateHelper.today().toLocalDate())
            .build()

        when:
        final result = notificationReminder.isScheduledFor(inputDate)

        then:
        result == expectedResult

        where:
        inputDate                            || expectedResult
        DateHelper.tomorrow().toLocalDate()  || true
        DateHelper.today().toLocalDate()     || true
        DateHelper.yesterday().toLocalDate() || false
    }

}
