package com.javabatchmanager.example.jsr;

import javax.batch.api.chunk.ItemProcessor;

public class SimpleItemProcessor implements ItemProcessor{

	@Override
	public Object processItem(Object item) throws Exception {
		item = (String) item+"procesorTest";
		return item;
	}

}
