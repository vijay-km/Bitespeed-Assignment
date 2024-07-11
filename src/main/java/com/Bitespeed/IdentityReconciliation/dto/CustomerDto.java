package com.Bitespeed.IdentityReconciliation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        name = "Customer",
        description = "Schema to hold Customer and Account information"
)
public class CustomerDto
{
    @Schema(
            description = "Name of the customer", example = "vijay maurya"
    )
    @NotEmpty(message = "Name can not be null or empty")
    @Size(min = 5, max = 30, message = "Name should be of length 5 to 10")
    private String name;

    @NotEmpty(message = "email can not be null or empty")
    @Email(message = "Invalid Email Address")
    private  String email;

    @NotEmpty(message = "mobile Number can not be null or empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile Number should have 10 digits")
    private  String mobileNumber;

    private AccountsDto accountsDto;
}
