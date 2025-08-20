#include <iostream>
using namespace std;

class NWAccount
{
public:
    virtual void deposit(double amt) = 0;
};
class WAccount : public NWAccount
{
public:
    virtual void withdraw(double amt) = 0;
};

class SA : public WAccount
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
class CA : public WAccount
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
class FD : public NWAccount
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
    vector<NWAccount *> account1;
    vector<WAccount *> account2;

public:
    BankClient(vector<NWAccount *> acc1, vector<WAccount *> acc2)
    {
        this->account1 = acc1;
        this->account2 = acc2;
    }
    void processTrans()
    {
        for (auto it : account1)
        {
            it->deposit(1000);
            // try
            // {
            //     it->withdraw(200);
            // }
            // catch (const logic_error &e)
            // {
            //     cout << "Exception" << e.what() << endl;
            // }
        }
        for (auto it : account2)
        {
            it->deposit(1000);
            it->withdraw(200);
        }
    }
};
int main()
{
    vector<WAccount *> acc1;
    vector<NWAccount *> acc2;
    acc1.push_back(new SA());
    acc1.push_back(new CA());
    acc2.push_back(new FD());
    BankClient *Client = new BankClient(acc2, acc1);
    Client->processTrans();
    return 0;
}