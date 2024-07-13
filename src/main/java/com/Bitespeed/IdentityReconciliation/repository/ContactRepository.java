package com.Bitespeed.IdentityReconciliation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Bitespeed.IdentityReconciliation.entity.Contact;

import java.util.Collection;
import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByEmail(String email);
    List<Contact> findByPhoneNumber(String phoneNumber);

    List<Contact> findByLinkedId(Long id);
}
