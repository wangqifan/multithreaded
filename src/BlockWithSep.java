import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BlockWithSep {


    private static final int n = 50000;

    private volatile static int count = 1;
    private volatile static int count2 = 5;
    private volatile static int count3 = 1;


    private static void print(String filename, char ch) {
        try {
            File file =new File(filename);
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file.getName(),true);
            fileWriter.write(ch);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static  void signalNext(ReentrantLock lock, Condition condition) {
        lock.lock();
        try {
            condition.signal();
        }  finally {
            lock.unlock();
        }
    }

    private static void getCondition(Semaphore semp) {
        try {
            semp.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {

        }
    }

    public static void main(String[] args) throws InterruptedException {

        long startTime = System.currentTimeMillis();

        String file1 = "f1.txt";
        String file2 = "f2.txt";
        String file3 = "f3.txt";

        Semaphore semp1 = new Semaphore(2);
        Semaphore semp2 = new Semaphore(0);
        Semaphore semp3 = new Semaphore(0);
        Semaphore semp4 = new Semaphore(0);
        Semaphore semp5 = new Semaphore(1);


        Thread thread1 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 3 * n; ) {
                    getCondition(semp1);
                    System.out.println("thread1: " +count+ " " + count2 + " " + count3   + " " + i);
                    if(count == 1) {
                        print(file1, '1');
                        count = 2;
                        semp2.release();
                        i++;
                        continue;
                    }
                    if(count2 == 1) {
                        print(file2, '1');
                        count2 = 5;
                        semp5.release();
                        i++;
                        continue;
                    }
                    if(count3 == 1) {
                        print(file3, '1');
                        count3 = 3;
                        semp3.release();
                        i++;
                    }

                }
            }
        };

        Thread thread2 = new Thread(){
            @Override
            public void run() {
                for(int i = 0; i < 3 * n;) {
                    getCondition(semp2);
                    System.out.println("thread2: " +count + " " + count2  + " " + count3  + " " + i );
                    if(count == 2) {
                        print(file1, '2');
                        count = 3;
                        semp3.release();
                        i++;
                        continue;
                    }

                    if(count2 == 2) {
                        print(file2, '2');
                        count2 = 1;
                        semp1.release();
                        i++;
                        continue;
                    }

                    if(count3 == 2) {
                        print(file3, '2');
                        count3 = 5;
                        semp5.release();
                        i++;
                    }

                }
            }
        };

        Thread thread3 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 3 * n;) {
                    getCondition(semp3);
                    System.out.println("thread3: " +count + " " + count2  + " " + count3 + " " + i);
                    if( count == 3 ) {
                        print(file1, '3');
                        count = 4;
                        semp4.release();
                        i++;
                        continue;
                    }

                    if(count2 == 3 ) {
                        print(file2, '3');
                        count2 = 2;
                        semp2.release();
                        i++;
                        continue;
                    }

                    if(count3  == 3) {
                        print(file3, '3');
                        count3 = 4;
                        semp4.release();
                        i++;
                    }
                }
            }
        };

        Thread thread4 = new Thread(){
            @Override
            public void run() {
                for(int i = 0; i < 3 * n; ) {

                    getCondition(semp4);
                    System.out.println("thread4: " +count + " " + count2  + " " + count3  + " " + i);
                    if(count == 4){
                        print(file1, '4');
                        count = 5;
                        semp5.release();
                        i++;
                        continue;
                    }
                    if(count2 == 4) {
                        print(file2, '4');
                        count2 = 3;
                        semp3.release();
                        i++;
                        continue;
                    }
                    if(count3 == 4 ) {
                        print(file3, '4');
                        count3 = 2;
                        semp2.release();
                        i++;
                    }
                }
            }
        };

        Thread thread5 = new Thread(){
            @Override
            public void run() {
                for(int i = 0; i < 3 * n;) {
                    getCondition(semp5);
                    System.out.println("thread5: " +count + " " + count2  + " " + count3  + " " + i );
                    if(count == 5) {
                        print(file1, '5');
                        count = 1;
                        semp1.release();
                        i++;
                        continue;
                    }

                    if(count2== 5) {
                        print(file2, '5');
                        count2 = 4;
                        semp4.release();
                        i++;
                        continue;
                    }
                    if(count3 == 5 ) {
                        print(file3, '5');
                        count3 = 1;
                        semp1.release();
                        i++;
                    }

                }
            }
        };

        thread1.setName("th1");
        thread2.setName("th2");
        thread3.setName("th3");
        thread4.setName("th4");
        thread5.setName("th5");

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();

        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
        thread5.join();
        long endTime = System.currentTimeMillis();
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
}
}
