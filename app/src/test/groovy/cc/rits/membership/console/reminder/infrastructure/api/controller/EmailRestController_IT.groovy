package cc.rits.membership.console.reminder.infrastructure.api.controller

import cc.rits.membership.console.reminder.exception.BadRequestException
import cc.rits.membership.console.reminder.exception.ErrorCode
import cc.rits.membership.console.reminder.exception.UnauthorizedException
import cc.rits.membership.console.reminder.helper.RandomHelper
import cc.rits.membership.console.reminder.infrastructure.api.request.EmailSendRequest
import org.springframework.http.HttpStatus

/**
 * EmailRestControllerの統合テスト
 */
class EmailRestController_IT extends AbstractRestController_IT {

    // API PATH
    static final String BASE_PATH = "/api/email"
    static final String SEND_EMAIL_PATH = BASE_PATH + "/send"

    def "メール送信API: 正常系 メール送信できる"() {
        given:
        this.login([])

        final requestBody = EmailSendRequest.builder()
            .subject(RandomHelper.alphanumeric(10))
            .body(RandomHelper.alphanumeric(10))
            .userIds([1])
            .build()

        when:
        final request = this.postRequest(SEND_EMAIL_PATH, requestBody)
        execute(request, HttpStatus.OK)

        then:
        1 * this.iamClient.sendEmail(requestBody.subject, requestBody.body, requestBody.userIds)
    }

    def "メール送信API: 異常系 リクエストボディのバリデーション"() {
        given:
        this.login([])

        final requestBody = EmailSendRequest.builder()
            .subject(RandomHelper.alphanumeric(10))
            .body(RandomHelper.alphanumeric(10))
            .userIds([])
            .build()

        expect:
        final request = this.postRequest(SEND_EMAIL_PATH, requestBody)
        execute(request, new BadRequestException(ErrorCode.USER_IDS_MUST_NOT_BE_EMPTY))
    }

    def "メール送信API: 異常系 ログインしていない場合は401エラー"() {
        given:
        final requestBody = EmailSendRequest.builder()
            .subject(RandomHelper.alphanumeric(10))
            .body(RandomHelper.alphanumeric(10))
            .userIds([1])
            .build()

        expect:
        final request = this.postRequest(SEND_EMAIL_PATH, requestBody)
        execute(request, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

}
