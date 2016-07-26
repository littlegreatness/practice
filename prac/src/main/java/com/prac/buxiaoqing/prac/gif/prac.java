package com.prac.buxiaoqing.prac.gif;

/**
 * author：buxiaoqing on 16/7/26 14:03
 * Just do IT(没有梦想,何必远方)
 */
public class prac {


    public static void main(String[] args) {

        String str = "We are happy";

        String n = replaceSpace(str);
        System.out.println(n);

    }

    private static String replaceSpace(String str) {
        String copy = "";

        if (str == null)
            return null;


        char[] chars = str.toCharArray();
        int blankNum = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '\n')
                blankNum++;
        }

        char[] copyC = new char[chars.length + blankNum * 2];
        int curBlank = 0;

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '\n') {
                curBlank++;
                copyC[i + curBlank * 2] = '%';
                copyC[i + curBlank * 2 + 1] = '2';
                copyC[i + curBlank * 2 + 2] = '0';

            } else {
                copyC[i + curBlank * 2] = chars[i];
            }
        }

        copy = copyC.toString();


        return copy;
    }
}
