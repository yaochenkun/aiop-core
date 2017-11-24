package com.bupt.aiop.aialg;

import org.bupt.aiop.rpcapi.dubbo.NlpAlgDubboService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/application-context.xml")
public class NlpAlgDubboServiceTest {

    @Autowired
    private NlpAlgDubboService nlpAlgDubboService;

    @Test
    public void word2VecTest(){
        System.out.println(nlpAlgDubboService.word_2_vec("北京"));
    }

}
