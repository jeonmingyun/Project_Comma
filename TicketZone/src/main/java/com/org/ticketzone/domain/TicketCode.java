package com.org.ticketzone.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
//TicketCode�� ������ִ� �ڵ�
public class TicketCode {
	private String Makecode;
	private int code;
	
	public int makeCode(String Makecode) {
		code = Integer.parseInt(Makecode);
		 return code;
	}
	
}
