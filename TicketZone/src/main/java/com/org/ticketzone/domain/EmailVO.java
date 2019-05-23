package com.org.ticketzone.domain;

import lombok.Data;

@Data
public class EmailVO {
	private String senderName; //�߽��� �̸�
	private String senderMail; // �߽��� �̸����ּ�
	private String receiveMail; //������ �̸����ּ�
	private String subject; //����
	private String message; //����

@Override
public String toString(){
	return "EmailVo [senderName=" + senderName + ", senderMail=" + ", subject=" + subject + ", message=" + message + "]";
	}
}