import java.util.List;

import models.BalanceMap;
import models.Split;
import models.Users;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        var bp = new BalanceMap();
        var u1 = new Users();
        u1.setId(0);

        var u2 = new Users();
        u2.setId(1);

        var s1 = new Split(u1, 200, 50);
        var s2 = new Split(u2, 200, 50);

        bp.addExpense(u1, List.of(s1, s2));
        System.out.println(bp.getBm()[0][0]);
        System.out.println(bp.getBm()[1][0]);
    }
}
