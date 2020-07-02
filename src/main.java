

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;



public class main {

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

    public static void main(String[] args) throws InterruptedException {

      long startTime = System.currentTimeMillis();

      String file1 = "f1.txt";
      String file2 = "f2.txt";
      String file3 = "f3.txt";

      Thread thread1 = new Thread() {
          @Override
          public void run() {
              for (int i = 0; i < 3 * n; ) {
                  while (count != 1 && count2 != 1 && count3 != 1) {

                  }
                  System.out.println("thread1: " +count+ " " + count2 + " " + count3   + " " + i);
                  if(count == 1) {
                      print(file1, '1');
                      count = 2;
                      i++;
                  }
                 if(count2 == 1) {
                      print(file2, '1');
                      count2 = 5;
                      i++;
                  }
                  if(count3 == 1) {
                      print(file3, '1');
                      count3 = 3;
                      i++;
                  }

              }
          }
      };

      Thread thread2 = new Thread(){
            @Override
            public void run() {
                for(int i = 0; i < 3 * n;) {
                    while (count != 2 && count2 != 2 && count3 != 2) {

                    }
                    System.out.println("thread2: " +count + " " + count2  + " " + count3  + " " + i );
                    if(count == 2) {
                        print(file1, '2');
                        count = 3;
                        i++;
                    }

                    if(count2 == 2) {
                        print(file2, '2');
                        count2 = 1;
                        i++;
                    }

                    if(count3 == 2) {
                        print(file3, '2');
                        count3 = 5;
                        i++;
                    }

                }
            }
        };

        Thread thread3 = new Thread() {
           @Override
           public void run() {
               for (int i = 0; i < 3 * n;) {
                   while (count != 3 && count2 != 3 && count3 != 3) {

                   }
                   System.out.println("thread3: " +count + " " + count2  + " " + count3 + " " + i);
                   if( count == 3 ) {
                       print(file1, '3');
                       count = 4;
                       i++;
                   }

                   if(count2 == 3 ) {
                       print(file2, '3');
                       count2 = 2;
                       i++;
                   }

                   if(count3  == 3) {
                       print(file3, '3');
                       count3 = 4;
                       i++;
                   }
               }
           }
       };

        Thread thread4 = new Thread(){
            @Override
            public void run() {
                for(int i = 0; i < 3 * n; ) {
                    while (count != 4 && count2 != 4 && count3 != 4 ) {

                    }
                    System.out.println("thread4: " +count + " " + count2  + " " + count3  + " " + i);
                    if(count == 4){
                        print(file1, '4');
                        count = 5;
                        i++;
                    }
                    if(count2 == 4) {
                        print(file2, '4');
                        count2 = 3;
                        i++;
                    }
                    if(count3 == 4 ) {
                        print(file3, '4');
                        count3 = 2;
                        i++;
                    }
                }
            }
        };

        Thread thread5 = new Thread(){
            @Override
            public void run() {
                for(int i = 0; i < 3 * n;) {
                    while (count != 5 && count2 != 5 && count3 != 5) {

                    }
                    System.out.println("thread5: " +count + " " + count2  + " " + count3  + " " + i );
                    if(count == 5) {
                        print(file1, '5');
                        count = 1;
                        i++;
                    }

                    if(count2== 5) {
                        print(file2, '5');
                        count2 = 4;
                        i++;
                    }
                    if(count3 == 5 ) {
                        print(file3, '5');
                        count3 = 1;
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