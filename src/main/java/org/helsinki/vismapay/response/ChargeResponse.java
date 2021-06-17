package org.helsinki.vismapay.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.helsinki.vismapay.model.paymenttoken.PaymentMethod;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChargeResponse extends VismaPayResponse {
	private String token;
	private PaymentMethod.Type type;
}
