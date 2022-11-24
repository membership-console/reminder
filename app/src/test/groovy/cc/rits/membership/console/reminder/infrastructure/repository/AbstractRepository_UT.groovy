package cc.rits.membership.console.reminder.infrastructure.repository

import cc.rits.membership.console.reminder.AbstractDatabaseSpecification
import cc.rits.membership.console.reminder.client.IamClient
import org.spockframework.spring.SpringBean

/**
 * Repository単体テストの基底クラス
 */
abstract class AbstractRepository_UT extends AbstractDatabaseSpecification {

    @SpringBean
    IamClient iamClient = Mock()

}