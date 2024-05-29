package com.blackcoffee.projectmanagement.service;

import com.blackcoffee.projectmanagement.dto.StripeChargeDto;
import com.blackcoffee.projectmanagement.dto.StripeTokenDto;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Token;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {
    @Value("${api.stripe.key}")
    private String stripeApi;



    @PostConstruct
    public void init(){
        Stripe.apiKey=stripeApi;
    }

    public StripeTokenDto createCardToken(StripeTokenDto stripeTokenDto){
        System.out.println(stripeApi);
        try{

            Map<String, Object> card= new HashMap<>();
            card.put("number", stripeTokenDto.getCardNumber());
            card.put("cvc", stripeTokenDto.getCvc());
            card.put("exp_month",Integer.parseInt(stripeTokenDto.getExpMonth()));
            card.put("exp_year",Integer.parseInt(stripeTokenDto.getExpYear()));

            Map<String, Object> params= new HashMap<>();
            params.put("card",card);
            Token token= Token.create(params);
            if(token!=null && token.getId()!=null){
                stripeTokenDto.setSuccess(true);
                stripeTokenDto.setToken(token.getId());
            }
            return stripeTokenDto;

        }catch (StripeException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public StripeChargeDto charge(StripeChargeDto chargeRequest){
        try{
            chargeRequest.setSuccess(false);
            Map<String, Object> chargeParams= new HashMap<>();
            chargeParams.put("amount",(int)(chargeRequest.getAmount()*100));
            chargeParams.put("currency","usd");
            chargeParams.put("description","Payment for id "+chargeRequest.getAdditionalInfo().getOrDefault("","ID_TAG"));
            chargeParams.put("source",chargeRequest.getStripeToken());
            Map<String,Object> metadata= new HashMap<>();
            metadata.put("id",chargeRequest.getChargeId());
            metadata.putAll(chargeRequest.getAdditionalInfo());
            chargeParams.put("metadata",metadata);
            Charge charge= Charge.create(chargeParams);
            chargeRequest.setMessage(charge.getOutcome().getSellerMessage());
            if(charge.getPaid()){
                chargeRequest.setChargeId(charge.getId());
                chargeRequest.setSuccess(true);
            }
            return chargeRequest;
        }catch (StripeException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
