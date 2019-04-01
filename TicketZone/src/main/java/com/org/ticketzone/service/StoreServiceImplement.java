package com.org.ticketzone.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.org.ticketzone.domain.StoreVO;
import com.org.ticketzone.mapper.StoreMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StoreServiceImplement implements StoreService{
	
	private StoreMapper mapper;
	
	@Override
	public ArrayList<StoreVO> storeList() {
		
		return mapper.storeList();
	}

	@Override
	public ArrayList<StoreVO> storeInfo(String license) {
		
		return mapper.storeInfo(license);
	}

}
