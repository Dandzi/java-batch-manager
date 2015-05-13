package com.javabatchmanager.example.batch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.chunk.AbstractItemReader;


public class SampleItemReader extends AbstractItemReader {

    private BufferedReader reader;

    @Override
    public void open(Serializable checkpoint) throws Exception {
        reader = new BufferedReader(
                new InputStreamReader(
                    this
                    .getClass()
                    .getClassLoader()
                    .getResourceAsStream("/META-INF/mydata.csv")
                )
            );
    }

    @Override
    public String readItem() {
        try {
            return reader.readLine();
        } catch (IOException ex) {
            Logger.getLogger(SampleItemReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
