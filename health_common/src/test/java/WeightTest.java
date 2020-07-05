import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeightTest {

    public static void main(String[] args){

        //测试数据
        List<WeightCategory> categoryList=new ArrayList<>();
        WeightCategory weightCategory1=new WeightCategory("一等奖",10);
        WeightCategory weightCategory2=new WeightCategory("二等奖",20);
        WeightCategory weightCategory3=new WeightCategory("三等奖",30);
        WeightCategory weightCategory4=new WeightCategory("四等奖",40);
        categoryList.add(weightCategory1);
        categoryList.add(weightCategory2);
        categoryList.add(weightCategory3);
        categoryList.add(weightCategory4);

        String result="";
        int a1=0,a2=0,a3=0,a4=0;
        for (int i=0;i<100;i++){
            result = getWeight(categoryList);
            System.out.println(i+"  开奖结果： "+result);
            if(result.equals("一等奖")){
                a1++;
            }
            else if(result.equals("二等奖")){
                a2++;
            }
            else if(result.equals("三等奖")){
                a3++;
            }
            else if(result.equals("四等奖")){
                a4++;
            }
        }

        System.out.println("一等奖共出现 "+a1);
        System.out.println("二等奖共出现 "+a2);
        System.out.println("三等奖共出现 "+a3);
        System.out.println("四等奖共出现 "+a4);

    }




    /**
     * 权重获取方法
     * @param categorys
     * @return
     */
    public static String getWeight(List<WeightCategory> categorys) {
        Integer weightSum = 0;
        String result=null;
        for (WeightCategory wc : categorys) {
            weightSum += wc.getWeight();
        }

        if (weightSum <= 0) {
            System.err.println("Error: weightSum=" + weightSum.toString());
            return result;
        }
        Random random = new Random();
        Integer n = random.nextInt(weightSum); // n in [0, weightSum)
        Integer m = 0;
        for (WeightCategory wc : categorys) {
            if (m <= n && n < m + wc.getWeight()) {
                result=wc.getCategory();
                break;
            }
            m += wc.getWeight();
        }
        return result;
    }


}

class WeightCategory{

    private String category;//类别
    private int weight;//权重值

    public WeightCategory(String category, int weight) {
        this.category = category;
        this.weight = weight;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
