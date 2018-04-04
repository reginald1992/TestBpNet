import java.util.Random;
public class BpDeep{
    public double[][] layer;//神经网络各层节点
    public double[][] layerErr;//神经网络各节点误差
    public double[][][] layerWeight;//各层节点权重
    public double[][][] layerWeightDelta;//各层节点权重动量
    public double mobp;//动量系数
    public double rate;//学习系数

    public BpDeep(int[] layerNum, double rate, double mobp){
        this.mobp = mobp;
        this.rate = rate;
        layer = new double[layerNum.length][];
        layerErr = new double[layerNum.length][];
        layerWeight = new double[layerNum.length][][];
        layerWeightDelta = new double[layerNum.length][][];
        Random random = new Random();
        for(int k=0;k<layerNum.length;k++){
            layer[k]=new double[layerNum[k]];
            layerErr[k]=new double[layerNum[k]];
            if(k+1<layerNum.length){
                layerWeight[k]=new double[layerNum[k]+1][layerNum[k+1]];
                layerWeightDelta[k]=new double[layerNum[k]+1][layerNum[k+1]];
                for(int j=0;j<layerNum[k]+1;j++)
                    for(int i=0;i<layerNum[k+1];i++)
                        layerWeight[k][j][i]=random.nextDouble();//随机初始化权重
            }
        }
    }
    //逐层向前计算输出
    public double[] computeOut(double[] in){
        for(int k=1;k<layer.length;k++){
            for(int j=0;j<layer[k].length;j++){
                double z= layerWeight[k-1][layer[k-1].length][j];
                for(int i=0;i<layer[k-1].length;i++){
                    layer[k-1][i]=k==1?in[i]:layer[k-1][i];
                    z+= layerWeight[k-1][i][j]*layer[k-1][i];
                }
                layer[k][j]=1/(1+Math.exp(-z));
            }
        }
        return layer[layer.length-1];
    }
    //逐层反向计算误差并修改权重
    public void updateWeight(double[] tar){
        int l=layer.length-1;
        for(int j=0;j<layerErr[l].length;j++)
            layerErr[l][j]=layer[l][j]*(1-layer[l][j])*(tar[j]-layer[l][j]);

        while(l-->0){
            for(int j=0;j<layerErr[l].length;j++){
                double z = 0.0;
                for(int i=0;i<layerErr[l+1].length;i++){
                    z=z+l>0?layerErr[l+1][i]* layerWeight[l][j][i]:0;
                    layerWeightDelta[l][j][i]= mobp* layerWeightDelta[l][j][i]+rate*layerErr[l+1][i]*layer[l][j];//隐含层动量调整
                    layerWeight[l][j][i]+= layerWeightDelta[l][j][i];//隐含层权重调整
                    if(j==layerErr[l].length-1){
                        layerWeightDelta[l][j+1][i]= mobp* layerWeightDelta[l][j+1][i]+rate*layerErr[l+1][i];//截距动量调整
                        layerWeight[l][j+1][i]+= layerWeightDelta[l][j+1][i];//截距权重调整
                    }
                }
                layerErr[l][j]=z*layer[l][j]*(1-layer[l][j]);//记录误差
            }
        }
    }

    public void train(double[] in, double[] tar){
        double[] out = computeOut(in);
        updateWeight(tar);
    }
}
