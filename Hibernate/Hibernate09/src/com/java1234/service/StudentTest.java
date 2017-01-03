package com.java1234.service;


import java.util.Iterator;
import java.util.List;

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

/*==========================类级别的检索========只在class.hbm.xml中配置，其他地方的lazy等属性为了不影响测试效果都不配置=========================*/
	/**
	 * 测试类级别的检索	class.hbm.xml中class节点中设置lazy="true" 延迟检索
	 * 
	 * 先在数据库中手动插入id为1的数据
	 * 当执行1时，Hibernate并没有发送SQL语句，而只是返回了一个对象
	 * 当执行2时，可以看到打印出来的对象，是一个"代理对象"===========>class com.java1234.model.Class_$$_jvste04_1
	 * 当执行3时，获取ID的时候，也没有发送SQL语句 ===========>1
	 * 当执行4时，来得到name的时候(使用了id以外的其它属性时)，这个时候才发送了SQL语句进行真正的查询，并且WHERE条件中带上的就是ID
	 * 		===========>Hibernate: select class0_.classId as classId1_0_0_, class0_.className as classNam2_0_0_ from t_class class0_ where class0_.classId=?
	 * 					10计算机
	 */
	@Test
	public void testLazy1() { //类级别的检索
		Class c=(Class)session.load(Class.class, 1L);//1
		System.out.println(c.getClass());			 //2
		System.out.println(c.getId());				 //3
		//Hibernate.initialize(c);        //执行此句也会打印sql语句
		System.out.println(c.getName());			 //4			
	}

	/**
	 * 测试类级别的检索	class.hbm.xml中class节点中设置lazy="true" 延迟检索  测试懒加载异常
	 * 
	 * 前面的部分和testLazy1分析相同，当获取name属性时报"懒加载异常"==========>org.hibernate.LazyInitializationException: could not initialize proxy - no Session
	 * 		简单理解该异常就是Hibernate在使用延迟加载时，并没有将数据实际查询出来，而只是得到了一个代理对象，
	 * 		当使用属性的时候才会去查询，而如果这个时候session关闭了，则会报该异常）！
	 */
	
	@Test
	public void testLazy2() { //类级别的检索	
		Class c=(Class)session.load(Class.class, 1L);
		System.out.println(c.getClass());
		System.out.println(c.getId());
		session.close();				//加上此句测试懒加载异常
		System.out.println(c.getName());		
		
	}
	
	/**
	 * 	测试类级别的检索	class.hbm.xml中class节点中设置lazy="false" 立即检索
	 * 
	 * Hibernate: select class0_.classId as classId1_0_0_, class0_.className as classNam2_0_0_ from t_class class0_ where class0_.classId=?
	 * class com.java1234.model.Class
	 * 1
	 * 10计算机
	 * 
	 * 当调用load方法时，会发送SQL语句，并且得到的不再是代理对象。这个时候就算我们在输出name属性之前将session关闭，也不会报错。
	 */
	@Test
	public void testLazy3() {
		Class c=(Class)session.load(Class.class, 1L);//1
		System.out.println(c.getClass());			 //2
		System.out.println(c.getId());				 //3
		session.close();							 //4
		System.out.println(c.getName());			 //5	
	}

	/**
	 * 	测试类级别的检索	类级别的懒加载是对get方法无效的 	lazy="true"或lazy="false"打印的sql语句都一样
	 * 
	 */
	@Test
	public void testLazy4() {
		Class c=(Class)session.get(Class.class, 1L);
		System.out.println(c.getClass());
		System.out.println(c.getId());
		System.out.println(c.getName());
	}	
/*=========关联关系级别的检索====由一的一方查询多的一方========只在class.hbm.xml中配置，其他地方的lazy等属性为了不影响测试效果都不配置============*/
	/**
	 * 	class.hbm.xml的<set>中没有配置fetch 和 lazy情况（默认情况）
	 * 
	 * 1处sql===> Hibernate: select class0_.classId as classId1_0_0_, class0_.className as classNam2_0_0_ 
	 * from t_class class0_ where class0_.classId=?
	 * 
	 * 2处sql====> Hibernate: select students0_.classId as classId3_0_0_, students0_.stuId as stuId1_1_0_,
	 *  students0_.stuId as stuId1_1_1_, students0_.stuName as stuName2_1_1_, 
	 *  students0_.classId as classId3_1_1_ from t_student students0_ where students0_.classId=?
	 * 
	 */
	@Test
	public void testLazy5() {
		Class c=(Class)session.get(Class.class, 1L); //1运行到此处，打印出查询班级的sql
		System.out.println(c.getStudents().size());	 //2运行到此处，才打印出与班级关联的学生的sql，是延迟的效果
	}
	
	/**
	 * 	class.hbm.xml的<set>中配置fetch="join"时lazy会被忽略!!!
	 * 
	 *      * 发送迫切左外连接查询两个表.
	 *      
	 *   只打印出一条sql语句，运行到1时。查询两个表的信息（将班级，和班级里的所有学生都查出来了）   
	 *      
	 *   Hibernate: select class0_.classId as classId1_0_0_, class0_.className as classNam2_0_0_, 
	 *   	students1_.classId as classId3_0_1_, students1_.stuId as stuId1_1_1_, 
	 *   	students1_.stuId as stuId1_1_2_, students1_.stuName as stuName2_1_2_, 
	 *   	students1_.classId as classId3_1_2_ from t_class class0_ left outer join t_student students1_ on 
	 *   	class0_.classId=students1_.classId where class0_.classId=?   
	 */
	@Test
	public void testLazy6() {
		Class c=(Class)session.get(Class.class, 1L); //1
		System.out.println(c.getStudents().size());	 //2
	}	

	/**
	 * 	class.hbm.xml的<set>中配置fetch="select"时lazy="true"
	 *	
	 *  发送多条sql语句，可以发现和testLazy5的默认情况一样的，由此可以看出，他的默认值了
	 * 
	 *	Hibernate: select class0_.classId as classId1_0_0_, class0_.className as classNam2_0_0_ from
	 * 		 t_class class0_ where class0_.classId=?
	 * 
	 *  Hibernate: select students0_.classId as classId3_0_0_, students0_.stuId as stuId1_1_0_,
	 *   	students0_.stuId as stuId1_1_1_, students0_.stuName as stuName2_1_1_, 
	 *   	students0_.classId as classId3_1_1_ from t_student students0_ where students0_.classId=?
	 */
	@Test
	public void testLazy7() {
		Class c=(Class)session.get(Class.class, 1L); //1运行到此处时，只发送一条只查询班级的SQL
		System.out.println(c.getStudents().size());	 //2需要用到班级所关联的学生的信息时，再发送一条sql查询学生信息
	}
	
	/**
	 * 	class.hbm.xml的<set>中配置fetch="select"时lazy="false"	lazy:false:关联对象的检索不使用延迟
	 * 
	 *  *  发送多条sql语句，不延迟
	 * 
	 *	Hibernate: select class0_.classId as classId1_0_0_, class0_.className as classNam2_0_0_ from
	 * 		 t_class class0_ where class0_.classId=?
	 * 
	 *  Hibernate: select students0_.classId as classId3_0_0_, students0_.stuId as stuId1_1_0_,
	 *   	students0_.stuId as stuId1_1_1_, students0_.stuName as stuName2_1_1_, 
	 *   	students0_.classId as classId3_1_1_ from t_student students0_ where students0_.classId=?
	 */
	@Test
	public void testLazy8() {
		Class c=(Class)session.get(Class.class, 1L); //1运行到此处时，查询班级信息时，就同时查询出班级中学生的信息，同时发送2条sql
		System.out.println(c.getStudents().size());	
	}	
	
	/**
	 * 	class.hbm.xml的<set>中配置fetch="select"时lazy="extra"	lazy:extra极其懒惰.第二条sql只查询数量
	 * 
	 *  *  发送3条sql语句，及其懒惰,需要数量时就查数量，需要具体信息时，再去查询具体的完整信息
	 * 
	 *	Hibernate: select class0_.classId as classId1_0_0_, class0_.className as classNam2_0_0_ from
	 *		 t_class class0_ where class0_.classId=?
	 * 
	 *  Hibernate: select count(stuId) from t_student where classId =?
	 *  
	 *  Hibernate: select students0_.classId as classId3_0_0_, students0_.stuId as stuId1_1_0_, 
	 *  	students0_.stuId as stuId1_1_1_, students0_.stuName as stuName2_1_1_, 
	 *  	students0_.classId as classId3_1_1_ from t_student students0_ where students0_.classId=?
	 */
	@Test
	public void testLazy9() {
		Class c=(Class)session.get(Class.class, 1L); //1运行到此处时，只发送一条只查询班级的SQL
		System.out.println(c.getStudents().size());	 //2需要用到班级所关联的学生的“人数”时，再发送一条sql查询学生的“人数”

		for (Student students : c.getStudents()) {
			System.out.println(students);			 //3需要查询班级所关联学生的具体信息是，再查询一次，发送一次sql
		}
	}
	
	/**
	 * 	class.hbm.xml的<set>中配置fetch="subselect"时lazy="true"【lazy="false"、lazy="extra"的情况和上面类似的】
	 * 
	 *  *  使用subselect的时候 需要使用 query接口进行测试.
	 * 
	 *	当查询出只有1个“班级”时，第2条sql语句：(后面的条件处是“=”)
	 *		Hibernate: select students0_.classId as classId3_0_0_, students0_.stuId as stuId1_1_0_,
	 *		 students0_.stuId as stuId1_1_1_, students0_.stuName as stuName2_1_1_, 
	 *		 students0_.classId as classId3_1_1_ from t_student students0_ where students0_.classId=?	
	 *
	 *  当查询出只有多个“班级”时，第2条sql语句：(后面的条件处是“in”)
	 *     Hibernate: select students0_.classId as classId3_0_1_, students0_.stuId as stuId1_1_1_, 
	 *     		students0_.stuId as stuId1_1_0_, students0_.stuName as stuName2_1_0_, 
	 *    		 students0_.classId as classId3_1_0_ from t_student students0_ where students0_.classId in 
	 *    		 (select class0_.classId from t_class class0_)
	 *  	
	 */	
	@SuppressWarnings("unchecked")
	@Test
	public void testLazy10() {
		List<Class> list= session.createQuery("from Class").list();//（数据库中class表中存2条数据和1条数据的情况下分别测试“in”和“=”的情况）
		for (Class c : list) {
			System.out.println(c.getStudents().size());			 //需要查询班级所关联学生的具体信息是，再查询一次，发送一次sql
		}
	}
	
	/**
	 * 	批量抓取   一方查多方	
	 * 		就是多条sql语句才能完成的查询，现在一条sql语句就能解决多条sql语句才能完成的事情          n+1问题
	 * 		 class.hbm.xml
	 * 		<set>集合上配置batch-size="3"
	 * 
			n+1问题（在数据表中按条件插入数据进行测试）：Class和Student，现在有3个班级，每个“班级”中的人数可能一样，也可能不一样，要求，查询每个“班级”中的“学生”。
		那么我们写的话就需要发送4条sql语句，哪4条呢？第一条查询班级表中所有的班级，剩下3条，拿到每一个班级的ID，去学生表中查找每个班级中的学生，要查3次，
		因为有3个班级。本来只有3个班级，现在需要发送4条sql语句，这就是n+1问题，看下面代码
		
			解决：使用一个属性，batch-size。
		从班级查学生。也就是从单向一对多，从一方查多方，在映射文件中的set中设置batch-size。有多少个“班级”，
		就至少设置多少，意思就是一次性查询多少个。可以看出，发送了3条对Student的查询语句，所以这里batch-size至少为3，大于3可以浪费,
		小于3的话，又会多发sql语句。所以如果能够确定查询多少个，那么就写确定值，如果不能确定，那么就写稍微大一点；
			
			可以看结果，只发送两条sql语句，第一条是查询班级的，第二条是一看，使用关键字 IN 来将所有的班级ID含括在内，我们应该就知道了，原来原理是这样，
		这样就只需要发送一条sql语句，来达到发送4条sql语句才能完成的功能。 这就是批量检索，其实原理很简单
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testBatch1() {
		List<Class> list= session.createQuery("from Class").list();
		for (Class c : list) {
			System.out.println(c.getStudents());
		}		
	}

	/**
	 * 批量抓取 	多方查一方	
	 * 	class.hbm.xml中<class> 中配置batch-size="3"
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testBatch2() {
		List<Student> list= session.createQuery("from Student").list();
		for (Student s : list) {
			System.out.println(s.getName()+"  "+s.getC());
		}		
	}
	
/*=========关联关系级别的检索====由多的一方查询一的一方=========只在student.hbm.xml中配置，其他地方的lazy等属性为了不影响测试效果都不配置===========*/	
	/**
	 * 	没有在<many-to-one>标签上配置（默认条件下）:
	 *  * 发送多条SQL进行查询.
	 *  
	 *  Hibernate: select student0_.stuId as stuId1_1_0_, student0_.stuName as stuName2_1_0_, 
	 *  	student0_.classId as classId3_1_0_ from t_student student0_ where student0_.stuId=?
	 *  
	 *  Hibernate: select class0_.classId as classId1_0_0_, class0_.className as classNam2_0_0_ from t_class class0_ where class0_.classId=?
	 *  	
	 */	
	@Test
	public void testLazy11() {
		Student student = (Student)session.get(Student.class, 1L);// 运行到此处只会发送一条查询学生的SQL.
		System.out.println("学生所在班级名称："+student.getC().getName());// 使用学生对应的班级对象的时候,又发送一条SQL查询学生对应的班级对象
	}

	/**
	 * 	在<many-to-one>标签上配置:
	 *  * fetch="join" lazy="被忽略"
	 *  
	 *  * 发送1条SQL进行查询		发送迫切左外连接	
	 *  
	 *  Hibernate: select student0_.stuId as stuId1_1_0_, student0_.stuName as stuName2_1_0_,
	    	student0_.classId as classId3_1_0_, class1_.classId as classId1_0_1_, class1_.className as classNam2_0_1_
	     	from t_student student0_ left outer join t_class class1_ on student0_.classId=class1_.classId where student0_.stuId=?
	 */	
	@Test
	public void testLazy12() {
		Student student = (Student)session.get(Student.class, 1L);// 运行到此处只会发送一条查询学生的SQL.
		System.out.println("学生所在班级名称："+student.getC().getName());// 使用学生对应的班级对象的时候,又发送一条SQL查询学生对应的班级对象
	}	
	
	/**
	 * 	在<many-to-one>标签上配置:
	 *  * fetch="select" lazy="false" 立即检索
	 *  * 发送多条select语句	
	 */	
	@Test
	public void testLazy13() {
		Student student = (Student)session.get(Student.class, 1L);// 运行到此处只会发送2条SQL.
		System.out.println("学生所在班级名称："+student.getC().getName());		
	}	

	/**
	 * 	在<many-to-one>标签上配置:
	 *  * fetch="select" lazy="proxy" 默认为此配置（和testLazy11一样）	注意：这时关联对象采用什么样的检索策略取决于关联对象的类级别检索策略.就是说参考<class>上的lazy的值（取决一的一方）
	 *					 lazy="no-proxy"	:不使用代理（此处没研究）
	 *  * 发送多条select语句	
	 */	
	@Test
	public void testLazy14() {
		Student student = (Student)session.get(Student.class, 1L);
		System.out.println("学生所在班级名称："+student.getC().getName());		
	}
	
}
