package com.org.ticketzone.service;

import java.util.ArrayList;
import java.util.List;

import com.org.ticketzone.domain.Criteria;
import com.org.ticketzone.domain.NoticeBoardVO;


public interface NoticeBoardService {
	public ArrayList<NoticeBoardVO> noticeBoardList(Criteria cri); //�Խñ� ����Ʈ ��ȯ
	public ArrayList<NoticeBoardVO> noticeBoardUpdInfo(String notice_no); // ����â���� �̵�
	public void noticeBoardUpd(NoticeBoardVO notice); // �Խñ� ����
	public void noticeBoardDel(NoticeBoardVO notice); // �Խñ� ����
	public void noticeInsert(NoticeBoardVO notice); // �Խñ� �ۼ�
	public ArrayList<NoticeBoardVO> getListWithPaging(Criteria cri);
	public int total(Criteria cri);
}
