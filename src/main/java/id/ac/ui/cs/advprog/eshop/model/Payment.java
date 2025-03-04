package id.ac.ui.cs.advprog.eshop.model;

import enums.PaymentMethod;
import enums.PaymentStatus;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
public class Payment {
    String id;
    String method;
    @Setter
    String status;
    Map<String, String> paymentData;

    public Payment(String id, String method,Map<String, String> paymentData){
        this.id = id;
        this.setMethod(method);
        this.status = PaymentStatus.CHECK_PAYMENT.getValue();
        this.paymentData = paymentData;
    }
    public Payment(String id, String method,String status, Map<String, String> paymentData){
        this(id, method, paymentData);
        this.setStatus(status);
    }
    public void setStatus(String status){
        if (PaymentStatus.contains(status)){
            this.status = status;
        }
        else{
            throw new IllegalArgumentException();
        }
    }
    public void setMethod(String method){
        if (PaymentMethod.contains(method)){
            this.method = method;
        }
        else{
            throw new IllegalArgumentException();
        }
    }
}