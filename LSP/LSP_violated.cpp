#include <iostream>
using namespace std;

class Account
{
public:
    virtual void deposit(double amt) = 0;
    virtual void withdraw(double amt) = 0;
};

class SA : public Account
{
private:
    double balance;

public:
    SA()
    {
        balance = 0;
    }
    void deposit(double amt)
    {
        balance += amt;
        cout << amt << " deposited in account" << endl;
    }
    void withdraw(double amt)
    {
        balance -= amt;
        cout << amt << " withdraw from account" << endl;
    }
};
class CA : public Account
{
private:
    double balance;

public:
    CA()
    {
        balance = 0;
    }
    void deposit(double amt)
    {
        balance += amt;
        cout << amt << " deposited in account" << endl;
    }
    void withdraw(double amt)
    {
        balance -= amt;
        cout << amt << " withdraw from account" << endl;
    }
};
class FD : public Account
{
private:
    double balance;

public:
    FD()
    {
        balance = 0;
    }
    void deposit(double amt)
    {
        balance += amt;
        cout << amt << " deposited in account" << endl;
    }
    void withdraw(double amt)
    {
        throw logic_error(" No withdraw allowes for FD");
    }
};
class BankClient
{
private:
    vector<Account *> account;
public:
    BankClient( vector<Account *> acc){
        this->account=acc;
    }
    void processTrans(){
        for(auto it:account){
            it->deposit(1000);
            try{
                it->withdraw(200);
            }
            catch(const logic_error&e){
                cout<<"Exception"<<e.what()<<endl;
            }
        }
    }
}; 
int main()
{
    vector<Account *> acc;
    acc.push_back(new SA());
    acc.push_back(new CA());
    acc.push_back(new FD());
    BankClient* Client= new BankClient(acc);
    Client->processTrans();
    return 0;
}