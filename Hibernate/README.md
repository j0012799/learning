# Hibernate4 #
---
*导入eclipse时手动添加jar,注意路径（jar包含在哪个文件夹下），上传公共的jar包到gitHub中`common-files文件夹`方便寻找*
## Hibernate01-----first commit第一个hibernate4的helloWorld
* 没表自动生成表结构，有表更新数据`hibernate.cfg.xml中配置`

## Hibernate04-----xml方式单向关联映射之*[单向多对一(many-to-one)]*
* 多对一是最常见的，也是最容易理解的一种关联。比如：多个员工属性同一个部门。多个产品属于同一个类别，多个产品订单属于同一个账号。`本类中是多个学生在一个班级，一个班级里有多个学生。Student(多)、 Class(一)`单向的多对一指的是多方可以访问一方，而一方不知道多方的存在。
* 根据代码自动生成表结构、junit注解简化测试方法、cascade级联操作
* 具体看代码分析
* 导入eclipse测试时junit包引用本机中eclipse自带的
[网上many-to-one讲解](http://z-xiaofei168.iteye.com/blog/1017383)
[网上级联讲解](http://www.xuehuile.com/blog/9fb6686db07b4868a0dd4602fe697a2b.html)