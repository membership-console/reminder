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
        final user = UserModel.builder()
            .id(inputUserId)
            .build()

        when:
        final result = notification.isViewed(user)

        then:
        result == expectedResult

        where:
        inputUserId || expectedResult
        1           || true
        2           || false
    }

    def "isContributed: ユーザが投稿者かチェック"() {
        given:
        final notification = NotificationModel.builder()
            .contributor(Optional.ofNullable(inputContributor))
            .build()
        final user = UserModel.builder()
            .id(inputUserId)
            .build()

        when:
        final result = notification.isContributed(user)

        then:
        result == expectedResult

        where:
        inputUserId | inputContributor                  || expectedResult
        1           | UserModel.builder().id(1).build() || true
        1           | UserModel.builder().id(2).build() || false
        1           | null                              || false
    }

}
