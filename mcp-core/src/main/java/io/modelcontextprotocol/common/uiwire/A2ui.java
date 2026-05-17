package io.modelcontextprotocol.common.uiwire;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class UiWire {
    private UiWire() {}

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
        return mapOf("literalStringArray", values);
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

    public static Map<String, Object> timeline(
        String id,
        Map<String, Object> children,
        String orientation,
        String alignment,
        Map<String, Object> autoFollow,
        String laneMode,
        Map<String, Object> currentItemId
    ) {
        Map<String, Object> props = new LinkedHashMap<>();
        props.put("children", children);
        if (orientation != null && !orientation.isEmpty()) { props.put("orientation", orientation); }
        if (alignment != null && !alignment.isEmpty()) { props.put("alignment", alignment); }
        if (autoFollow != null) { props.put("autoFollow", autoFollow); }
        if (laneMode != null && !laneMode.isEmpty()) { props.put("laneMode", laneMode); }
        if (currentItemId != null) { props.put("currentItemId", currentItemId); }
        return component(id, "Timeline", props, null);
    }

    public static Map<String, Object> timelineItem(
        String id,
        String itemId,
        Map<String, Object> title,
        Map<String, Object> subtitle,
        Map<String, Object> timestamp,
        String kind,
        String state,
        String severity,
        Map<String, Object> icon,
        String contentChild,
        Map<String, Object> action
    ) {
        Map<String, Object> props = new LinkedHashMap<>();
        if (itemId != null && !itemId.isEmpty()) { props.put("itemId", itemId); }
        if (title != null) { props.put("title", title); }
        if (subtitle != null) { props.put("subtitle", subtitle); }
        if (timestamp != null) { props.put("timestamp", timestamp); }
        if (kind != null && !kind.isEmpty()) { props.put("kind", kind); }
        if (state != null && !state.isEmpty()) { props.put("state", state); }
        if (severity != null && !severity.isEmpty()) { props.put("severity", severity); }
        if (icon != null) { props.put("icon", icon); }
        if (contentChild != null && !contentChild.isEmpty()) { props.put("contentChild", contentChild); }
        if (action != null) { props.put("action", action); }
        return component(id, "TimelineItem", props, null);
    }

    public static Map<String, Object> timelineGroup(
        String id,
        String groupId,
        Map<String, Object> title,
        Map<String, Object> summary,
        Map<String, Object> children,
        Map<String, Object> collapsed,
        Map<String, Object> badgeCount,
        String groupState
    ) {
        Map<String, Object> props = new LinkedHashMap<>();
        if (groupId != null && !groupId.isEmpty()) { props.put("groupId", groupId); }
        if (title != null) { props.put("title", title); }
        if (summary != null) { props.put("summary", summary); }
        if (children != null) { props.put("children", children); }
        if (collapsed != null) { props.put("collapsed", collapsed); }
        if (badgeCount != null) { props.put("badgeCount", badgeCount); }
        if (groupState != null && !groupState.isEmpty()) { props.put("groupState", groupState); }
        return component(id, "TimelineGroup", props, null);
    }

    public static Map<String, Object> timelineLane(
        String id,
        String laneId,
        Map<String, Object> title,
        Map<String, Object> children
    ) {
        Map<String, Object> props = new LinkedHashMap<>();
        if (laneId != null && !laneId.isEmpty()) { props.put("laneId", laneId); }
        if (title != null) { props.put("title", title); }
        if (children != null) { props.put("children", children); }
        return component(id, "TimelineLane", props, null);
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
