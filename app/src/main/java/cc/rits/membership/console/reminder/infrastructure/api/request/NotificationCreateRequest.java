package cc.rits.membership.console.reminder.infrastructure.api.request;

import cc.rits.membership.console.reminder.exception.BadRequestException;
import cc.rits.membership.console.reminder.exception.ErrorCode;
import cc.rits.membership.console.reminder.util.ValidationUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * お知らせ作成リクエスト
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationCreateRequest implements IRequest {

    /**
     * タイトル
     */
    @Schema(required = true)
    String title;

    /**
     * 本文
     */
    @Schema(required = true)
    String body;

    /**
     * バリデーション
     */
    @Override
    public void validate() {
        // タイトル
        if (!ValidationUtil.checkStringLength(this.getTitle(), 1, 100)) {
            throw new BadRequestException(ErrorCode.INVALID_NOTIFICATION_TITLE);
        }

        // 本文
        if (!ValidationUtil.checkStringLength(this.getBody(), 1, 1000)) {
            throw new BadRequestException(ErrorCode.INVALID_NOTIFICATION_BODY);
        }
    }

}
