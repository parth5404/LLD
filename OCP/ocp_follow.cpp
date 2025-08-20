#include <iostream>
#include <vector>
using namespace std;

class Product {
public:
    string name;
    double price;
    Product(string name, double price) {
        this->price = price;
        this->name = name;
    }
};
class Cart {
private:
    vector<Product*>vec;
public:
    void addProduct(Product* item) {
        vec.push_back(item);
    }
    vector<Product*> getProduct() {
        return vec;
    }
    double calcPrice() {
        double sum = 0;
        for (int i = 0;i < vec.size();i++) {
            sum = sum + vec[i]->price;
        }
        return sum;
    }

};
class PrintInvoice {
private:
    Cart* cart;
public:
    PrintInvoice(Cart* cart) {
        this->cart = cart;
    }
    void printing() {
        for (auto& it : cart->getProduct()) {
            cout << it->name << " " << it->price << endl;
        }
    }
};
class DBstorage {
private:
    Cart* cart;
public:
    virtual void save(Cart* cart) = 0;
    // void saveTofile() {
    //     cout << "file saved" << endl;
    // }
    // void savetoSQL() {
    //     cout << "SQL saved" << endl;
    // }
    // void savetoMongo() {
    //     cout << "Mongo Saved" << endl;
    // }
};
class SQL :public DBstorage {
public:
    void save(Cart* cart)override {
        cout << "SQL saved" << endl;
    }
};
class File :public DBstorage {
public:
    void save(Cart* cart) override {
        cout << "File saved" << endl;
    }
};
class Mongo :public DBstorage {
public:
    void save(Cart* cart)override {
        cout << "Mongo Saved" << endl;
    }
};

int main() {
    Cart* ShopCart = new Cart();
    ShopCart->addProduct(new Product("Laptop", 1500));
    ShopCart->addProduct(new Product("Mobile", 500));
    PrintInvoice* invoi = new PrintInvoice(ShopCart);
    invoi->printing();
    DBstorage* store = new SQL();
    store->save(ShopCart);
    // store->saveTofile();
}