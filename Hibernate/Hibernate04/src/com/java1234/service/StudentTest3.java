package com.java1234.service;

import static org.junit.Assert.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.java1234.model.Class;
import com.java1234.model.Student;
import com.java1234.util.HibernateUtil;
/**
 * 用junit的注解减少冗余的代码 
 * 在测试方法之前生成session、开启事务
 * 在测试方法之后提交事务、关闭session
 * @author xiog
 *
 */
public class StudentTest3 {

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
	public void testSaveClassAndStudent() {
		Class c=new Class();
	    c.setName("08计本");
	    session.save(c);////若设 cascade="all或者save-update" ,则可无此行代码
	    
	    Student s1=new Student();
	    s1.setName("张三");
	    s1.setC(c);
	    
	    Student s2=new Student();
	    s2.setName("李四");
	    s2.setC(c);
	    
	    session.save(s1);
	    session.save(s2);
	}
	
	@Test
	public void testSaveClassAndStudentWithCascade() {
		Class c=new Class();
	    c.setName("10机电");
	    				//注意级联操作此处的对比(对比上面)
	    
	    Student s1=new Student();
	    s1.setName("王五");
	    s1.setC(c);
	    
	    Student s2=new Student();
	    s2.setName("赵六");
	    s2.setC(c);
	    
	    session.save(s1);
	    session.save(s2);
	}

}
