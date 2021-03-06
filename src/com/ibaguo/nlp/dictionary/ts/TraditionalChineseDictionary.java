
package com.ibaguo.nlp.dictionary.ts;

import static com.ibaguo.nlp.utility.Predefine.logger;

import com.ibaguo.nlp.MyNLP;
import com.ibaguo.nlp.collection.AhoCorasick.AhoCorasickDoubleArrayTrie;
import com.ibaguo.nlp.collection.trie.DoubleArrayTrie;


public class TraditionalChineseDictionary extends BaseChineseDictionary
{
    
    static AhoCorasickDoubleArrayTrie<String> trie = new AhoCorasickDoubleArrayTrie<String>();

    static
    {
        long start = System.currentTimeMillis();
        if (!load(MyNLP.Config.TraditionalChineseDictionaryPath, trie, false))
        {
            throw new IllegalArgumentException("繁简词典" + MyNLP.Config.TraditionalChineseDictionaryPath + "加载失败");
        }

        logger.info("繁简词典" + MyNLP.Config.TraditionalChineseDictionaryPath + "加载成功，耗时" + (System.currentTimeMillis() - start) + "ms");
    }

    public static String convertToSimplifiedChinese(String traditionalChineseString)
    {
        return segLongest(traditionalChineseString.toCharArray(), trie);
    }

    public static String convertToSimplifiedChinese(char[] traditionalChinese)
    {
        return segLongest(traditionalChinese, trie);
    }

}
