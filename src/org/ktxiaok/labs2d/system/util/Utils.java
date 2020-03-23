package org.ktxiaok.labs2d.system.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class Utils {
    private Utils(){

    }
    public static boolean isEqual(Object a,Object b){
        if(a==null){
            if(b==null){
                return true;
            }else{
                return false;
            }
        }else{
            return a.equals(b);
        }
    }
    public static int computeHashCode(Object[] objs) {
        int result=1;
        for(Object obj:objs) {
            result=result*31+((obj==null)?0:obj.hashCode());
        }
        return result;
    }
    public static Object[] combineArray(Object[] a1,Object[] a2) {
        Object[] result=new Object[a1.length+a2.length];
        System.arraycopy(a1,0,result,0,a1.length);
        System.arraycopy(a2,0,result,a1.length,a2.length);
        return result;
    }
    public static float[] combineArray(float[] a1,float[] a2) {
        float[] result=new float[a1.length+a2.length];
        System.arraycopy(a1,0,result,0,a1.length);
        System.arraycopy(a2,0,result,a1.length,a2.length);
        return result;
    }
    public static int[] combineArray(int[] a1,int[] a2) {
        int[] result=new int[a1.length+a2.length];
        System.arraycopy(a1,0,result,0,a1.length);
        System.arraycopy(a2,0,result,a1.length,a2.length);
        return result;
    }
    public static String exceptionToString(Exception e) {
        Writer writer = null;
        PrintWriter printWriter = null;
        String result="error";
        try {
            writer = new StringWriter();
            printWriter = new PrintWriter(writer);
            e.printStackTrace(printWriter);
            result=writer.toString();
        } finally {
            try {
                if (writer != null)
                    writer.close();
                if (printWriter != null)
                    printWriter.close();
            } catch (IOException e1) { }
        }
        return result;
    }
}
