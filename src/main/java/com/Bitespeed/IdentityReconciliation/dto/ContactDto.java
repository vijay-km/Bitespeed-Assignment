package com.Bitespeed.IdentityReconciliation.dto;

import com.Bitespeed.IdentityReconciliation.customValidation.ValidContact;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
@Schema(
        name = "Contact",
        description = "Schema to hold Customer and information"
)
@ValidContact
public class ContactDto
{

    @Schema(
            description = "Name of the customer", example = "vijay920@gmail.com"
    )
   // @NotEmpty(message = "email can not be null or empty")
    @Email(message = "Invalid Email Address")
    private  String email;
   // @NotEmpty(message = "mobile Number can not be null or empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile Number should have 10 digits")
    private  String mobileNumber;

}
