package com.javabatchmanager.example.jsr;

import java.util.List;

import javax.batch.api.chunk.AbstractItemWriter;

public class SimpleItemWriter extends AbstractItemWriter{

	@Override
	public void writeItems(List<Object> items){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(items);		
	}

}
