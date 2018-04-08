public class TestRmse{
    public static void main(String[] args) {
        Rmse r = new Rmse();
        double[] arr1 = new double[]{1.2, 2.3, 3.6, 2.6, 6.9};
        double[] arr2 = new double[]{2.3, 3.8, 2.8, 6.9, 5.8};
        r.calcuRmse(arr1, arr2);
        System.out.println(r.calcuRmse(arr1, arr2));
    }
}
class Rmse {
    public double[] arr1;
    public double[] arr2;
//    构造器
    public Rmse(){}

    public Rmse(double[] arr1, double[] arr2){
        this.arr1 = arr1;
        this.arr2 = arr2;
    }

    public double[] getArr1() {
        return arr1;
    }

    public void setArr1(double[] arr1) {
        this.arr1 = arr1;
    }

    public double[] getArr2() {
        return arr2;
    }

    public void setArr2(double[] arr2) {
        this.arr2 = arr2;
    }

    public boolean compareArrLength(double[] arr1, double[] arr2){
        if (arr1.length == arr2.length){
            return true;
        }else {
            return false;
        }
    }

    public double calcuRmse(double[] arr1, double[] arr2){
        this.arr1 = arr1;
        this.arr2 = arr2;
        double sum = 0;
        for (int i = 0; i < arr1.length; i++){
            sum += Math.pow((arr1[i] - arr2[i]),2);
        }
        return Math.sqrt(sum/arr1.length);
    }
}
