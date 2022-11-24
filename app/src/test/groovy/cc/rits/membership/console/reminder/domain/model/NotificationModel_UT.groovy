package cc.rits.membership.console.reminder.domain.model

import cc.rits.membership.console.reminder.AbstractSpecification

/**
 * NotificationModelの単体テスト
 */
class NotificationModel_UT extends AbstractSpecification {

    def "isViewed: ユーザが閲覧済みかチェック"() {
        given:
        final notification = NotificationModel.builder()
            .browsingHistory(NotificationBrowsingHistoryModel.builder().userId(1).build())
            .build()

        when:
        final result = notification.isViewed(inputUserId)

        then:
        result == expectedResult

        where:
        inputUserId || expectedResult
        1           || true
        2           || false
    }

}
