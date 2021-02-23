package IO;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.*;

public class MyCompressorOutputStream extends OutputStream {

    private OutputStream out;
    public MyCompressorOutputStream(OutputStream out) {
        this.out = out;
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        for (int i = 0; i < 16; i++) {
            write(b[i]);
        }
        int lucky_number = 22000;
        if (b.length-16 <= lucky_number){
            write(0);
            StringBuilder builder = new StringBuilder();
            for (int i = 16; i < b.length;i++){
                builder.append(b[i]);
            }
            ArrayList<Integer> bytes = getDecimals(builder);
            for (int i = 0; i < bytes.size();i++){
                write(bytes.get(i));
            }
            return;
        }
        write(1);
        HashMap<String,Integer> freq = getFreq(b);
        Node n = buildHuffmanTree(freq);
        HashMap<String,String> codes = new HashMap<String, String>();
        getCodes(n,"",codes);
        HashMap<Node,Integer> data = new HashMap<Node,Integer>();
        ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream("huffman.node"));
        StringBuilder encoded = encode(codes,b);
        data.put(n,encoded.length());
        ois.writeObject(data);
        ArrayList<Integer> for_write = getDecimals(encoded);
        for (int i = 0; i < for_write.size();i++){
            write(for_write.get(i));
        }
    }

    private HashMap<String,Integer> getFreq(byte[] b) {
        HashMap<String, Integer> freq = new HashMap<String, Integer>();
        String s = "";
        for (int i = 16; i < b.length; i++) {
            s += b[i];
            if (s.length() == 6 || i == b.length - 1) {
                if (freq.containsKey(s))
                    freq.put(s, freq.get(s) + 1);
                else {
                    freq.put(s, 1);
                }
                s = "";
            }
        }
        return freq;
    }

    private Node buildHuffmanTree(HashMap<String,Integer> freq){
        PriorityQueue<Node> q = new PriorityQueue<Node>();
        for (Map.Entry elem : freq.entrySet()){
            String s = (String) elem.getKey();
            int f = (int) elem.getValue();
            Node node = new Node(s,f);
            q.add(node);
        }
        Node root = new Node();
        while (q.size() > 1){
            Node first = q.peek();
            q.poll();
            Node second = q.peek();
            q.poll();
            Node f = new Node();
            int f_data = first.getFreq() + second.getFreq();
            f.setFreq(f_data);
            f.setLeft(first);
            f.setRight(second);
            root = f;
            q.add(f);
        }
        return root;

    }

    private StringBuilder encode(HashMap<String,String> codes, byte[] b){
        StringBuilder encoded_maze = new StringBuilder();
        String s = "";
        for (int i = 16; i < b.length;i++){
            s+=b[i];
            if (s.length() == 6 || i == b.length - 1){
                encoded_maze.append(codes.get(s));
                s="";
            }
        }
        return encoded_maze;
    }

    private void getCodes(Node root,String s,HashMap<String,String> codes){
        if (root.getLeft() == null && root.getRight() == null && !root.getS().equals("")) {
            codes.put(root.getS(), s);
            return;
        }
            getCodes(root.getLeft(), s + "0",codes);
            getCodes(root.getRight(), s + "1",codes);
        }



    private ArrayList<Integer> getDecimals(StringBuilder encoded_maze){
        ArrayList<Integer> decimals =  new ArrayList<Integer>();
        String s = "";
        for (int i = 0; i < encoded_maze.length(); i++) {
            s += encoded_maze.charAt(i);
            if (s.length() == 8 || i == encoded_maze.length() - 1) {
                int bin = Integer.parseInt(s, 2);
                decimals.add(bin);
                s = "";
            }
        }
        return decimals;
    }
}
