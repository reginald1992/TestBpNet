import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class BpDeepTest {
    public static void main(String[] args) {
        //初始化神经网络的基本配置
        //第一个参数是一个整型数组，表示神经网络的层数和每层节点数，inputNodeNum：输入层节点数；outputNodeNum:输出层节点数
        // 比如{20,10,10,10,5}表示输入层是20个节点,输出层是5个节点，中间有3层隐含层，每层10个节点
        //第二个参数是学习步长，第三个参数是动量系数
        int inputNodeNum = 20;
        int outputNodeNum = 5;
        BpDeep bp = new BpDeep(new int[]{inputNodeNum, 10, 10, outputNodeNum}, 0.1, 0.8);

        //数据读取并分割成训练集与预测集
        Path path = Paths.get("./data/", "cv.csv");
        File file = path.toFile();
        try
                (
                        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))
                ) {
            String line = null;
            ArrayList<Double> al = new ArrayList<Double>();
            while ((line = br.readLine()) != null) {
                al.add(Double.parseDouble(line));
            }
            double[] data = new double[al.size()];
            for (int i = 0; i < data.length; ++i) {
                data[i] = al.get(i);
            }

//            overlap 数据分割间隔
//            设置样本数据：train
//            设置目标数据：target
            int overlap = inputNodeNum + outputNodeNum;
            double[][] train = new double[data.length / overlap][inputNodeNum];
            double[][] target = new double[data.length / overlap][outputNodeNum];
            for (int i = 0; i < data.length / overlap * overlap; i += overlap) {
                for (int j = i; j < i + inputNodeNum; j++) {
                    train[i / overlap][j % inputNodeNum] = data[j];
                }
                for (int j = i + inputNodeNum; j < i + overlap; j++) {
                    target[i / overlap][(j - inputNodeNum) % outputNodeNum] = data[j];
                }
            }

            //迭代训练10000次
            for (int n = 0; n < 30000; n++)
                for (int i = 0; i < train.length; i++)
                    bp.train(train[i], target[i]);

            //根据训练结果来检验样本数据
//            for (int j = 0; j < train.length; j++) {
//                double[] resultTrain = bp.computeOut(train[j]);
//                System.out.println(Arrays.toString(train[j]) + ":" + Arrays.toString(resultTrain));
//            }

            //根据训练结果来预测一条新数据
            Path path2 = Paths.get("./data/", "test8.csv");
            File file2 = path2.toFile();
            try
                    (
                            BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(file2)))
                    ) {
                String line2 = null;
                ArrayList<Double> a2 = new ArrayList<Double>();
                while ((line2 = br2.readLine()) != null) {
                    a2.add(Double.parseDouble(line2));
                }
                double[] data2 = new double[a2.size()];
                for (int i = 0; i < data2.length; ++i) {
                    data2[i] = a2.get(i);
                }

                double[] resultTest = bp.computeOut(data2);
                System.out.println("样本序列："+ Arrays.toString(data2));
                System.out.println("预测序列："+ Arrays.toString(resultTest));

                // 计算训练样本目标和实际值之间的RMSE
                Path path3 = Paths.get("./data/", "result8.csv");
                File file3 = path3.toFile();
                try
                        (
                                BufferedReader br3 = new BufferedReader(new InputStreamReader(new FileInputStream(file3)))
                        ) {
                    String line3 = null;
                    ArrayList<Double> a3 = new ArrayList<Double>();
                    while ((line3 = br3.readLine()) != null) {
                        a3.add(Double.parseDouble(line3));
                    }
                    double[] data3 = new double[a3.size()];
                    for (int i = 0; i < data3.length; ++i) {
                        data3[i] = a3.get(i);
                    }
                    System.out.println("真实序列"+ Arrays.toString(data3));
                    Rmse rmse = new Rmse();
                    System.out.println("预测序列与真实序列维数是否一致？"+ rmse.compareArrLength(data3, resultTest));
                    System.out.println("预测值与真实值的RMSE = "+ rmse.calcuRmse(data3, resultTest));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}