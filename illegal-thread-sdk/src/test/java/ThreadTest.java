import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.illegalaccess.thread.sdk.thread.NamedBoundedBlockingQueue;
import com.illegalaccess.thread.sdk.thread.NamedExecutors;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by xiao on 2019/12/20.
 */
public class ThreadTest {

    @Test
    public void postTest() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Map<String, Object> map = new HashMap();
        System.out.println(om.writeValueAsString(map));
    }

    @Test
    public void queueTest() {
        NamedBoundedBlockingQueue<String> queue = new NamedBoundedBlockingQueue<>("ss");
        queue.add("aaa");
        queue.add("bbb");
        queue.add("ccc");
        queue.add("ddd");
        System.out.println("size:" + queue.size() + ", remaining:" + queue.remainingCapacity());
        queue.poll();
        System.out.println("size:" + queue.size() + ", remaining:" + queue.remainingCapacity());
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
        ExecutorService es = NamedExecutors.newSingleThreadPoolExecutor("test");
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
