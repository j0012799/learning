# Hibernate4 #
---
*导入eclipse时手动添加jar,注意路径（jar包含在哪个文件夹下），上传公共的jar包到gitHub中`common-files文件夹`方便寻找*
## Hibernate01-----first commit第一个hibernate4的helloWorld
* 没表自动生成表结构，有表更新数据`hibernate.cfg.xml中配置`

## Hibernate04-----xml方式单向关联映射之*[单向多对一(many-to-one)]*
* 多对一是最常见的，也是最容易理解的一种关联。比如：多个员工属性同一个部门。多个产品属于同一个类别，多个产品订单属于同一个账号。`本类中是多个学生在一个班级，一个班级里有多个学生。Student(多)、 Class(一)`单向的多对一指的是多方可以访问一方，而一方不知道多方的存在。
* 根据代码自动生成表结构、junit注解简化测试方法、cascade级联操作
* 具体看代码分析
[网上many-to-one讲解](http://z-xiaofei168.iteye.com/blog/1017383)
[网上级联讲解](http://www.xuehuile.com/blog/9fb6686db07b4868a0dd4602fe697a2b.html)

## Hibernate04-02-----xml方式双向关联映射之*[一对多双向(one-to-many)]*
* 一对多双向关联映射:即在一的一端存在多的一端的一个`集合对象`，在多的一端存在一的一端的一个`对象`，这样就可以保证在加载一的一端或多的一端将被指向端的集合或对象加载上来，即保证双向关联

* 1.cascade="all"且inverse="false"
 - // 执行对class的插入
 - Hibernate: insert into t_class (className) values (?)
 - // cascade = 'all'，所以进行级联操作
 - Hibernate: insert into t_student (stuName, classId) values (?, ?)
 - Hibernate: insert into t_student (stuName, classId) values (?, ?)
 - // inverse = 'false'，由class来维护关系（可以看到这些操作是多余的）
 - Hibernate: update t_student set classId=? where stuId=?
 - Hibernate: update t_student set classId=? where stuId=?

* 2.cascade = "none" 且 inverse = "false"
 - // 执行对class的插入
 - Hibernate: insert into t_class (className) values (?)
 - // inverse='false'，所以更新关系 
 - Hibernate: update t_student set classId=? where stuId=?
 - // 但由于cascade='none'，student并未插入数据库，导致如下exception
 - org.hibernate.TransientObjectException: object references an unsaved transient instance - save the transient instance before flushing: 

* 3.cascade = "all" 且 inverse = "true"
 - //执行对class的插入 
 - Hibernate: insert into t_class (className) values (?)
 - //cascade='all'，执行对student的插入 
 - Hibernate: insert into t_student (stuName, classId) values (?, ?)
 - Hibernate: insert into t_student (stuName, classId) values (?, ?)
 - // 但由于inverse='true'，所以未有对关系的维护。但由于一对多的关系中，关系本身在“多”方的表中。所以，无需更新关系

* 4.cascade = "none" 且 inverse = "true"
 - //只执行对class的插入
 - Hibernate: insert into t_class (className) values (?)

* [cascade与inverse详细讲解1](http://blog.csdn.net/tlycherry/article/details/8976416)

* [cascade与inverse详细讲解2](http://m.doc00.com/doc/10110270p)

*在代码中手动更改进行测试*

**可以看到，对于一对多关系，关系应由“多”方来维护（指定“一”方的inverse为true），并且应在“一”方指定相应的级联操作。**

## Hibernate05-----深入理解hibernate的三种状态
* *具体结合代码分析*
* [深入hibernate的三种状态](http://www.cnblogs.com/xiaoluo501395377/p/3380270.html)
* 总结：
 - ①.对于刚创建的一个对象，如果session中和数据库中都不存在该对象，那么该对象就是`瞬时对象(Transient)`
 - ②.瞬时对象调用save方法，或者离线对象调用update方法可以使该对象变成`持久化对象(Persistent)`，如果对象是持久化对象时，那么对该对象的任何修改，都会在提交事务时才会与之进行比较，如果不同，则发送一条update语句，否则就不会发送语句
 - ③.`离线对象(Detached)`就是，数据库存在该对象，但是该对象又没有被session所托管

![状态分析](http://upload-images.jianshu.io/upload_images/2771329-3268b87f0a40e5b1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## Hibernate09-----hibernate检索策略
**hibernate类级别检索策略**
* 【testLazy1～testLazy４类级别的`立即检索`与`延迟检索`具体测试代码】
```
在class中配置lazy
```

**hibernate关联级别检索策略(基于一对多的双向关系)**
== 1 由一的一方关联多的一方（此处是查询班级时，同时查询出学生）【test5~test10】

```
* <set>
* fetch:控制sql语句的类型
  > join		:发送迫切左外连接的SQL查询关联对象.fetch=”join”那么lazy被忽略了.
  > select		:默认值,发送多条SQL查询关联对象.
  > subselect	:发送子查询查询关联对象.(需要使用Query接口测试)

* lazy:控制关联对象的检索是否采用延迟.
  > true		:默认值, 查询关联对象的时候使用延迟检索
  > false		:查询关联对象的时候不使用延迟检索.
  > extra		:及其懒惰.
***** 如果fetch是join的情况,lazy属性将会忽略.

* bitch-size     :n+1问题【testBatch2、testBatch2】
  > 一方查多方：在一方的set中就代表着多方的对象，将batch-size放在set中，就可以理解查多方的时候，就使用批量检索了。
  >多方查一方：在一方的class中设置batch-size。可以理解，当多方查到一方时，在一方的映射文件中的class部分遇到了batch-size就知道查询一方时需要批量检索了
```
== 2 由多的一方关联一的一方（此处是查询学生时，同时查询出班级）【test11~test14】
```

* <many-to-one>
* fetch:控制SQL语句发送格式
  > join		:发送一个迫切左外连接查询关联对象.fetch=”join”,lay属性会被忽略.
  > select		:发送多条SQL检索关联对象.
* lazy:关联对象检索的时候,是否采用延迟
  > false		:不延迟
  > proxy		:使用代理.检索订单额时候,是否马上检索客户 由Class对象的映射文件中<class>上lazy属性来决定(一的一方上觉定).
  > no-proxy	:不使用代理（此处没研究）
```

* [Hibernate：检索策略的学习1](http://tracylihui.github.io/2015/07/10/Hibernate%EF%BC%9A%E6%A3%80%E7%B4%A2%E7%AD%96%E7%95%A5%E7%9A%84%E5%AD%A6%E4%B9%A01/)

* [get和load的区别](http://tracylihui.github.io/2015/07/20/Hibernate%EF%BC%9Aget%E5%92%8Cload/)

* [Hibernate<六> Hibernate的检索策略](https://my.oschina.net/wangning0535/blog/513220)

* http://www.cnblogs.com/whgk/p/6164862.html
