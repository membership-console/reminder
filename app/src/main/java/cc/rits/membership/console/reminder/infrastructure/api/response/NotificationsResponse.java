package cc.rits.membership.console.reminder.infrastructure.api.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * お知らせリストレスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationsResponse {

    /**
     * お知らせリスト
     */
    List<NotificationResponse> notifications;

}
