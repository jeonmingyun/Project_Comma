package com.org.ticketzone;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.org.ticketzone.domain.AddressVO;
import com.org.ticketzone.domain.NumberTicketVO;
import com.org.ticketzone.domain.StoreVO;
import com.org.ticketzone.service.AddressService;
import com.org.ticketzone.service.CategorieService;
import com.org.ticketzone.service.NumberTicketService;
import com.org.ticketzone.service.StoreService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class MngrOnlyController {

	private StoreService storeService;
	private CategorieService categorieService;
	private AddressService addressService;
	private NumberTicketService numberTicketService;
	// ������ ùȭ��
	@RequestMapping(value = "/mngrOnly")
	public String admin(Model model) {

		return "/mngrOnly/mStore";
	}

	// ������ ������
	@RequestMapping(value = "/mStore_Register", method = RequestMethod.GET)
	public String mStore_Register(Model model) {

		model.addAttribute("cate", categorieService.categorieFoodList());

		return "mngrOnly/mStore/mStore_Register";
	}

	// ������ ó��(insert)
	@RequestMapping(value = "/mStore_Reg", method = RequestMethod.POST)
	public String Register(Model model, StoreVO store, AddressVO address) {

		addressService.insertAddress(address);
		storeService.storeRegister(store);

		return "/mngrOnly/mStore";
	}

	// ������ ������ �̵�

	@RequestMapping(value = "/mCustomer", method = RequestMethod.GET)
	public String mCustomer(Model model) {

		return "/mngrOnly/mCustomer";
	}

	// ������� ������ �̵�

	@RequestMapping(value = "/mState", method = RequestMethod.GET)
	public String mState(Model model) {
		
		return "/mngrOnly/mState";
	}
	
	// ��ȣǥ �߱�
	@RequestMapping(value ="/issue_ticket", method = RequestMethod.POST)
	public String isuue_ticket(Model model, NumberTicketVO ticket) {
		String codeMaker = numberTicketService.codeSelect(ticket);
		if(codeMaker.equals("not")) {
			System.out.println("�ڵ带 �߱��ؾ��մϴ�!");
			numberTicketService.firstCode(ticket);
			numberTicketService.makeTicket(ticket);
		} else {
			System.out.println("�̹��ڵ尡 �ֽ��ϴ�!");
			numberTicketService.plusTicket(ticket);
		}
		
		return "/mngrOnly/mCustomer";
	}
	
	@RequestMapping(value ="/minus_ticket", method = RequestMethod.POST)
	public String minus_ticket(Model model, NumberTicketVO ticket) {
		numberTicketService.minusTicket(ticket);
		System.out.println("��������Դϴ�.");
		return "/mngrOnly/mCustomer";
	}
}
