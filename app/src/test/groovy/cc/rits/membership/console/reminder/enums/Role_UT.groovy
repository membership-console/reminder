package cc.rits.membership.console.reminder.enums

import cc.rits.membership.console.reminder.AbstractSpecification

/**
 * Roleの単体テスト
 */
class Role_UT extends AbstractSpecification {

    def "find: ロールを検索"() {
        when:
        final result = Role.find(id)

        then:
        result == expectedResult

        where:
        id || expectedResult
        2  || Optional.of(Role.REMINDER_ADMIN)
        -1 || Optional.empty()
    }

}
