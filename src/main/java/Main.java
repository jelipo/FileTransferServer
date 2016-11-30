/**
 * Created by 10441 on 2016/11/28.
 */
public class Main {

    public static void main(String[] args) {
       System.out.println(longestValidParentheses("()"));
    }
    public static int longestValidParentheses(String s) {
        int max=0;
        String tem="";
        while(true){
            tem=tem+"()";
            if(s.replace(tem,"").equals(s)){
                return max*2;
            }
            max++;
        }
    }


}
