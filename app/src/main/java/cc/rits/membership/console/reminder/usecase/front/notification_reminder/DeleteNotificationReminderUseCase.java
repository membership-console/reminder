package cc.rits.membership.console.reminder.usecase.front.notification_reminder;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.reminder.domain.model.UserModel;
import cc.rits.membership.console.reminder.domain.repository.INotificationReminderRepository;
import cc.rits.membership.console.reminder.domain.repository.INotificationRepository;
import cc.rits.membership.console.reminder.enums.Role;
import cc.rits.membership.console.reminder.exception.ErrorCode;
import cc.rits.membership.console.reminder.exception.ForbiddenException;
import cc.rits.membership.console.reminder.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * リマインダー削除ユースケース
 */
@RequiredArgsConstructor
@Component
@Transactional
public class DeleteNotificationReminderUseCase {

    private final INotificationRepository notificationRepository;

    private final INotificationReminderRepository notificationReminderRepository;

    /**
     * Handle UseCase
     * 
     * @param loginUser ログインユーザ
     * @param notificationId お知らせID
     * @param notificationReminderId リマインダーID
     */
    public void handle(final UserModel loginUser, final Integer notificationId, final Integer notificationReminderId) {
        // お知らせを取得
        final var notification = this.notificationRepository.selectById(notificationId) //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_NOTIFICATION));

        // リマインダーの管理者 or 投稿者ならリマインダー削除可能
        if (loginUser.hasRole(Role.REMINDER_ADMIN) || notification.isContributed(loginUser)) {
            if (this.notificationReminderRepository.existsById(notificationReminderId)) {
                this.notificationReminderRepository.deleteById(notificationReminderId);
            } else {
                throw new NotFoundException(ErrorCode.NOT_FOUND_NOTIFICATION_REMINDER);
            }
        } else {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }
    }

}
