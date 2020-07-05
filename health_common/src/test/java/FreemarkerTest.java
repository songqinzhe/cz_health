import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模板+数据=文本
 */
public class FreemarkerTest {

    public static void main(String[] args) throws Exception{
        //1拿模板
        Configuration configuration=new Configuration(Configuration.getVersion());
        configuration.setDefaultEncoding("utf-8");
        configuration.setDirectoryForTemplateLoading(new File("d:/ftl"));
        Template template = configuration.getTemplate("test.ftl");

        //2拿数据 业务逻辑
        Map<String, Object> map=new HashMap<>();
        map.put("name", "太原黑马16期");
        map.put("message", "大家工资过万不是梦！");
        map.put("success", true);

        List goodsList=new ArrayList();

        Map goods1=new HashMap();
        goods1.put("name", "苹果");
        goods1.put("price", 5.8);

        Map goods2=new HashMap();
        goods2.put("name", "香蕉");
        goods2.put("price", 2.5);

        Map goods3=new HashMap();
        goods3.put("name", "橘子");
        goods3.put("price", 3.2);

        goodsList.add(goods1);
        goodsList.add(goods2);
        goodsList.add(goods3);

        map.put("goodsList", goodsList);

        //3生成文本
        FileWriter fileWriter = new FileWriter(new File("d:/ftl/test.html"));
        template.process(map,fileWriter);
        fileWriter.close();

    }

    public static void genHtml(String templateName,Map map,String fileName) throws Exception{
        //1拿模板
        Configuration configuration=new Configuration(Configuration.getVersion());
        configuration.setDefaultEncoding("utf-8");
        configuration.setDirectoryForTemplateLoading(new File("d:/ftl"));
        Template template = configuration.getTemplate(templateName);

        //2拿数据 业务逻辑
        // Map<String, String> map=new HashMap<>();
        // map.put("name", "太原黑马16期");
        // map.put("message", "大家工资过万不是梦！");

        //3生成文本
        FileWriter fileWriter = new FileWriter(new File("d:/ftl/"+fileName));
        template.process(map,fileWriter);
        fileWriter.close();
    }
}
