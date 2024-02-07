package ScoringService;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SSHelper {
    private SSHelper(){}

    public static int getXORValue(List<Integer> window1, List<Integer> window2){
        List<Integer> XORvalue = new ArrayList<>();

        int minSize = Math.min(window1.size(), window2.size());

        for(int i = 0; i< minSize; i++){
            if(Objects.equals(window1.get(i), window2.get(i))){
                XORvalue.add(1);
            }else{
                XORvalue.add(-1);
            }
        }

        return XORvalue.stream().reduce(Integer::sum).orElse(0);
    }
}
