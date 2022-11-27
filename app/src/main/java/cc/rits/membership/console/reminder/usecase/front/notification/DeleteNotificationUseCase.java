package cc.rits.membership.console.reminder.usecase.front.notification;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.reminder.domain.model.UserModel;
import cc.rits.membership.console.reminder.domain.repository.INotificationRepository;
import cc.rits.membership.console.reminder.enums.Role;
import cc.rits.membership.console.reminder.exception.ErrorCode;
import cc.rits.membership.console.reminder.exception.ForbiddenException;
import cc.rits.membership.console.reminder.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * お知らせ削除ユースケース
 */
@RequiredArgsConstructor
@Component
@Transactional
public class DeleteNotificationUseCase {

    private final INotificationRepository notificationRepository;

    /**
     * Handle UseCase
     * 
     * @param loginUser ログインユーザ
     * @param notificationId お知らせID
     */
    public void handle(final UserModel loginUser, final Integer notificationId) {
        // お知らせを取得
        final var notification = this.notificationRepository.selectById(notificationId) //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_NOTIFICATION));

        // リマインダーの管理者 or 投稿者なら削除可能
        if (loginUser.hasRole(Role.REMINDER_ADMIN) || notification.isContributed(loginUser)) {
            this.notificationRepository.deleteById(notificationId);
        } else {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }
    }

}
