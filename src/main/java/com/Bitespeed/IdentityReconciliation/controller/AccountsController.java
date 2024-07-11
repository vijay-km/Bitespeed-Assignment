package com.Bitespeed.IdentityReconciliation.controller;

import com.Bitespeed.IdentityReconciliation.constants.AccountsConstants;
import com.Bitespeed.IdentityReconciliation.dto.ErrorResponseDto;
import com.Bitespeed.IdentityReconciliation.dto.ResponseDto;
import com.Bitespeed.IdentityReconciliation.service.IAccountsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.Bitespeed.IdentityReconciliation.dto.CustomerDto;

@Tag(
        name = "CRUD REST APIs for Accounts in EazyBank",
        description = "CRUD REST APIs in EazyBank to CREATE, UPDATE, FETCH AND DELETE account details"
)
@RestController
@RequestMapping(path = "/api/v1", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
public class AccountsController {

    private IAccountsService iAccountsService;

    @Operation(
            summary = "Create Account REST API",
            description = "REST API to create new Customer &  Account inside EazyBank"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto){
        iAccountsService.CreateAccount(customerDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }

    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchCustomer(@RequestParam
                                                     @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile Number should have 10 digits")
                                                     String mobileNumber){
        CustomerDto customerDto = iAccountsService.fetchCustomer(mobileNumber);

        return new ResponseEntity<>(customerDto, HttpStatus.OK);
    }

    @PutMapping("/update")
    public  ResponseEntity<ResponseDto> updateAccount(@Valid @RequestBody CustomerDto customerDto){
        boolean isupdated = iAccountsService.updateCustomer(customerDto);

        if(isupdated){
            return new ResponseEntity<>(new ResponseDto(AccountsConstants.STATUS_200,AccountsConstants.MESSAGE_200),HttpStatus.OK);
        }
        else {
            return  new ResponseEntity<>(new ResponseDto(AccountsConstants.MESSAGE_500, AccountsConstants.STATUS_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteCustomer(@Valid @RequestBody CustomerDto customerDto){
        boolean isDeleted = iAccountsService.deleteAccount(customerDto);

        if(isDeleted){
            return new ResponseEntity<>(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ResponseDto(AccountsConstants.STATUS_500, AccountsConstants.MESSAGE_500),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
