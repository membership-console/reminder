package cc.rits.membership.console.reminder.usecase.front.email;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.reminder.client.IamClient;
import cc.rits.membership.console.reminder.infrastructure.api.request.EmailSendRequest;
import lombok.RequiredArgsConstructor;

/**
 * メール送信ユースケース
 */
@RequiredArgsConstructor
@Component
@Transactional
public class SendEmailUseCase {

    private final IamClient iamClient;

    /**
     * Handle UseCase
     *
     * @param requestBody メール送信リクエスト
     */
    public void handle(final EmailSendRequest requestBody) {
        this.iamClient.sendEmail(requestBody.getSubject(), requestBody.getBody(), requestBody.getUserIds());
    }

}
