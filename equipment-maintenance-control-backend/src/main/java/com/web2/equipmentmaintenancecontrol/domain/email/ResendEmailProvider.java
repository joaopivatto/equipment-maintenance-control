package com.web2.equipmentmaintenancecontrol.domain.email;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ResendEmailProvider implements EmailProvider {
  @Value("${email.provider.api-key}")
  private String apiKey;

  public void dispatch(Email email) {
    Resend resend = new Resend(apiKey);

    String subject = "Olá! Sua senha de acesso foi gerada";
    CreateEmailOptions params =
        CreateEmailOptions.builder()
            .from(email.sender())
            .to(email.recipient())
            .subject(subject)
            .html(email.body())
            .build();
    try {
      resend.emails().send(params);
    } catch (Exception ex) {
      throw new AppException(ErrorCode.INTERNAL_ERROR);
    }
  }
}
