package sk.filo.plantdiary.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import sk.filo.plantdiary.config.ConfigProperties;

@Service
public class MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    private ConfigProperties configProperties;

    private JavaMailSender javaMailSender;

    public MailService(ConfigProperties configProperties, JavaMailSender javaMailSender) {
        this.configProperties = configProperties;
        this.javaMailSender = javaMailSender;
    }

    public void sendVerificationEmail(String recipient, String activationKey) {
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(configProperties.getServerEmail());
            messageHelper.setTo(recipient);
            messageHelper.setSubject("My plant diary - Email verification"); // TODO bundle
            messageHelper.setText("Please use this unique activation key to verify your email address: " + activationKey, false); // TODO bundle
        };
        javaMailSender.send(mimeMessagePreparator);
    }

}
