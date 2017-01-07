package com.java1234.activiti.table;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

public class ActivitiTest01 {

	/**
	 * 生成Activiti需要的25表	http://blog.csdn.net/qciwyy/article/details/48050999
	 */
	@Test
	public void testCreateTable(){
		//创建Activiti流程引擎配置文件
		ProcessEngineConfiguration pec=ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration(); // 获取流程引擎配置
		//配置驱动
		pec.setJdbcDriver("com.mysql.jdbc.Driver"); 
		//配置连接地址
		pec.setJdbcUrl("jdbc:mysql://localhost:3306/db_activiti"); 
		 //用户名
		pec.setJdbcUsername("root");
		 //密码
		pec.setJdbcPassword("root");
		
		//配置数据库建表策略  true 自动创建和更新表
		/**
		 * DB_SCHEMA_UPDATE_FALSE = "false";不能自动创建表，需要表存在
		 * DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop";先删除表再创建表
		 * DB_SCHEMA_UPDATE_TRUE = "true";如果表不存在，自动创建表
		 */
		pec.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		
		//根据引擎配置对象，创建流程引擎
		ProcessEngine pe=pec.buildProcessEngine(); 
	}
	
	/**
	 * 生成Activiti需要的25表 使用配置文件
	 */
	@Test
	public void testCreateTableWithXml(){
		 // 引擎配置
	    ProcessEngineConfiguration pec=ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
	    // 获取流程引擎对象
	    ProcessEngine processEngine=pec.buildProcessEngine();
	}
}
