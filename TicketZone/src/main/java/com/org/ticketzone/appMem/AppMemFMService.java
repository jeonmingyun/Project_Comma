package com.org.ticketzone.appMem;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.ticketzone.service.AppMemService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class AppMemFMService {
	@Autowired AppMemService memberService;
	
	/* save FCM token */
	@ResponseBody
	@RequestMapping(value = "/mem_set_fcm_token", method = RequestMethod.POST)
	public String setFCM(HttpServletRequest request) {
        String token = request.getParameter("Token");
        // ��ġ �� ���� ����ø� ������ ���� token��ȯ
        // ���� ����� �ܿ��� null��ȯ
		System.out.println(token);
	    return "jsonView";
	}

	@ResponseBody
	@RequestMapping(value = "/mem_send_fcm", method = RequestMethod.POST)
	// ���� ���� ����
	public String pushFCMNotification() throws Exception {
		String API_KEY = "AAAAmzUb7vI:APA91bGojWKNi-FXv5coHWtMOPz0-Em5c5LomZGDWa54xPy81aoQ7e-1ArcCWh8xQdFcohSRUeXFtSbIpAgAda7LJYCnjodFk5q-jGCsrOsIIEjejNUzUYvkcXjb734RK4GxhdhADeEF";
		String token = "ft1mhicUqZU:APA91bFxz4KECTQYJ-XL5sRdKgcKouajgAqhc6zzt6HKEhiDcd3e8oy5rm8UO_TmoqHLVO4pFX25MerUaKScBMZ77rAZkh0PEzIaDHUIB6uTab05NmYrauThgUBxKvdmTMaQyla30BSh";

		// if(pushKeyAuth()){
		URL url = new URL("https://fcm.googleapis.com/fcm/send");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization", "key=" + API_KEY);
		conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		// �Ϲ� �ؽ�Ʈ ���޽� Content-Type , application/x-www-form-urlencoded;charset=UTF-8

		// �˸� + ������ �޼��� ������ ����		
		JSONObject infoJson = new JSONObject();
		JSONObject json = new JSONObject();
		infoJson.put("title", "alert title");
		infoJson.put("body", "alert body");
		json.put("to", token.trim());
		json.put("notification", infoJson);

        // �������� ������ �ѱ� ������ ����� �Ʒ�ó��  UTF-8�� ���ڵ��ؼ� ��������
		OutputStreamWriter owr = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
		owr.write(json.toString());
		owr.flush();
		owr.close();
        
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			System.out.println("Output from Server : " + conn.getResponseCode());
		} else {
			// 400, 401, 500 ��
			System.out.println("Failed : HTTP error code : " + conn.getResponseCode());			
		}
        conn.disconnect();
        
        return json.toString();
	}
	
//	/* send FCM */
//	@ResponseBody
//	@RequestMapping(value = "/mem_send_fcm2", method = RequestMethod.POST)
//	public void fcmTest() throws Exception {
//        try {    
//        	System.out.println("18");
//        	
//        	String token = "fiSOH8FIm4g:APA91bEWhjN29G2ZmfFfh-5-zM1ZeBsbX86hW7onF6kf1Qftwn8LXAvmbupXg120Jra1ttjQt-Lg4Sr2-L_ta1fAif3_QAH186_6sfBqLcPstgbZimHtF6-BQLmBPeHrUyJkJyKjqc1X";
//            String path = "C:\\Users\\bon300-12\\Desktop\\comma\\TicketZone\\src\\main\\webapp\\resources\\json\\smartwait-3cb6a-firebase-adminsdk-qql5m-4e06f12d08.json";
////            String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
//            String MESSAGING_SCOPE = "smartwait-3cb6a";
//            String[] SCOPES = { MESSAGING_SCOPE };
//            
//            GoogleCredential googleCredential = GoogleCredential
//                                .fromStream(new FileInputStream(path))
//                                .createScoped(Arrays.asList(SCOPES));
//            googleCredential.refreshToken();
//                                
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("content-type" , MediaType.APPLICATION_JSON_VALUE);
//            headers.add("Authorization", "Bearer " + googleCredential.getAccessToken());
//            
//            JSONObject notification, message, jsonParams;
//            notification = new JSONObject();
//            notification.put("body", "alert body");
//            notification.put("title", "alert title");
//            
//            message = new JSONObject();
//            message.put("token", token);
//            message.put("notification", notification);
//            
//            jsonParams = new JSONObject();
//            jsonParams.put("message", message);
//            
//            HttpEntity<JSONObject> httpEntity = new HttpEntity<JSONObject>(jsonParams, headers);
//            RestTemplate rt = new RestTemplate();            
//            
//            ResponseEntity<String> res = rt.exchange("https://fcm.googleapis.com/v1/projects/TicketZone/messages:send"
//                    , HttpMethod.POST
//                    , httpEntity
//                    , String.class);
//        
////            if (res.getStatusCode() != HttpStatus.OK) {
////                log.debug("FCM-Exception");
////                log.debug(res.getStatusCode().toString());
////                log.debug(res.getHeaders().toString());
////                log.debug(res.getBody().toString());
////                
////            } else {
////                log.debug(res.getStatusCode().toString());
////                log.debug(res.getHeaders().toString());
////                log.debug(res.getBody().toLowerCase());
////                
////            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
	
}
