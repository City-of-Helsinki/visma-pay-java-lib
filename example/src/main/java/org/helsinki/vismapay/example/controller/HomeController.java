package org.helsinki.vismapay.example.controller;

import org.helsinki.vismapay.example.service.GetPaymentMethodsCommand;
import org.helsinki.vismapay.example.util.Strings;
import org.helsinki.vismapay.model.paymentmethods.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@Autowired
	private GetPaymentMethodsCommand getPaymentMethodsCommand;

	@RequestMapping("/")
	public String home(@ModelAttribute("message") String message, Model model) {
		addPaymentMethodsForRender(model);
		addMessageForRender(message, model);

		return "index";
	}

	private void addPaymentMethodsForRender(Model model) {
		try {
			PaymentMethod[] merchantPaymentMethods = getPaymentMethodsCommand.getMerchantPaymentMethods();
			model.addAttribute("merchantPaymentMethods", merchantPaymentMethods);
		} catch (Exception ignored) {
			model.addAttribute("merchantPaymentMethods", new String[0]);
		}
	}

	private void addMessageForRender(String message, Model model) {
		if (!Strings.isNullOrEmpty(message)) {
			model.addAttribute("paymentReturn", message);
		}
	}
}
