package com.Bitespeed.IdentityReconciliation.customValidation;

import com.Bitespeed.IdentityReconciliation.dto.ContactDto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ContactValidator implements ConstraintValidator<ValidContact, ContactDto> {

    @Override
    public void initialize(ValidContact constraintAnnotation) {
    }

    @Override
    public boolean isValid(ContactDto contactDto, ConstraintValidatorContext context) {
        return contactDto.getEmail() != null || contactDto.getMobileNumber() != null;
    }
}
