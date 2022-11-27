package cc.rits.membership.console.reminder.usecase.notification_reminder;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.reminder.domain.model.NotificationReminderModel;
import cc.rits.membership.console.reminder.domain.model.UserModel;
import cc.rits.membership.console.reminder.domain.repository.INotificationReminderRepository;
import cc.rits.membership.console.reminder.domain.repository.INotificationRepository;
import cc.rits.membership.console.reminder.enums.Role;
import cc.rits.membership.console.reminder.exception.ErrorCode;
import cc.rits.membership.console.reminder.exception.ForbiddenException;
import cc.rits.membership.console.reminder.exception.NotFoundException;
import cc.rits.membership.console.reminder.infrastructure.api.request.NotificationReminderCreateRequest;
import lombok.RequiredArgsConstructor;

/**
 * リマインダー作成ユースケース
 */
@RequiredArgsConstructor
@Component
@Transactional
public class CreateNotificationReminderUseCase {

    private final INotificationRepository notificationRepository;

    private final INotificationReminderRepository notificationReminderRepository;

    /**
     * Handle UseCase
     * 
     * @param loginUser ログインユーザ
     * @param notificationId お知らせID
     * @param requestBody リマインダー作成リクエスト
     */
    public void handle(final UserModel loginUser, final Integer notificationId, final NotificationReminderCreateRequest requestBody) {
        // お知らせを取得
        final var notification = this.notificationRepository.selectById(notificationId) //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_NOTIFICATION));

        // リマインダーの管理者 or 投稿者ならリマインダー作成可能
        if (loginUser.hasRole(Role.REMINDER_ADMIN) || notification.isContributed(loginUser)) {
            final var notificationReminder = NotificationReminderModel.builder() //
                .notificationId(notificationId) //
                .reminderDate(requestBody.getReminderDate()) //
                .build();
            this.notificationReminderRepository.insert(notificationReminder);
        } else {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }
    }

}
