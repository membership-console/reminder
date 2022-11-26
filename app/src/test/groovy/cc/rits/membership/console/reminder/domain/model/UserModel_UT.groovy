package cc.rits.membership.console.reminder.domain.model

import cc.rits.membership.console.reminder.AbstractSpecification
import cc.rits.membership.console.reminder.enums.Role

/**
 * UserModelの単体テスト
 */
class UserModel_UT extends AbstractSpecification {

    def "hasRole: ロールを持つかチェック"() {
        given:
        final userGroup = UserGroupModel.builder()
            .roles(inputRoles)
            .build()
        final user = UserModel.builder()
            .userGroup(userGroup)
            .build()

        when:
        final result = user.hasRole(Role.REMINDER_ADMIN)

        then:
        result == expectedResult

        where:
        inputRoles               || expectedResult
        [Role.REMINDER_ADMIN.id] || true
        []                       || false
    }

}
