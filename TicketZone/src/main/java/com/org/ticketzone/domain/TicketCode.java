package com.org.ticketzone.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
//4��2�� �̿ϼ� �ڵ常����ִ�VO
public class TicketCode {
	private String ticketcode;
	
	public String CodeMake() {
				
		return ticketcode;
	}
	
}
