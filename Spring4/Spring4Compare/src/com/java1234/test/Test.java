package com.java1234.test;

import com.java1234.service.JavaWork;
import com.java1234.service.Lisi;

public class Test {

	/**
	 * 主管执行命令
	 * @param args
	 */
	public static void main(String[] args) {
		//传统的方式
		JavaWork javaWork=new JavaWork();
		// javaWork.setTester(new ZhangSan());
		javaWork.setTester(new Lisi());
		javaWork.doTest();
	}
}
