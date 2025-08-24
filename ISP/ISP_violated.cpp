#include <iostream>
using namespace std;
class Shape{
    public:
        virtual void area() = 0;
        virtual void vol()=0;
    
};
class Rect:public Shape{
    void area() override{
        cout<<"sher"<<endl;
    }
    void vol() override{
        
    }
};

int main(){

}