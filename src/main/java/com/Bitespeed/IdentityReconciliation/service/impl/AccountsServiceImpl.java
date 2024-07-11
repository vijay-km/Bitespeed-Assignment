package com.Bitespeed.IdentityReconciliation.service.impl;

import com.Bitespeed.IdentityReconciliation.constants.AccountsConstants;
import com.Bitespeed.IdentityReconciliation.dto.CustomerDto;
import com.Bitespeed.IdentityReconciliation.entity.Customer;
import com.Bitespeed.IdentityReconciliation.exception.CustomerAlreadyExistsException;
import com.Bitespeed.IdentityReconciliation.mapper.CustomerMapper;
import com.Bitespeed.IdentityReconciliation.service.IAccountsService;
import com.Bitespeed.IdentityReconciliation.repository.AccountsRepository;
import com.Bitespeed.IdentityReconciliation.repository.CustomerRepository;
import com.Bitespeed.IdentityReconciliation.entity.Accounts;
import com.Bitespeed.IdentityReconciliation.exception.ResourceNotFoundException;
import com.Bitespeed.IdentityReconciliation.mapper.CustomerMapper;
import com.Bitespeed.IdentityReconciliation.mapper.AccountsMapper;
import com.Bitespeed.IdentityReconciliation.dto.AccountsDto;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    /**
     * @param customerDto - CustomerDto Object
     */
    @Override
    public void CreateAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(new Customer(), customerDto);
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(!optionalCustomer.isEmpty()){
            throw  new CustomerAlreadyExistsException("Customer Already Exists with this " + customerDto.getMobileNumber());
        }
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("Vijay");
        Customer savedCustomer = customerRepository.save(customer);
        Accounts customerAcccount = CreateAccount(savedCustomer);
        accountsRepository.save(customerAcccount);
    }

    @Override
    public CustomerDto fetchCustomer(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                ()->new ResourceNotFoundException("Customer", "mobileNumber" , mobileNumber));

        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));
        return customerDto;
    }

    /**
     * @param customerDto
     * @return boolean indicating that account updated or not
     */
    @Override
    public boolean updateCustomer(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if(accountsDto!=null){
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    ()-> new ResourceNotFoundException ("Accounts", "accountNumber", accountsDto.getAccountNumber().toString())
            );
            AccountsMapper.mapToAccounts(accounts, accountsDto);
            accounts = accountsRepository.save(accounts);
            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    ()-> new ResourceNotFoundException("Customer", "customerId", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customer, customerDto);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return isUpdated;
    }

    /**
     * @param customerDto
     */
    @Override
    public boolean deleteAccount(CustomerDto customerDto) {
        boolean isDeleted = false;
        Customer customer = customerRepository.findByMobileNumber(customerDto.getMobileNumber()).orElseThrow(
                ()-> new ResourceNotFoundException("Customer", "mobileNumber", customerDto.getMobileNumber())
        );
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        isDeleted = true;
        return isDeleted;
    }

    private Accounts CreateAccount(Customer customer){
        Accounts newAccount = new Accounts();
        long AccountNumber = 1000000000 + new Random().nextInt(900000000);
        newAccount.setCustomerId(customer.getCustomerId());
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setAccountNumber(AccountNumber);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setCreatedBy("Vijay");
        return newAccount;
    }
}
