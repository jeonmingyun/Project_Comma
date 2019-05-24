package com.org.ticketzone.appMem;

import org.json.simple.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.ticketzone.domain.MemberVO;
import com.org.ticketzone.domain.NumberTicketVO;
import com.org.ticketzone.domain.OwnerVO;
import com.org.ticketzone.service.AppMemService;
import com.org.ticketzone.service.MemberService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class AppMemHomeController {
	MemberService memberService;
	AppMemService appMemService;
	
	@ResponseBody
	@RequestMapping(value = "/test", method = RequestMethod.POST)
	public String test(@RequestBody OwnerVO o ) {
		System.out.println(o.getOwner_id());

		return o.getOwner_id() +"app";
	}

	@ResponseBody
	@RequestMapping(value = "/mem_db_login", method = RequestMethod.POST)
	public JSONArray mem_db_login(@RequestBody MemberVO mvo ) {
		System.out.println(mvo.getMember_id());
		JSONArray arr = new JSONArray();
		
		arr.add(appMemService.ownerList());
		arr.add(appMemService.categorieList()); //F�� �����;��ϰ�
		arr.add(appMemService.coordinatesList());
		arr.add(appMemService.storeList());
		arr.add(appMemService.menuList());
		arr.add(appMemService.NumberTicketList());
		return arr;
	}
  //��ȣǥ �߱�
  @ResponseBody
  @RequestMapping(value = "/Mem_issue_ticket", method = RequestMethod.POST)
  public String id_check(@RequestBody NumberTicketVO vo) {
//	   appMngrService.M_issue_ticket(vo);
	   String codeMaker = appMemService.Mem_codeSelect(vo);
		System.out.println(codeMaker + "code");
		System.out.println(vo + "ticket");
		if (codeMaker.equals("not")) {
			System.out.println("�ڵ带 �߱��ؾ��մϴ�!");
			appMemService.Mem_firstCode(vo);
			appMemService.Mem_makeTicket(vo);
		} else {
			System.out.println("�̹��ڵ尡 �ֽ��ϴ�!");
			appMemService.Mem_plusTicket(vo);
		}
	   
     return "1";
  }
}
