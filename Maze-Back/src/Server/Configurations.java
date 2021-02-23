package Server;

import java.io.*;
import java.util.Properties;

public class  Configurations {

    //private static InputStream inputStream;
    //private static OutputStream outputStream;



    public static void setProperty(String file, String competition algorithm,String solving){

    }
    public static void set_generator(String file_path,String algorithm){

        try {
            OutputStream  outputStream = new FileOutputStream(file_path);
            Properties properties = new Properties();

            properties.setProperty("generator", algorithm);

            properties.store(outputStream,null);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
    public static void set_solver(String file_path,String algorithm) {

        try {
            OutputStream outputStream = new FileOutputStream(file_path);
            Properties properties = new Properties();

            properties.setProperty("solver", algorithm);

            properties.store(outputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getGenerate(String file_path){
        String gn="";

        try {
            InputStream  inputStream = new FileInputStream(file_path);

            Properties properties = new Properties();
            properties.load(inputStream);
            gn =gn+properties.getProperty("generator");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gn;

    }
    public static String getSolve(String file_path){
        String Solver="";

        try {
            InputStream inputStream = new FileInputStream(file_path);
            Properties properties = new Properties();
            properties.load(inputStream);
            Solver=Solver+properties.getProperty("solve");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Solver ;
    }

   /* public static String getThreads(){
        String ThreadsN="";

        try {
            InputStream inputStream = new FileInputStream(System.getProperty("user.dir")+"/resources/config.properties");
            Properties properties = new Properties();
            //System.out.println(inputStream);
            properties.load(inputStream);
            ThreadsN = properties.getProperty("nThreas");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ThreadsN;

    }*/
}