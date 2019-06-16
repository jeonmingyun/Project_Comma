package com.org.ticketzone.mapper;

import java.util.ArrayList;

import com.org.ticketzone.domain.StoreCriteria;
import com.org.ticketzone.domain.StoreMenuVO;
import com.org.ticketzone.domain.StoreVO;

public interface StoreMapper {
	public ArrayList<StoreVO> storeList(StoreCriteria cri);
	
	public ArrayList<StoreVO> storeInfo(String license);
	
	public void storeRegister(StoreVO store);
	
	public ArrayList<StoreVO> storeGet(String id);
	
	// �������������
	public StoreVO storeUpdate(String license); //���̵� �ϳ��� �޾ƿ� �� ���
	
	// ���� ���� ó��(update)
	public void storeUpdCom(StoreVO store);
	
	public int SearchCount(StoreCriteria cri);

	public ArrayList<StoreVO> getListWithPaging(StoreCriteria cri);
	
	public int total(StoreCriteria cri);
	
	public int searchTotal(StoreVO store);
	
	public ArrayList<StoreVO> getListWithSearchPaging(StoreCriteria cri);
	
	public ArrayList<StoreVO> getStore(String license_number);
	
	public ArrayList<StoreMenuVO> store_menu(String license_number);
	public ArrayList<StoreMenuVO> store_cate(String license_number);
	public ArrayList<StoreMenuVO> click_cate(StoreMenuVO menu);
}
