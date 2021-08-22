package com.rupjit.qaRestAssured.utils;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * This class is used for manipulation of json. It uses jsonPath which is basically xpath pointing to the json
 * node you want to manipulate.
 */
public class PayloadBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(PayloadBuilder.class);

    private ObjectMapper om;
    private JsonNode payload;

    public PayloadBuilder(String basePayload) {
        om = new ObjectMapper();
        try {
            payload = om.readTree(basePayload);
        } catch (IOException e) {
            LOGGER.debug("Unable to parse base payload [{}]", basePayload, e);
            throw new IllegalArgumentException("Unable to parse base payload.", e);
        }
    }

    public String getPayload() {
        return payload.toString();
    }

    public void addOrUpdate(JsonPointer pointer, JsonNode jsonNode) {

        JsonPointer parentPointer = pointer.head();
        JsonNode parentNode = payload.at(parentPointer);
        String fieldName = pointer.last().toString().substring(1);

        if (parentNode.isMissingNode() || parentNode.isNull()) {

            parentNode = StringUtils.isNumeric(fieldName) ? om.createArrayNode() : om.createObjectNode();
            addOrUpdate(parentPointer, parentNode);

        } else if (parentNode.isArray()) {

            ArrayNode parentArrayNode = (ArrayNode) parentNode;
            int fieldIndex = Integer.valueOf(fieldName);
            for (int i = parentArrayNode.size(); i <= fieldIndex; i++) {
                parentArrayNode.addNull();
            }
            parentArrayNode.set(fieldIndex, jsonNode);

        } else if (parentNode.isObject()) {

            ObjectNode parentObjectNode = (ObjectNode) parentNode;
            parentObjectNode.set(fieldName, jsonNode);
        } else {

            LOGGER.debug("Field [{}] can't be set as it's parent node [{}] is is of type [{}]. Excepted Array or Object type.",
                    fieldName, parentPointer.toString(), parentNode.getNodeType().name());
            throw new IllegalArgumentException("Field '" + fieldName + "' can't be set because parent node is of type '"
                    + parentNode.getNodeType().name() + "'. Expected Array or Object type");
        }
    }

    public void addOrUpdate(JsonPointer pointer, String value) {
        if (value == null) {
            addOrUpdate(pointer, NullNode.getInstance());
        } else if (value.startsWith("{") || value.startsWith("[")) {
            try {
                addOrUpdate(pointer, om.readTree(value));
            } catch (IOException e) {
                LOGGER.debug("Unable to parse value [{}].", value, e);
                throw new IllegalArgumentException("Unable to parse given value.", e);
            }
        } else {
            addOrUpdate(pointer, new TextNode(value));
        }
    }

    public void addOrUpdate(String xPath, String value) {
        addOrUpdate(JsonPointer.compile(xPath), value);
    }

    public void addOrUpdate(String xPath, long value) {
        addOrUpdate(JsonPointer.compile(xPath), LongNode.valueOf(value));
    }

    public void addOrUpdate(String xPath, double value) {
        addOrUpdate(JsonPointer.compile(xPath), DoubleNode.valueOf(value));
    }

    public void addOrUpdate(String xPath, boolean value) {
        addOrUpdate(JsonPointer.compile(xPath), BooleanNode.valueOf(value));
    }

    public void addOrUpdate(String xPath, byte[] value) {
        addOrUpdate(JsonPointer.compile(xPath), BinaryNode.valueOf(value));
    }

    public void addOrUpdate(String xPath, BigInteger value) {
        addOrUpdate(JsonPointer.compile(xPath), BigIntegerNode.valueOf(value));
    }

    public void addOrUpdate(String xPath, BigDecimal value) {
        addOrUpdate(JsonPointer.compile(xPath), DecimalNode.valueOf(value));
    }

    public void addOrUpdate(String xPath, float value) {
        addOrUpdate(JsonPointer.compile(xPath), FloatNode.valueOf(value));
    }

    public void addOrUpdate(String xPath, int value) {
        addOrUpdate(JsonPointer.compile(xPath), IntNode.valueOf(value));
    }

    public void addOrUpdate(String xPath, short value) {
        addOrUpdate(JsonPointer.compile(xPath), ShortNode.valueOf(value));
    }

    public void addOrUpdate(String xPath, Object value) {

        if (value instanceof Byte) {
            addOrUpdate(xPath, (byte[]) value);

        } else if (value instanceof Short) {
            addOrUpdate(xPath,(short) value);

        } else if (value instanceof Boolean) {
            addOrUpdate(xPath, (boolean) value);

        } else if (value instanceof Integer) {
            addOrUpdate(xPath, (int) value);

        } else if (value instanceof BigInteger) {
            addOrUpdate(xPath, (BigInteger) value);

        } else if (value instanceof Long) {
            addOrUpdate(xPath, (long) value);

        } else if (value instanceof BigDecimal) {
            addOrUpdate(xPath, (BigDecimal) value);

        } else if (value instanceof Float) {
            addOrUpdate(xPath, (float) value);

        } else if (value instanceof Double) {
            addOrUpdate(xPath, (double) value);

        } else if (value instanceof String) {
            addOrUpdate(xPath, (String) value);

        } else if (value instanceof JsonNode) {
            addOrUpdate(xPath, (JsonNode) value);

        } else {
            LOGGER.debug("Type [{}] is not supported for values.", value.getClass().getSimpleName());
            throw new IllegalArgumentException("Unsupported type '" + value.getClass().getSimpleName() + "'.");
        }
    }

    public void remove(String xPath) {
        remove(JsonPointer.compile(xPath));
    }

    /**
     * Deletes the json node given by the xPath.
     *
     * @param pointer
     */
    public void remove(JsonPointer pointer) {

        JsonPointer parentPointer = pointer.head();
        JsonNode parentNode = payload.at(parentPointer);
        String fieldName = pointer.last().toString().substring(1);

        if (parentNode.isArray()) {

            ArrayNode parentArrayNode = (ArrayNode) parentNode;
            parentArrayNode.remove(Integer.parseInt(fieldName));

        } else if (parentNode.isObject()) {

            ObjectNode parentObjectNode = (ObjectNode) parentNode;
            parentObjectNode.remove(fieldName);

        } else {
            if (parentNode.isMissingNode() || parentNode.isNull()) {
                LOGGER.debug("Could not delete field [{}] as it's parent node [{}] is missing.", fieldName,
                        parentPointer.toString());
                throw new IllegalStateException(String.format("Could not delete field %s as it's parent node %s is missing.",fieldName,parentPointer.toString()));
                
            }

            LOGGER.debug("Field [{}] can't be deleted as it's parent node [{}] is is of type [{}]. Excepted Array or Object type.",
                    fieldName, parentPointer.toString(), parentNode.getNodeType().name());
            throw new IllegalArgumentException("Field '" + fieldName + "' can't be deleted because parent node is of type '"
                    + parentNode.getNodeType().name() + "'. Expected Array or Object type");
        }
    }

    public JsonNode get(String jsonPointerStr) {
        return get(JsonPointer.compile(jsonPointerStr));
    }

    public JsonNode get(JsonPointer pointer) {

        JsonNode node = payload.at(pointer);
        if (node == null || node.isMissingNode()) {
            LOGGER.debug("Nothing found at path [{}].", pointer.toString());
            throw new IllegalStateException("Nothing found at path '" + pointer.toString() + "'");
        }

        return node;
    }

}
