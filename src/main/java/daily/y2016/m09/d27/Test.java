package daily.y2016.m09.d27;

public class Test {
    public static int a() {
        int i = 0;

        try {
            i++;
            return ++i;

        } finally {
            i++;
            System.out.println("finally:" + i);
        }
    }

    public static void main(String[] args) {
        System.out.println(a());
    }
}