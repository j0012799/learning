package com.java1234.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.java1234.entity.People;

public class TestAutoWire {

	private ApplicationContext ac;
	
	@Test
	public void autoWireByName() {
		ac=new ClassPathXmlApplicationContext("resources/autoWireByName.xml");
		People people=(People)ac.getBean("people1");
		System.out.println(people);
	}
	
	@Test
	public void autoWireByType() {
		ac=new ClassPathXmlApplicationContext("resources/autoWireByType.xml");
		People people=(People)ac.getBean("people1");
		System.out.println(people);
	}
	
	@Test
	public void autoWireByConstructor() {
		ac=new ClassPathXmlApplicationContext("resources/autoWireByConstructor.xml");
		People people=(People)ac.getBean("people1");
		System.out.println(people);
	}	
}
