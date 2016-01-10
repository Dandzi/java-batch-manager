package com.javabatchmanager.example.jsr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.batch.api.chunk.AbstractItemReader;

public class SimpleItemReader extends AbstractItemReader{
	private Set<String> simpleWords = new HashSet<>(Arrays.asList("word","anotherWord","testWord",
														"simpleWord", "loooooongWord","shrtWrd"));
	@Override
	public Object readItem() throws Exception {
		if(!simpleWords.isEmpty()){
			String word = simpleWords.iterator().next();
			simpleWords.remove(word);
			return word;
		}
		return null;
	}

}
