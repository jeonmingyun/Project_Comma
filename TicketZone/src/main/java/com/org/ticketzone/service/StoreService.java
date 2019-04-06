package com.org.ticketzone.service;

import java.util.ArrayList;

import com.org.ticketzone.domain.StoreVO;

public interface StoreService {
	public ArrayList<StoreVO> storeList();

	public ArrayList<StoreVO> storeInfo(String license);

	public void storeRegister(StoreVO store);

	public ArrayList<StoreVO> storeGet(String id);

	//	�������������
	public StoreVO storeUpdate(String license);

	// ���� ���� ó��(update)
	public void storeUpdCom(StoreVO store);
}
