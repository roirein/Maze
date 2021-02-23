package IO;

import java.io.Serializable;

class Node implements Comparable<Node>, Serializable
{
    private String s;
    private int freq;
    private Node left, right;


    public Node (){
        freq = 0;
    }
    public Node(String s, int freq)
    {
        this.s = s;
        this.freq = freq;
    }

    public Node(String s, int freq, Node left, Node right) {
        this.s = s;
        this.freq = freq;
        this.left = left;
        this.right = right;
    }

    @Override
    public int compareTo(Node o) {
        return this.freq - o.freq;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

};
