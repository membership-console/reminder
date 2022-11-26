package cc.rits.membership.console.reminder.usecase.notification

import cc.rits.membership.console.reminder.domain.model.UserModel
import cc.rits.membership.console.reminder.helper.RandomHelper
import cc.rits.membership.console.reminder.infrastructure.api.request.NotificationCreateRequest
import cc.rits.membership.console.reminder.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * CreateNotificationUseCaseの単体テスト
 */
class CreateNotificationUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    CreateNotificationUseCase sut

    def "handle: お知らせを作成すると全ユーザにメール配信される"() {
        given:
        final loginUser = RandomHelper.mock(UserModel)
        final requestBody = RandomHelper.mock(NotificationCreateRequest)

        final users = [
            RandomHelper.mock(UserModel),
            RandomHelper.mock(UserModel),
        ]

        when:
        this.sut.handle(loginUser, requestBody)

        then:
        noExceptionThrown()
        1 * this.notificationRepository.insert(_)
        1 * this.iamClient.getUsers() >> users
        1 * this.iamClient.sendEmail(requestBody.title, requestBody.body, users*.id)
    }

}
