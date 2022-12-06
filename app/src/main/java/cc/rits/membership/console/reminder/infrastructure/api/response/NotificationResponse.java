package cc.rits.membership.console.reminder.infrastructure.api.response;

import java.time.LocalDateTime;
import java.util.List;

import cc.rits.membership.console.reminder.domain.model.NotificationBrowsingHistoryModel;
import cc.rits.membership.console.reminder.domain.model.NotificationModel;
import cc.rits.membership.console.reminder.domain.model.UserModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * お知らせレスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {

    /**
     * お知らせID
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Integer id;

    /**
     * タイトル
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String title;

    /**
     * 本文
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String body;

    /**
     * 既読フラグ
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Boolean isViewed;

    /**
     * 投稿者
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, nullable = true)
    UserResponse contributor;

    /**
     * 投稿日
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDateTime postedDate;

    /**
     * リマインダーリスト
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    List<NotificationReminderResponse> reminders;

    public NotificationResponse(final NotificationModel notificationModel, final UserModel loginUser) {
        this.id = notificationModel.getId();
        this.title = notificationModel.getTitle();
        this.body = notificationModel.getBody();
        this.isViewed = notificationModel.getBrowsingHistories().stream() //
            .map(NotificationBrowsingHistoryModel::getUserId) //
            .toList() //
            .contains(loginUser.getId());
        this.contributor = notificationModel.getContributor().isPresent() //
            ? new UserResponse(notificationModel.getContributor().get()) //
            : null;
        this.postedDate = notificationModel.getPostedDate();
        this.reminders = notificationModel.getReminders().stream() //
            .map(NotificationReminderResponse::new) //
            .toList();
    }

}
