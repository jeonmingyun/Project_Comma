package com.org.ticketzone.domain;

import lombok.Data;

@Data
public class NumberTicketVO {
	private String ticket_code; //��ȣǥ �ڵ�
	private int wait_number; //��� �ο� ��
	private int the_number; // ���� �ο� ��
	private String license_number;
	private String member_tel;
	private int ticket_status;
}
