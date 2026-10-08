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

    CreateEmailOptions params =
        CreateEmailOptions.builder()
            .from(email.sender())
            .to(email.recipient())
            .subject(email.subject())
            .html(email.body())
            .build();
    try {
      resend.emails().send(params);
    } catch (Exception ex) {
      throw new AppException(ErrorCode.EMAIL_PROVIDER_INTEGRATION_ERROR);
    }
  }
}
