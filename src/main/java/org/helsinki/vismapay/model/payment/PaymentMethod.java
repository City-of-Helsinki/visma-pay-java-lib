package org.helsinki.vismapay.model.payment;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.Instant;

@Data
@Accessors(chain = true)
public class PaymentMethod implements Serializable {

	@SuppressWarnings("SpellCheckingInspection")
	public final static String TYPE_EPAYMENT = "e-payment";
	public final static String TYPE_EMBEDDED = "embedded";
	public final static String TYPE_TERMINAL = "terminal";
	public final static String TYPE_CARD = "card";

	public PaymentMethod() {
		setType(TYPE_EPAYMENT);
	}

	private String type;

	@SerializedName(value = "register_card_token")
	private Boolean registerCardToken;

	@SerializedName(value = "return_url")
	private String returnUrl;

	@SerializedName(value = "notify_url")
	private String notifyUrl;

	private String lang = "fi";

	@SerializedName(value = "token_valid_until")
	private Instant tokenValidUntil;

	@SerializedName(value = "override_auto_settlement")
	private Integer overrideAutoSettlement;

	@SerializedName(value = "selected_terminal")
	private String[] selectedTerminal;

	@SerializedName(value = "skip_receipt")
	private Boolean skipReceipt;

	private String[] selected;
}
