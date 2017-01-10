package com.java1234.test;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.java1234.entity.People;

public class T {

	private ApplicationContext ac;

	@Before
	public void setUp() throws Exception {
		ac=new ClassPathXmlApplicationContext("beans.xml");
	}

	// 基本类型值
	@Test
	public void test1() {
		People people=(People)ac.getBean("people1");
		System.out.println(people);
	}
	
	// 注入bean
	@Test
	public void test2() {
		People people=(People)ac.getBean("people2");
		System.out.println(people);
	}
		
	// 内部bean
	@Test
	public void test3() {
		People people=(People)ac.getBean("people3");
		System.out.println(people);
	}
	
	// 注入null,狗的属性注入为null值
	@Test
	public void test4() {
		People people=(People)ac.getBean("people4");
		System.out.println(people);
	}
	
	// 级联属性        注意：属性需要先初始化后才能为级联属性赋值
	@Test
	public void test5() {
		People people=(People)ac.getBean("people5");
		System.out.println(people);
	}
	
	// 注入集合 包括：List、Set、Map、Properties
	@Test
	public void test6() {
		People people=(People)ac.getBean("people6");
		System.out.println(people);
	}
}
