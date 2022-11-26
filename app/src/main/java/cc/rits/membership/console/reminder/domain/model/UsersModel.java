package cc.rits.membership.console.reminder.domain.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ユーザリストモデル
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersModel implements Serializable {

    /**
     * ユーザリスト
     */
    List<UserModel> users;

}
