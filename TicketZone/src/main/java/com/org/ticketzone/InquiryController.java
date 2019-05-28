package com.org.ticketzone;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.ticketzone.domain.BoardVO;
import com.org.ticketzone.domain.Criteria;
import com.org.ticketzone.domain.InqAttachVO;
import com.org.ticketzone.domain.PageDTO;
import com.org.ticketzone.domain.ReplyVO;
import com.org.ticketzone.service.BoardService;
import com.org.ticketzone.service.CategorieService;
import com.org.ticketzone.service.IncludeService;
import com.org.ticketzone.service.InqAttachService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class InquiryController {
	private BoardService boardService;
	private IncludeService includeService;
	private CategorieService categorieService;
	private InqAttachService inqAttachService;

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
		model.addAttribute("cate", categorieService.boardCateList());
		
		return "inquiry/inquiryWrite";
	}

	// ���� �۾��� ó��
	@RequestMapping(value = "/insertInquiry", method = RequestMethod.POST)
	public String insertInquiry(Model model, BoardVO board, InqAttachVO inq, HttpServletRequest request) {
		String content = board.getBoard_content();
		content = content.replace("\n", "<br />");
	
		board.setBoard_content(content);
		
		if(inq.getInq_filename() == null) {	
			boardService.boardInsert(board);
		} else {
			boardService.boardInsert(board);
			inqAttachService.Inq_fileinsert(inq);
		}
		return "redirect:/inquiry";
	}
	
	// ���� �۾��� ��� üũ
	@RequestMapping(value = "/board_pass_form", method = RequestMethod.GET)
	public String inquiry_pass_form() {
		return "interceptor/board_pass_form";
	}
		
	// ���� �۾��� ��� üũ
	@RequestMapping(value = "/board_pass_pro", method = RequestMethod.POST)
	public String inquiry_pass_pro(Model model, BoardVO board) {
		BoardVO board_pass_ck = boardService.board_pass(board);
		String board_no = board.getBoard_no() + "";
		model.addAttribute("InquiryUpd", boardService.boardUpdInfo(board_no));
		model.addAttribute("replyList", includeService.replyList(board_no));
		model.addAttribute("board_no", board_no);
		
		if(board_pass_ck != null) { // pass ������
			return "inquiry/showInquiry";
		} else { // pass ���н�
			return "redirect:board_pass_form";
		}
		
	}
	
	// ���Ǳ� �󼼺���
	@RequestMapping(value = "/showInquiry", method = RequestMethod.GET)
	public String showInquiry(Model model, HttpServletRequest request) {
		String board_no = request.getParameter("board_no");
		model.addAttribute("InquiryUpd", boardService.boardUpdInfo(board_no));
		model.addAttribute("replyList", includeService.replyList(board_no));
		model.addAttribute("file", inqAttachService.Inq_findByBoard_no(Integer.parseInt(board_no)));
		return "inquiry/showInquiry";
	}

	// ���Ǳ� ���������� �̵�
	@RequestMapping(value = "/updInquiry", method = RequestMethod.GET)
	public String updInquiry(Model model, HttpServletRequest request) {
		String board_no = request.getParameter("board_no");
		model.addAttribute("InquiryUpd", boardService.boardUpdInfo(board_no));
		model.addAttribute("file", inqAttachService.Inq_findByBoard_no(Integer.parseInt(board_no)));
		return "inquiry/updInquiry";
	}

	// ���ǻ��� ����ó��
	@ResponseBody
	@RequestMapping(value = "/updInquiryForm", method = RequestMethod.POST)
	public String updateBoard(BoardVO board, InqAttachVO inq) {
		String content = board.getBoard_content();
		content = content.replace("\n", "<br />");
		
		board.setBoard_content(content);
		
		if(inq.getInq_uuid() == null) {
			System.out.println("���Ͼ���");
			boardService.boardUpd(board);
		} else {
			inqAttachService.Inq_fileModifyinsert(inq);
			boardService.boardUpd(board);
		}
		
		return "success";
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
		
		String addReply_content = reply.getReply_content();
		addReply_content = addReply_content.replace("\n","<br/>");
		reply.setReply_content(addReply_content);
		
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
	public ArrayList<ReplyVO> replyUpdate(Model model, HttpServletRequest request) {
		String reply_content = request.getParameter("reply_content");
		String board_no = request.getParameter("board_no");
		
		reply_content = reply_content.replace("\n","<br/>");
		includeService.reply_update(reply_content, board_no);

		return includeService.replyList(board_no);
	}

}
