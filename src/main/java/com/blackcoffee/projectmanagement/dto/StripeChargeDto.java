package com.blackcoffee.projectmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StripeChargeDto {
    String stripeToken;
    String email;
    Double amount;
    Boolean success;
    String message;
    String chargeId;
    Map<String, Object> additionalInfo= new HashMap<>();
}
