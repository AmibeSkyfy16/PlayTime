package ch.skyfy.playtime.test;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.DeflaterOutputStream;

public class Test implements Serializable {

    @org.junit.jupiter.api.Test
    public void test() throws IOException {
        var list = new ArrayList<PlayerTime2>();
        for(int i = 0; i < 10; i++){
            list.add(new PlayerTime2(("i"+i).getBytes()){{
                var v = getOrCreateToday();
                for(int j = 0; j < 1_000_000; j++) {
                    var v2 = new ElapsedTime2("hadsdfsdfsfsfsfsfsdsds".getBytes(), "dadadadad".getBytes(), false, false, System.currentTimeMillis());
                    v.add(PlayerTimePerDay2.TimeType.WALKING, v2);
                }
            }});
        }

//        for (int i = 0; i < list.size(); i++) {
//            var item = list.get(i);
//            try (var oos = new ObjectOutputStream(new DeflaterOutputStream(new FileOutputStream(new File("C:\\test\\test"+i))))) {
//                oos.writeObject(item);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        try (var oos = new ObjectOutputStream(new DeflaterOutputStream(new FileOutputStream(new File("C:\\test\\test-byte2-true"))))) {
            oos.writeObject(list.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
