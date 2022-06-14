package com.aries.template.widget.mgson.types;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/******
 * 专门处理空对象的任务
 *
 * @author  ::: louis luo
 * Date ::: 2022/6/13 5:13 PM
 *
 */
public class StringNullAdapter extends TypeAdapter<String> {

    /**
     *  java -> write ->JSON
      */
    @Override
    public void write(JsonWriter out, String value) throws IOException {
        out.value(value);
    }

    /**
     * 如果是空，则返回一个"null"
     * JSON -> read ->JAVA
     */
    @Override
    public String read(JsonReader in) throws IOException {
        if (in.peek()== JsonToken.NULL){
            in.nextNull();
            return "";
        }
        try {
            return in.nextString();
        }catch (Exception e){
            in.nextName(); // 跳转下一个对象，并返回这个对象key name属性
            return "";
        }

    }

}
