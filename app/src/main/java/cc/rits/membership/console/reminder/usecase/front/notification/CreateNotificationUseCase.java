package cc.rits.membership.console.reminder.usecase.front.notification;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.reminder.client.IamClient;
import cc.rits.membership.console.reminder.domain.model.NotificationModel;
import cc.rits.membership.console.reminder.domain.model.UserModel;
import cc.rits.membership.console.reminder.domain.repository.INotificationRepository;
import cc.rits.membership.console.reminder.infrastructure.api.request.NotificationCreateRequest;
import lombok.RequiredArgsConstructor;

/**
 * お知らせ作成ユースケース
 */
@RequiredArgsConstructor
@Component
@Transactional
public class CreateNotificationUseCase {

    private final INotificationRepository notificationRepository;

    private final IamClient iamClient;

    /**
     * Handle UseCase
     * 
     * @param loginUser ログインユーザ
     * @param requestBody お知らせ作成リクエスト
     */
    public void handle(final UserModel loginUser, final NotificationCreateRequest requestBody) {
        // お知らせを作成
        final var notification = new NotificationModel(requestBody.getTitle(), requestBody.getBody(), loginUser);
        this.notificationRepository.insert(notification);

        // お知らせメールを全ユーザに配信
        // TODO: 同じアクセストークンを使い回す
        final var userIds = this.iamClient.getUsers().stream() //
            .map(UserModel::getId) //
            .toList();
        this.iamClient.sendEmail(requestBody.getTitle(), requestBody.getBody(), userIds);
    }

}
