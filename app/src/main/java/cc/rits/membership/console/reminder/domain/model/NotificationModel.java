package cc.rits.membership.console.reminder.domain.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import cc.rits.membership.console.reminder.infrastructure.db.entity.join.NotificationWithRemindersAndBrowsingHistories;
import lombok.*;

/**
 * お知らせモデル
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationModel implements Serializable {

    /**
     * お知らせID
     */
    Integer id;

    /**
     * タイトル
     */
    String title;

    /**
     * 本文
     */
    String body;

    /**
     * 投稿者
     */
    Optional<UserModel> contributor;

    /**
     * 投稿日
     */
    @Builder.Default
    LocalDateTime postedDate = LocalDateTime.now();

    /**
     * リマインダーリスト
     */
    @Singular
    List<NotificationReminderModel> reminders;

    /**
     * 閲覧履歴リスト
     */
    @Singular
    List<NotificationBrowsingHistoryModel> browsingHistories;

    public NotificationModel(final NotificationWithRemindersAndBrowsingHistories notification, final Optional<UserModel> contributor) {
        this.id = notification.getId();
        this.title = notification.getTitle();
        this.body = notification.getBody();
        this.contributor = contributor;
        this.postedDate = notification.getPostedDate();
        this.reminders = notification.getReminders().stream() //
            .map(NotificationReminderModel::new) //
            .collect(Collectors.toList());
        this.browsingHistories = notification.getBrowsingHistories().stream() //
            .map(NotificationBrowsingHistoryModel::new) //
            .collect(Collectors.toList());
    }

    public NotificationModel(final String title, final String body, final UserModel user) {
        this.title = title;
        this.body = body;
        this.contributor = Optional.of(user);
        this.postedDate = LocalDateTime.now();
        this.reminders = List.of();
        this.browsingHistories = List.of();
    }

    /**
     * ユーザが閲覧済みかチェック
     * 
     * @param user ユーザ
     * @return チェック結果
     */
    public boolean isViewed(final UserModel user) {
        return this.browsingHistories.stream() //
            .anyMatch(browsingHistory -> browsingHistory.getUserId().equals(user.getId()));
    }

    /**
     * ユーザが投稿者かチェック
     * 
     * @param user ユーザ
     * @return チェック結果
     */
    public boolean isContributed(final UserModel user) {
        return this.getContributor().isPresent() && Objects.equals(this.getContributor().get().getId(), user.getId());
    }

}
