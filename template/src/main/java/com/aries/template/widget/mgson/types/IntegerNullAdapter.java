package com.aries.template.widget.mgson.types;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/******
 * 专门处理空对象的任务
 *
 * @author  ::: louis luo
 * Date ::: 2022/6/13 5:13 PM
 *
 */
public class IntegerNullAdapter extends TypeAdapter<Integer> {

    @Override
    public void write(JsonWriter out, Integer value) throws IOException {
        out.value(value);
    }

    /**
     * 如果是空，则返回一个"null"
     */
    @Override
    public Integer read(JsonReader in) throws IOException {
        if (in.peek()==null){
            in.skipValue();
            return -1;
        }
        BigDecimal bigDecimal = new BigDecimal(in.nextString());
        return bigDecimal.intValue();
    }

}
