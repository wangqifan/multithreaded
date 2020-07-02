import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Block {

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

    private static void getCondition(ReentrantLock lock, Condition condition, int no) {
        lock.lock();
        try {
            while (count != no && count2 != no && count3 != no) {
                condition.await();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        long startTime = System.currentTimeMillis();

        String file1 = "f1.txt";
        String file2 = "f2.txt";
        String file3 = "f3.txt";

        ReentrantLock lock1 = new ReentrantLock();
        Condition condition1 = lock1.newCondition();

        ReentrantLock lock2 = new ReentrantLock();
        Condition condition2 = lock2.newCondition();

        ReentrantLock lock3 = new ReentrantLock();
        Condition condition3 = lock3.newCondition();

        ReentrantLock lock4 = new ReentrantLock();
        Condition condition4 = lock4.newCondition();

        ReentrantLock lock5 = new ReentrantLock();
        Condition condition5 = lock5.newCondition();

        Thread thread1 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 3 * n; ) {
                    getCondition(lock1, condition1, 1);
                    if(count == 1) {
                        print(file1, '1');
                        count = 2;
                        Block.signalNext(lock2, condition2);
                        i++;
                    }
                    if(count2 == 1) {
                        print(file2, '1');
                        count2 = 5;
                        Block.signalNext(lock5, condition5);
                        i++;
                    }
                    if(count3 == 1) {
                        print(file3, '1');
                        count3 = 3;
                        Block.signalNext(lock3, condition3);
                        i++;
                    }

                }
            }
        };

        Thread thread2 = new Thread(){
            @Override
            public void run() {
                for(int i = 0; i < 3 * n;) {
                    getCondition(lock2, condition2, 2);
                    if(count == 2) {
                        print(file1, '2');
                        count = 3;
                        Block.signalNext(lock3, condition3);
                        i++;
                    }

                    if(count2 == 2) {
                        print(file2, '2');
                        count2 = 1;
                        Block.signalNext(lock1, condition1);
                        i++;
                    }

                    if(count3 == 2) {
                        print(file3, '2');
                        count3 = 5;
                        Block.signalNext(lock5, condition5);
                        i++;
                    }

                }
            }
        };

        Thread thread3 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 3 * n;) {
                    getCondition(lock3, condition3, 3);
                    if( count == 3 ) {
                        print(file1, '3');
                        count = 4;
                        Block.signalNext(lock4, condition4);
                        i++;
                    }

                    if(count2 == 3 ) {
                        print(file2, '3');
                        count2 = 2;
                        Block.signalNext(lock2, condition2);
                        i++;
                    }

                    if(count3  == 3) {
                        print(file3, '3');
                        count3 = 4;
                        Block.signalNext(lock4, condition4);
                        i++;
                    }
                }
            }
        };

        Thread thread4 = new Thread(){
            @Override
            public void run() {
                for(int i = 0; i < 3 * n; ) {
                    getCondition(lock4, condition4, 4);
                    if(count == 4){
                        print(file1, '4');
                        count = 5;
                        Block.signalNext(lock5, condition5);
                        i++;
                    }
                    if(count2 == 4) {
                        print(file2, '4');
                        count2 = 3;
                        Block.signalNext(lock3, condition3);
                        i++;
                    }
                    if(count3 == 4 ) {
                        print(file3, '4');
                        count3 = 2;
                        Block.signalNext(lock2, condition2);
                        i++;
                    }
                }
            }
        };

        Thread thread5 = new Thread(){
            @Override
            public void run() {
                for(int i = 0; i < 3 * n;) {
                    getCondition(lock5, condition5, 5);
                    if(count == 5) {
                        print(file1, '5');
                        count = 1;
                        Block.signalNext(lock1, condition1);
                        i++;
                    }

                    if(count2== 5) {
                        print(file2, '5');
                        count2 = 4;
                        Block.signalNext(lock4, condition4);
                        i++;
                    }
                    if(count3 == 5 ) {
                        print(file3, '5');
                        count3 = 1;
                        Block.signalNext(lock1, condition1);
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
