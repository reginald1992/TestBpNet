import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class BpDeepTest{
    public static void main(String[] args){
        //初始化神经网络的基本配置
        //第一个参数是一个整型数组，表示神经网络的层数和每层节点数，inputNodeNum：输入层节点数；outputNodeNum:输出层节点数
        // 比如{20,10,10,10,5}表示输入层是20个节点,输出层是5个节点，中间有3层隐含层，每层10个节点
        //第二个参数是学习步长，第三个参数是动量系数
        int inputNodeNum = 20;
        int outputNodeNum = 5;
        BpDeep bp = new BpDeep(new int[]{inputNodeNum,10,10,outputNodeNum}, 0.15, 0.8);

        //数据读取并分割成训练集与预测集
        Path path = Paths.get("./data/", "cv.csv");
        File file = path.toFile();
        try
                (
                        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))
                )
        {
            String line = null;
            ArrayList<Double> al=new ArrayList<Double>();
            while ((line = br.readLine()) != null)
            {
                al.add(Double.parseDouble(line));
            }
            double [] data = new double[al.size()];
            for (int i = 0; i < data.length; ++i)
            {
                data[i] = al.get(i);
            }

//            overlap 数据分割间隔
//            设置样本数据：train
//            设置目标数据：target
            int overlap = inputNodeNum + outputNodeNum;
            double[][] train = new double[data.length/overlap][inputNodeNum];
            double[][] target = new double[data.length/overlap][outputNodeNum];
            for(int i = 0; i<data.length/overlap*overlap; i += overlap) {
                for(int j = i; j<i+20; j++) {
                    train[i/25][j%20] = data[j];
                }
                for(int j = i+20;j<i+25;j++) {
                    target[i/25][(j-20)%5] = data[j];
                }
            }

        //迭代训练10000次
        for(int n=0;n<10000;n++)
            for(int i=0;i<train.length;i++)
                bp.train(train[i], target[i]);

        //根据训练结果来检验样本数据
        for(int j=0;j<train.length;j++){
            double[] result = bp.computeOut(train[j]);
            System.out.println(Arrays.toString(train[j])+":"+Arrays.toString(result));
        }

//            double[][] dataTemp = new double[data.length/inputNodeNum][20];
//            for(int i = 0;i<data.length/inputNodeNum*20;i++) {
//                dataTemp[i/20][i%20] = data[i];
//            }

        //根据训练结果来预测一条新数据的分类
//        double[] x = new double[]{0.5};
//        double[] result = bp.computeOut(x);
//        System.out.println(Arrays.toString(x)+":"+Arrays.toString(result));
    } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
