package com.Bitespeed.IdentityReconciliation.service.impl;

import com.Bitespeed.IdentityReconciliation.dto.ContactDto;
import com.Bitespeed.IdentityReconciliation.dto.ContactResponseDto;
import com.Bitespeed.IdentityReconciliation.entity.Contact;
import com.Bitespeed.IdentityReconciliation.entity.LinkPrecedence;
import com.Bitespeed.IdentityReconciliation.repository.ContactRepository;
import com.Bitespeed.IdentityReconciliation.service.IAccountsService;
import com.Bitespeed.IdentityReconciliation.exception.ResourceNotFoundException;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    private ContactRepository contactRepository;

    /**
     * @param contactDto
     */
    @Override
    public ContactResponseDto AddUserAccount(ContactDto contactDto) {
        List<Contact> contactsByEmail = contactRepository.findByEmail(contactDto.getEmail());
        List<Contact> contactsByPhone = contactRepository.findByPhoneNumber(contactDto.getMobileNumber());

        Comparator<Contact> precedenceComparator = (c1, c2) -> {
            if (c1.getLinkPrecedence() == LinkPrecedence.PRIMARY && c2.getLinkPrecedence() == LinkPrecedence.SECONDARY) {
                return -1;
            } else if (c1.getLinkPrecedence() == LinkPrecedence.SECONDARY && c2.getLinkPrecedence() == LinkPrecedence.PRIMARY) {
                return 1;
            }
            return 0;
        };
        contactsByEmail.sort(precedenceComparator);
        contactsByPhone.sort(precedenceComparator);

        Set<String> emailSet = new HashSet<>();
        contactsByEmail.forEach(contact -> emailSet.add(contact.getEmail()));
        contactsByPhone.forEach(contact -> emailSet.add(contact.getEmail()));
        Set<String> phoneSet = new HashSet<>();
        contactsByEmail.forEach(contact -> phoneSet.add(contact.getPhoneNumber()));
        contactsByPhone.forEach(contact -> phoneSet.add(contact.getPhoneNumber()));

        // Convert sets to lists for response
        List<String> allEmails = new ArrayList<>(emailSet);
        List<String> allPhoneNumbers = new ArrayList<>(phoneSet);

        Contact primaryContact = null;
        if (!contactsByPhone.isEmpty()) {
            primaryContact = linkContacts(contactsByPhone.get(0), contactDto.getEmail(), contactDto, true);
        } else if (!contactsByEmail.isEmpty()) {
            primaryContact = linkContacts(contactsByEmail.get(0), contactDto.getMobileNumber(),contactDto, false);
        } else {
            primaryContact = createNewContact(contactDto.getEmail(), contactDto.getMobileNumber());
        }

        if (!allEmails.contains(primaryContact.getEmail())) {
            allEmails.add(primaryContact.getEmail());
        }

        if (!allPhoneNumbers.contains(primaryContact.getPhoneNumber())) {
            allPhoneNumbers.add(primaryContact.getPhoneNumber());
        }

        List<Long> secondaryContactIds = contactRepository.findByLinkedId(primaryContact.getId())
                .stream()
                .map(Contact::getId)
                .collect(Collectors.toList());

        return new ContactResponseDto(
                primaryContact.getId(),
                allEmails,
                allPhoneNumbers,
                secondaryContactIds
        );
    }

    private Contact linkContacts(Contact primaryContact, String newContactInfo, ContactDto contactDto, boolean viaPhoneNumber) {
        Contact newContact = new Contact();
        if (newContactInfo.contains("@")) {
            newContact.setEmail(newContactInfo);
            newContact.setPhoneNumber(contactDto.getMobileNumber());
        } else {
            newContact.setPhoneNumber(newContactInfo);
            newContact.setEmail(contactDto.getEmail());
        }

        if(viaPhoneNumber){
            newContact.setLinkPrecedence(LinkPrecedence.PRIMARY);
            newContact.setCreatedAt(LocalDateTime.now());
            newContact.setUpdatedAt(LocalDateTime.now());
            newContact = contactRepository.save(newContact);
             Contact contact = contactRepository.findById(primaryContact.getId()).orElseThrow(
                    ()-> new ResourceNotFoundException ("Contact", "with given :", contactDto.getEmail() + "and" + contactDto.getMobileNumber() )
            );
            contact.setLinkedId(newContact.getId());
            contact.setLinkPrecedence(LinkPrecedence.SECONDARY);
            contact = contactRepository.save(contact);
        }
        else{
            newContact.setLinkPrecedence(LinkPrecedence.SECONDARY);
            newContact.setLinkedId(primaryContact.getId());
            newContact.setCreatedAt(LocalDateTime.now());
            newContact.setUpdatedAt(LocalDateTime.now());
            contactRepository.save(newContact);
        }
        return primaryContact;
    }

    private Contact createNewContact(String email, String phoneNumber) {
        Contact newContact = new Contact();
        newContact.setEmail(email);
        newContact.setPhoneNumber(phoneNumber);
        newContact.setLinkPrecedence(LinkPrecedence.PRIMARY);
        newContact.setCreatedAt(LocalDateTime.now());
        newContact.setUpdatedAt(LocalDateTime.now());
        return contactRepository.save(newContact);
    }
}
