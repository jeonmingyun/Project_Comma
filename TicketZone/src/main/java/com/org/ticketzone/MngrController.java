package com.org.ticketzone;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.ticketzone.domain.OwnerVO;
import com.org.ticketzone.domain.StoreVO;
import com.org.ticketzone.service.OwnerService;
import com.org.ticketzone.service.StoreService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class MngrController {
	private OwnerService ownerService;
	private StoreService storeService;
	// ������ �α���ȭ��
	@RequestMapping(value = "/mngr")
	public String admin(Model model) {
		return "mngr";
	}

	// ������ �α���ó��
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	@ResponseBody
	public String loginCheck(Model model, HttpSession session, HttpSession session2, @RequestParam String id, @RequestParam String password) {
		ArrayList<OwnerVO> arr = new ArrayList<OwnerVO>();
		ArrayList<StoreVO> arr2 = new ArrayList<StoreVO>();
		arr = ownerService.login(id);
		arr2 = storeService.storeGet(id);
		session.setAttribute("id", arr);
		session2.setAttribute("store", arr2);

		if (arr.size() != 0) {
			if (arr.get(0).getOwner_password().equals(password)) {
				return "mngrOnly";
			} else {
				return "fail";
			}
		} else {
			return "fail";			
		}
	}

	// ���̵� ã��������(��ɹ̿ϼ�)
	@RequestMapping(value = "/mngr_find", method = RequestMethod.GET)
	public String mngr_find(Model model) {
				
		return "mngr/mngr_find";
	}
	
	
	//���̵� ã��
	@ResponseBody
	@RequestMapping(value = "/mngr_findTel_pro", method = RequestMethod.POST)
	public String mngr_findTel_pro(Model model, OwnerVO owner, HttpServletRequest request, HttpSession session){
		String owner_name = request.getParameter("owner_name");
		String owner_tel = request.getParameter("owner_tel");
		ArrayList<OwnerVO> arr = new ArrayList<OwnerVO>();
		
		arr = ownerService.findOwner(owner_name, owner_tel);
		session.setAttribute("tel", arr);
		if (arr.size() != 0) {
			if (arr.get(0).getOwner_tel().equals(owner_tel)) {
				return "mngr_findTel";
			} else {
				return "fail";
			}
		} else {
			return "fail";
		}
		
	}
	
	// ���̵� ã��������(��ɹ̿ϼ�)
	@RequestMapping(value = "/mngr_findTel", method = RequestMethod.GET)
	public String mngr_findTel() {			
		return "mngr/mngr_findTel";
	}
	
	// ��й�ȣ ã�� ������ �̵�
	@RequestMapping(value = "/mngr_passwd", method = RequestMethod.GET)
	public String mngr_passwd() {
		return "mngr/mngr_passwd";
	}
	
	// ������ ȸ������������ �̵�
	@RequestMapping(value = "/mngr_register")
	public String mngr_register(Model model) {
		return "mngr/mngr_register";
	}

	// ������ ȸ������ó��
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(Model model, OwnerVO owner) {
		ownerService.insertOwner(owner);

		return "mngr";
	}
	
}