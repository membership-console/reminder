package cc.rits.membership.console.reminder.domain.model;

import java.io.Serializable;
import java.util.List;

import lombok.*;

/**
 * ユーザグループモデル
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupModel implements Serializable {

    /**
     * ユーザグループID
     */
    Integer id;

    /**
     * ユーザグループ名
     */
    String name;

    /**
     * ロールIDリスト
     */
    @Singular
    List<Integer> roles;

}
