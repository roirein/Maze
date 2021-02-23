package Model;

import java.io.*;
import java.util.Properties;

public class  Configurations {

    //private static InputStream inputStream;
    //private static OutputStream outputStream;


    public static void setProperty(String Solver) throws IOException {
        File file = new File(Configurations.class.getClassLoader().getResource("Config.Properties").toExternalForm());
        FileOutputStream out = new FileOutputStream(file);
        Properties properties = new Properties();
        //properties.setProperty("Generator", generator);
        properties.setProperty("Solver", Solver);
        //properties.setProperty("Rival", competive_algorithm);
        properties.setProperty("Number of Threads", Integer.toString(Runtime.getRuntime().availableProcessors() * 2));
        properties.store(out,null);
    }






    public static String[] getallProperties(String file){
        String[] props = new String[4];

        try {
            InputStream inputStream = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(inputStream);
            //props[0] = properties.getProperty("Generator");
            props[0] = properties.getProperty("Solver");
            //props[2] = properties.getProperty("Rival");
            props[1] = properties.getProperty("Number of Threads");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return props;
    }

    public static String getGenerate(){
        String generator = "";

        try {
            InputStream inputStream = Configurations.class.getClassLoader().getResourceAsStream("Config.Properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            generator = properties.getProperty("Generator");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return generator;

    }

    public static String getSolve(){
        String solver = "";

        try {
            InputStream inputStream = Configurations.class.getClassLoader().getResourceAsStream("Config.Properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            solver = properties.getProperty("Solver");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return solver;
    }


    public static String getRival(){
        String rival = "";

        try {
            InputStream inputStream = Configurations.class.getClassLoader().getResourceAsStream("Config.Properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            rival = properties.getProperty("Rival");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rival;
    }

    public static int getThreads(){
        String ThreadsN="";

        try {
            InputStream inputStream = Configurations.class.getClassLoader().getResourceAsStream("Config.Properties");
            Properties properties = new Properties();
            //System.out.println(inputStream);
            properties.load(inputStream);
            ThreadsN = properties.getProperty("Number of Threads");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(ThreadsN);

    }
}