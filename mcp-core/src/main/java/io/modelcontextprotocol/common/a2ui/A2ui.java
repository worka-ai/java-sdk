package io.modelcontextprotocol.common.a2ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class A2ui {
    private A2ui() {}

    public static Map<String, Object> stringRefLiteral(String value) {
        return mapOf("literalString", value);
    }

    public static Map<String, Object> stringRefPath(String path) {
        return mapOf("path", path);
    }

    public static Map<String, Object> numberRefLiteral(double value) {
        return mapOf("literalNumber", value);
    }

    public static Map<String, Object> numberRefPath(String path) {
        return mapOf("path", path);
    }

    public static Map<String, Object> boolRefLiteral(boolean value) {
        return mapOf("literalBoolean", value);
    }

    public static Map<String, Object> boolRefPath(String path) {
        return mapOf("path", path);
    }

    public static Map<String, Object> stringArrayRefLiteral(List<String> values) {
        return mapOf("literalArray", values);
    }

    public static Map<String, Object> stringArrayRefPath(String path) {
        return mapOf("path", path);
    }

    public static Map<String, Object> actionValuePath(String path) {
        return mapOf("path", path);
    }

    public static Map<String, Object> actionValueLiteralString(String value) {
        return mapOf("literalString", value);
    }

    public static Map<String, Object> actionValueLiteralNumber(double value) {
        return mapOf("literalNumber", value);
    }

    public static Map<String, Object> actionValueLiteralBoolean(boolean value) {
        return mapOf("literalBoolean", value);
    }

    public static Map<String, Object> action(String name, List<Map<String, Object>> context) {
        Map<String, Object> action = mapOf("name", name);
        if (context != null && !context.isEmpty()) {
            action.put("context", context);
        }
        return action;
    }

    public static Map<String, Object> actionContextEntry(String key, Map<String, Object> value) {
        return mapOf("key", key, "value", value);
    }

    public static Map<String, Object> childrenExplicit(List<String> ids) {
        return mapOf("explicitList", ids);
    }

    public static Map<String, Object> childrenTemplate(String componentId, String dataBinding) {
        return mapOf("template", mapOf("componentId", componentId, "dataBinding", dataBinding));
    }

    public static Map<String, Object> component(String id, String type, Map<String, Object> props, Double weight) {
        Map<String, Object> entry = mapOf("id", id, "component", mapOf(type, props));
        if (weight != null) {
            entry.put("weight", weight);
        }
        return entry;
    }

    public static Map<String, Object> text(String id, Map<String, Object> text, String usageHint) {
        Map<String, Object> props = mapOf("text", text);
        if (usageHint != null && !usageHint.isEmpty()) {
            props.put("usageHint", usageHint);
        }
        return component(id, "Text", props, null);
    }

    public static Map<String, Object> image(String id, Map<String, Object> url, String fit, String usageHint) {
        Map<String, Object> props = mapOf("url", url);
        if (fit != null && !fit.isEmpty()) {
            props.put("fit", fit);
        }
        if (usageHint != null && !usageHint.isEmpty()) {
            props.put("usageHint", usageHint);
        }
        return component(id, "Image", props, null);
    }

    public static Map<String, Object> icon(String id, Map<String, Object> name) {
        return component(id, "Icon", mapOf("name", name), null);
    }

    public static Map<String, Object> divider(String id, String axis) {
        Map<String, Object> props = new LinkedHashMap<>();
        if (axis != null && !axis.isEmpty()) {
            props.put("axis", axis);
        }
        return component(id, "Divider", props, null);
    }

    public static Map<String, Object> row(
        String id,
        Map<String, Object> children,
        String distribution,
        String alignment
    ) {
        Map<String, Object> props = mapOf("children", children);
        if (distribution != null && !distribution.isEmpty()) {
            props.put("distribution", distribution);
        }
        if (alignment != null && !alignment.isEmpty()) {
            props.put("alignment", alignment);
        }
        return component(id, "Row", props, null);
    }

    public static Map<String, Object> column(
        String id,
        Map<String, Object> children,
        String distribution,
        String alignment
    ) {
        Map<String, Object> props = mapOf("children", children);
        if (distribution != null && !distribution.isEmpty()) {
            props.put("distribution", distribution);
        }
        if (alignment != null && !alignment.isEmpty()) {
            props.put("alignment", alignment);
        }
        return component(id, "Column", props, null);
    }

    public static Map<String, Object> list(
        String id,
        Map<String, Object> children,
        String direction,
        String alignment
    ) {
        Map<String, Object> props = mapOf("children", children);
        if (direction != null && !direction.isEmpty()) {
            props.put("direction", direction);
        }
        if (alignment != null && !alignment.isEmpty()) {
            props.put("alignment", alignment);
        }
        return component(id, "List", props, null);
    }

    public static Map<String, Object> button(String id, String child, Map<String, Object> action, Boolean primary) {
        Map<String, Object> props = mapOf("child", child, "action", action);
        if (primary != null) {
            props.put("primary", primary);
        }
        return component(id, "Button", props, null);
    }

    public static Map<String, Object> textField(
        String id,
        Map<String, Object> text,
        Map<String, Object> label,
        String textFieldType,
        String validationRegexp,
        Map<String, Object> onSubmittedAction
    ) {
        Map<String, Object> props = new LinkedHashMap<>();
        if (text != null) { props.put("text", text); }
        if (label != null) { props.put("label", label); }
        if (textFieldType != null && !textFieldType.isEmpty()) {
            props.put("textFieldType", textFieldType);
        }
        if (validationRegexp != null && !validationRegexp.isEmpty()) {
            props.put("validationRegexp", validationRegexp);
        }
        if (onSubmittedAction != null) {
            props.put("onSubmittedAction", onSubmittedAction);
        }
        return component(id, "TextField", props, null);
    }

    public static Map<String, Object> checkBox(String id, Map<String, Object> label, Map<String, Object> value) {
        return component(id, "CheckBox", mapOf("label", label, "value", value), null);
    }

    public static Map<String, Object> card(String id, String child) {
        return component(id, "Card", mapOf("child", child), null);
    }

    public static Map<String, Object> modal(String id, String entryPointChild, String contentChild) {
        return component(id, "Modal", mapOf("entryPointChild", entryPointChild, "contentChild", contentChild), null);
    }

    public static Map<String, Object> tabs(String id, List<Map<String, Object>> tabItems) {
        return component(id, "Tabs", mapOf("tabItems", tabItems), null);
    }

    public static Map<String, Object> tabItem(Map<String, Object> title, String child) {
        return mapOf("title", title, "child", child);
    }

    public static Map<String, Object> multipleChoice(
        String id,
        Map<String, Object> selections,
        List<Map<String, Object>> options,
        Integer maxAllowedSelections
    ) {
        Map<String, Object> props = new LinkedHashMap<>();
        props.put("selections", selections);
        props.put("options", options);
        if (maxAllowedSelections != null) {
            props.put("maxAllowedSelections", maxAllowedSelections);
        }
        return component(id, "MultipleChoice", props, null);
    }

    public static Map<String, Object> choiceOption(Map<String, Object> label, String value) {
        return mapOf("label", label, "value", value);
    }

    public static Map<String, Object> slider(String id, Map<String, Object> value, Double minValue, Double maxValue) {
        Map<String, Object> props = new LinkedHashMap<>();
        props.put("value", value);
        if (minValue != null) { props.put("minValue", minValue); }
        if (maxValue != null) { props.put("maxValue", maxValue); }
        return component(id, "Slider", props, null);
    }

    public static Map<String, Object> dateTimeInput(
        String id,
        Map<String, Object> value,
        Boolean enableDate,
        Boolean enableTime,
        String firstDate,
        String lastDate
    ) {
        Map<String, Object> props = new LinkedHashMap<>();
        props.put("value", value);
        if (enableDate != null) { props.put("enableDate", enableDate); }
        if (enableTime != null) { props.put("enableTime", enableTime); }
        if (firstDate != null && !firstDate.isEmpty()) { props.put("firstDate", firstDate); }
        if (lastDate != null && !lastDate.isEmpty()) { props.put("lastDate", lastDate); }
        return component(id, "DateTimeInput", props, null);
    }

    public static Map<String, Object> audioPlayer(String id, Map<String, Object> url) {
        return component(id, "AudioPlayer", mapOf("url", url), null);
    }

    public static Map<String, Object> video(String id, Map<String, Object> url) {
        return component(id, "Video", mapOf("url", url), null);
    }

    private static Map<String, Object> mapOf(Object... entries) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < entries.length; i += 2) {
            map.put((String) entries[i], entries[i + 1]);
        }
        return map;
    }
}
