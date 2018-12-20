package org.bupt.aiop.aialg;

import com.hankcs.hanlp.classification.classifiers.NaiveBayesClassifier;
import com.hankcs.hanlp.classification.models.NaiveBayesModel;
import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.mining.word2vec.DocVectorModel;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class AppConfig {

    /**
     * nlp模型
     */

    @Value("${model.nlp.vec}")
    private String vecModelPath;

    @Value("${model.nlp.motion2}")
    private String motionClassifierModelPath2;

    @Value("${model.nlp.motion5}")
    private String motionClassifierModelPath5;

    @Value("${model.nlp.category}")
    private String categoryClassifierModelPath;

    @Bean(name = "wordVectorModel")
    public WordVectorModel getWordVectorModel() throws IOException {
        return new WordVectorModel(vecModelPath);
    }

    @Bean(name = "motionClassifierModel2")
    public NaiveBayesClassifier getMotionClassifierModel2(){
        return new NaiveBayesClassifier((NaiveBayesModel) IOUtil.readObjectFrom(motionClassifierModelPath2));
    }

    @Bean(name = "motionClassifierModel5")
    public NaiveBayesClassifier getMotionClassifierModel5(){
        return new NaiveBayesClassifier((NaiveBayesModel)IOUtil.readObjectFrom(motionClassifierModelPath5));
    }

    @Bean(name = "categoryClassifierModel")
    public NaiveBayesClassifier getCategoryClassifierModel(){
        return new NaiveBayesClassifier((NaiveBayesModel) IOUtil.readObjectFrom(categoryClassifierModelPath));
    }

    @Bean(name = "docVectorModel")
    @Autowired
    public DocVectorModel getDocVectorModel(WordVectorModel wordVectorModel){
        return new DocVectorModel(wordVectorModel);
    }

}
