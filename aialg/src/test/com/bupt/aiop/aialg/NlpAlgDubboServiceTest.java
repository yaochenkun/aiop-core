package com.bupt.aiop.aialg;

import org.bupt.aiop.rpcapi.dubbo.NlpAlgDubboService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.OutputStream;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/application-context.xml")
public class NlpAlgDubboServiceTest {

    @Autowired
    private NlpAlgDubboService nlpAlgDubboService;

    @Test
    public void textKeywordTest() {
        String content = "程序员(英文Programmer)是从事程序开发、维护的专业人员。一般将程序员分为程序设计人员和程序编码人员，但两者的界限并不非常清楚，特别是在中国。软件从业人员分为初级程序员、高级程序员、系统分析员和项目经理四大类。";
        String result = nlpAlgDubboService.text_keywords(content, 5);
        System.out.println(result);
    }

    @Test
    public void textSummariesTest() {
        String document = "算法可大致分为基本算法、数据结构的算法、数论算法、计算几何的算法、图的算法、动态规划以及数值分析、加密算法、排序算法、检索算法、随机化算法、并行算法、厄米变形模型、随机森林算法。\n" +
                "算法可以宽泛的分为三类，\n" +
                "一，有限的确定性算法，这类算法在有限的一段时间内终止。他们可能要花很长时间来执行指定的任务，但仍将在一定的时间内终止。这类算法得出的结果常取决于输入值。\n" +
                "二，有限的非确定算法，这类算法在有限的时间内终止。然而，对于一个（或一些）给定的数值，算法的结果并不是唯一的或确定的。\n" +
                "三，无限的算法，是那些由于没有定义终止定义条件，或定义的条件无法由输入的数据满足而不终止运行的算法。通常，无限算法的产生是由于未能确定的定义终止条件。";
        String result = nlpAlgDubboService.text_summaries(document, 3);
        System.out.println(result);
    }

    @Test
    public void textPhraseTest() {
        String text = "算法工程师\n" +
                "算法（Algorithm）是一系列解决问题的清晰指令，也就是说，能够对一定规范的输入，在有限时间内获得所要求的输出。如果一个算法有缺陷，或不适合于某个问题，执行这个算法将不会解决这个问题。不同的算法可能用不同的时间、空间或效率来完成同样的任务。一个算法的优劣可以用空间复杂度与时间复杂度来衡量。算法工程师就是利用算法处理事物的人。\n" +
                "\n" +
                "1职位简介\n" +
                "算法工程师是一个非常高端的职位；\n" +
                "专业要求：计算机、电子、通信、数学等相关专业；\n" +
                "学历要求：本科及其以上的学历，大多数是硕士学历及其以上；\n" +
                "语言要求：英语要求是熟练，基本上能阅读国外专业书刊；\n" +
                "必须掌握计算机相关知识，熟练使用仿真工具MATLAB等，必须会一门编程语言。\n" +
                "\n" +
                "2研究方向\n" +
                "视频算法工程师、图像处理算法工程师、音频算法工程师 通信基带算法工程师\n" +
                "\n" +
                "3目前国内外状况\n" +
                "目前国内从事算法研究的工程师不少，但是高级算法工程师却很少，是一个非常紧缺的专业工程师。算法工程师根据研究领域来分主要有音频/视频算法处理、图像技术方面的二维信息算法处理和通信物理层、雷达信号处理、生物医学信号处理等领域的一维信息算法处理。\n" +
                "在计算机音视频和图形图像技术等二维信息算法处理方面目前比较先进的视频处理算法：机器视觉成为此类算法研究的核心；另外还有2D转3D算法(2D-to-3D conversion)，去隔行算法(de-interlacing)，运动估计运动补偿算法(Motion estimation/Motion Compensation)，去噪算法(Noise Reduction)，缩放算法(scaling)，锐化处理算法(Sharpness)，超分辨率算法(Super Resolution),手势识别(gesture recognition),人脸识别(face recognition)。\n" +
                "在通信物理层等一维信息领域目前常用的算法：无线领域的RRM、RTT，传送领域的调制解调、信道均衡、信号检测、网络优化、信号分解等。\n" +
                "另外数据挖掘、互联网搜索算法也成为当今的热门方向。\n" +
                "算法工程师逐渐往人工智能方向发展。";
        String result = nlpAlgDubboService.text_phrases(text, 10);
        System.out.println(result);
    }

    @Test
    public void wordPosTest() {

        String[] texts = new String[]{
                "中国科学院计算技术研究所的宗成庆教授正在教授自然语言处理课程",
                "北川景子参演了林诣彬导演的《速度与激情3》",
                "蓝翔给宁夏固原市彭阳县红河镇黑牛沟村捐赠了挖掘机",
                "我经常在台川喜宴餐厅吃饭，",
                "一桶冰水当头倒下，微软的比尔盖茨、Facebook的扎克伯格跟桑德博格、亚马逊的贝索斯、苹果的库克全都不惜湿身入镜，这些硅谷的科技人，飞蛾扑火似地牺牲演出，其实全为了慈善。",
                "签约仪式前，秦光荣、李纪恒、仇和等一同会见了参加签约的企业家。",
                "我在上海林原科技有限公司兼职工作",
                "偶尔去地中海影城看电影。"
        };
        for (String text : texts) {
            String result = nlpAlgDubboService.word_pos(text, true, true, true);
            System.out.println(result);
            result = nlpAlgDubboService.word_pos_normal(text);
            System.out.println(result);
        }
    }

    @Test
    public void wordNerTest(){
        String[] texts = new String[]{
                "中国科学院计算技术研究所的宗成庆教授正在教授自然语言处理课程",
                "北川景子参演了林诣彬导演的《速度与激情3》",
                "蓝翔给宁夏固原市彭阳县红河镇黑牛沟村捐赠了挖掘机",
                "我经常在台川喜宴餐厅吃饭，",
                "一桶冰水当头倒下，微软的比尔盖茨、Facebook的扎克伯格跟桑德博格、亚马逊的贝索斯、苹果的库克全都不惜湿身入镜，这些硅谷的科技人，飞蛾扑火似地牺牲演出，其实全为了慈善。",
                "签约仪式前，秦光荣、李纪恒、仇和等一同会见了参加签约的企业家。",
                "我在上海林原科技有限公司兼职工作",
                "偶尔去地中海影城看电影。"
        };
        for (String text : texts) {
            String result = nlpAlgDubboService.word_ner(text);
            System.out.println(result);
        }
    }

    @Test
    public void word2PinyinTest() {
        String text = "重载不是重任, 声母不是韵母";
        System.out.println(nlpAlgDubboService.word_2_pinyin(text));
    }

    @Test
    public void simplified2TraditionalTest() {
        String text = "用笔记本电脑写程序";
        System.out.println(nlpAlgDubboService.simplified_2_traditional(text));
    }

    @Test
    public void traditional2SimplifiedTest() {
        String text = "以後等妳當上皇后，就能買士多啤梨慶祝了";
        System.out.println(nlpAlgDubboService.traditional_2_simplified(text));
    }

    @Test
    public void motionClassifyTest() {
        String[] texts = new String[]{
                "前台客房服务态度非常好！早餐很丰富，房价很干净。再接再厉！",
                "结果大失所望，灯光昏暗，空间极其狭小，床垫质量恶劣，房间还伴着一股霉味。",
                "可利用文本分类实现情感分析，效果还行"
        };
        for (String text : texts) {
            String result = nlpAlgDubboService.motion_classify_2(text);
            System.out.println(result);
        }
    }

    @Test
    public void motionClassifyTest2(){
        String[] texts = new String[]{
                "金天心情很好，咱们出去郊游吧",
                "本来想出去玩，结果遇到这么烦心的事情，没了心情",
                "生活一点意义都没有，还不如死了算了"
        };
        for(String text:texts){
            String result = nlpAlgDubboService.motion_classify_5(text);
            System.out.println(result);
        }
    }

    @Test
    public void categoryClassifyTest() {
        String[] texts = new String[]{
                "NBA-哈登31分火箭大胜",
                "英国造航母耗时8年仍未服役 被中国速度远远甩在身后",
                "研究生考录模式亟待进一步专业化",
                "如果真想用食物解压,建议可以食用燕麦",
                "通用及其部分竞争对手目前正在考虑解决库存问题"
        };
        for (String text : texts) {
            System.out.println(nlpAlgDubboService.category_classify(text));
        }
    }

    @Test
    public void word2VecTest() throws IOException {
        System.out.println(nlpAlgDubboService.word_2_vec("北京"));
    }

    @Test
    public void wordSimTest() {
        System.out.println(nlpAlgDubboService.word_sim("美国", "中国"));
        System.out.println(nlpAlgDubboService.word_sim("美丽", "漂亮"));
        System.out.println(nlpAlgDubboService.word_sim("购买", "计算机"));
    }

    @Test
    public void docSimTest() {
        String[] documents = new String[]{
                "山东苹果丰收",
                "农民在江苏种水稻",
                "奥运会女排夺冠",
                "世界锦标赛胜出",
                "中国足球失败",
        };
        System.out.println(nlpAlgDubboService.document_sim(documents[0], documents[1]));
        System.out.println(nlpAlgDubboService.document_sim(documents[0], documents[2]));
    }

    @Test
    public void dependencyParserTest(){
        System.out.println(nlpAlgDubboService.dependency_parse("徐先生还具体帮助他确定了把画雄鹰、松鼠和麻雀作为主攻目标。"));
        System.out.println(nlpAlgDubboService.dependency_parse("它熟悉一个民族的历史"));
    }

}
