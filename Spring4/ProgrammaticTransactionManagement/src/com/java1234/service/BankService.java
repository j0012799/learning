package com.java1234.service;

public interface BankService {

	/**
	 * A向B转账count元
	 * @param count
	 * @param userIdA
	 * @param userIdB
	 */
	public void transferAccounts(int count,int userIdA,int userIdB);
}
