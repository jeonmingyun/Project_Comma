package com.org.ticketzone.mapper;

import java.util.ArrayList;

import com.org.ticketzone.domain.InqAttachVO;

public interface InqAttachMapper {
	// ���� ���� �μ�Ʈ
	public void Inq_fileinsert(InqAttachVO inq);

	// ���� ���� �μ�Ʈ
	public void Inq_fileModifyinsert(InqAttachVO inq);

	// ���� ����
	public void Inq_delete(int board_no);

	// ���� ��ȸ
	public ArrayList<InqAttachVO> Inq_findByBoard_no(int board_no);

}
