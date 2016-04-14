package com.proxy;

public class CountServiceImpl implements CountService {

	private int count = 0;
	
	@Override
	public int count() {
		System.out.println("call CountServiceImpl count()");
		return count++;
	}

}
