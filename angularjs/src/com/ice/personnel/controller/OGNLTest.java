package com.ice.personnel.controller;

import java.util.Map;

import ognl.Ognl;

import com.ice.personnel.bean.User;

public class OGNLTest {

	public static void main(String[] args) {
		User user = new User();
		user.setAddress("address");
		user.setEmail("12321@1212.com");
		user.setZip(1212);
		user.setId("asdsa2131");
		@SuppressWarnings("rawtypes")
		Map ognl = Ognl.createDefaultContext(user);
		
		System.out.println(ognl);

	}

}
