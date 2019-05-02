package com.org.ticketzone;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//github.com/jeonmingyun/comma.git
import com.org.ticketzone.domain.Criteria;
import com.org.ticketzone.domain.NoticeAttachVO;
import com.org.ticketzone.domain.NoticeBoardVO;
import com.org.ticketzone.domain.NumberTicketVO;
import com.org.ticketzone.domain.PageDTO;
import com.org.ticketzone.service.NoticeAttachService;
import com.org.ticketzone.service.NoticeBoardService;
import com.org.ticketzone.service.NumberTicketService;

import lombok.AllArgsConstructor;

/**
 * Handles requests for the application home page.
 */
@AllArgsConstructor
@Controller
public class HomeController {

	private NoticeBoardService noticeBoardService;
	private NoticeAttachService noticeAttachService;
	private NumberTicketService numberTicketService;
	

	// �� Ȩ
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model, Criteria Cri) {
		// ��������
		int total = noticeBoardService.total(Cri);
		model.addAttribute("noticeList", noticeBoardService.noticeBoardList(Cri));
		model.addAttribute("list", noticeBoardService.getListWithPaging(Cri));
		model.addAttribute("pageMaker", new PageDTO(Cri, total));

		return "home";
	}

	// �������� �۾��� ������ �̵�
	@RequestMapping(value = "/noticeWrite", method = RequestMethod.GET)
	public String noticeWrite(Model model) {

		return "home/noticeWrite";
	}

	// �������� �۾��� ó��
	@RequestMapping(value = "/noticeInsert", method = RequestMethod.POST)
	public String noticeInsert(Model model, NoticeBoardVO notice, NoticeAttachVO attach, HttpServletRequest request,
			RedirectAttributes rttr) {
		String notice_status = request.getParameter("notice_status");
		System.out.println(notice);
		System.out.println(attach);
		System.out.println(notice_status);
		
		if (attach.getFileName() == null) {			
			noticeBoardService.insertSelectKey(notice);
		} else {			
			noticeBoardService.InsertStatus(notice);
			noticeAttachService.Fileinsert(attach);			
	}

		return "redirect:/";
	}

	// �������� �󼼺���
	@RequestMapping(value = "/showNotice", method = RequestMethod.GET)
	public String showNotice(Model model, HttpServletRequest request) {
		String notice_no = request.getParameter("notice_no");
		model.addAttribute("noticeUpd", noticeBoardService.noticeBoardUpdInfo(notice_no));
		model.addAttribute("file", noticeAttachService.findByNotice_no(notice_no));

		return "home/showNotice";
	}

	// �������� ���������� �̵�
	@RequestMapping(value = "/updNotice", method = RequestMethod.GET)
	public String updNotice(Model model, HttpServletRequest request) {
		String notice_no = request.getParameter("notice_no");
		model.addAttribute("noticeUpd", noticeBoardService.noticeBoardUpdInfo(notice_no));
		model.addAttribute("file", noticeAttachService.findByNotice_no(notice_no));
		return "home/updNotice";
	}

	// �������� ����ó��
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateNotice(NoticeBoardVO notice, NoticeAttachVO vo) {
		
		if(vo.getUuid() == null) {
			System.out.println("���Ͼ���");
			noticeBoardService.noticeBoardUpd(notice);
		} else {
			System.out.println("��������");
			noticeAttachService.FileModifyinsert(vo);
			noticeBoardService.noticeBoardUpd(notice);			
		}


		return "success";
	}

	// �������� ����ó��
	@RequestMapping(value = "/delNotice", method = RequestMethod.GET)
	public String deleteNotice(NoticeBoardVO notice) {
		noticeBoardService.noticeBoardDel(notice);

		return "redirect:/";
	}

	// �������� �˻�
//	@RequestMapping(value = "/searchKeyword", method = RequestMethod.GET)
//	public String searchNotice(Model model, NoticeBoardVO notice, Criteria Cri) {
//		int count = noticeBoardService.SearchCount(Cri);
//		model.addAttribute("noticeList", noticeBoardService.noticeSearch(notice));
//		model.addAttribute("list", noticeBoardService.getListWithPaging(Cri));
//		model.addAttribute("pageMaker", new PageDTO(Cri, count));
//		
//		
//		return "redirect:/";
//	}

	// �׽�Ʈ��

	@ResponseBody
	@RequestMapping(value = "/chart")
	public ArrayList<NumberTicketVO> test(HttpServletRequest request) {
		String today = request.getParameter("today");
		
		System.out.println(today);
		 
		return numberTicketService.getTotal();
	}
	
	@RequestMapping(value = "/chart2")
	public String test2(Model model) {
		model.addAttribute("today", numberTicketService.today());
		return "chart";
	}
	
	@ResponseBody
	@RequestMapping(value ="/chartDel", method= RequestMethod.POST)
	public ArrayList<NumberTicketVO> test3(Model model, String today, String day) {
		
		
		return numberTicketService.getTotalDel(today);
	}
	
	@ResponseBody
	@RequestMapping(value ="chartAdd", method=RequestMethod.POST)
	public ArrayList<NumberTicketVO> test4(Model model, String today, String day){
		
		System.out.println(numberTicketService.getTotalAdd(today));
		
		
		return numberTicketService.getTotalAdd(today);
	}
	
	//adminChart
	
	
}
