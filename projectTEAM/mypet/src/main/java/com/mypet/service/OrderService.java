package com.mypet.service;

import java.util.List;

import com.mypet.domain.OrderVO;

public interface OrderService {
			 
	//상품 번호, 수량가지고 OrderVO를 만들고 반환
	public List<OrderVO> createOrderByOneProduct(int product_no,int quantity) throws Exception;
	
	//cart_no 가지고 OrderVO를 만들고 반환
	public List<OrderVO> createOrderByMultiProduct(int[] cartNumberArr) throws Exception;	
	
}
