package org.helsinki.vismapay.example.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

	@RequestMapping("/error")
	public String handleError(@ModelAttribute("error") Object error, Model model) {
		addErrorMessageForRender(error, model);
		return "error";
	}

	@Override
	public String getErrorPath() {
		return null;
	}

	private void addErrorMessageForRender(Object error, Model model) {
		if (error instanceof Exception) {
			model.addAttribute("error", ((Exception) error).getMessage());
		} else if(error instanceof String) {
			model.addAttribute("error", error);
		} else {
			model.addAttribute("error", null);
		}
	}
}
