
package com.org.ticketzone;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.ticketzone.domain.CoordinatesVO;
import com.org.ticketzone.domain.NumberTicketVO;
import com.org.ticketzone.domain.OwnerVO;
import com.org.ticketzone.domain.StoreVO;
import com.org.ticketzone.service.CategorieService;
import com.org.ticketzone.service.CoordinatesService;
import com.org.ticketzone.service.MemberService;
import com.org.ticketzone.service.NumberTicketService;
import com.org.ticketzone.service.StoreService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class MngrOnlyController {

	private StoreService storeService;
	private CategorieService categorieService;
	private CoordinatesService coordinatesService;
	private NumberTicketService numberTicketService;
	private MemberService memberService;

	// ������ ùȭ��
	@RequestMapping(value = "/mngrOnly")
	public String admin(Model model, HttpSession session) {
		ArrayList<StoreVO> store_arr = new ArrayList<StoreVO>();
		ArrayList<OwnerVO> owner_arr = (ArrayList<OwnerVO>) session.getAttribute("id");
		String owner_id = owner_arr.get(0).getOwner_id();

		store_arr = storeService.storeGet(owner_id);
		session.setAttribute("store", store_arr);

		return "/mngrOnly/mStore";
	}

	// ������ �α׾ƿ�
	@RequestMapping(value = "/mngrLogout")
	public String logout(HttpSession session) {
		session.invalidate();

		return "mngr";
	}

	// ������ ������
	@RequestMapping(value = "/mStore_Register", method = RequestMethod.GET)
	public String mStore_Register(Model model) {
		model.addAttribute("cate", categorieService.categorieFoodList());

		return "mngrOnly/mStore/mStore_Register";
	}

	// ������ ó��(insert)
	@RequestMapping(value = "/mStore_Reg", method = RequestMethod.POST)
	public String Register(Model model, StoreVO store, CoordinatesVO coor) {
		coordinatesService.insertXY(coor);
		storeService.storeRegister(store);

		return "/mngrOnly/mStore";
	}

	// ������� ������ �̵�
	@RequestMapping(value = "/updmStore_Register", method = RequestMethod.GET)
	public String updmStore_Register(StoreVO storeVO, Model model, CoordinatesVO coor) {
		String license = storeVO.getLicense_number();
		model.addAttribute("coor", coordinatesService.XYList(license));
		model.addAttribute("updmStore", storeService.storeUpdate(license));
		model.addAttribute("cate", categorieService.categorieFoodList());
		return "/mngrOnly/mStore/updmStore_Register";
	}

	// ������� ó��
	@RequestMapping(value = "/updmStore_Complete", method = RequestMethod.POST)
	public String updmStore_Complete(StoreVO store) {
		
		storeService.storeUpdCom(store);

		return "redirect:/mngrOnly";
	}

	// ������ ������ �̵�
	@RequestMapping(value = "/mCustomer", method = RequestMethod.GET)
	public String mCustomer(Model model, HttpServletRequest request) {
		String license_number = request.getParameter("license_number");
		model.addAttribute("license_number", numberTicketService.waitList(license_number));
		model.addAttribute("wait", numberTicketService.tWait(license_number));
		model.addAttribute("success", numberTicketService.tSuccess(license_number));
		model.addAttribute("cancel", numberTicketService.tCancel(license_number));
		model.addAttribute("absence", numberTicketService.tAbsence(license_number));
		model.addAttribute("member", memberService.memberTest());

		return "/mngrOnly/mCustomer";
	}

	@ResponseBody
	@RequestMapping(value = "/getLicense", method = RequestMethod.POST)
	public String getLicense(Model model, HttpServletRequest request) {
		String license_number = request.getParameter("license_number");
		model.addAttribute("license_number", numberTicketService.waitList(license_number));

		return " ";
	}

	// ������� ������ �̵�
	@RequestMapping(value = "/mState", method = RequestMethod.GET)
	public String mState(Model model) {

		return "/mngrOnly/mState";
	}

	// ��ȣǥ �߱�
	@ResponseBody
	@RequestMapping(value = "/issue_ticket", method = RequestMethod.POST)
	public String isuue_ticket(Model model, NumberTicketVO ticket) {

		String codeMaker = numberTicketService.codeSelect(ticket);
		System.out.println(codeMaker + "code");
		System.out.println(ticket + "ticket");
		if (codeMaker.equals("not")) {
			System.out.println("�ڵ带 �߱��ؾ��մϴ�!");
			numberTicketService.firstCode(ticket);
			numberTicketService.makeTicket(ticket);
		} else {
			System.out.println("�̹��ڵ尡 �ֽ��ϴ�!");
			numberTicketService.plusTicket(ticket);
		}

		return " ";
	}

	// ��ȣǥ ��ȯ(����ο��� -1)
	@RequestMapping(value = "/minus_ticket", method = RequestMethod.POST)
	public String minus_ticket(Model model, NumberTicketVO ticket) {
		numberTicketService.minusTicket(ticket);
		System.out.println("��������Դϴ�.");
		return "/mngrOnly/mCustomer";
	}

	// ��ȣǥ �߱����(����ѻ�� ���� -1)
	@RequestMapping(value = "/cancel_ticket", method = RequestMethod.POST)
	public String cancel_ticket(Model model, NumberTicketVO ticket) {

		numberTicketService.cancelStatus(ticket);
		numberTicketService.cancelTicket(ticket);

		return "/mngrOnly/mCustomer";
	}

	// ��ȣǥ ����(�������λ�� ���� -1)
	@RequestMapping(value = "/absence_ticket", method = RequestMethod.POST)
	public String absence_ticket(Model model, NumberTicketVO ticket) {

		numberTicketService.absenceStatus(ticket);
		numberTicketService.cancelTicket(ticket);
		return "/mngrOnly/mCustomer";
	}

//	@RequestMapping(value = "/getLicense", method = RequestMethod.POST)
//	public String getLicense(Model model, NumberTicketVO ticket) {
//		model.addAttribute("ticket", numberTicketService.waitList(ticket));
//		return "/mngrOnly/mCustomer";
//	}
}
