import clone.Student;

public static void main(String[] args) {
    String str="abcd";
    int cnt=1;
    for(int i=0;i<str.length();i++){
        for(int j=i;j<str.length();j++){
            System.out.print(cnt);
            System.out.println(str.substring(i,j+1));
            cnt++;
        }
    }



}