package Lesson2;


public class HomeWork2 {
    /*
     Ваши методы должны бросить исключения в случаях:
    Если размер матрицы, полученной из строки, не равен 4x4;
    Если в одной из ячеек полученной матрицы не число;
     (например символ или слово)
 4. В методе main необходимо вызвать полученные методы,
  обработать возможные исключения и вывести результат расчета.

     */

    public static void main(String args[]) {


        String str = "10 3 1 2\n2 3 2 2\n5 6 7 1\n300 3 1 0\n300 3 1 0";
        String space = " ";
        String n = "\n";


        System.out.println("String [][]:");

        String[][] dubleString = strArr(str, n, space);
        if (dubleString.length != 4) {
            try {
                throw new ArrayIndexOutOfBoundsException("Массив не 4х4");
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Int[][]:");

        int[][] numArr = dubleStringToInt(dubleString);
        System.out.println("Sum=" + sumDubleIntArr(numArr));
        System.out.println("Sum/2=" + (sumDubleIntArr(numArr)) / 2);

    }

    public static int sumDubleIntArr(int[][] intArr) {
        int sum = 0;
        for (int i = 0; i < intArr.length; i++) {
            for (int j = 0; j < intArr.length; j++) sum += intArr[i][j];
        }
        return sum;
    }

    public static int[][] dubleStringToInt(String[][] dubleString) throws NumberFormatException {

        int[][] num = new int[dubleString.length][dubleString.length];
        for (int i = 0; i < dubleString.length; i++) {
            for (int j = 0; j < dubleString.length; j++) {
                if (dubleString[i][j] == null) {
                    throw new NumberFormatException("тут пусто");
                }

                num[i][j] = Integer.parseInt(dubleString[i][j]);
                System.out.print(num[i][j] + "|");
            }
            System.out.println();
        }
        return num;
    }

    /**
     * Запутался в методе, но он заработал
     *
     * @param str    начальная строка
     * @param regex1 параметр для первого разбиения
     * @param regex2 параметр для второго разбиения
     * @return
     */
    public static String[][] strArr(String str, String regex1, String regex2) {
        String[] strArr = str.split(regex1);

        String[] numArr = new String[strArr.length];
        String[][] dubleArr = new String[strArr.length][strArr.length];

        for (int i = 0; i < strArr.length; i++) {
            numArr[i] = strArr[i];
        }

        for (int i = 0; i < numArr.length; i++) {
            strArr = numArr[i].split(regex2);
            for (int j = 0; j < strArr.length; j++) {
                dubleArr[i][j] = strArr[j];
                System.out.print(dubleArr[i][j]);
            }
            System.out.println();
        }

        return dubleArr;
    }

}
