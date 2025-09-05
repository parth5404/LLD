#include <iostream>
using namespace std;
class Robot {
   private:
    Talkable* ing;
    // Walkable* walk;
    // Fly* fly;
   public:
    Robot(Talkable* sher) { this->ing = sher; }
    void talk() { ing->talk(); }
    virtual void projection() = 0;
};
class Talkable {
   public:
    virtual void talk() = 0;
};
class NormalTalk : public Talkable {
   public:
    void talk() override { cout << "Normal Talk" << endl; }
};
class NoTalk : public Talkable {
   public:
    void talk() override { cout << "No Talk" << endl; }
};
// class Walkable {};
// class Fly {};

int main() {
    
}