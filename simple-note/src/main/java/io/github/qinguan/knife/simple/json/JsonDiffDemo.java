package io.github.qinguan.knife.simple.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.flipkart.zjsonpatch.DiffFlags;
import com.flipkart.zjsonpatch.JsonDiff;

import java.io.IOException;
import java.util.EnumSet;

import static com.flipkart.zjsonpatch.DiffFlags.ADD_ORIGINAL_VALUE_ON_REPLACE;
import static com.flipkart.zjsonpatch.DiffFlags.OMIT_COPY_OPERATION;
import static com.flipkart.zjsonpatch.DiffFlags.OMIT_MOVE_OPERATION;
import static com.flipkart.zjsonpatch.DiffFlags.OMIT_VALUE_ON_REMOVE;

/*******************************************************
 * Copyright (C) 2018 iQIYI.COM - All Rights Reserved
 *
 * <p>This file is part of java-knife.
 * Unauthorized copy of this file, via any medium is strictly prohibited.
 * Proprietary and Confidential.
 *
 * <p>Author(s): Xu Guojun
 * Created: 2019/6/1 23:00
 *******************************************************/
public class JsonDiffDemo {

    static String jsonA = "{\"a\":1,\"b\":[{\"m\":\"a\"},{\"n\":\"b\"}]}";
    static String JsonB = "{\"a\":1,\"b\":[{\"n\":\"b\"},{\"m\":\"a\"}]}";

    static boolean compare(String s1, String s2) throws IOException {
        ObjectMapper jackson = new ObjectMapper();
        jackson.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        jackson.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
        jackson.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        JsonNode beforeNode = jackson.readTree(s1);
        JsonNode afterNode = jackson.readTree(s2);
        JsonNode patchNode = JsonDiff.asJson(beforeNode, afterNode,
                EnumSet.of(OMIT_MOVE_OPERATION, OMIT_VALUE_ON_REMOVE, OMIT_COPY_OPERATION, ADD_ORIGINAL_VALUE_ON_REPLACE));
        if (patchNode.isArray()) {
            for (JsonNode node : patchNode) {
                System.out.println(node.toString());
            }
        }
        return true;
    }

    public static void main(String[] args) {
        try {
            compare(jsonA, JsonB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
