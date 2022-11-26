package cc.rits.membership.console.reminder.domain.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import cc.rits.membership.console.reminder.annotation.SwaggerHiddenParameter;
import cc.rits.membership.console.reminder.enums.Role;
import lombok.*;

/**
 * ユーザモデル
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SwaggerHiddenParameter
public class UserModel implements Serializable {

    /**
     * ユーザID
     */
    Integer id;

    /**
     * ファーストネーム
     */
    String firstName;

    /**
     * ラストネーム
     */
    String lastName;

    /**
     * メールアドレス
     */
    String email;

    /**
     * 入学年度
     */
    Integer entranceYear;

    /**
     * 所属するユーザグループリスト
     */
    @Singular
    List<UserGroupModel> userGroups;

    /**
     * ロールを持つかチェック
     *
     * @param role ロール
     * @return チェック結果
     */
    public boolean hasRole(final Role role) {
        return this.getUserGroups().stream() //
            .map(UserGroupModel::getRoles) //
            .flatMap(Collection::stream) //
            .anyMatch(roleId -> role.getId().equals(roleId));
    }

}
