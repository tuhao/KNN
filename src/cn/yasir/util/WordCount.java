package cn.yasir.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;


public class WordCount {
	
	public static void chineseCharacterWordCount(Map<String,Integer> wordCountMap,String text){
		StringReader line = new StringReader(text);
		IKSegmenter segment = new IKSegmenter(line,true);
		try {
			for(Lexeme lexeme = segment.next();lexeme != null;lexeme=segment.next()){
				String word = lexeme.getLexemeText();
				if (!CharacterEncoding.isChinese(word)) continue;
				if(wordCountMap.get(word) != null){
					wordCountMap.put(word, wordCountMap.get(word) + 1);
				}else{
					wordCountMap.put(word, 1);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
