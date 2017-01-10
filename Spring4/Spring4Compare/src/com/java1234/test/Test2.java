package com.java1234.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.java1234.service.JavaWork;

public class Test2 {

	public static void main(String[] args) {
		//第一步:获取应用程序上下文对象 
		ApplicationContext ac=new ClassPathXmlApplicationContext("beans.xml");
		//第二步：根据应用程序上下文对象的get Bean
		JavaWork javaWork=(JavaWork)ac.getBean("javaWork");
		javaWork.doTest();
	}
}
