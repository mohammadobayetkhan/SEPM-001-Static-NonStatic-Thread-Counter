import java.util.concurrent.atomic.AtomicLong;


public class Obayet_Thread implements Runnable {

   
    private static final AtomicLong atomicStaticCounter = new AtomicLong(0);

    private static long unsafeStaticCounter = 0;

    private long instanceCounter = 0;

    private final long increments;
    private final boolean threadSafe;

    public Obayet_Thread(long increments, boolean threadSafe) {
        this.increments = increments;
        this.threadSafe = threadSafe;
    }

    @Override
    public void run() {
        for (long i = 0; i < increments; i++) {
            instanceCounter++; 

            if (threadSafe) {
                atomicStaticCounter.incrementAndGet();
            } else {
                unsafeStaticCounter++;             }
        }
    }

    public long getInstanceCounter() {
        return instanceCounter;
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 3) {
            System.out.println("Usage: java Obayet_Thread <threads> <incrementsPerThread> <true|false>");
            return;
        }

        int threads;
        long increments;
        try {
            threads = Integer.parseInt(args[0]);
            increments = Long.parseLong(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Threads and increments must be integers.");
            return;
        }
        boolean threadSafe = Boolean.parseBoolean(args[2]);

        if (threads < 0 || increments < 0) {
            System.out.println("Threads and increments must be non-negative.");
            return;
        }

        // Reset static counters
        atomicStaticCounter.set(0);
        unsafeStaticCounter = 0;

        Obayet_Thread[] tasks = new Obayet_Thread[threads];
        Thread[] workers = new Thread[threads];

        for (int i = 0; i < threads; i++) {
            tasks[i] = new Obayet_Thread(increments, threadSafe); 
            workers[i] = new Thread(tasks[i], "Worker-" + i);
        }

        for (Thread t : workers) t.start();
        for (Thread t : workers) t.join();

        long expected = (long) threads * increments;
        long staticCount = threadSafe ? atomicStaticCounter.get() : unsafeStaticCounter;

        long nonStaticTotal = 0;
        for (Obayet_Thread task : tasks) {
            nonStaticTotal += task.getInstanceCounter();
        }

        long absDiff = Math.abs(staticCount - nonStaticTotal);

        String percentDiff;
        if (nonStaticTotal == 0) {
            percentDiff = (staticCount == 0) ? "0.00%" : "undefined";
        } else {
            percentDiff = String.format("%.4f%%", (absDiff * 100.0) / nonStaticTotal);
        }

        System.out.println("Mode                     : " + (threadSafe ? "Thread-safe (AtomicLong)" : "Unsynchronized (static long)"));
        System.out.println("Threads (T)              : " + threads);
        System.out.println("Increments per thread (N): " + increments);
        System.out.println("Expected count (T x N)   : " + expected);
        System.out.println("Static count             : " + staticCount);
        System.out.println("Non-static total         : " + nonStaticTotal);
        System.out.println("Absolute difference      : " + absDiff);
        System.out.println("Percentage difference    : " + percentDiff);
    }
}