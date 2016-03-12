package com.fexco.carshare.service;

import com.fexco.carshare.domain.Journey;
import com.fexco.carshare.domain.JourneyBid;
import com.fexco.carshare.domain.User;

import org.apache.commons.lang.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.mail.internet.MimeMessage;

import java.util.Locale;

/**
 * Service for sending e-mails.
 * <p/>
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    @Inject
    private Environment env;

    @Inject
    private JavaMailSenderImpl javaMailSender;

    @Inject
    private MessageSource messageSource;
    
    @Inject
    private SpringTemplateEngine templateEngine;

    /**
     * System default email address that sends the e-mails.
     */
    private String from;

    @PostConstruct
    public void init() {
        this.from = env.getProperty("spring.mail.from");
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom("carshareireland@gmail.com");//from
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'", to);
        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }

    @Async
    public void sendActivationEmail(User user, String baseUrl) {
        log.debug("Sending activation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("activationEmail", context);
        String subject = messageSource.getMessage("email.activation.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }
    
    
    @Async
    public void sendJourneyBidReceivedEmail(User user, String baseUrl, User sender, JourneyBid jBid, Journey journey) {
        log.debug("Sending JourneyBid e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseUrl", baseUrl);
        context.setVariable("sender", sender);
        context.setVariable("bid", jBid);
        context.setVariable("journey", journey);
        String content = templateEngine.process("journeyBidReceivedEmail", context);
        String subject = "Your journey received a bid";
        sendEmail(user.getEmail(), subject, content, false, true);
    }
    
    @Async
    public void sendOfferReceivedEmail(User user, String baseUrl, User sender) {
    	Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseUrl", baseUrl);
        context.setVariable("sender", sender);
        String content = templateEngine.process("journeyRequestOfferReceived", context);
        String subject = "Your journey request received an offer";
        sendEmail(user.getEmail(), subject, content, false, true);
    }
    
    @Async
	public void bidStatusChange(User user, String baseUrl,
			User sender, String stat) {
    	Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseUrl", baseUrl);
        context.setVariable("sender", sender);
        context.setVariable("status", stat);
        String content = templateEngine.process("bidStatusChangeEmail", context);
        String subject = "One of your bids has been " + stat;
        sendEmail(user.getEmail(), subject, content, false, true);
	}
}
