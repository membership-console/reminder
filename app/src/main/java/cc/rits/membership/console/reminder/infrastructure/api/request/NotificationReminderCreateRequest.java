package cc.rits.membership.console.reminder.infrastructure.api.request;

import java.time.LocalDate;

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
     * リマインド予定日
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate scheduledDate;

    /**
     * バリデーション
     */
    @Override
    public void validate() {
        // リマインド予定日
        if (this.getScheduledDate().isBefore(LocalDate.now())) {
            throw new BadRequestException(ErrorCode.INVALID_NOTIFICATION_REMINDER_DATE);
        }
    }

}
