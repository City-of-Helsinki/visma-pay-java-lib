package org.helsinki.vismapay.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.helsinki.vismapay.model.Source;
import org.helsinki.vismapay.model.paymenttoken.PaymentMethod;

@EqualsAndHashCode(callSuper = true)
@Data
public class PaymentStatusResponse extends VismaPayResponse {
	private Boolean settled;
	private PaymentMethod.Type type;
	private Source source;
}
