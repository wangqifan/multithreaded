import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class test {
    public static void main(String[] args) throws InterruptedException {
        Semaphore semp1 = new Semaphore(1);
        semp1.acquire();
        System.out.println(1);
        semp1.acquire();
        System.out.println(2);
        semp1.release();
        semp1.release();
        System.out.println(semp1);
    }
}
