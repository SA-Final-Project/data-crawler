package ScoringService;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SSTracker {

    private static final int WINDOW_SIZE = 5;

    private static SSTracker instance;
    List<Integer> windowedTopic1 = new CopyOnWriteArrayList<>();
    List<Integer> windowedTopic2 = new CopyOnWriteArrayList<>();
    private SSTracker(){}

    static synchronized SSTracker getIntstance(){
        if(instance == null){
            instance = new SSTracker();
        }
        return instance;
    }

    void addToWindowTopic1(Integer value){
        while(windowedTopic1.size() >= WINDOW_SIZE){
            windowedTopic1.removeFirst();
        }
        windowedTopic1.add(value);
    }

    void addToWindowTopic2(Integer value){
        while(windowedTopic2.size() >= WINDOW_SIZE){
            windowedTopic2.removeFirst();
        }
        windowedTopic2.add(value);
    }


}
