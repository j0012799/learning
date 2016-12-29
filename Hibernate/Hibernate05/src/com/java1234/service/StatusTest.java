package com.java1234.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.java1234.model.Class;
import com.java1234.util.HibernateUtil;

public class StatusTest {	

		SessionFactory sessionFactory=HibernateUtil.getSessionFactory();
		Session session;
		
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
		public void TestTransient0(){
			Class c1=new Class(); 
		    c1.setName("10机电");	 //以上c1就是一个Transient(瞬时状态),此时c1并没有被session进行托管，即在session的 缓存中还不存在c1这个对象
		    session.save(c1); //当执行完save方法后，此时c1被session托管，并且数据库中存在了该对象c1就变成了一个Persistent(持久化对象)
		    
						    /*此时我们知道hibernate会发出一条insert的语句，执行完save方法后，该c1对象就变成了持久化的对象了
						     * 
						     *	Hibernate: insert into t_class (className) values (?)
						     * 
						     * */
		}
	    
		@Test
		public void TestTransient1(){
			Class c1=new Class(); 
		    c1.setName("10机电");	 //以上c1就是一个Transient(瞬时状态),此时c1并没有被session进行托管，即在session的 缓存中还不存在c1这个对象
		    session.save(c1); //当执行完save方法后，此时c1被session托管，并且数据库中存在了该对象c1就变成了一个Persistent(持久化对象)
		    
		    c1.setName("10数控"); //这时user又重新修改了属性值，那么在提交事务时，此时hibernate对象就会拿当前这个c1r对象和保存在session缓存中的c1对象进行比较，
		    					//如果两个对象相同，则不会发送update语句，否则，如果两个对象不同，则会发出update语句
		    
						    /*此时会发出2条sql，一条用户做插入，一条用来做更新
						     * 
						     *	Hibernate: insert into t_class (className) values (?)
						     *  Hibernate: update t_class set className=? where classId=?
						     * 
						     * */
		}
		
		@Test
		public void TestTransient2(){
			Class c1=new Class(); 
		    c1.setName("10机电");	
		    session.save(c1);
		    
		    c1.setName("10数控");
		    session.save(c1); //该条语句没有意义					
		    
						    /*这个时候会发出多少sql语句呢？还是同样的道理，在调用save方法后，c1此时已经是持久化对象了，记住一点：如果一个对象已经是持久化状态了，       
						     *那么此时对该对象进行各种修改，或者调用多次update、save方法时，hibernate都不会发送sql语句，只有当事物提交的时候， 
						     *此时hibernate才会拿当前这个对象与之前保存在session中的持久化对象进行比较，如果不相同就发送一条update的sql语句，否则就不会发送update语句 
						     * 
						     *	Hibernate: insert into t_class (className) values (?)
						     *  Hibernate: update t_class set className=? where classId=?
						     * 
						     * */
		}		

		@Test
		public void TestTransient3(){ //测试此方法前先执行TestTransient0，插入一条数据
			Class c1=(Class) session.load(Class.class, 1L); //此时c1是Persistent
		    c1.setName("10计算机");	 //由于c1这个对象和session中的对象不一致，所以会发出sql完成更新
		    
						    /*当session调用load、get方法时，此时如果数据库中有该对象，则该对象也变成了一个持久化对象，被session所托管。
						     *因此，这个时候如果对对象进行操作，在提交事务时同样会去与session中的持久化对象进行比较，因此这里会发送两条sql语句
						     * 
						     *	Hibernate: select class0_.classId as classId1_0_0_, class0_.className as classNam2_0_0_ from t_class class0_ where class0_.classId=?
						     * 	Hibernate: update t_class set className=? where classId=?
						     * 
						     * */
		}
		
		//TestTransient4在另一个main方法中
		
		@Test
		public void TestDetached1(){ //此处认为id为1的数据已保存在数据库中
			Class c1=new Class();
			c1.setId(1L);			//此时c1是一个离线对象，没有被session托管
			c1.setName("10护理");
			session.save(c1);		//当执行save的时候总是会添加一条数据，此时id就会根据Hibernate所定义的规则来生成
			
		    /*当调用了u.setId(1L)时，此时c1是一个离线的对象，因为数据库中存在id=1的这个对象，但是该对象又没有被session所托管
		     *所以这个对象就是离线的对象，要使离线对象变成一个持久化的对象，应该调用什么方法呢？我们知道调用save方法可以将一个对象变成一个持久化对象，
		     *当save一执行的时候，此时hibernate会根据id的生成策略往数据库中再插入一条数据
		     * 
		     * 	Hibernate: insert into t_class (className) values (?)
		     * 
		     * */			
		}
	
		@Test
		public void TestDetached2(){ //此处认为id为1的数据已保存在数据库中
			Class c1=new Class();
			c1.setId(1L);			//此时c1是一个离线对象，没有被session托管
			session.update(c1);	//完成update之后也会变成持久化状态
			c1.setName("10护理");
			session.update(c1);	
			
		    /*当调用了第一个update方法以后，此时c1已经变成了一个持久化的对象，那么如果此时对c1对象进行修改操作后，
		     *在事务提交的时候，则会拿该对象和session中刚保存的持久化对象进行比较，如果不同就发一条sql语句
		     * 
		     * 	Hibernate: update t_class set className=? where classId=?
		     * 
		     * */			
		}
		
		@Test
		public void TestDetached3(){ //此处认为id为1的数据已保存在数据库中
			Class c1=new Class();
			c1.setId(1L);			//此时c1是一个离线对象，没有被session托管
			session.update(c1);	//完成update之后也会变成持久化状态
			c1.setName("10护理");
			c1.setId(111L);
			
		    /*当调用了第一个update方法以后，此时c1已经变成了一个持久化的对象，那么如果此时对c1对象进行修改操作后，
		     *在对c1进行一些修改后，此时又通过 c1.setId(111)方法设置了c1的ID，那么这个时候，hibernate会报错，因为我们的c1当前已经是一个持久化对象,
		     *如果试图修改一个持久化对象的ID的值的话，就会抛出异常，这点要特别注意
		     * 
		     * 	org.hibernate.HibernateException: identifier of an instance of com.java1234.model.Class was altered from 1 to 111
		     * 
		     * */			
		}	
		
		@Test
		public void TestDetached4(){ //此处认为id为1的数据已保存在数据库中
			Class c1=new Class();
			c1.setId(1L);			
			session.delete(c1);	//此时c1已经是瞬时对象，不会被session和数据库所管理
			c1.setName("10英语");
			
		    /*这里在调用了session.delete()方法以后，此时后c1就会变成一个瞬时对象，因为此时数据库中已经不存在该对象了,
		     *既然c1已经是一个瞬时对象了，那么对c1再进行各种修改操作的话，hibernate也不会发送任何的修改语句，因此这里只会 有一条 delete的语句发生
		     * 
		     * 	Hibernate: delete from t_class where classId=?
		     * 
		     * */			
		}		
		
}
