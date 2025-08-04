package com.example.email_sender.controllers;

import com.example.email_sender.DTO.EmailDTO;
import com.example.email_sender.service.EmailService;
import com.example.email_sender.validators.EmailValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email_sender")
public class EmailController {
    private final EmailService emailService;
    private final EmailValidator emailValidator;
    @PostMapping("/send_recovery_code")
    public ResponseEntity<HttpStatus> send(@RequestBody @Valid EmailDTO emailDTO, BindingResult errors){
        emailValidator.validate(errors);

        emailService.sendRestoreCode(emailDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
