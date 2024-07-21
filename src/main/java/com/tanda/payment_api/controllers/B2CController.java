package com.tanda.payment_api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tanda.payment_api.entities.B2CTransactions;
import com.tanda.payment_api.exceptions.HttpStatusException;
import com.tanda.payment_api.models.*;
import com.tanda.payment_api.services.b2c.B2CTransactionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public ResponseEntity<?> b2cResult(
            @RequestBody B2CResultRequestBodyForm form
    ) {
        B2CTransactions b2CTransactions = b2CTransactionService.onB2cResult(form);

        var status = HttpStatus.OK;

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .status(status.value())
                .description("Result recorded")
                .data(b2CTransactions)
                .build();

        return new ResponseEntity<>(apiResponse, status);
    }

    @PostMapping("test")
    public ResponseEntity<?> initiateB2cRequest(
            @Valid @RequestBody B2CRequestBodyForm form
    ) throws JsonProcessingException {
        B2CTransactions b2CTransactions = b2CTransactionService.initiateB2C(form);

        var status = HttpStatus.OK;

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .status(status.value())
                .description("B2C initiated")
                .data(b2CTransactions)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("transaction")
    public ResponseEntity<?> getB2CTransactionById(
            @RequestParam("transaction_id") String transactionId
    ) {

        B2CTransactions transactions = b2CTransactionService
                .findByTransactionId(transactionId)
                .orElseThrow(() -> HttpStatusException.notFound("Transaction not found"));

        var status = HttpStatus.OK;
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .status(status.value())
                .description("Transaction found")
                .data(transactions)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("transactions")
    public ResponseEntity<?> getB2cTransactions(
            @RequestParam(name = "page_size", defaultValue = "10", required = false) Integer pageSize,
            @RequestParam( defaultValue = "0", required = false) Integer page
    ) {
        Page<B2CTransactions> b2cTransactions = b2CTransactionService.getB2cTransactions(PageRequest.of(page, pageSize, Sort.by("createdAt").descending()));
        if(b2cTransactions.isEmpty()) throw HttpStatusException.notFound("No transactions found");

        var status = HttpStatus.OK;
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .status(status.value())
                .description(b2cTransactions.getNumberOfElements() + " items in page")
                .data(b2cTransactions.getContent())
                .pageDescription(PageDescription.pageDescriptionFromPage(b2cTransactions))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("transaction")
    public ResponseEntity<?> testDeleteTransaction(
            @RequestParam String id
    ) {

        b2CTransactionService.removeTransaction(id);

        var status = HttpStatus.OK;
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .description("Transaction has been removed")
                .status(status.value())
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
