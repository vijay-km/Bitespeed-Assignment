package com.Bitespeed.IdentityReconciliation.service;

import com.Bitespeed.IdentityReconciliation.dto.ContactDto;
import com.Bitespeed.IdentityReconciliation.dto.ContactResponseDto;

public interface IAccountsService {
    public ContactResponseDto AddUserAccount(ContactDto contactDto);
}