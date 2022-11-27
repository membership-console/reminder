package cc.rits.membership.console.reminder.infrastructure.api.response;

import java.time.LocalDate;

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
     * リマインド予定日
     */
    @Schema(required = true)
    LocalDate scheduledDate;

    public NotificationReminderResponse(final NotificationReminderModel notificationReminderModel) {
        this.id = notificationReminderModel.getId();
        this.notificationId = notificationReminderModel.getNotificationId();
        this.scheduledDate = notificationReminderModel.getScheduledDate();
    }

}
