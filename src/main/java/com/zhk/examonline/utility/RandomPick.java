package com.zhk.examonline.utility;

import java.util.Collections;
import java.util.List;

public class RandomPick {

    public static <T> List<T> randomPicker(List<T> list,int num){
        int length=list.size();
        if(num>length||num<=0)
            return null;
        for(int i=length-1;i>=length-num;i--){
            Collections.swap(list,i,(int)Math.floor(Math.random()*(i+1)));
        }
        return list.subList(length-num,length);
    }

}
