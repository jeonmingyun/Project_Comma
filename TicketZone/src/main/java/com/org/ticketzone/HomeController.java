package com.org.ticketzone;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.JsonNode;
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
	

	

	// 고객 홈
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model, Criteria Cri) {
		// 공지사항
		int total = noticeBoardService.total(Cri);
		model.addAttribute("noticeList", noticeBoardService.noticeBoardList(Cri));
		model.addAttribute("list", noticeBoardService.getListWithPaging(Cri));
		model.addAttribute("pageMaker", new PageDTO(Cri, total));

		return "home";
	}

	// 공지사항 글쓰기 페이지 이동
	@RequestMapping(value = "/noticeWrite", method = RequestMethod.GET)
	public String noticeWrite(Model model) {

		return "home/noticeWrite";
	}

	// 공지사항 글쓰기 처리
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

	// 공지사항 상세보기
	@RequestMapping(value = "/showNotice", method = RequestMethod.GET)
	public String showNotice(Model model, HttpServletRequest request) {
		String notice_no = request.getParameter("notice_no");
		model.addAttribute("noticeUpd", noticeBoardService.noticeBoardUpdInfo(notice_no));
		model.addAttribute("file", noticeAttachService.findByNotice_no(notice_no));

		return "home/showNotice";
	}

	// 공지사항 수정페이지 이동
	@RequestMapping(value = "/updNotice", method = RequestMethod.GET)
	public String updNotice(Model model, HttpServletRequest request) {
		String notice_no = request.getParameter("notice_no");
		model.addAttribute("noticeUpd", noticeBoardService.noticeBoardUpdInfo(notice_no));
		model.addAttribute("file", noticeAttachService.findByNotice_no(notice_no));
		return "home/updNotice";
	}

	// 공지사항 수정처리
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateNotice(NoticeBoardVO notice, NoticeAttachVO vo) {
		
		if(vo.getUuid() == null) {
			System.out.println("파일없음");
			noticeBoardService.noticeBoardUpd(notice);
		} else {
			System.out.println("파일있음");
			noticeAttachService.FileModifyinsert(vo);
			noticeBoardService.noticeBoardUpd(notice);			
		}


		return "success";
	}

	// 공지사항 삭제처리
	@RequestMapping(value = "/delNotice", method = RequestMethod.GET)
	public String deleteNotice(NoticeBoardVO notice) {
		noticeBoardService.noticeBoardDel(notice);

		return "redirect:/";
	}

	// 공지사항 검색
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

	// 테스트용

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
	@RequestMapping(value ="/chartAdd", method=RequestMethod.POST)
	public ArrayList<NumberTicketVO> test4(Model model, String today, String day){
		
		System.out.println(numberTicketService.getTotalAdd(today));
		
		
		return numberTicketService.getTotalAdd(today);
	}
	
	
	@RequestMapping(value = "/kakao", produces = "application/json", method=RequestMethod.GET)
//	public String kakao(@RequestParam("code") String code, RedirectAttributes ra, HttpSession session, Model model)
		public String kakao(Model model){
//		System.out.println("kakao:" + code);
		return "test";
	}
	
	@RequestMapping(value ="/kakaoLogin", produces = "application/json", method=RequestMethod.GET)
	public String kakaoLogin(@RequestParam("code") String code, RedirectAttributes ra, HttpSession session, HttpServletResponse response) throws IOException {
		System.out.println("kakao code : " + code);
		JsonNode jsonToken = KakaoAccessToken.getKakaoAccessToken(code);
        // 여러 json객체 중 access_token을 가져온다
        JsonNode accessToken = jsonToken.get("access_token");
 
        System.out.println("access_token : " + accessToken);
        
        JsonNode userInfo = KakaoUserInfo.getKakaoUserInfo(accessToken);
        
        // Get id
        String id = userInfo.path("id").asText();
        String name = null;
        String email = null;
 
        // 유저정보 카카오에서 가져오기 Get properties
        JsonNode properties = userInfo.path("properties");
        JsonNode kakao_account = userInfo.path("kakao_account");
 
        name = properties.path("nickname").asText();
        email = kakao_account.path("email").asText();
 
        System.out.println("id : " + id);
        System.out.println("name : " + name);
        System.out.println("email : " + email);


		return "test2";				
	}
	
}
