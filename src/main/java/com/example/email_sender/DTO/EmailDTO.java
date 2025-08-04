package com.example.email_sender.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmailDTO {
    @NotBlank(message = "Email should be not empty")
    private String email;
    @Size(min = 6, max = 6, message = "The code must be 6 characters long.")
    private String code;
}
