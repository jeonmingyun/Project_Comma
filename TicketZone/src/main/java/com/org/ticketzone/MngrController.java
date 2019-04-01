package com.org.ticketzone;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.ticketzone.domain.OwnerVO;
import com.org.ticketzone.service.OwnerService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class MngrController {
	private OwnerService ownerService;

	// ������ �α���ȭ��
	@RequestMapping(value = "/mngr")
	public String admin(Model model) {
		return "mngr";
	}

	// ������ �α���ó��
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	@ResponseBody
	public String loginCheck(Model model, HttpSession session, @RequestParam String id, @RequestParam String password) {
		ArrayList<OwnerVO> arr = new ArrayList<OwnerVO>();
		arr = ownerService.login(id);
		session.setAttribute("id", arr);
		System.out.println(arr);
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

//	// �α��� ����������(���� �̿ϼ�)
//	@RequestMapping(value = "/mStore", method = RequestMethod.GET)
//	public String mstore(Model model) {
//		
//		return "/mngrOnly/mStore";
//	}

	// ���̵� ã��������(��ɹ̿ϼ�)
	@RequestMapping(value = "/mngr_find", method = RequestMethod.GET)
	public String mngr_find(Model model) {
		return "mngr/mngr_find";
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
