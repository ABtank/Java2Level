package Lesson3.HomeWork3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class HomeWork3_1 {

    public static String a = "Во всем на свете совершенство достигается не тогда когда уже нечего добавить а тогда когда уже нечего убрать ";
    public static String b = "Никто не говорит что будет легко но в конце ты будешь благодарен себе за то что прошел через все это";


    public static void main(String[] args) {
        String text = a + b;
        String[] strArr = stringToSplit(text," ");
        arrList(strArr);
        arrTree(strArr);
        searchDuplicate(strArr);

    }

    private static void searchDuplicate(String[] strArr) {
        HashMap<String,Integer> hashMap =new HashMap<>();
        for (int i = 0; i <strArr.length ; i++) {
            if(hashMap.containsKey(strArr[i])){
                hashMap.put(strArr[i],hashMap.get(strArr[i])+1);
            }else{
                hashMap.put(strArr[i],1);
            }
        }
        System.out.println("Подсчет повторов: "+hashMap);
    }


    public static String[] stringToSplit(String text, String regex) {
        String[] strArr = text.split(regex);
        for (int i = 0; i < strArr.length; i++) {
            System.out.print(strArr[i] + " | ");
        }
        System.out.println();
        return strArr;
    }

    public static void arrTree(String[]arrStr){
        TreeSet<String> tree = new TreeSet<>();
        for (int i = 0; i <arrStr.length ; i++) {
            tree.add(arrStr[i]);
        }
        System.out.println("Список слов="+tree);
    }

    public static void arrList(String[]arrStr){
        ArrayList<String>list= new ArrayList<>();
        for (int i = 0; i <arrStr.length ; i++) {
            list.add(arrStr[i]);
        }
        System.out.println("Список с пвторами= "+list);
    }


}
