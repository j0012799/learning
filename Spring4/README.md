# Spring4 #
---
*导入eclipse时手动添加jar,注意路径（jar包含在哪个文件夹下），上传公共的jar包到gitHub中`common-files文件夹`方便寻找*
## Spring4HelloWorld------体验HelloWorld最简单代码
* 注入一个对象，调用对象的方法

## Spring4Compare------Test方法传统的new出对象的方式和依赖注入(set注入)方式简单对比

## SpringIocDetails------spring ioc详解之几种注入方式
```
1.sprning装配一个Bean(前面已了解过)--->people(没放入属性)
2.属性注入set方法--->people2
3.构造方法注入(通过类型)--->people3
4.构造方法注入(通过索引)--->people4
5.构造方法注入(类型、索引混合)--->people5
6.工厂方法注入(非静态方法)--->id="peopleFactory"   people7   factory-bean 指定是哪个方法
7.工厂方法注入(静态方法)--->people8 静态方法不需要new 不需要factory方法
8.泛型依赖注入(spring4+hibernate4整合时注意此处没研究)
```
## InjectionParameter------依赖注入的各种方法之上,bean下的参数注入  的各种方式  [类似分析](http://www.lai18.com/content/7929309.html)
* 1.基本类型值；
* 2.注入 bean；
* 3.内部 bean；
* 4.null 值；
 - 如，不知道这只狗的信息，dog就是空
* 5.级联属性；
 - 注意：属性需要先初始化后才能为级联属性赋值和structs2不同
* 6.集合类型属性；
 - List类型
 - Set类型
 - Map类型
 - Properties类型

## AutoWire------自动装配详解
```
注意：在配置bean时，<bean>标签中Autowire属性的优先级比其上级标
签高，即是说，如果在上级标签中定义default-autowire属性为byName，
而在<bean>中定义为byType时，Spring IoC容器会优先使用<bean>标签
的配置。
```
* 1.byName
 - 通过名称进行自动匹配: 配置文件中 有dog、 dog1、 people 三个对象，通过名称将dog注入到people中去(注入的名字和类中属性名一致)
 - 在autoWireByName.xml中设置了autowire="byName",这样配置文件会自动根据 `People.java 中的getDog找bean id="dog"的bean`，然后自动配上去，如果没找到就不装配。`注意：byName的name是java中setXxxx 的Xxxx, 和的private Dog dog中dog拼写毫无关系`

* 2.byType
 - id="people1"的bean在按照byType进行装配时，可以适配的名字为dog2、cat的bean的类型，所以dog2、cat会被注入到people1的dog2、cat属性中，在按照类型进行装配的时候，如过有两个bean的类型符合的话Spring就不知道最终该使用哪个，但是当存在多个类型符合的bean时，会报错。

* 3.constructor：和 byType 类似，只不过它是根据构造方法注入而言的，根据类型，自动注入；

*建议：自动装配机制慎用，它屏蔽了装配细节，容易产生潜在的错误*

## ProgrammaticTransactionManagement------编程式事务管理
[详细分析1](http://www.cnblogs.com/kabi/p/5182064.html) 
[详细分析2](http://sishuok.com/forum/blogPost/list/2506.html)

**步骤：**
* 编写spring配置文件
 - jdbc事务管理器:使用DataSourceTransactionManager来管理JDBC事务,事务管理器传的参数是数据源
 - 声明事务模板

* 调用模板类的方法启用编程式事务管理
 - 通过调用模板类的参数类型为TransactionCallback或TransactionCallbackWithoutResult的execute方法来自动享受事务管理 

**说明：**
```
本例以银行转账为例，故意将转入的方法写错（sql语句中），模拟实际中出现的问题，发现转出了，并没有转入，
在数据库中发现数据并没有发生变化，说明数据出现了回滚
```
## DeclarativeTransactionManagement------声明式事务管理
**步骤：**
* 1.jdbc事务管理器 
* 2.配置事务通知
* 3.配置事务切面：拦截哪些方法(切入点表达式)+应用上面的事务通知 
* ![声明式事务配置.JPG](http://upload-images.jianshu.io/upload_images/2771329-ac74574b6c368bdf.JPG?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

