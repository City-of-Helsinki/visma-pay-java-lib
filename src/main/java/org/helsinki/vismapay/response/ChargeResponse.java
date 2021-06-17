package org.helsinki.vismapay.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChargeResponse extends VismaPayResponse {
	private String token;
	private String type;
	private ArrayList<String> errors;
}
