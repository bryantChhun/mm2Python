package mm2python.DataStructures;

import java.util.concurrent.ConcurrentLinkedDeque;


/**
 * sliding window moving average
 */
public class MovingAverage {
    public static long sum;
    public static int size;
    public static ConcurrentLinkedDeque<Long> list;

    /** Initialize your data structure here. */
    public MovingAverage(int window_size) {
        list = new ConcurrentLinkedDeque<>();
        size = window_size;
    }

    public static long next(long val) {
        try {
            sum += val;
            list.offer(val);

            if (list.size() <= size) {
                return sum / list.size();
            }

            while(list.size() >= size) {
                sum -= list.remove();
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }
        return sum / list.size();

    }
}