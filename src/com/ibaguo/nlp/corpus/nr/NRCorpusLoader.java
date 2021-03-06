
package com.ibaguo.nlp.corpus.nr;

import static com.ibaguo.nlp.utility.Predefine.logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.ibaguo.nlp.corpus.dictionary.DictionaryMaker;
import com.ibaguo.nlp.corpus.dictionary.item.Item;
import com.ibaguo.nlp.corpus.document.sentence.word.Word;
import com.ibaguo.nlp.corpus.tag.NR;

public class NRCorpusLoader
{
    public static boolean load(String path)
    {
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            String line;
            DictionaryMaker dictionaryMaker = new DictionaryMaker();
            while ((line = br.readLine()) != null)
            {
                if (line.matches(".*[\\p{P}+~$`^=|<>～`$^+=|<>￥×|\\s|a-z0-9A-Z]+.*")) continue;
                // 只载入两字和三字的名字
                Integer length = line.length();
                switch (length)
                {
                    case 2:
                    {
                        Word wordB = new Word(line.substring(0, 1), NR.B.toString());
                        Word wordE = new Word(line.substring(1), NR.E.toString());
                        dictionaryMaker.add(wordB);
                        dictionaryMaker.add(wordE);
                        break;
                    }
                    case 3:
                    {
                        Word wordB = new Word(line.substring(0, 1), NR.B.toString());
                        Word wordC = new Word(line.substring(1, 2), NR.C.toString());
                        Word wordD = new Word(line.substring(2, 3), NR.D.toString());
                        dictionaryMaker.add(wordB);
                        dictionaryMaker.add(wordC);
                        dictionaryMaker.add(wordD);
                        break;
                    }
                    default:
//                        L.trace("放弃【{}】", line);
                        break;
                }
            }
            br.close();
            logger.info(dictionaryMaker.toString());
            dictionaryMaker.saveTxtTo("data/dictionary/person/name.txt", new DictionaryMaker.Filter()
            {
                @Override
                public boolean onSave(Item item)
                {
                    return false;
                }
            });
        }
        catch (Exception e)
        {
            logger.warning("读取" + path + "发生错误");
            return false;
        }

        return true;
    }

    public static void main(String[] args)
    {
//        NRCorpusLoader.load("data/corpus/name.txt");
        combine();
    }

    public static void combine()
    {
        DictionaryMaker dictionaryMaker = DictionaryMaker.combine(new String[]{
                "data/dictionary/person/nr.txt",
//                "data/dictionary/person/name.txt",
                "data/dictionary/person/authornames.txt",
//                "data/dictionary/person/ansj_person_out.txt",
        });
        dictionaryMaker.saveTxtTo("data/dictionary/person/nr.txt");
    }
}
