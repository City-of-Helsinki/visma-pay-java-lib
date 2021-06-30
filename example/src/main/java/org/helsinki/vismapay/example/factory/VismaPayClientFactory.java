package org.helsinki.vismapay.example.factory;

import org.helsinki.vismapay.VismaPayClient;
import org.springframework.stereotype.Component;

@Component
public class VismaPayClientFactory {

	public VismaPayClient create() {
		return new VismaPayClient(
				"api_key",
				"private_key"
		);
	}
}
