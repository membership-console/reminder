package cc.rits.membership.console.reminder

import org.springframework.transaction.annotation.Transactional

/**
 * DBテストの基底クラス
 */
@Transactional
abstract class AbstractDatabaseSpecification extends AbstractSpecification {

    /**
     * cleanup after test case
     */
    void cleanup() {
        // DBを初期化するために、テスト終了時にロールバック
        sql.rollback()
    }

}
