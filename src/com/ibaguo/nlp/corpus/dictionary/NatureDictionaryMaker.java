
package com.ibaguo.nlp.corpus.dictionary;

import static com.ibaguo.nlp.utility.Predefine.logger;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import com.ibaguo.nlp.corpus.document.CorpusLoader;
import com.ibaguo.nlp.corpus.document.Document;
import com.ibaguo.nlp.corpus.document.sentence.word.IWord;
import com.ibaguo.nlp.corpus.document.sentence.word.Word;
import com.ibaguo.nlp.corpus.tag.Nature;
import com.ibaguo.nlp.corpus.util.CorpusUtil;
import com.ibaguo.nlp.corpus.util.Precompiler;
import com.ibaguo.nlp.utility.Predefine;
import com.ibaguo.nlp.utility.TextUtility;


public class NatureDictionaryMaker extends CommonDictionaryMaker
{
    public NatureDictionaryMaker()
    {
        super(null);
    }

    @Override
    protected void addToDictionary(List<List<IWord>> sentenceList)
    {
        logger.info("开始制作词典");
        // 制作NGram词典
        for (List<IWord> wordList : sentenceList)
        {
            IWord pre = null;
            for (IWord word : wordList)
            {
                // 制作词性词频词典
                dictionaryMaker.add(word);
                if (pre != null)
                {
                    nGramDictionaryMaker.addPair(pre, word);
                }
                pre = word;
            }
        }
    }

    @Override
    protected void roleTag(List<List<IWord>> sentenceList)
    {
        logger.info("开始标注");
        int i = 0;
        for (List<IWord> wordList : sentenceList)
        {
            logger.info(++i + " / " + sentenceList.size());
            for (IWord word : wordList)
            {
                Precompiler.compile(word);  // 编译为等效字符串
            }
            LinkedList<IWord> wordLinkedList = (LinkedList<IWord>) wordList;
            wordLinkedList.addFirst(new Word(Predefine.TAG_BIGIN, Nature.begin.toString()));
            wordLinkedList.addLast(new Word(Predefine.TAG_END, Nature.end.toString()));
        }
    }

    
    static boolean makeCoreDictionary(String inPath, String outPath)
    {
        final DictionaryMaker dictionaryMaker = new DictionaryMaker();
        final TreeSet<String> labelSet = new TreeSet<String>();

        CorpusLoader.walk(inPath, new CorpusLoader.Handler()
        {
            @Override
            public void handle(Document document)
            {
                for (List<Word> sentence : document.getSimpleSentenceList(true))
                {
                    for (Word word : sentence)
                    {
                        if (shouldInclude(word))
                            dictionaryMaker.add(word);
                    }
                }
//                for (List<Word> sentence : document.getSimpleSentenceList(false))
//                {
//                    for (Word word : sentence)
//                    {
//                        if (shouldInclude(word))
//                            dictionaryMaker.add(word);
//                    }
//                }
            }

            
            boolean shouldInclude(Word word)
            {
                if ("m".equals(word.label) || "mq".equals(word.label) || "w".equals(word.label) || "t".equals(word.label))
                {
                    if (!TextUtility.isAllChinese(word.value)) return false;
                }
                else if ("nr".equals(word.label))
                {
                    return false;
                }

                return true;
            }
        });
        if (outPath != null)
        return dictionaryMaker.saveTxtTo(outPath);
        return false;
    }

    public static void main(String[] args)
    {
//        makeCoreDictionary("D:\\JavaProjects\\CorpusToolBox\\data\\2014", "data/dictionary/CoreNatureDictionary.txt");
//        EasyDictionary dictionary = EasyDictionary.create("data/dictionary/CoreNatureDictionary.txt");
        final NatureDictionaryMaker dictionaryMaker = new NatureDictionaryMaker();
        CorpusLoader.walk("D:\\JavaProjects\\CorpusToolBox\\data\\2014", new CorpusLoader.Handler()
        {
            @Override
            public void handle(Document document)
            {
                dictionaryMaker.compute(CorpusUtil.convert2CompatibleList(document.getSimpleSentenceList(false))); // 再打一遍不拆分的
                dictionaryMaker.compute(CorpusUtil.convert2CompatibleList(document.getSimpleSentenceList(true)));  // 先打一遍拆分的
            }
        });
        dictionaryMaker.saveTxtTo("data/test/CoreNatureDictionary");
    }
}
