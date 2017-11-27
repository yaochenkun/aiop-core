package com.bupt.aiop.demo;

import com.hankcs.hanlp.classification.classifiers.IClassifier;
import com.hankcs.hanlp.classification.classifiers.NaiveBayesClassifier;
import com.hankcs.hanlp.classification.models.NaiveBayesModel;
import com.hankcs.hanlp.corpus.io.IOUtil;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class DemoSentimentAnalysis {

    public static final String CORPUS_FOLDER = "/home/windylee/Documents/Data/ChnSentiCorp情感分析酒店评论";

    static{
        File corpusFolder = new File(CORPUS_FOLDER);
        if(!corpusFolder.exists() || !corpusFolder.isDirectory()){
            System.out.println("语料文件不存在");
            System.exit(1);
        }
    }

    public static void main(String[] args) throws IOException {
        NaiveBayesModel model = (NaiveBayesModel) IOUtil.readObjectFrom("/home/windylee/Documents/motion.model");
        IClassifier classifier = new NaiveBayesClassifier(model);
//        classifier.train(CORPUS_FOLDER);
//        AbstractModel model = classifier.getModel();
//        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("/home/windylee/Documents/motion.model"));
//        out.writeObject(model);

        predict(classifier, "前台客房服务态度很好～早餐很丰富，房间很干净，再接再厉！");
        predict(classifier, "结果大失所望，灯光昏暗，空间极其狭小，床垫质量恶略，房间还伴着一股霉味");
        predict(classifier, "可利用文本分类实现情感分析，效果还行");
    }

    private static void predict(IClassifier classifier, String text) {
        Map<String, Double> result = classifier.predict(text);
        for(Map.Entry<String, Double> item: result.entrySet()){
            System.out.println(item.getKey() + " " + item.getValue());
        }
        System.out.printf("(%s) 情感极性是 [%s]\n", text, classifier.classify(text));
    }

}
