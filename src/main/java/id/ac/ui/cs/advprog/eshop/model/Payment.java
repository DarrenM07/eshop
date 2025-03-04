package id.ac.ui.cs.advprog.eshop.model;

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
        this.status = "CHECKING_PAYMENT";
        this.paymentData = paymentData;
    }
    public Payment(String id, String method,String status, Map<String, String> paymentData){
        this(id, method, paymentData);
        this.setStatus(status);
    }
    public void setStatus(String status){
        if (status.equals("SUCCESS") || status.equals("REJECTED")){
            this.status = status;
        }
        else{
            throw new IllegalArgumentException();
        }
    }
    public void setMethod(String method){
        if (method.equals("VOUCHER") || method.equals("CASH")){
            this.method = method;
        }
        else{
            throw new IllegalArgumentException();
        }
    }
}