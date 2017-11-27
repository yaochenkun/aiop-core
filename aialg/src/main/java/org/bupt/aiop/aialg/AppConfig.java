package org.bupt.aiop.aialg;

import com.hankcs.hanlp.classification.classifiers.NaiveBayesClassifier;
import com.hankcs.hanlp.classification.models.NaiveBayesModel;
import com.hankcs.hanlp.corpus.io.IOUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
public class AppConfig {

    @Value("${model.motion}")
    private String motionPath;
    @Value("${model.category}")
    private String classifierPath;

    @Bean(name = "motionClassifier")
    public NaiveBayesClassifier getMotionClassifier(){
//        System.out.println(motionPath);
        NaiveBayesModel model = (NaiveBayesModel) IOUtil.readObjectFrom(motionPath);
        NaiveBayesClassifier classifier = new NaiveBayesClassifier(model);
        return classifier;
    }

    @Bean(name = "categoryClassifier")
    public NaiveBayesClassifier getCategoryClassifier(){
//        System.out.println(classifierPath);
        NaiveBayesModel model = (NaiveBayesModel) IOUtil.readObjectFrom(classifierPath);
        NaiveBayesClassifier classifier = new NaiveBayesClassifier(model);
        return classifier;
    }

}
