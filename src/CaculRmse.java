/**
 * 计算RMSE值
 * */
public class CaculRmse {

    public boolean compareArryLength(double[] arr1, double[] arr2){
        if (arr1.length == arr2.length){
            return true;
        }else {
            return false;
        }
    }

    public double Rmse(double[] arr1, double[] arr2){
        double temp;
        double sum = 0;

        if (arr1.length == arr2.length){
            for (int i = 1; i <= arr1.length;i++){
                temp = Math.pow(arr1[i]-arr2[i],2);
                sum += temp;
            }
        }else {
            System.out.println("输入矩阵维数不一致");
        }
        return sum;
    }
}




