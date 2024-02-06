package com.example.JDRSlistin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.apache.commons.lang.StringUtils.substring;

public class JSONNumberValueExtractor {
    private JSONNumberValueExtractor() {

    }

    public static List<String[]>extractNumericValues(String input) {
        Map<String, String> numeric = new HashMap<>();
        String value="";
        String key="";
        char[] reverseKey ;
        int initial=0;
        List<String[]> numericKeyValue= new ArrayList<>();
        String[] numberKey= new String[2];
        String jsonString =input.replace(" ", "");
        jsonString =jsonString.replace("\\", "");
        char[] charJson =jsonString.toCharArray();
        for(int i=2;i<charJson.length;i++){
            if((charJson[i-1]==':'||  Character.isDigit(charJson[i-1])||(Character.isDigit(charJson[i-2]) && charJson[i-1]=='.')) && (Character.isDigit(charJson[i]) || charJson[i]=='.')){
//                System.out.println(charJson[i]);
                if(charJson[i-1]==':')
//                if(charJson[i]=='{'){
//                    for(int p=i-3;i>0;i-- ){
//                        if(charJson[i]=='"'){
//                            break;
//                        }
//                        tempPath+=charJson[i];
//                    }
//                    path=path+"."+tempPath;
//                }
//                System.out.println(path);
//
//                if(charJson[i]=='}'){
//
//                    for(int p=i-2;i>0;i-- ){
//                        if(charJson[i]=='"'){
//                            break;
//                        }
//                        tempPath=tempPath.substring(charJson.length-1);
//                    }
//                    path=path.substring("."+tempPath);
//                }
                {initial = i-3;}
                value+=charJson[i];
                if(Character.isDigit(charJson[i]) && (charJson[i+1]==',')||charJson[i+1]=='}'){
                    for (int j=initial;j>0;j--){
                        if(charJson[j]=='"'){
                            break;
                        }
                        key+=charJson[j];
                    }
                    reverseKey= key.toCharArray();
                    char temp;
                    for(int k=0;k<reverseKey.length/2;k++){
                        temp=reverseKey[k];
                        reverseKey[k]=reverseKey[reverseKey.length-1-k];
                        reverseKey[reverseKey.length-1-k]=temp;
                    }
                    ;
                    numeric.put(new String(reverseKey),value);
                    numberKey[0]=new String(reverseKey);
                    numberKey[1]=value;
                    numericKeyValue.add(numberKey);
                    value="";
                    initial=0;
                    key="";
                }
            }
        }
//        return numeric;
        return numericKeyValue;
    }
}

