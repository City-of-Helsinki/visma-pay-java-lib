package org.helsinki.vismapay.model.paymenttoken;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

@Data
@Accessors(chain = true)
public class PaymentMethod implements Serializable {

	public PaymentMethod() {
		setType(Type.EPAYMENT);
	}

	public enum Type {
		@SuppressWarnings("SpellCheckingInspection")
		@SerializedName("e-payment")
		EPAYMENT("e-payment"),

		@SerializedName("embedded")
		EMBEDDED("embedded"),

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

	public enum Lang {
		@SerializedName("fi")
		FI("fi"),

		@SerializedName("en")
		EN("en"),

		@SerializedName("sv")
		SV("sv"),

		@SerializedName("ru")
		RU("ru");

		private final String name;

		Lang(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private Type type;

	@SerializedName(value = "register_card_token")
	private Boolean registerCardToken;

	@SerializedName(value = "return_url")
	private String returnUrl;

	@SerializedName(value = "notify_url")
	private String notifyUrl;

	private Lang lang = Lang.FI;

	@SerializedName(value = "token_valid_until")
	private Instant tokenValidUntil;

	@SerializedName(value = "override_auto_settlement")
	private Integer overrideAutoSettlement;

	@SerializedName(value = "selected_terminal")
	private Set<String> selectedTerminal;

	@SerializedName(value = "skip_receipt")
	private Boolean skipReceipt;

	private Set<String> selected;
}
