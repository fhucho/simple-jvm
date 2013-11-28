package cz.simplejvm;


public class ExampleClass implements Comparable<String> {



    @Override
    public int compareTo(String o) {
        int a=1;
        int b=2;
        int c = a+b;
        return c;
    }

    public static void start() {
        ExampleClass ex = new ExampleClass();
        ex.compareTo("oo");
    }

}

