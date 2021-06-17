package org.helsinki.vismapay.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Source implements Serializable {

	public enum Type {
		@SuppressWarnings("SpellCheckingInspection")
		@SerializedName("e-payment")
		EPAYMENT("e-payment"),

		@SerializedName("card")
		CARD("card"),

		@SerializedName("terminal")
		TERMINAL("terminal");

		private final String name;

		Type(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public enum ThreeDSecureUsed {
		@SerializedName("Y")
		USED("Y"),

		@SerializedName("N")
		NOT_USED("N"),

		@SerializedName("A")
		ATTEMPTED("A");

		private final String name;

		ThreeDSecureUsed(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private Type object;
	private String brand;
	private String last4;

	@SerializedName("customer_receipt")
	private String customerReceipt;

	@SerializedName("merchant_receipt")
	private String merchantReceipt;

	@SerializedName("signature_required")
	private Boolean signatureRequired;

	@SerializedName("id_check_required")
	private Boolean idCheckRequired;

	@SerializedName("exp_year")
	private Short expYear;

	@SerializedName("exp_month")
	private Byte expMonth;

	@SerializedName("card_token")
	private String cardToken;

	@SerializedName("card_verified")
	private ThreeDSecureUsed cardVerified;

	@SerializedName("card_country")
	private String cardCountry;

	@SerializedName("client_ip_country")
	private String clientIpCountry;

	@SerializedName("error_code")
	private Integer errorCode; // TODO: ok?
}
