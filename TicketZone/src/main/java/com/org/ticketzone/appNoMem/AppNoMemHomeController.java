package com.org.ticketzone.appNoMem;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.ticketzone.domain.NumberTicketVO;
import com.org.ticketzone.domain.OwnerVO;
import com.org.ticketzone.service.AppMemService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class AppNoMemHomeController {
   
	AppMemService appMemService;
	
   @ResponseBody
   @RequestMapping(value = "/nomemTicket", method = RequestMethod.POST)
   public String nomemTicket(@RequestBody NumberTicketVO vo) {
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