package cc.rits.membership.console.reminder.client;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import cc.rits.membership.console.reminder.domain.model.UserModel;
import cc.rits.membership.console.reminder.domain.model.UsersModel;
import cc.rits.membership.console.reminder.property.IamProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * IAMクライアント
 * 
 * TODO: IAMクライアントを実装する
 */
@RequiredArgsConstructor
@Component
public class IamClient {

    private final IamProperty iamProperty;

    /**
     * ユーザリストを取得
     * 
     * @return ユーザリスト
     */
    public List<UserModel> getUsers() {
        final var accessToken = this.authenticate();
        final var headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, String.format("%s %s", accessToken.token_type, accessToken.access_token));

        final var restTemplate = new RestTemplate();
        final var response = restTemplate.exchange( //
            String.format("%s/api/oauth2/users", this.iamProperty.getUrl()), //
            HttpMethod.GET, //
            new HttpEntity<String>(headers), //
            UsersModel.class //
        );

        return response.getBody().getUsers();
    }

    /**
     * ユーザを取得
     * 
     * @param userId ユーザID
     * @return ユーザリスト
     */
    public Optional<UserModel> getUser(final Integer userId) {
        final var accessToken = this.authenticate();
        final var headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, String.format("%s %s", accessToken.token_type, accessToken.access_token));

        final var restTemplate = new RestTemplate();
        final var response = restTemplate.exchange( //
            String.format("%s/api/oauth2/users/%d", this.iamProperty.getUrl(), userId), //
            HttpMethod.GET, //
            new HttpEntity<String>(headers), //
            UserModel.class //
        );
        return Optional.ofNullable(response.getBody());
    }

    /**
     * メールを送信
     * 
     * @param subject 件名
     * @param body 本文
     * @param userIds ユーザIDリスト
     */
    public void sendEmail(final String subject, final String body, final List<Integer> userIds) {
        if (userIds.isEmpty()) {
            return;
        }

        final var accessToken = this.authenticate();
        final var headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, String.format("%s %s", accessToken.token_type, accessToken.access_token));

        final var requestBody = new EmailSendRequest(subject, body, userIds);
        final var restTemplate = new RestTemplate();
        restTemplate.exchange( //
            String.format("%s/api/oauth2/email/send", this.iamProperty.getUrl()), //
            HttpMethod.POST, //
            new HttpEntity<>(requestBody, headers), //
            Void.class //
        );
    }

    /**
     * クライアント認証する
     * 
     * @return アクセストークン
     */
    public AccessToken authenticate() {
        final var url = String.format("%s/oauth2/token", this.iamProperty.getUrl());
        return null;
    }

    /**
     * アクセストークン
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AccessToken {

        /**
         * アクセストークン
         */
        String access_token;

        /**
         * トークンタイプ
         */
        String token_type;

        /**
         * スコープ
         */
        String scope;

        /**
         * TTL
         */
        Integer expires_in;

    }

    /**
     * メール送信リクエスト
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EmailSendRequest {

        /**
         * 件名
         */
        String subject;

        /**
         * 本文
         */
        String body;

        /**
         * ユーザIDリスト
         */
        List<Integer> userIds;

    }

}
