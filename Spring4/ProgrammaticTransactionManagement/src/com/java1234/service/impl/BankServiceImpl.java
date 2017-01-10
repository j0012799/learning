package com.java1234.service.impl;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.java1234.dao.BankDao;
import com.java1234.service.BankService;


public class BankServiceImpl implements BankService{

	private BankDao bankDao;
	
	private TransactionTemplate transactionTemplate;
	
	public void setBankDao(BankDao bankDao) {
		this.bankDao = bankDao;
	}
	
	//注入模板类
	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	@Override
	public void transferAccounts(final int count, final int userIdA, final int userIdB) {
		
		//通过调用模板类的参数类型为TransactionCallback或TransactionCallbackWithoutResult的execute方法来自动享受事务管理 
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus arg0) {//doInTransactionWithoutResult方法实现中定义了需要事务管理的操作
				// TODO Auto-generated method stub
				bankDao.outMoney(count, userIdA);
				bankDao.inMoney(count, userIdB);				
			}
		});
	}

}
