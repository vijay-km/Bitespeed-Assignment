package com.Bitespeed.IdentityReconciliation.controller;

import com.Bitespeed.IdentityReconciliation.constants.AccountsConstants;
import com.Bitespeed.IdentityReconciliation.dto.*;
import com.Bitespeed.IdentityReconciliation.entity.Contact;
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

@Tag(
        name = "CRUD REST APIs for Contact identification at Bitespeed",
        description = "CRUD REST APIs in BiteSpeed to CREATE, UPDATE, Contact details"
)
@RestController
@RequestMapping(path = "/api/v1", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
public class AccountsController {

    private IAccountsService iAccountsService;

    @Operation(
            summary = "Create Contact REST API",
            description = "REST API to create and Update Coontact at BiteSpeed"
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
    @PostMapping("/identify")
    public ResponseEntity<ContactResponseDto> AddCreateContact(@Valid @RequestBody ContactDto contactDto){

        ContactResponseDto contactResponseDto =  iAccountsService.AddUserAccount(contactDto);

        return new ResponseEntity<>(contactResponseDto,HttpStatus.OK);

    }

}
