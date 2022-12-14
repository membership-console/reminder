package cc.rits.membership.console.reminder.infrastructure.api.controller.front

import cc.rits.membership.console.reminder.infrastructure.api.controller.AbstractRestController_IT
import org.springframework.http.HttpStatus

/**
 * HealthCheckRestControllerの統合テスト
 */
class HealthCheckRestController_IT extends AbstractRestController_IT {

    // API PATH
    static final String BASE_PATH = "/api/health"
    static final String HEALTH_CHECK_PATH = BASE_PATH

    def "ヘルスチェックAPI: 200 OKを返す"() {
        expect:
        final request = this.getRequest(HEALTH_CHECK_PATH)
        execute(request, HttpStatus.OK)
    }

}
