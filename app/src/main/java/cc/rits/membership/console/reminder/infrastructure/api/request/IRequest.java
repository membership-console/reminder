package cc.rits.membership.console.reminder.infrastructure.api.request;

import cc.rits.membership.console.reminder.exception.BaseException;

/**
 * リクエストボディのインターフェース
 */
public interface IRequest {

    /**
     * リクエストボディのバリデーション
     */
    void validate() throws BaseException;

}
