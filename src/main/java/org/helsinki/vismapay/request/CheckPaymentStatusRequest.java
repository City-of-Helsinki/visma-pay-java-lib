package org.helsinki.vismapay.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.helsinki.vismapay.VismaPayClient;
import org.helsinki.vismapay.request.payload.trait.impl.BaseOrderIdentifiablePayload;
import org.helsinki.vismapay.response.PaymentStatusResponse;
import org.helsinki.vismapay.util.AuthCodeCalculator;

import java.io.Serializable;

@RequiredArgsConstructor
public class CheckPaymentStatusRequest extends VismaPayPostRequest<PaymentStatusResponse, CheckPaymentStatusRequest.PaymentStatusPayload> {

	@NonNull
	private final PaymentStatusPayload paymentStatusPayload;

	@Override
	protected PaymentStatusPayload getPayload(VismaPayClient client) {
		String authCodeStr = client.getApiKey() +
				"|" + (paymentStatusPayload.getToken() != null ? paymentStatusPayload.getToken() : paymentStatusPayload.getOrderNumber());
		paymentStatusPayload.setAuthCode(AuthCodeCalculator.calcAuthCode(client.getPrivateKey(), authCodeStr));

		return paymentStatusPayload;
	}

	@Override
	public String path() {
		return "check_payment_status";
	}

	@Override
	public Class<PaymentStatusResponse> getResponseType() {
		return PaymentStatusResponse.class;
	}

	@EqualsAndHashCode(callSuper = true)
	@Data
	@Accessors(chain = true)
	public static class PaymentStatusPayload
			extends BaseOrderIdentifiablePayload<PaymentStatusPayload>
			implements Serializable {
		private String token;
	}
}
