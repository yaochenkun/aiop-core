package com.bupt.aiop.aialg;

import com.hankcs.hanlp.classification.classifiers.IClassifier;
import com.hankcs.hanlp.classification.classifiers.NaiveBayesClassifier;
import com.hankcs.hanlp.classification.models.AbstractModel;
import com.hankcs.hanlp.classification.models.NaiveBayesModel;
import org.bupt.aiop.rpcapi.dubbo.NlpAlgDubboService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/application-context.xml")
public class NlpAlgDubboServiceTest {

    @Autowired
    private NlpAlgDubboService nlpAlgDubboService;

    @Test
    public void word2VecTest() throws IOException {
        System.out.println(nlpAlgDubboService.word_2_vec("北京"));
    }

}
