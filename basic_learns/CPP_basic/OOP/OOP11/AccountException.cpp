#include "BankingCommonDecl.h"
#include "AccountException.h"

InputException::InputException(int money)
	:input(money) {}

void InputException::ShowExceptionReason()
{
	cout << "[���ܹ߻� : " << input << "�Ұ���]" << endl;
}

WithdrawException::WithdrawException(int money)
	:balance(money) {}

void WithdrawException::ShowExceptionReason()
{
	cout << "[���ܹ߻� : �ܾ�(" << balance << ")����]" << endl;
}