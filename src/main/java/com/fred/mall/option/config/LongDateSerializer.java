package com.fred.mall.option.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author liuxiaokun
 * @version 0.0.1
 * @since 2018/12/13
 */
public class LongDateSerializer extends JsonSerializer<Long> {

    @Override
    public void serialize(Long value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {

        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(value));
        jsonGenerator.writeString(format);
    }
}
