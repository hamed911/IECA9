package net.internetengineering.domain;


import net.internetengineering.domain.dealing.TransactionType;

public class Account{
	private Long balance;
        
	public Account(){
		executeTransaction(TransactionType.INITIAL, 0);
	}
	
	public long getBalance() {
		return balance;
	}

	public void executeTransaction(TransactionType type, long monetaryUnit) {
		if(TransactionType.DEPOSIT.equals(type))
			this.balance += monetaryUnit;
		else if(TransactionType.WITHDRAW.equals(type))
			this.balance -= monetaryUnit;
		else
			this.balance = monetaryUnit;
	}
	
	public boolean isEnoughMoney(long money){
		if(money <= this.balance)
			return true;
		return false;
	}
}

