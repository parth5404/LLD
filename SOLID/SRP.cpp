#include <iostream>
#include <vector>
using namespace std;

int main(){
    vector<int> v;
    v.push_back(1);
    for(int i = 1; i < 10; i++) {
        v.push_back(i);
        cout << v[i] << endl;
    }
    return 0;
}