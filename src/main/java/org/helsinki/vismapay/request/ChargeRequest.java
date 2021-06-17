package org.helsinki.vismapay.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.helsinki.vismapay.VismaPayClient;
import org.helsinki.vismapay.gson.adapter.InstantSerializer;
import org.helsinki.vismapay.gson.adapter.ProductTypeSerializer;
import org.helsinki.vismapay.model.Customer;
import org.helsinki.vismapay.model.Product;
import org.helsinki.vismapay.model.paymenttoken.PaymentMethod;
import org.helsinki.vismapay.request.payload.trait.impl.BaseOrderIdentifiablePayload;
import org.helsinki.vismapay.response.ChargeResponse;
import org.helsinki.vismapay.util.AuthCodeCalculator;

import java.math.BigInteger;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class ChargeRequest extends VismaPayPostRequest<ChargeResponse, ChargeRequest.PaymentTokenPayload> {

	@NonNull
	private final PaymentTokenPayload paymentTokenPayload;

	@Override
	protected PaymentTokenPayload getPayload(VismaPayClient client) {
		String authCodeStr = client.getApiKey() + "|" + paymentTokenPayload.getOrderNumber();
		paymentTokenPayload.setAuthCode(AuthCodeCalculator.calcAuthCode(client.getPrivateKey(), authCodeStr));

		return paymentTokenPayload;
	}

	@Override
	public String path() {
		return "auth_payment";
	}

	@Override
	public Class<ChargeResponse> getResponseType() {
		return ChargeResponse.class;
	}

	@Override
	protected Gson getDefaultGson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Product.Type.class, new ProductTypeSerializer());
		gsonBuilder.registerTypeAdapter(Instant.class, new InstantSerializer());

		return gsonBuilder.create();
	}

	@EqualsAndHashCode(callSuper = true)
	@Data
	@Accessors(chain = true)
	public static class PaymentTokenPayload extends BaseOrderIdentifiablePayload<PaymentTokenPayload> {

		private BigInteger amount;
		private String currency;
		private String email;

		@SerializedName("payment_method")
		private PaymentMethod paymentMethod;

		private Customer customer;
		private Set<Product> products = new HashSet<>();

		public void addProduct(Product product) {
			products.add(product);
		}
	}
}
