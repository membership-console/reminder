package cc.rits.membership.console.reminder.usecase.email


import cc.rits.membership.console.reminder.helper.RandomHelper
import cc.rits.membership.console.reminder.infrastructure.api.request.EmailSendRequest
import cc.rits.membership.console.reminder.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * SendEmailUseCaseの単体テスト
 */
class SendEmailUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    SendEmailUseCase sut

    def "handle: メールを送信できる"() {
        given:
        final requestBody = RandomHelper.mock(EmailSendRequest)

        when:
        this.sut.handle(requestBody)

        then:
        noExceptionThrown()
        1 * this.iamClient.sendEmail(requestBody.subject, requestBody.body, requestBody.userIds)
    }

}
