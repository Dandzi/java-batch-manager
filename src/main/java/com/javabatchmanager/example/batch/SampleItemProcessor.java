package com.javabatchmanager.example.batch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

import javax.batch.api.chunk.ItemProcessor;



public class SampleItemProcessor implements ItemProcessor {
    SimpleDateFormat format = new SimpleDateFormat("M/dd/yy");

    public Person processItem(Object t) {
        System.out.println("processItem: " + t);
        
        StringTokenizer tokens = new StringTokenizer((String)t, ",");

        String name = tokens.nextToken();
        String date;
        
        try {
            date = tokens.nextToken();
            format.setLenient(false);
            format.parse(date);
        } catch (ParseException e) {
            return null;
        }
        
        return new Person(name, date);
    }

}
