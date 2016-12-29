package com.java1234.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.java1234.model.Class;
import com.java1234.util.HibernateUtil;

public class TestTransient4 {

	public static void main(String[] args) {	//由于测试session.clean()所以写在main方法中测试，测试此方法前先执行TestTransient0，插入一条数据
		SessionFactory sessionFactory=HibernateUtil.getSessionFactory();
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		Class c1=(Class) session.load(Class.class, 1L);//此时c1是Persistent
		c1.setName("10计算机");
		session.clear();								//清空session
		session.getTransaction().commit();
		
			/*调用session.clear()方法，这个时候就会将session的缓存对象清空，那么session中就没有了c1这个对象，
			 *这个时候在提交事务的时候，发现已经session中已经没有该对象了，所以就不会进行任何操作，因此这里只会发送一条select语句 
			 * 
			 * Hibernate: select class0_.classId as classId1_0_0_, class0_.className as classNam2_0_0_ from t_class class0_ where class0_.classId=?
			 * 
			 */
		
		session.close();
	}

}
