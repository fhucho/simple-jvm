package cz.simplejvm;


public class ExampleClass implements Comparable<String> {
    int a = 5456455;
    String b = "sasa";



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

        int array[] =new int[20];
        for(int i=0; i<12; i++) {
            array[i]=i;
        }


    }

    public int testArray(int a) {

        ExampleClass array[] =new ExampleClass[20];
        for(int i=0; i<12; i++) {
            array[i]=new ExampleClass();
        }
        return a;

    }

    public int testArray2() {

        ExampleClass array[] =new ExampleClass[20];
        for(int i=0; i<12; i++) {
            array[i]=new ExampleClass();
        }
        return 20;

    }

    public ExampleClass test() {
        testArray(45);
        return null;
    }

}

