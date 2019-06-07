package com.org.ticketzone.appMem;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.ticketzone.domain.MemberVO;
import com.org.ticketzone.service.MemberService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class AppMemFMService {
	MemberService memberService;
	
	/* save FCM token */
	@ResponseBody
	@RequestMapping(value = "/mem_set_fcm_token", method = RequestMethod.POST)
	public String setFCM(HttpServletRequest request, @RequestBody MemberVO vo) {
        
        String token = request.getParameter("Token");
		System.out.println(token);
	    return "jsonView";
	}
	
//	/* send FCM */
//	@ResponseBody
//	@RequestMapping(value = "/mem_send_fcm", method = RequestMethod.POST)
//	public String senFCM(HttpServletRequest request, @RequestBody MemberVO vo) throws Exception{
//        
//        List<MemberVO> tokenList = memberService.loadFCMInfoList(vo); 
//        
//        String token = tokenList.get(count).getDEVICE_ID();
//        
//        final String apiKey = "AAAAmzUb7vI:APA91bHJf9WOpgpXQv1GWakiIASiBYHvu-GRbAL67Wpx9UrMwhJTi3P3c7UoI0UGnxdTtLu8k4E8k8yZsdjOGO0uxLb8JDAjnCQ1c6EU_QbDb6vbVEtWp5j6_qx2MH0-5Voa3QrrRI0Q";
//        URL url = new URL("https://fcm.googleapis.com/fcm/send");
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-Type", "application/json");
//        conn.setRequestProperty("Authorization", "key=" + apiKey);
//        conn.setDoOutput(true);
//        conn.connect();
//        
////        String userId =(String) request.getSession().getAttribute("ssUserId");
//        String title = "";
//        String body = "";
//        String to = "/topics/ALL"; // default : all user
//        // �̷��� ������ ������ ALL�� �����س��� ��� ��������� �˸��� �����ش�.
//        String input = "{\"notification\" : {\"title\" : \""+title+"\", \"body\" : \""+body+"\"}, \"to\":\""+ to +"\"}";
//        
////        // �̰ɷ� ������ Ư�� ��ū�� �������ִ� ���ÿ��� �˸��� �����ش�  ���� ���߿� �Ѱ� ��� ��������
////        String input = "{\"notification\" : {\"title\" : \" ����� ����ֱ� \", \"body\" : \"����� ���� �ֱ�\"}, \"to\":\" ���Ⱑ ���� ��� ��ū  \"}";
//
//        OutputStreamWriter owr = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
//        owr.write(input.toString());
//        owr.flush();
//        owr.close();
//
//        int responseCode = conn.getResponseCode();
//        System.out.println("\nSending 'POST' request to URL : " + url);
//        System.out.println("Post parameters : " + input);
//        System.out.println("Response Code : " + responseCode);
//        
//        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        String inputLine;
//        StringBuffer response = new StringBuffer();
//
//        while ((inputLine = in.readLine()) != null) {
//            response.append(inputLine);
//        }
//        in.close();
//        // print result
//        System.out.println(response.toString());
//	            
//	    return "jsonView";
//	}
}
