package com.cloudtravel.common.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * 树是一个无向图，其中任何两个顶点只通过一条路径连接。 换句话说，一个任何没有简单环路的连通图都是一棵树。
 * 给你一棵包含 n 个节点的树，标记为 0 到 n - 1 。
 * 给定数字 n 和一个有 n - 1 条无向边的 edges 列表（每一个边都是一对标签），其中 edges[i] = [ai, bi] 表示树中节点 ai 和 bi 之间存在一条无向边。
 * 可选择树中任何一个节点作为根。当选择节点 x 作为根节点时，设结果树的高度为 h 。在所有可能的树中，具有最小高度的树（即，min(h)）被称为 最小高度树 。
 * 请你找到所有的 最小高度树 并按 任意顺序 返回它们的根节点标签列表。
 * 树的 高度 是指根节点和叶子节点之间最长向下路径上边的数量。
 * 来源：力扣（LeetCode）
 */
public class FindTreeMinHeightTrees {

    public int getRoot(int [][] param) {
        int p = 0;
        while (param[p] != null) p = getNext(param , p);
        return p;
    }

    public int getNext(int [][] param , int p) {
        int[] gra = param[p];
        return gra[0] == p ? gra[1] : gra[0];
    }

    public int[] getNode(int [][] param , int root) {
        int n = param.length;
        int max = 0, node = 0;
        int [] h = new int[n];
        int [] stack = new int[n];
        int size = 0;
        for (int i = 0 ; i < n ; i ++){
            int p = i,count = 0;
            while (p != root && h[p] ==0){
                stack[size ++] = p;
                p = getNext(param , p);
            }
            while (size != 0){
                int temp = stack[-- size];
                h[temp] = h[p] + 1;
                if(h[temp] > max) {
                    max = h[temp];
                    node = temp;
                }
                p = temp;
            }
        }
        return new int []{node , h[node]};
    }

    private int reverse(int[][] gra, int root, int p) {
        int ret = p;
        int[] pre = null;
        while(p != root) {
            int next = getNext(gra, p);
            int[] temp = gra[p];
            gra[p] = pre;
            pre = temp;
            p = next;
        }
        gra[root] = pre;
        return ret;
    }

    public List<Integer> findMinHeightTrees(int n , int [][] edges) {
        List<Integer> res = new ArrayList<>();
        int [] [] gra = new int [n][];
        for (int[] edge : edges) {
            int a = edge[0] , b = edge[1];
            if(gra[a] == null) {
                gra[a] = edge;
            }else {
                gra[b] = edge;
            }
        }
        int root = getRoot(gra);
        int [] node = getNode(gra , root);
        root = reverse(gra , root , node[0]);
        node = getNode(gra , root);
        int len = node[1] / 2;
        int p = node[0];
        while (len -- != 0) p = getNext(gra , p);
        res.add(p);
        if((node[1] & 1) == 1) {
            res.add(getNext(gra , p));
        }
        return res;
    }

    public static void main(String[] args) {
        FindTreeMinHeightTrees find = new FindTreeMinHeightTrees();
        int [][] param = new int[5][];
        param[0] = new int[]{3,0};
        param[1] = new int[]{3,1};
        param[2] = new int[]{3,2};
        param[3] = new int[]{3,4};
        param[4] = new int[]{4,5};
        System.out.println(find.findMinHeightTrees(6, param));

    }
}
