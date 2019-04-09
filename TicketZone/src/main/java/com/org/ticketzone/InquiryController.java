package com.org.ticketzone;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.ticketzone.domain.BoardVO;
import com.org.ticketzone.domain.Criteria;
import com.org.ticketzone.domain.PageDTO;
import com.org.ticketzone.domain.ReplyVO;
import com.org.ticketzone.service.BoardService;
import com.org.ticketzone.service.IncludeService;
import com.org.ticketzone.service.InterceptorService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class InquiryController {
	private BoardService boardService;
	private IncludeService includeService;
	private InterceptorService interceptorService;

	// ������
	@RequestMapping(value = "/inquiry", method = RequestMethod.GET)
	public String inquiry(Model model, Criteria Cri) {
		int total = boardService.total(Cri);
		model.addAttribute("suggestList", boardService.boardList(Cri));
		model.addAttribute("list", boardService.getListWithPaging(Cri));
		model.addAttribute("pageMaker", new PageDTO(Cri, total));
		
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
	
	// ���� �۾��� ��� üũ
	@RequestMapping(value = "/board_pass_form", method = RequestMethod.GET)
	public String inquiry_pass_form() {
		return "interceptor/board_pass_form";
	}
		
	// ���� �۾��� ��� üũ
	@RequestMapping(value = "/board_pass_pro", method = RequestMethod.POST)
	public String inquiry_pass_pro(BoardVO board) {
//			interceptorService.board_pass(board);
		System.out.println(board);
		return "inquiry/inquiryWrite";
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

	// ���ǻ��� ����ó��
	@RequestMapping(value = "/delInquiry", method = RequestMethod.GET)
	public String deleteBoard(BoardVO board) {
		boardService.boardDel(board);
			
		return "redirect:/inquiry";
	}
	
	/*
	 * @ResponseBody
	 * 
	 * @RequestMapping(value = "/addReply", method = RequestMethod.POST) public
	 * ArrayList<ReplyVO> searchCustomer(Model model, ReplyVO
	 * reply,HttpServletRequest request) { String board_no =
	 * request.getParameter("board_no"); includeService.addReply(reply); // insert
	 * 
	 * return includeService.replyList(board_no); //select }
	 */
	

	// ��� �߰�
	@ResponseBody
	@RequestMapping(value = "/addReply", method = RequestMethod.POST)
	public ArrayList<ReplyVO> searchCustomer(Model model, ReplyVO reply,HttpServletRequest request) {
		String board_no = request.getParameter("board_no");
		includeService.addReply(reply); // insert
		
		return includeService.replyList(board_no); //select
	}
	
	// ��� ����
	@ResponseBody
	@RequestMapping(value = "/reply_delete", method = RequestMethod.POST)
	public String replyDelete(Model model, HttpServletRequest request) {
		String board_no = request.getParameter("board_no");
		includeService.reply_delete(board_no);
		
		return "";
	}
	
	// ��� ����
	@ResponseBody
	@RequestMapping(value = "/reply_update", method = RequestMethod.POST)
	public String replyUpdate(Model model, HttpServletRequest request) {
		String reply_content = request.getParameter("reply_content");
		String board_no = request.getParameter("board_no");
		includeService.reply_update(reply_content, board_no);

		return "";
	}

}
