#include <fstream>
#include <iostream>
using namespace std;

class DocElement {
   public:
    virtual string render() = 0;
};
class TextElement : public DocElement {
   private:
    string text;

   public:
    TextElement(string text) { this->text = text; }
    string render() override { return "Text: " + text; }
};
class ImgElement : public DocElement {
   private:
    string img;

   public:
    ImgElement(string text) { this->img = text; }
    string render() override { return "Image Path: " + img; }
};
class Document {
   private:
    vector<DocElement*> elements;

   public:
    void addElement(DocElement* el) { elements.push_back(el); }
    string render() {
        string ans = "";
        for (auto el : elements) {
            ans = ans + el->render();
        }
        return ans;
    }
};
class Persistance {
   public:
    virtual void save(string str) = 0;
};
class SavetoFile : public Persistance {
   public:
    void save(string str) override {
        ofstream outFile("document.txt");
        if (outFile) {
            outFile << str;
            outFile.close();
            cout << "Document saved to document.txt" << endl;
        } else {
            cout << "Error: Unable to open file for writing." << endl;
        }
    }
};
class DB : public Persistance {
   public:
    void save(string str) override { cout << "DB " << str << endl; }
};

class Editor {
   private:
    Document* doc;
    Persistance* db;
    string str;

   public:
    Editor(Document* document, Persistance* storage) {
        this->doc = document;
        this->db = storage;
    }
    void addtext(string text) { doc->addElement(new TextElement(text)); }
    void addImage(string img) { doc->addElement(new ImgElement(img)); }
    string renderDocument() {
        if (str.empty()) {
            str = doc->render();
        }
        return str;
    }
    void saveDocument() { db->save(renderDocument()); }
};

int main() {
    Persistance* storage = new SavetoFile();
    Document* doc = new Document();
    Editor* editor = new Editor(doc, storage);
    editor->addtext("sher 5404");
    editor->renderDocument();
    editor->saveDocument();

    return 0;
}
