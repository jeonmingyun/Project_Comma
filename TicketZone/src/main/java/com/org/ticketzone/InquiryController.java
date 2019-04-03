package com.org.ticketzone;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.ticketzone.domain.BoardVO;
import com.org.ticketzone.domain.ReplyVO;
import com.org.ticketzone.service.BoardService;
import com.org.ticketzone.service.IncludeService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class InquiryController {
	private BoardService boardService;
	private IncludeService includeService;

	// ������
	@RequestMapping(value = "/inquiry", method = RequestMethod.GET)
	public String inquiry(Model model) {
		model.addAttribute("suggestList", boardService.boardList());
		return "inquiry";
	}

	// ���� �۾��� ������ �̵�
	@RequestMapping(value = "/inquiryWrite", method = RequestMethod.GET)
	public String inquiryWrite(Model model) {
		return "inquiry/inquiryWrite";
	}

	// ���� �۾��� ó��
	@RequestMapping(value = "/insertInquiry", method = RequestMethod.POST)
	public String insertInquiry(Model model, BoardVO board) {
		
		boardService.boardInsert(board);
		return "redirect:/inquiry";
	}
		
	// ���Ǳ� �󼼺���
	@RequestMapping(value = "/showInquiry", method = RequestMethod.GET)
	public String showInquiry(Model model, HttpServletRequest request) {
		String board_no = request.getParameter("board_no");
		model.addAttribute("InquiryUpd", boardService.boardUpdInfo(board_no));
		model.addAttribute("replyList", includeService.replyList(board_no));

		return "inquiry/showInquiry";
	}

	// ���Ǳ� ���������� �̵�
	@RequestMapping(value = "/updInquiry", method = RequestMethod.GET)
	public String updInquiry(Model model, HttpServletRequest request) {
		String board_no = request.getParameter("board_no");
		model.addAttribute("InquiryUpd", boardService.boardUpdInfo(board_no));
		return "inquiry/updInquiry";
	}

	// ���ǻ��� ����ó��
	@RequestMapping(value = "/updInquiryForm", method = RequestMethod.POST)
	public String updateBoard(BoardVO board) {
		boardService.boardUpd(board);

		return "redirect:inquiry";
	}
	
	@ResponseBody
	@RequestMapping(value = "/addReply", method = RequestMethod.POST)
	public ArrayList<ReplyVO> searchCustomer(Model model, ReplyVO reply,HttpServletRequest request) {
		String board_no = request.getParameter("board_no");
		includeService.addReply(reply); // insert
		
		return includeService.replyList(board_no); //select
	}

	// ���ǻ��� ����ó��
	@RequestMapping(value = "/delInquiry", method = RequestMethod.GET)
	public String deleteBoard(BoardVO board) {
		boardService.boardDel(board);
			
		return "redirect:/inquiry";
	}	
}
