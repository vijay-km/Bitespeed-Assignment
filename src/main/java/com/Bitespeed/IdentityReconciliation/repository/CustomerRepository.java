package com.Bitespeed.IdentityReconciliation.repository;


import com.Bitespeed.IdentityReconciliation.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    //Derived named method
    Optional<Customer> findByMobileNumber(String MobileNumber);


}
