package org.helsinki.vismapay.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.helsinki.vismapay.model.Source;

@EqualsAndHashCode(callSuper = true)
@Data
public class CardTokenResponse extends VismaPayResponse {
	private Source source;
}
