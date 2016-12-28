package com.java1234.service;

import java.util.Iterator;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.java1234.model.Class;
import com.java1234.model.Student;
import com.java1234.util.HibernateUtil;

public class StudentTest {

	private SessionFactory sessionFactory=HibernateUtil.getSessionFactory();
	private Session session;
	
	@Before
	public void setUp() throws Exception {
		session=sessionFactory.openSession(); // 生成一个session
	    session.beginTransaction(); // 开启事务
	}

	@After
	public void tearDown() throws Exception {
		 session.getTransaction().commit(); // 提交事务
		 session.close(); // 关闭session
	}

	@Test
	public void testSaveClassAndStudent() {//保存“一”的一方,根据cascade和inverse配置的不同，观察打印的日志进行分析
		Class c=new Class();
	    c.setName("08计本");
	    
	    Student s1=new Student();
	    s1.setName("张三");
	    
	    Student s2=new Student();
	    s2.setName("李四");
	    
	    c.getStudents().add(s1);
	    c.getStudents().add(s2);
	    
	    session.save(c);
	    
	}
}
