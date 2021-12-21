package ew.quilt.util;

import static java.lang.Math.abs;
import java.util.Random;

public class MathCaptcha {

    public static String[] getCaptcha() {
        int x;
        int y;
        int guess = 999;
        int mode;
        String calculate[] = {"+", "-", "*", "/"};
        String ret[] = {"", ""};
        Random ran = new Random();
        x = ran.nextInt(100);
        y = ran.nextInt(100);
        mode = ran.nextInt(4);
        switch (mode) {
            case 0:
                guess = x + y;
                break;
            case 1:
                guess = x - y;
                break;
            case 2: // 壓在 10 * 10 以下結果
                x = ran.nextInt(11);
                y = ran.nextInt(11);
                guess = x * y;
                break;
            case 3:
                y = ran.nextInt(99) + 1; // 如果 / 0 會是很好玩的例外狀況
                guess = x / y;
        }
        if (mode == 0 && x + y > 100) { // 結果壓在 100 以下
            while (x + y > 100) {
                x = x - ran.nextInt(x);
                y = y - ran.nextInt(y);
            }
            guess = x + y;
        } else if (mode == 1 && x - y < 0) { // 禁止負數存在
            x = x ^ y;
            y = x ^ y;
            x = x ^ y;
            guess = abs(guess);
        } else if (mode == 3 && (x % y != 0)) { // 防止餘數
            x = x - (x % y);
        }
        ret[0] = x + " " + calculate[mode] + " " + y + " = ";
        ret[1] = String.valueOf(guess);
        return ret;
    }
}
