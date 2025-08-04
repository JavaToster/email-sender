package com.example.email_sender.validators;

import com.example.email_sender.util.ErrorMessageCreator;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
@RequiredArgsConstructor
public class EmailValidator {
    private final ErrorMessageCreator creator;
    public void validate(BindingResult errors){
        if (errors.hasErrors()){
            throw new ValidationException(creator.createErrorMessage(errors));
        }
    }
}
