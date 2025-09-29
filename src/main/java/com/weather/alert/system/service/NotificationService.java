package com.weather.alert.system.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
@Slf4j
public class NotificationService {

    @Value("${twilio.account-sid}")
    private String twilioAccountSid;

    @Value("${twilio.auth-token}")
    private String twilioAuthToken;

    @Value("${twilio.phone-number}")
    private String twilioPhoneNumber;

    @Value("${mailgun.api-key}")
    private String mailgunApiKey;

    @Value("${mailgun.domain}")
    private String mailgunDomain;

    @Value("${mailgun.base-url}")
    private String mailgunBaseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean sendSms(String phoneNumber, String message) {
        try {
            String url = String.format("https://api.twilio.com/2010-04-01/Accounts/%s/Messages.json", twilioAccountSid);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            String auth = twilioAccountSid + ":" + twilioAuthToken;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            headers.set("Authorization", "Basic " + encodedAuth);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("From", twilioPhoneNumber);
            body.add("To", phoneNumber);
            body.add("Body", message);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                log.info("SMS sent successfully to {}", phoneNumber);
                return true;
            }
        } catch (Exception e) {
            log.error("Error sending SMS to {}: {}", phoneNumber, e.getMessage());
        }
        return false;
    }

    public boolean sendEmail(String email, String subject, String message) {
        try {
            String url = String.format("%s/%s/messages", mailgunBaseUrl, mailgunDomain);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            String auth = "api:" + mailgunApiKey;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            headers.set("Authorization", "Basic " + encodedAuth);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("from", "Weather Alert System <noreply@" + mailgunDomain + ">");
            body.add("to", email);
            body.add("subject", subject);
            body.add("text", message);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Email sent successfully to {}", email);
                return true;
            }
        } catch (Exception e) {
            log.error("Error sending email to {}: {}", email, e.getMessage());
        }
        return false;
    }

    public String formatWeatherAlert(String location, String condition, String description) {
        return String.format(
            "🌦️ WEATHER ALERT for %s\n\n" +
            "Condition: %s\n" +
            "Description: %s\n\n" +
            "Please take necessary precautions and stay safe!\n\n" +
            "Weather Alert System",
            location, condition, description
        );
    }
}
