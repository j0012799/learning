package com.java1234.service;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 此方法是用来测试理解junit的注解的 
 * @author xiog
 *
 */
public class StudentTest2 {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("类初始化前调用...");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("类初始化后调用...");
	}

	@Before
	public void setUp() throws Exception {
		System.out.println("在测试方法前调用...");
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("在测试方法后调用...");
	}

	@Test
	public void test() {
		System.out.println("测试方法");
	}

}
