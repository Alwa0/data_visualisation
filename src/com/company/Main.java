package com.company;

public class Main {

    public static void main(String[] args) {
        System.out.println(transform("", "element", "1=1"));
    }

    public static String transform(String initial, String element, String condition){
        String newCondition = condition;
        String newElement = element;

        //find number of calls
        int n = 0;
        while(true){
            String edited = initial.replaceFirst("%>%", "@");
            if(initial.equals(edited))
                break;
            initial = edited;
            n++;
        }
        if(initial.equals("")) n=-1;

        //create array of all calls
        String[] calls = initial.split("@");

        //parse calls
        for(int i=0; i<=n; i++){
            String call = calls[i];

            //map calls
            if(call.contains("map")){
                newElement = call.substring(4, call.length()-1);
            }

            //filter calls
            else if(call.contains("filter")){
                newCondition = call.substring(7, call.length()-1);
                if(!(condition.equals("1=1"))){
                    newCondition = newCondition + "&" + condition;
                }
            }

            //no filter and no map
            else {
                return ("TYPE ERROR");
            }

            if(!check_call(call)){
                return ("SYNTAX ERROR");
            }

            //if it is not last call then replace all elements
            if(i!=n) {
                StringBuilder newInitial = new StringBuilder();
                for(int j=i+1; j<=n; j++){
                    newInitial.append(calls[j].replaceAll(element, newElement));
                    if(j!=n) newInitial.append("%>%");
                }
                return transform(newInitial.toString(), newElement, newCondition);
            }
        }
        return ("filter{" + newCondition + "}%>%map{" + newElement + "}");
    }

    public static boolean check_call(String call){
        String expression;

        if((call.substring(0, 4).equals("map{"))&&(call.endsWith("}"))){
            expression = call.substring(4, call.length()-1);
        }
        else if((call.substring(0, 7).equals("filter{"))&&(call.endsWith("}"))){
            expression = call.substring(7, call.length()-1);
        }
        else return false;

        return check_expression(expression);
    }

    public static boolean check_expression(String expression){
        //element
        if(expression.equals("element")) return true;

        //constant-expression
        else {
            int n = 0;
            for(int i=0; i<expression.length(); i++){
                if((expression.charAt(i)<=57&&expression.charAt(i)>=48)||(i==0&&expression.charAt(i)=='-'))
                    n++;
            }
            if(n==expression.length()) return true;
        }

        //binary-expression
        if(expression.startsWith("(")&&expression.endsWith(")")){
            String exp = expression.substring(1, expression.length()-1);
            int open=0;
            int[] blocked = new int[exp.length()];
            for(int i=0; i<exp.length(); i++){
                blocked[i]=0;
                if(exp.charAt(i)=='(') open++;
                if(exp.charAt(i)==')') open--;
                if(open>0) blocked[i] = 1;
            }
            String[] operations = {String.valueOf('+'), String.valueOf('*'), ">", "<", "=", "&", String.valueOf('|'), "-"};

            for(int i=0; i<8; i++){
                for(int j=0; j<exp.length(); j++){
                    if(exp.charAt(j)==operations[i].toCharArray()[0] && blocked[j]==0){
                        return check_expression(exp.substring(0,j))&&check_expression(exp.substring(j+1));
                    }
                }
            }
        }
        return false;
    }
}
