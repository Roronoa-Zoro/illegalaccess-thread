import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.illegalaccess.thread.sdk.thread.*;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xiao on 2019/12/20.
 */
public class ThreadTest {



    @Test
    public void futureTest() {
        Set<Integer> set = new HashSet<>();
        int loop = 1;
        int cnt = 0;

        for (int i = 0; i < loop; i++) {
            FutureTask<String> ft = new FutureTask<String>(() -> UUID.randomUUID().toString());
            System.out.println("ft===" + ft);
            boolean seted = set.add(ft.hashCode());
            if (!seted) {
//                System.out.println("found same toString=" + i);
//                break;
                cnt++;
            }
        }
        System.out.println(set.size() + ", cnt=" + cnt + ",=");
    }


    @Test
    public void policyTest() throws InterruptedException {
        AtomicInteger ai = new AtomicInteger(0);
        ai.incrementAndGet();
        ai.incrementAndGet();
        ai.incrementAndGet();
        ai.incrementAndGet();
        ai.incrementAndGet();
        ai.incrementAndGet();
        int i = 3;
        System.out.println("current1=" + ai.get());
        ai.getAndAdd(-i);
        System.out.println("current2=" + ai.get());
//        TracedThreadPoolExecutor es = (TracedThreadPoolExecutor) TracedExecutors.newThreadPoolExecutor("test", 1,2, 10, TimeUnit.SECONDS, 5);
////        ThreadPoolExecutor es = new ThreadPoolExecutor(1, 2, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(5));
//        for (int i = 0; i < 1; i++) {
//            Runnable r = () -> {
//                try {
//                    TimeUnit.SECONDS.sleep(1);
//                    System.out.println("hehe......");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            };
//            es.submit(tr);
//        }

//        int i = 0;
//        while (i < 50) {
//            System.out.println(es.getActiveCount() + ", " + es.getQueue().size());
//            TimeUnit.MILLISECONDS.sleep(100);
//            i++;
//        }
//        TaskLifecycleSavepoint sv = new TaskLifecycleSavepoint();
//        sv.setThreadPoolName("test");
//        MetricPipeline.Instance.emit(MetricPipeline.Instance.INNER_SAVE_POINT_QUEUE, sv);
//        TimeUnit.MINUTES.sleep(5);
//        es.shutdown();
    }


    @Test
    public void postTest() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Map<String, Object> map = new HashMap();
        System.out.println(om.writeValueAsString(map));
    }

    @Test
    public void queueTest() throws InterruptedException {
        TracedBoundedBlockingQueue<String> queue = new TracedBoundedBlockingQueue<String>();
        AtomicInteger ai = new AtomicInteger(0);
        new Thread(() -> {

            while (ai.get() < 10) {
                try {
                    System.err.println(Thread.currentThread().getName() + " will put a value");
                    queue.put(UUID.randomUUID().toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ai.incrementAndGet();
            }

        }).start();

        while (true) {
            if (ai.get() >= 5) {
                break;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        queue.enlargeQueue(15);
        System.err.println("queue is enlarged....");
        TimeUnit.MILLISECONDS.sleep(1000);
        System.out.println("counter===" + ai.get());


//        TracedBoundedBlockingQueue2<String> queue = new TracedBoundedBlockingQueue2<>("ss");
//        queue.add("aaa");
//        queue.add("bbb");
//        queue.add("ccc");
//        queue.add("ddd");
//        System.out.println("size:" + queue.size() + ", remaining:" + queue.remainingCapacity());
//        queue.poll();
//        System.out.println("size:" + queue.size() + ", remaining:" + queue.remainingCapacity());
    }


    @Test
    public void dateTest() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime after15s = now.plusSeconds(15);
        System.out.println(now);
        System.out.println(after15s);
    }

    @Test
    public void listTest() {
        CopyOnWriteArrayList<String> ss = new CopyOnWriteArrayList<>();
        ss.add("aaa");
        ss.add("bbb");

        CopyOnWriteArrayList<String> sss = new CopyOnWriteArrayList<>();
        sss.addAll(ss);
        ss.clear();
        System.out.println(sss);
    }

    @Test
    public void test() throws InterruptedException {
        ExecutorService es = TracedExecutors.newSingleThreadPoolExecutor("test");
        for (int i = 0; i < 10; i++) {
//            es.submit(() -> {
//                try {
//                    TimeUnit.SECONDS.sleep(10L);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            });

            es.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    try {
                        TimeUnit.SECONDS.sleep(10L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "hehe";
                }
            });
        }

        FutureTask<Object> ft = new FutureTask<Object>(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                try {
                    TimeUnit.SECONDS.sleep(10L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "hehe";
            }
        });

        TimeUnit.MINUTES.sleep(5L);

    }

}
