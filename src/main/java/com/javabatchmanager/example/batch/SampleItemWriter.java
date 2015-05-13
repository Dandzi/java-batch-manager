package com.javabatchmanager.example.batch;

import java.util.List;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class SampleItemWriter extends AbstractItemWriter {
    
    @PersistenceContext
    EntityManager em;

    @Override
    public void writeItems(List list) {
        System.out.println("writeItems: " + list);
        for (Object person : list) {
            em.persist(person);
        }
    }
}
