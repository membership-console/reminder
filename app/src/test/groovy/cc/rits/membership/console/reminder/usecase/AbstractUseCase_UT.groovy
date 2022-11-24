package cc.rits.membership.console.reminder.usecase

import cc.rits.membership.console.reminder.AbstractSpecification
import cc.rits.membership.console.reminder.domain.repository.INotificationRepository
import org.spockframework.spring.SpringBean

/**
 * UseCase単体テストの基底クラス
 */
abstract class AbstractUseCase_UT extends AbstractSpecification {

    @SpringBean
    INotificationRepository notificationRepository = Mock()

}