package com.bupt.aiop.demo;

import com.hankcs.hanlp.classification.classifiers.IClassifier;
import com.hankcs.hanlp.classification.classifiers.NaiveBayesClassifier;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

public class DemoTextClassification {

    public static final String CORPUS_FOLDER = "/home/windylee/Documents/Data/THUCTC/THUCNews/THUCNews";
//
//    static{
//        File file = new File(CORPUS_FOLDER);
//        if(!file.exists()||!file.isDirectory()){
//            System.out.println("语料文件不存在");
//            System.exit(1);
//        }
//    }

    public static void main(String[] args) {
//        NaiveBayesModel model = (NaiveBayesModel) IOUtil.readObjectFrom("/home/windylee/Documents/classifier.model");
        IClassifier classifier = new NaiveBayesClassifier();
        try {
            classifier.train(CORPUS_FOLDER);
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("/home/windylee/Documents/classifier.model"));
            out.writeObject(classifier.getModel());
        }catch (Exception e){
            e.printStackTrace();
        }

        predict(classifier, "小米更新了新的系统，新系统支持免费拨打电话");
        predict(classifier, "《清明上河图》是一幅文学价值极高的史诗画作");
        predict(classifier, "NBA-哈登31分火箭大胜");
        predict(classifier, "英国造航母耗时9年仍未服役 被中国速度圆圆摔在身后");
        predict(classifier, "研究生考录模式亟待进一步专业化");
        predict(classifier, "如果真想用食物解压，建议可以使用燕麦");
        predict(classifier, "通用及其部分竞争对手目前正在考虑解决库存问题");
    }

    private static void predict(IClassifier classifier, String text) {
        Map<String, Double> result = classifier.predict(text);
        for(Map.Entry<String, Double> item: result.entrySet()){
            System.out.println(item.getKey() + " " + item.getValue());
        }
        System.out.printf("(%s) 属于分类 [%s]\n", text, classifier.classify(text));
    }

}
