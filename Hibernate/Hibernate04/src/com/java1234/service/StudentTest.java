package com.java1234.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.java1234.model.Class;
import com.java1234.model.Student;
import com.java1234.util.HibernateUtil;
/**
 * 没用junit的测试 
 * @author xiog
 *
 */
public class StudentTest {
	
	public static void main(String[] args) {
		SessionFactory sessionFactory=HibernateUtil.getSessionFactory();
		Session session=sessionFactory.openSession(); // 生成一个session
	    session.beginTransaction(); // 开启事务
	    
	    Class c=new Class();
	    c.setName("08计本");
	    session.save(c);
	    
	    Student s1=new Student();
	    s1.setName("张三");
	    s1.setC(c);
	    
	    Student s2=new Student();
	    s2.setName("李四");
	    s2.setC(c);
	    
	    session.save(s1);
	    session.save(s2);
	   
	    session.getTransaction().commit(); // 提交事务
	    session.close(); // 关闭session
	}
}
