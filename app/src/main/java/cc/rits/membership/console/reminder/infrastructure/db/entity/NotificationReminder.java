package cc.rits.membership.console.reminder.infrastructure.db.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationReminder {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column notification_reminder.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column notification_reminder.notification_id
     *
     * @mbg.generated
     */
    private Integer notificationId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column notification_reminder.reminder_date
     *
     * @mbg.generated
     */
    private LocalDateTime reminderDate;
}