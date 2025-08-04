package com.example.email_sender.util;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

@Component
@Scope("prototype")
public class ErrorMessageCreator {
    public String createErrorMessage(BindingResult errors){
        StringBuilder errorMessage = new StringBuilder();

        for (ObjectError error: errors.getAllErrors()){
            errorMessage.append(error.getDefaultMessage()).append(";");
        }
        return errorMessage.toString();
    }
}
