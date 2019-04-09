package com.org.ticketzone;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.org.ticketzone.domain.BoardVO;
import com.org.ticketzone.domain.Criteria;
import com.org.ticketzone.domain.PageDTO;
import com.org.ticketzone.service.BoardService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class MInquiryController {
	private BoardService boardService;
	
	// ������ ���� ����
	@RequestMapping(value = "/mInquiry", method = RequestMethod.GET)
	public String mInquiry(Model model, Criteria Cri) {
		int total = boardService.total(Cri);
		model.addAttribute("suggestList", boardService.boardList(Cri));
		model.addAttribute("list", boardService.getListWithPaging(Cri));
		model.addAttribute("pageMaker", new PageDTO(Cri, total));
		
		return "mngrOnly/mInquiry";
	}
	
	// ���ǻ��� �۾��� ������ �̵�
		@RequestMapping(value = "/mInquiryWrite", method = RequestMethod.GET)
		public String mInquiryWrite(Model model) {
			return "mngrOnly/mInquiry/mInquiryWrite";
		}
		
		// ���� �۾��� ó��
		@RequestMapping(value = "/mInsertInquiry", method = RequestMethod.POST)
		public String mInsertInquiry(Model model, BoardVO board) {
			
			boardService.boardInsert(board);
			return "redirect:/mInquiry";
		}	
	
		// ������ ���ǻ��� �󼼺���
		@RequestMapping(value = "/mShowInquiry", method = RequestMethod.GET)
		public String mShowInquiry(Model model, HttpServletRequest request) {
			String board_no = request.getParameter("board_no");
			model.addAttribute("InquiryUpd", boardService.boardUpdInfo(board_no));
			return "mngrOnly/mInquiry/mShowInquiry";
		}	
		
		// ���Ǳ� ���������� �̵�
		@RequestMapping(value = "/mUpdInquiry", method = RequestMethod.GET)
		public String mUpdInquiry(Model model, HttpServletRequest request) {
			String board_no = request.getParameter("board_no");
			model.addAttribute("InquiryUpd", boardService.boardUpdInfo(board_no));
			return "mngrOnly/mInquiry/mUpdInquiry";
		}
		
		// ���ǻ��� ����ó��
		@RequestMapping(value = "/mUpdInquiryForm", method = RequestMethod.POST)
		public String mUpdateBoard(BoardVO board) {
			boardService.boardUpd(board);

			return "redirect:/mInquiry";
		}
			
		// ���ǻ��� ����ó��
	  	
		@RequestMapping(value = "/mDelInquiry", method = RequestMethod.GET) 
		public String mDeleteInquiry(BoardVO board) { 
			boardService.boardDel(board);

			return "redirect:/mInquiry"; 
	 
	  }
}
