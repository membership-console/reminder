package cc.rits.membership.console.reminder.infrastructure.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cc.rits.membership.console.reminder.infrastructure.api.request.EmailSendRequest;
import cc.rits.membership.console.reminder.infrastructure.api.validation.RequestValidated;
import cc.rits.membership.console.reminder.usecase.email.SendEmailUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Emailコントローラ
 */
@Tag(name = "Email", description = "Eメール")
@RestController
@RequestMapping(path = "/api/email", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
public class EmailRestController {

    private final SendEmailUseCase sendEmailUseCase;

    /**
     * メール送信API
     *
     * @param requestBody メール送信リクエスト
     */
    @PostMapping("/send")
    @ResponseStatus(HttpStatus.OK)
    public void sendEmail( //
        @RequestValidated @RequestBody final EmailSendRequest requestBody //
    ) {
        this.sendEmailUseCase.handle(requestBody);
    }

}
