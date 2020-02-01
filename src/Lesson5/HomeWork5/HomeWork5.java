package Lesson5.HomeWork5;

public class HomeWork5 {

    static final int size = 10000000;
    static final int h = size / 2;


    public static void main(String[] args) {
        //firstMethod();
        //secondMethod();

        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
                firstMethod();
            }
        };
        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
               secondMethod();
            }
        };
        new Thread(r1,"Run1").start();
        new Thread(r2,"Run2").start();
    }

    private synchronized static void firstMethod() {
        float[] arr = new float[size];
        for (int i = 0; i < size; i++) {
            arr[i] = 1;
        }
        long a = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            arr[i] = (float) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        System.out.println("First method=" + (System.currentTimeMillis() - a));
    }

    private synchronized static void secondMethod() {
        float[] arr = new float[size];
        for (int i = 0; i < size; i++) {
            arr[i] = 1;
        }
        long a = System.currentTimeMillis();
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());

                Runnable r2 = getRunnableHalfArr(arr, a, 0);
                Runnable r3 = getRunnableHalfArr(arr, a, h);

                Thread t2 = new Thread(r2, "First half arr");
                Thread t3 = new Thread(r3, "Second half arr");

                t2.start();
                t3.start();

                waitThread(t2);
                waitThread(t3);
            }
        };
        Thread t1 = new Thread(r1, "Start Second method");
        t1.start();
        waitThread(t1);

        System.out.println("Second method" + " = " + (System.currentTimeMillis() - a));

    }

    private static void waitThread(Thread t) {
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            t.interrupt();
        }
    }

    private static Runnable getRunnableHalfArr(float[] arr, long a, int i2) {
        return new Runnable() {
            @Override
            public void run() {
                System.out.println("Start " + Thread.currentThread().getName());
                float[] a1 = new float[h];
                System.arraycopy(arr, i2, a1, 0, h);
                for (int i = 0; i < h; i++) {
                    a1[i] = (float) (a1[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                }
                System.arraycopy(a1, 0, arr, i2, h);
                System.out.println("Stop " + Thread.currentThread().getName() + " = " + (System.currentTimeMillis() - a));
            }
        };
    }
}
