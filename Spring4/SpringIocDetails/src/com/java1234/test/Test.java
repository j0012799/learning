package com.java1234.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.java1234.entity.People;


public class Test {

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext ac=new ClassPathXmlApplicationContext("beans.xml");
		
		//注入bean
		People people=(People)ac.getBean("people");
		System.out.println(people);
		
		// 属性注入
		People people2=(People)ac.getBean("people2");
		System.out.println(people2);
		
		//构造方法注入(通过类型)
		People people3=(People)ac.getBean("people3");
		System.out.println(people3);
		
		//构造方法注入(通过索引)
		People people4=(People)ac.getBean("people4");
		System.out.println(people4);
		
		//构造方法注入(类型、索引混合)
		People people5=(People)ac.getBean("people5");
		System.out.println(people5);
		
		//工厂方法注入(非静态方法)
		People people7=(People)ac.getBean("people7");
		System.out.println(people7);
		
		//工厂方法注入(静态方法)
		People people8=(People)ac.getBean("people8");
		System.out.println(people8);
	}
}
