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