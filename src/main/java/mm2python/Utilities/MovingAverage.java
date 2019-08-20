package mm2python.Utilities;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


/**
 * sliding window moving average
 */
public class MovingAverage {
    private static volatile AtomicLong sum;
    private static volatile AtomicLong mem_sum;
    private static volatile AtomicLong hash_sum;
    private static volatile AtomicLong queue_sum;
    private static volatile AtomicInteger counter;

    static {
        sum = new AtomicLong();
        mem_sum = new AtomicLong();
        hash_sum = new AtomicLong();
        queue_sum = new AtomicLong();
        counter = new AtomicInteger();
    }

    public static long next(long val, String type) {
        AtomicLong s = null;
        switch(type){
            case("sum"):
                s = sum;
                break;
            case("mem_sum"):
                s = mem_sum;
                break;
            case("hash_sum"):
                s = hash_sum;
                break;
            case("queue_sum"):
                s = queue_sum;
                break;
        }
        try {
            s.getAndAdd(val);
            counter.getAndAdd(1);

            return s.get()/counter.get();

        } catch (Exception ex) {
            System.out.println(ex);
        }
        System.out.println("UNABLE TO UPDATE ATOMIC MOVING AVERAGE");
        return s.get()/counter.get();
    }

    public static void reset() {
        sum.set(0);
        mem_sum.set(0);
        hash_sum.set(0);
        queue_sum.set(0);
        counter.set(0);
    }
}