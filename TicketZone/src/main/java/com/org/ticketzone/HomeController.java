package com.org.ticketzone;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.org.ticketzone.domain.AttachFileDTO;
//github.com/jeonmingyun/comma.git
import com.org.ticketzone.domain.Criteria;
import com.org.ticketzone.domain.NoticeAttachVO;
import com.org.ticketzone.domain.NoticeBoardVO;
import com.org.ticketzone.domain.PageDTO;
import com.org.ticketzone.service.NoticeAttachService;
import com.org.ticketzone.service.NoticeBoardService;
import com.org.ticketzone.service.TestService;

import lombok.AllArgsConstructor;

/**
 * Handles requests for the application home page.
 */
@AllArgsConstructor
@Controller
public class HomeController {

	private NoticeBoardService noticeBoardService;
	private NoticeAttachService noticeAttachService;
	private TestService test;

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
	public String noticeInsert(Model model, NoticeBoardVO notice, NoticeAttachVO attach, RedirectAttributes rttr) {
		
			System.out.println(notice);
			System.out.println(attach);
			
		noticeBoardService.insertSelectKey(notice);
		noticeAttachService.Fileinsert(attach);

		return "redirect:/";
	}

	// �������� �󼼺���
	@RequestMapping(value = "/showNotice", method = RequestMethod.GET)
	public String showNotice(Model model, HttpServletRequest request) {
		String notice_no = request.getParameter("notice_no");
		model.addAttribute("noticeUpd", noticeBoardService.noticeBoardUpdInfo(notice_no));
		model.addAttribute("file",noticeAttachService.findByNotice_no(notice_no));
		
		return "home/showNotice";
	}

	// �������� ���������� �̵�
	@RequestMapping(value = "/updNotice", method = RequestMethod.GET)
	public String updNotice(Model model, HttpServletRequest request) {
		String notice_no = request.getParameter("notice_no");
		model.addAttribute("noticeUpd", noticeBoardService.noticeBoardUpdInfo(notice_no));

		return "home/updNotice";
	}

	// �������� ����ó��
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateNotice(NoticeBoardVO notice) {
		noticeBoardService.noticeBoardUpd(notice);

		return "redirect:/";
	}

	// �������� ����ó��
	@RequestMapping(value = "/delNotice", method = RequestMethod.GET)
	public String deleteNotice(NoticeBoardVO notice) {
		noticeBoardService.noticeBoardDel(notice);

		return "redirect:/";
	}
	
	//�������� �˻�
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
	
}
