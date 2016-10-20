package com.prac.buxiaoqing.prac.gif;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * author：buxiaoqing on 16/7/26 14:03
 * Just do IT(没有梦想,何必远方)
 */
public class prac {


    public static void main(String[] args) {

//        String str = "We are happy";
//        System.out.println(str);
//
//        // TODO Auto-generated method stub
        Integer[] list = {34, 3, 53, 2, 23, 7, 14, 10};
        //快速排序
        QuicSort qs = new QuicSort();
        qs.quick(list);
        System.out.print("最后结果:     ");
        for (int i = 0; i < list.length; i++) {
            System.out.print(list[i] + " ");
        }
//        System.out.println();
//        //冒泡排序
//        sort(list);
//        reverse();
//        char ch = '1';
//        int a = ch;
//        byte b= (byte) ch;
//
//        System.out.println(" a = " + a + " b =" + b);
//        StringBuilder sb = new StringBuilder();
//        sb.append(a);
//        String str = sb.toString();
//        System.out.println(" string = " +  Integer .parseInt(str));
    }

    public static  void reverse() {
        LinkedList<Integer> list = new LinkedList<>();
        LinkedList<Integer> templist = new LinkedList<>();
        int i = 0;
        while (i < 6) {
            list.add(i);
            i++;
        }
        Iterator<Integer> it = list.iterator();
        int m;
        while (it.hasNext() && i >= 0) {
            m = it.next();
            templist.addFirst(m);//顺序取出来往前插入
            i--;
        }
        list = templist;
        System.out.println(list);
    }

    public static   void sort(Integer[] list){
        int length = list.length;
        for ( int i = 0; i < length ; i ++ ) {
            for(int j = i ; j < length ; j ++){
                if(list [i] > list[j] ){
                    int temp = list[i];
                    list[i ] = list[j];//把较大的数换到后面去
                    list[j] = temp;
                }
            }
        }
        for (int i = 0; i < list.length; i++) {
            System.out.print(list[i] + " ");
        }
    }

    static class QuicSort {

        public QuicSort() {

        }

        public int getMiddle(Integer[] list, int low, int high) {
            int tmp = list[low];    //数组的第一个作为中轴
            while (low < high) {
                while (low < high && list[high] > tmp) {
                    high--;
                }
                list[low] = list[high];   //比中轴小的记录移到低端
                while (low < high && list[low] < tmp) {
                    low++;
                }
                list[high] = list[low];   //比中轴大的记录移到高端
            }
            list[low] = tmp;              //中轴记录到尾

            return low;                   //返回中轴的位置
        }

        public void _quickSort(Integer[] list, int low, int high) {
            if (low < high) {
                int middle = getMiddle(list, low, high);  //将list数组进行一分为二
                _quickSort(list, low, middle - 1);        //对低字表进行递归排序
                _quickSort(list, middle + 1, high);       //对高字表进行递归排序
            }
        }


        public void quick(Integer[] str) {
            if (str.length > 0) {    //查看数组是否为空
                _quickSort(str, 0, str.length - 1);
            }
        }
    }
}
