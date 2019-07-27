package com.example.block_snake;
//block  的基本属性，主要包括颜色，形状以及旋转后的形状
public class B_Shape {
    //block 的颜色
    public static int[] color=new int[]{
            R.drawable.star_b,R.drawable.star_g,R.drawable.star_p,R.drawable.star_r,R.drawable.star_y,R.drawable.huaji,R.drawable.huaji1
    };
    //block 的形状
    public static int[][] shape=new int[][]{
            //L  对应的8种形状
            {0x8, 0x8, 0xc, 0x0},
            {0xe, 0x8, 0x0, 0x0},
            {0xc, 0x4, 0x4, 0x0},
            {0x2, 0xe, 0x0, 0x0},
            {0x4, 0x4, 0xc, 0x0},
            {0x8, 0xe, 0x0, 0x0},
            {0xc, 0x8, 0x8, 0x0},
            {0xe, 0x2, 0x0, 0x0},
            //Z   对应的4种形状
            {0x8, 0xc, 0x4, 0x0},
            {0x6, 0xc, 0x0, 0x0},
            {0x4, 0xc, 0x8, 0x0},
            {0xc, 0x6, 0x0, 0x0},
            //T   对应的4种形状
            {0x4, 0xe, 0x0, 0x0},
            {0x8, 0xc, 0x8, 0x0},
            {0xe, 0x4, 0x0, 0x0},
            {0x4, 0xc, 0x4, 0x0},
            //直线  对应的2种形状
            {0x4, 0x4, 0x4, 0x4},
            {0x0, 0xf, 0x0, 0x0},
            //田字形
            {0xc, 0xc, 0x0, 0x0}
    };
    //block  的初始位置,与上方的方块的形状对应
    public static int[][] initPosition=new int[][]{
            //(x,y)
            {2, -2},
            {3, -1},
            {2, -2},
            {3, -1},
            {2, -2},
            {3, -1},
            {2, -1},
            {3, -1},

            {2, -2},
            {3, -1},
            {2, -2},
            {3, -1},

            {3, -1},
            {2, -2},
            {3, -1},
            {2, -2},

            {2, -3},
            {3, -1},
            {2, -1}
    };
    //block  变换后的下一个形状 如shape[0] 为L形状，若想旋转后得到反L(shape[4]) 则可表示为 shape[nextshape[7]]
    public static int[] nextshape=new int[]{
            1, 2, 3, 0, 5, 6, 7, 4, 9, 8, 11, 10, 13, 14, 15, 12, 17, 16, 18
    };
}
