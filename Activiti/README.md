# Activiti5 #
---
*基于maven,本地仓库自己配置；jdk1.7*
## HelloWorld-----第一个activiti程序，环境搭建  *[入门以及环境搭建讲解](http://blog.csdn.net/qciwyy/article/details/48050999)*
* 两种方式生成Activiti 的 25 张表（版本不同生成表的数目不同）；
* 引入 Activiti 配置文件 activiti.cfg.xml；
* 在 Eclipse 上安装 Activiti 插件；[插件安装](http://blog.java1234.com/blog/articles/74.html)
* 初识 Activiti 流程设计工具；（画流程）
```
# Activiti数据库支持：
Activiti的后台是有数据库的支持，所有的表都以ACT_开头。 第二部分是表示表的用途的两个字母标识。 用途也和服务的API对应。
ACT_RE_*: 'RE'表示repository。 这个前缀的表包含了流程定义和流程静态资源 （图片，规则，等等）。
ACT_RU_*: 'RU'表示runtime。 这些运行时的表，包含流程实例，任务，变量，异步任务，等运行中的数据。 
              Activiti只在流程实例执行过程中保存这些数据， 在流程结束时就会删除这些记录。 这样运行时表可以一直很小速度很快。
ACT_ID_*: 'ID'表示identity。 这些表包含身份信息，比如用户，组等等。
ACT_HI_*: 'HI'表示history。 这些表包含历史数据，比如历史流程实例， 变量，任务等等。
ACT_GE_*: 通用数据， 用于不同场景下，如存放资源文件。
```

## Activiti-----综合分析
### 简单模拟流程执行过程 * [参考实例](http://blog.csdn.net/qciwyy/article/details/48051387)*
* 改变位置：将生成表的测试方法移到src/test/java/junit文件夹下，其它注释等未改变
* 测试方法位置：`src/main/java/com/activiti/a_helloworld中HelloWorld.java`,注意对应的helloworld.bpmn中的Properties,测试代码时，注意流程顺序位置 
* 流程图：
![相关流程图.png](http://upload-images.jianshu.io/upload_images/2771329-c815b73f1238f5f6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

* 模拟流程执行过程:
```
      部署流程定义===>启动流程实例===>查询当前人的个人任务===>完成我的任务
```
* services分析：
```
      RepositoryService	管理流程定义
      RuntimeService	执行管理，包括启动、推进、删除流程实例等操作
      TaskService	任务管理
      HistoryService	历史管理(执行完的数据的管理)
      IdentityService	组织机构管理
      FormService	一个可选服务，任务表单管理
      ManagerService	
```

### 管理流程定义
* 测试方法位置：`src/main/java/com/activiti/b_processDefinition中ProcessDefinitionTest.java`
* 部署流程定义(两种方式)
 - classpath路径加载文件

 - zip格式文件
```
将xx.bpmn和xx.png两个文件压缩成zip格式的文件，使用zip的输入流用作部署流程定义
```

* 部署流程定义操作的表：
```
会操作3张表：
a)	act_re_deployment（部署对象表）
存放流程定义的显示名和部署时间，每部署一次增加一条记录
b)	act_re_procdef（流程定义表）
存放流程定义的属性信息，部署每个新的流程定义都会在这张表中增加一条记录。
注意：当流程定义的key相同的情况下，使用的是版本升级
c)	act_ge_bytearray（资源文件表）
        存储流程定义相关的部署信息。即流程定义文档的存放地。每部署一次就会增加两条记录，一条是关于bpmn规则文件的，
一条是图片的（如果部署时只指定了bpmn一个文件，activiti会在部署时解析bpmn文件内容自动生成流程图,一般不建议这么做）。
两个文件不是很大，都是以二进制形式存储在数据库中。
```

* 查看流程定义
 - 从act_re_procdef（流程定义表）查询
 - 查询注意：
```
流程定义ID:========>对应流程定义的key+版本+随机生成数(中间用冒号隔开)
流程定义的名称:========>对应helloworld.bpmn文件中的name属性值
流程定义的key:========>对应helloworld.bpmn文件中的id属性值
流程定义的版本:========>当表中流程定义的key值相同的相同下，版本升级，默认1
```

 - 详细说明：
```
1)	流程定义和部署对象相关的Service都是RepositoryService。
2)	创建流程定义查询对象，可以在ProcessDefinitionQuery上设置查询的相关参数
3)	调用ProcessDefinitionQuery对象的list方法，执行查询，获得符合条件的流程定义列表
4)	由运行结果可以看出：
          Key和Name的值为：bpmn文件process节点的id和name的属性值 
5)	key属性被用来区别不同的流程定义。
6)	带有特定key的流程定义第一次部署时，version为1。之后每次部署都会在当前最高版本号上加1
7)	Id的值的生成规则为:{processDefinitionKey}:{processDefinitionVersion}:{generated-id}, 这里的generated-id是一个自动生成的唯一的数字
8)	重复部署一次，deploymentId的值以一定的形式变化   规则act_ge_property表生成
```
* 删除流程定义