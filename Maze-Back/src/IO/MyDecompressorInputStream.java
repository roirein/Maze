package IO;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyDecompressorInputStream extends InputStream {

    private InputStream in;

    public MyDecompressorInputStream(InputStream in) {
        this.in = in;
    }

    @Override
    public int read() throws IOException {
        return in.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        for (int i = 0; i < 16; i++) {
            b[i] = (byte) read();
        }
        int flag = read();
        ArrayList<Integer> bytes = new ArrayList<Integer>();
        int read_number = read();
        bytes.add(read_number);
        int counter = 1;
        while (read_number != -1) {
            read_number = read();
            bytes.add(read_number);
            }
        if(flag == 0){
            restore_maze_if_not_huffman(bytes,b);
            return counter;

        }
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("huffman.node"));
            HashMap<Node, Integer> data = null;
            try {
                data = (HashMap<Node, Integer>) inputStream.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            int length = 0;
            Node root = null;
            for (Map.Entry<Node, Integer> entry : data.entrySet()) {
                length = entry.getValue();
                root = entry.getKey();
            }
            StringBuilder encoded = getEncodedMaze(bytes, length);
            StringBuilder original_maze = decode(encoded, root);
            int ind = 16;
            for (int i = 0; i < original_maze.length(); i++) {
                b[ind] = (byte) Character.getNumericValue(original_maze.charAt(i));
                ind++;
            }
            return counter + 17;
        }

    private StringBuilder getEncodedMaze(ArrayList<Integer> bytes,int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.size(); i++) {
            int diff = 0;
            String s = Integer.toString(bytes.get(i), 2);
            if (length - sb.length() < 8) {
                int res = length - sb.length();
                if (s.length() < res)
                    diff = res - s.length();
            } else if (s.length() < 8) {
                diff = 8 - s.length();
            }
            String add = "";
            for (int j = 0; j < diff; j++) {
                add += "0";
            }
            s = add + s;
            sb.append(s);
        }
        return sb;
    }

    private StringBuilder decode(StringBuilder encoded_maze, Node root){
        StringBuilder ans = new StringBuilder();
        Node curr = root;
        for (int i = 0; i < encoded_maze.length();i++){
            if(encoded_maze.charAt(i) == '0')
                curr =curr.getLeft();
            else
                curr = curr.getRight();
            if (curr.getRight() == null && curr.getLeft() == null) {
                ans.append(curr.getS());
                curr = root;
            }
        }
        return ans;

    }

    private void restore_maze_if_not_huffman(ArrayList<Integer> bytes, byte[] b){
        int ind = 16;
        for (int i = 0; i < bytes.size()-1;i++){
            int diff = 0;
            String s =  Integer.toString(bytes.get(i),2);
            if (b.length - ind < 8){
                int res = b.length - ind;
                if (s.length() < res)
                    diff = res - s.length();
            }
            else if (s.length() < 8){
                diff = 8 - s.length();
            }
            String add = "";
            for (int j = 0; j < diff;j++){
                add+= "0";
            }
            s = add + s;
            for (int j = 0; j < s.length(); j++){
                b[ind] = (byte) Character.getNumericValue(s.charAt(j));
                 ind++;
            }


        }
    }
}

