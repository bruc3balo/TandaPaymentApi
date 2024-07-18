package com.tanda.payment_api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tanda.payment_api.entities.B2CTransactions;
import com.tanda.payment_api.exceptions.HttpStatusException;
import com.tanda.payment_api.models.*;
import com.tanda.payment_api.services.b2c.B2CTransactionService;
import jakarta.validation.Valid;
import org.apache.kafka.shaded.com.google.protobuf.Api;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("b2c")
public class B2CController {

    private final B2CTransactionService b2CTransactionService;

    public B2CController(B2CTransactionService b2CTransactionService) {
        this.b2CTransactionService = b2CTransactionService;
    }

    @PostMapping("result")
    public ResponseEntity<?> b2cResult(@RequestBody B2CResultRequestBodyForm form) {
        B2CTransactions b2CTransactions = b2CTransactionService.onB2cResult(form);

        var status = HttpStatus.OK;

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .status(status.value())
                .description("Result recorded")
                .data(b2CTransactions)
                .build();

        return new ResponseEntity<>(apiResponse, status);
    }

    @PostMapping
    public ResponseEntity<?> initiateB2cRequest(@Valid @RequestBody B2CRequestBodyForm form) throws JsonProcessingException {
        B2CTransactions b2CTransactions = b2CTransactionService.initiateB2C(form);
        var status = HttpStatus.OK;

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .status(status.value())
                .description("B2C initiated")
                .data(b2CTransactions)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getB2cTransactions(@RequestParam(name = "page_size", defaultValue = "10", required = false) Integer pageSize,
                                                @RequestParam( defaultValue = "0", required = false) Integer page) {
        Page<B2CTransactions> b2cTransactions = b2CTransactionService.getB2cTransactions(PageRequest.of(page, pageSize, Sort.by("createdAt").descending()));
        if(b2cTransactions.isEmpty()) throw HttpStatusException.notFound("No transactions found");

        var status = HttpStatus.OK;
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .status(status.value())
                .data(b2cTransactions.getContent())
                .pageDescription(PageDescription.pageDescriptionFromPage(b2cTransactions))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
