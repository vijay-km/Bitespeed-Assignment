package com.Bitespeed.IdentityReconciliation.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter @Setter @ToString @AllArgsConstructor
@NoArgsConstructor
public class Accounts extends BaseEntity{
    @Id
    @Column(name = "account_number")
    private  long accountNumber;

    @Column(name = "customer_id")
    private  Long customerId;

    @Column(name = "account_type")
    private String accountType ;

    @Column(name = "branch_address")
    private String branchAddress ;
}