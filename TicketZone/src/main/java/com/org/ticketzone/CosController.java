package com.org.ticketzone;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class CosController {

	// �ڽ�
	@RequestMapping(value = "/cos", method = RequestMethod.GET)
	public String cos(Model model) {
		return "cos";
	}
}
