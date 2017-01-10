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