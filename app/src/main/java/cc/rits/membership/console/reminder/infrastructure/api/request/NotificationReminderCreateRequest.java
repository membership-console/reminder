package cc.rits.membership.console.reminder.infrastructure.api.request;

import java.time.LocalDateTime;

import cc.rits.membership.console.reminder.exception.BadRequestException;
import cc.rits.membership.console.reminder.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * リマインダー作成リクエスト
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationReminderCreateRequest implements IRequest {

    /**
     * リマインド日時
     */
    @Schema(required = true)
    LocalDateTime reminderDate;

    /**
     * バリデーション
     */
    @Override
    public void validate() {
        // リマインド日時
        if (this.getReminderDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException(ErrorCode.INVALID_NOTIFICATION_REMINDER_DATE);
        }
    }

}
