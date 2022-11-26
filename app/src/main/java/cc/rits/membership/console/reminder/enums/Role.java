package cc.rits.membership.console.reminder.enums;

import java.util.Arrays;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ロール
 */
@Getter
@AllArgsConstructor
public enum Role {

    /**
     * リマインダーの管理者
     */
    REMINDER_ADMIN(2);

    /**
     * ロールID
     */
    private final Integer id;

    /**
     * ロールを検索
     *
     * @param id ロールID
     * @return ロール
     */
    public static Optional<Role> find(final Integer id) {
        return Arrays.stream(values()).filter(e -> e.getId().equals(id)).findFirst();
    }

}
