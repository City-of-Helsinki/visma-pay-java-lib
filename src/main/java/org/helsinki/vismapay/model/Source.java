package org.helsinki.vismapay.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Source implements Serializable {

	@SuppressWarnings("SpellCheckingInspection")
	public final static String TYPE_EPAYMENT = "e-payment";
	public final static String TYPE_CARD = "card";
	public final static String TYPE_TERMINAL = "terminal";

	private String object;
	private String brand;
	private String last4;

	@SerializedName("customer_receipt")
	private String customerReceipt;

	@SerializedName("merchant_receipt")
	private String merchantReceipt;

	@SerializedName("signature_required")
	private Boolean signatureRequired;

	@SerializedName("id_check_required")
	private Boolean idCheckRequired; // TODO: test if these work!

	@SerializedName("exp_year")
	private Short expYear;

	@SerializedName("exp_month")
	private Byte expMonth;

	@SerializedName("card_token")
	private String cardToken;

	@SerializedName("card_verified")
	private String cardVerified;

	@SerializedName("card_country")
	private String cardCountry;

	@SerializedName("client_ip_country")
	private String clientIpCountry;

	@SerializedName("error_code")
	private Integer errorCode; // TODO: ok?
}
