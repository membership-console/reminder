package cc.rits.membership.console.reminder.infrastructure.api.controller

import cc.rits.membership.console.reminder.AbstractDatabaseSpecification
import cc.rits.membership.console.reminder.auth.LoginUserDetails
import cc.rits.membership.console.reminder.client.IamClient
import cc.rits.membership.console.reminder.domain.model.UserGroupModel
import cc.rits.membership.console.reminder.domain.model.UserModel
import cc.rits.membership.console.reminder.enums.Role
import cc.rits.membership.console.reminder.exception.BaseException
import cc.rits.membership.console.reminder.helper.JsonConvertHelper
import cc.rits.membership.console.reminder.helper.RandomHelper
import cc.rits.membership.console.reminder.infrastructure.api.response.ErrorResponse
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.util.MultiValueMap
import org.springframework.web.context.WebApplicationContext
import spock.lang.Shared

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity

/**
 * RestController統合テストの基底クラス
 */
abstract class AbstractRestController_IT extends AbstractDatabaseSpecification {

    @SpringBean
    IamClient iamClient = Mock()

    private MockMvc mockMvc

    @Autowired
    private WebApplicationContext webApplicationContext

    @Autowired
    private PlatformTransactionManager transactionManager

    @Autowired
    private MessageSource messageSource

    @Shared
    protected Authentication authentication = null

    /**
     * ログインユーザのID
     */
    static final Integer LOGIN_USER_ID = 1

    /**
     * ログインユーザのメールアドレス
     */
    static final String LOGIN_USER_EMAIL = RandomHelper.email()

    /**
     * GET request
     *
     * @param path path
     *
     * @return HTTP request builder
     */
    protected MockHttpServletRequestBuilder getRequest(final String path) {
        return MockMvcRequestBuilders.get(path)
            .with(authentication(this.authentication))
    }

    /**
     * POST request
     *
     * @param path path
     *
     * @return HTTP request builder
     */
    protected MockHttpServletRequestBuilder postRequest(final String path) {
        return MockMvcRequestBuilders.post(path)
            .with(authentication(this.authentication))
    }

    /**
     * POST request (Form)
     *
     * @param path path
     * @param params query params
     *
     * @return HTTP request builder
     */
    protected MockHttpServletRequestBuilder postRequest(final String path, final MultiValueMap<String, String> params) {
        return MockMvcRequestBuilders.post(path)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .params(params)
            .with(authentication(this.authentication))
    }

    /**
     * POST request (JSON)
     *
     * @param path path
     * @param content request body
     *
     * @return HTTP request builder
     */
    protected MockHttpServletRequestBuilder postRequest(final String path, final Object content) {
        return MockMvcRequestBuilders.post(path)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(JsonConvertHelper.convertObjectToJson(content))
            .with(authentication(this.authentication))
    }

    /**
     * PUT request (JSON)
     *
     * @param path path
     * @param content request body
     *
     * @return HTTP request builder
     */
    protected MockHttpServletRequestBuilder putRequest(final String path, final Object content) {
        return MockMvcRequestBuilders.put(path)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(JsonConvertHelper.convertObjectToJson(content))
            .with(authentication(this.authentication))
    }

    /**
     * DELETE request
     *
     * @param path path
     *
     * @return HTTP request builder
     */
    protected MockHttpServletRequestBuilder deleteRequest(final String path) {
        return MockMvcRequestBuilders.delete(path)
            .with(authentication(this.authentication))
    }

    /**
     * Execute request
     *
     * @param request HTTP request builder
     * @param status expected HTTP status
     *
     * @return MVC result
     */
    protected MvcResult execute(final MockHttpServletRequestBuilder request, final HttpStatus status) {
        final result = this.mockMvc.perform(request).andReturn()

        assert result.response.status == status.value()
        return result
    }

    /**
     * Execute request / return response
     *
     * @param request HTTP request builder
     * @param status expected HTTP status
     * @param clazz response class
     *
     * @return response
     */
    protected <T> T execute(final MockHttpServletRequestBuilder request, final HttpStatus status, final Class<T> clazz) {
        final result = this.mockMvc.perform(request).andReturn()

        assert result.response.status == status.value()
        return JsonConvertHelper.convertJsonToObject(result.getResponse().getContentAsString(), clazz)
    }

    /**
     * Execute request / verify exception
     *
     * @param request HTTP request builder
     * @param exception expected exception
     *
     * @return error response
     */
    protected ErrorResponse execute(final MockHttpServletRequestBuilder request, final BaseException exception) {
        final result = this.mockMvc.perform(request).andReturn()
        final response = JsonConvertHelper.convertJsonToObject(result.response.contentAsString, ErrorResponse.class)

        final expectedErrorMessage = this.getErrorMessage(exception)

        assert result.response.status == exception.httpStatus.value()
        assert result.response.status == exception.errorCode.httpStatus.value()
        assert response.code == exception.errorCode.code
        assert response.message == expectedErrorMessage
        return response
    }

    /**
     * エラーメッセージを取得
     *
     * @param exception exception
     * @return エラーメッセージ
     */
    private String getErrorMessage(final BaseException exception) {
        final messageKey = exception.errorCode.messageKey
        final args = exception.args
        return this.messageSource.getMessage(messageKey, args, Locale.ENGLISH)
    }

    /**
     * ログイン
     *
     * @param roles ロールリスト
     * @return ログインユーザ
     */
    protected UserModel login(final List<Role> roles) {
        final loginUser = RandomHelper.mock(UserModel)
        loginUser.setId(LOGIN_USER_ID)
        loginUser.setEmail(LOGIN_USER_EMAIL)

        final userGroup = RandomHelper.mock(UserGroupModel)
        userGroup.setRoles(roles*.id)
        loginUser.setUserGroups([userGroup])

        final authorities = AuthorityUtils.createAuthorityList("ROLE_USER")
        final principal = new LoginUserDetails(loginUser, authorities)
        this.authentication = new UsernamePasswordAuthenticationToken(principal, null, authorities)

        return loginUser
    }

    /**
     * ログアウト
     */
    protected void logout() {
        this.authentication = null
    }

    /**
     * setup before test case
     */
    void setup() {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(this.webApplicationContext)
            .addFilter(({ request, response, chain ->
                response.setCharacterEncoding("UTF-8")
                chain.doFilter(request, response)
            }))
            .apply(springSecurity())
            .build()
    }

    /**
     * cleanup after test case
     */
    void cleanup() {
        this.logout()
    }

}
