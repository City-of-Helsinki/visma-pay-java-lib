package org.helsinki.vismapay.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChargeCardTokenResponse extends CardTokenResponse {
	private Byte settled;
	private Verify verify;
	private ArrayList<String> errors;

	@Data
	public static class Verify {
		private String token;
		private String type;
	}
}
