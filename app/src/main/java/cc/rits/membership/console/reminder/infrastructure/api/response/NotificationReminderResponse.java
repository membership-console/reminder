package cc.rits.membership.console.reminder.infrastructure.api.response;

import java.time.LocalDateTime;

import cc.rits.membership.console.reminder.domain.model.NotificationReminderModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * リマインダーレスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationReminderResponse {

    /**
     * リマインダーID
     */
    @Schema(required = true)
    Integer id;

    /**
     * お知らせID
     */
    @Schema(required = true)
    Integer notificationId;

    /**
     * リマインド日時
     */
    @Schema(required = true)
    LocalDateTime reminderDate;

    public NotificationReminderResponse(final NotificationReminderModel notificationReminderModel) {
        this.id = notificationReminderModel.getId();
        this.notificationId = notificationReminderModel.getNotificationId();
        this.reminderDate = notificationReminderModel.getReminderDate();
    }

}
