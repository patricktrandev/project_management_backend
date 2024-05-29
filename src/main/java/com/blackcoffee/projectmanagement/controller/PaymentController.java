package com.blackcoffee.projectmanagement.controller;

import com.blackcoffee.projectmanagement.dto.AuthResponse;
import com.blackcoffee.projectmanagement.dto.StripeChargeDto;
import com.blackcoffee.projectmanagement.dto.StripeTokenDto;
import com.blackcoffee.projectmanagement.service.PaymentService;
import com.blackcoffee.projectmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stripe")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private UserService userService;

    @PostMapping("/card/token")
    public ResponseEntity<StripeTokenDto> createCardToken(@RequestHeader("Authorization") String token, @RequestBody StripeTokenDto stripeTokenDto){
        AuthResponse loggedInUser= userService.findUserProfileByJwt(token);
        return new ResponseEntity<>(paymentService.createCardToken(stripeTokenDto), HttpStatus.CREATED);
    }

    @PostMapping("/charge")
    public StripeChargeDto charge(@RequestBody StripeChargeDto stripeChargeDto){
        return paymentService.charge(stripeChargeDto);
    }
}
