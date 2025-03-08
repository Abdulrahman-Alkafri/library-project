package org.example.library.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatronDTO {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 6,max = 12,message = "password length must be in the range of (6 -> 12)")
    private String password;
}
