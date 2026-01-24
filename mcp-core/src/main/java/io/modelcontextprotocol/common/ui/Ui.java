package io.modelcontextprotocol.common.ui;

import io.modelcontextprotocol.common.a2ui.A2ui;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class Ui {
    private Ui() {}

    public interface UiWidget {
        String id();
        Double weight();
        UiWidget withId(String id);
        UiWidget withWeight(Double weight);
    }

    public sealed interface UiString permits UiStringLiteral, UiStringPath {
        Map<String, Object> toRef();
    }

    public static final class UiStringLiteral implements UiString {
        public final String value;
        public UiStringLiteral(String value) { this.value = value; }
        public Map<String, Object> toRef() { return A2ui.stringRefLiteral(value); }
    }

    public static final class UiStringPath implements UiString {
        public final String path;
        public UiStringPath(String path) { this.path = path; }
        public Map<String, Object> toRef() { return A2ui.stringRefPath(path); }
    }

    public sealed interface UiNumber permits UiNumberLiteral, UiNumberPath {
        Map<String, Object> toRef();
    }

    public static final class UiNumberLiteral implements UiNumber {
        public final double value;
        public UiNumberLiteral(double value) { this.value = value; }
        public Map<String, Object> toRef() { return A2ui.numberRefLiteral(value); }
    }

    public static final class UiNumberPath implements UiNumber {
        public final String path;
        public UiNumberPath(String path) { this.path = path; }
        public Map<String, Object> toRef() { return A2ui.numberRefPath(path); }
    }

    public sealed interface UiBool permits UiBoolLiteral, UiBoolPath {
        Map<String, Object> toRef();
    }

    public static final class UiBoolLiteral implements UiBool {
        public final boolean value;
        public UiBoolLiteral(boolean value) { this.value = value; }
        public Map<String, Object> toRef() { return A2ui.boolRefLiteral(value); }
    }

    public static final class UiBoolPath implements UiBool {
        public final String path;
        public UiBoolPath(String path) { this.path = path; }
        public Map<String, Object> toRef() { return A2ui.boolRefPath(path); }
    }

    public sealed interface UiStringArray permits UiStringArrayLiteral, UiStringArrayPath {
        Map<String, Object> toRef();
    }

    public static final class UiStringArrayLiteral implements UiStringArray {
        public final List<String> values;
        public UiStringArrayLiteral(List<String> values) { this.values = values; }
        public Map<String, Object> toRef() { return A2ui.stringArrayRefLiteral(values); }
    }

    public static final class UiStringArrayPath implements UiStringArray {
        public final String path;
        public UiStringArrayPath(String path) { this.path = path; }
        public Map<String, Object> toRef() { return A2ui.stringArrayRefPath(path); }
    }

    public sealed interface UiActionValue permits UiActionPath, UiActionString, UiActionNumber, UiActionBool {
        Map<String, Object> toValue();
    }

    public static final class UiActionPath implements UiActionValue {
        public final String path;
        public UiActionPath(String path) { this.path = path; }
        public Map<String, Object> toValue() { return A2ui.actionValuePath(path); }
    }

    public static final class UiActionString implements UiActionValue {
        public final String value;
        public UiActionString(String value) { this.value = value; }
        public Map<String, Object> toValue() { return A2ui.actionValueLiteralString(value); }
    }

    public static final class UiActionNumber implements UiActionValue {
        public final double value;
        public UiActionNumber(double value) { this.value = value; }
        public Map<String, Object> toValue() { return A2ui.actionValueLiteralNumber(value); }
    }

    public static final class UiActionBool implements UiActionValue {
        public final boolean value;
        public UiActionBool(boolean value) { this.value = value; }
        public Map<String, Object> toValue() { return A2ui.actionValueLiteralBoolean(value); }
    }

    public static final class UiAction {
        public final String name;
        public final Map<String, UiActionValue> context;
        public UiAction(String name) { this(name, new LinkedHashMap<>()); }
        public UiAction(String name, Map<String, UiActionValue> context) {
            this.name = name;
            this.context = context;
        }
        Map<String, Object> toAction() {
            if (context.isEmpty()) {
                return A2ui.action(name, List.of());
            }
            List<Map<String, Object>> entries = new ArrayList<>();
            for (var entry : context.entrySet()) {
                entries.add(A2ui.actionContextEntry(entry.getKey(), entry.getValue().toValue()));
            }
            return A2ui.action(name, entries);
        }
    }

    public sealed interface UiChildren permits UiChildrenItems, UiChildrenTemplate {}

    public static final class UiChildrenItems implements UiChildren {
        public final List<UiWidget> items;
        public UiChildrenItems(List<UiWidget> items) { this.items = items; }
    }

    public static final class UiChildrenTemplate implements UiChildren {
        public final String dataBinding;
        public final UiWidget template;
        public UiChildrenTemplate(String dataBinding, UiWidget template) {
            this.dataBinding = dataBinding;
            this.template = template;
        }
    }

    public abstract static class BaseWidget implements UiWidget {
        private String id;
        private Double weight;

        public String id() { return id; }
        public Double weight() { return weight; }

        public UiWidget withId(String id) { this.id = id; return this; }
        public UiWidget withWeight(Double weight) { this.weight = weight; return this; }
    }

    public static final class Text extends BaseWidget {
        public final UiString text;
        public final String usageHint;
        public Text(UiString text, String usageHint) { this.text = text; this.usageHint = usageHint; }
    }

    public static final class Image extends BaseWidget {
        public final UiString url;
        public final String fit;
        public final String usageHint;
        public Image(UiString url, String fit, String usageHint) {
            this.url = url; this.fit = fit; this.usageHint = usageHint;
        }
    }

    public static final class Icon extends BaseWidget {
        public final UiString name;
        public Icon(UiString name) { this.name = name; }
    }

    public static final class Divider extends BaseWidget {
        public final String axis;
        public Divider(String axis) { this.axis = axis; }
    }

    public static final class Row extends BaseWidget {
        public final UiChildren children;
        public final String distribution;
        public final String alignment;
        public Row(UiChildren children, String distribution, String alignment) {
            this.children = children; this.distribution = distribution; this.alignment = alignment;
        }
    }

    public static final class Column extends BaseWidget {
        public final UiChildren children;
        public final String distribution;
        public final String alignment;
        public Column(UiChildren children, String distribution, String alignment) {
            this.children = children; this.distribution = distribution; this.alignment = alignment;
        }
    }

    public static final class ListWidget extends BaseWidget {
        public final UiChildren children;
        public final String direction;
        public final String alignment;
        public ListWidget(UiChildren children, String direction, String alignment) {
            this.children = children; this.direction = direction; this.alignment = alignment;
        }
    }

    public static final class Button extends BaseWidget {
        public final UiWidget child;
        public final UiAction action;
        public final Boolean primary;
        public Button(UiWidget child, UiAction action, Boolean primary) {
            this.child = child; this.action = action; this.primary = primary;
        }
    }

    public static final class TextField extends BaseWidget {
        public final UiString text;
        public final UiString label;
        public final String textFieldType;
        public final String validationRegexp;
        public final UiAction onSubmittedAction;
        public TextField(UiString text, UiString label, String textFieldType, String validationRegexp, UiAction onSubmittedAction) {
            this.text = text; this.label = label; this.textFieldType = textFieldType; this.validationRegexp = validationRegexp; this.onSubmittedAction = onSubmittedAction;
        }
    }

    public static final class CheckBox extends BaseWidget {
        public final UiString label;
        public final UiBool value;
        public CheckBox(UiString label, UiBool value) { this.label = label; this.value = value; }
    }

    public static final class Card extends BaseWidget {
        public final UiWidget child;
        public Card(UiWidget child) { this.child = child; }
    }

    public static final class Modal extends BaseWidget {
        public final UiWidget entryPoint;
        public final UiWidget content;
        public Modal(UiWidget entryPoint, UiWidget content) { this.entryPoint = entryPoint; this.content = content; }
    }

    public static final class TabItem {
        public final UiString title;
        public final UiWidget child;
        public TabItem(UiString title, UiWidget child) { this.title = title; this.child = child; }
    }

    public static final class Tabs extends BaseWidget {
        public final List<TabItem> tabItems;
        public Tabs(List<TabItem> tabItems) { this.tabItems = tabItems; }
    }

    public static final class ChoiceOption {
        public final UiString label;
        public final String value;
        public ChoiceOption(UiString label, String value) { this.label = label; this.value = value; }
    }

    public static final class MultipleChoice extends BaseWidget {
        public final UiStringArray selections;
        public final List<ChoiceOption> options;
        public final Integer maxAllowedSelections;
        public MultipleChoice(UiStringArray selections, List<ChoiceOption> options, Integer maxAllowedSelections) {
            this.selections = selections; this.options = options; this.maxAllowedSelections = maxAllowedSelections;
        }
    }

    public static final class Slider extends BaseWidget {
        public final UiNumber value;
        public final Double minValue;
        public final Double maxValue;
        public Slider(UiNumber value, Double minValue, Double maxValue) {
            this.value = value; this.minValue = minValue; this.maxValue = maxValue;
        }
    }

    public static final class DateTimeInput extends BaseWidget {
        public final UiString value;
        public final Boolean enableDate;
        public final Boolean enableTime;
        public final String firstDate;
        public final String lastDate;
        public DateTimeInput(UiString value, Boolean enableDate, Boolean enableTime, String firstDate, String lastDate) {
            this.value = value; this.enableDate = enableDate; this.enableTime = enableTime; this.firstDate = firstDate; this.lastDate = lastDate;
        }
    }

    public static final class AudioPlayer extends BaseWidget {
        public final UiString url;
        public AudioPlayer(UiString url) { this.url = url; }
    }

    public static final class Video extends BaseWidget {
        public final UiString url;
        public Video(UiString url) { this.url = url; }
    }

    public static final class Timeline extends BaseWidget {
        public final UiChildren children;
        public final String orientation;
        public final String alignment;
        public final UiBool autoFollow;
        public final String laneMode;
        public final UiString currentItemId;
        public Timeline(UiChildren children, String orientation, String alignment, UiBool autoFollow, String laneMode, UiString currentItemId) {
            this.children = children; this.orientation = orientation; this.alignment = alignment; this.autoFollow = autoFollow; this.laneMode = laneMode; this.currentItemId = currentItemId;
        }
    }

    public static final class TimelineItem extends BaseWidget {
        public final String itemId;
        public final UiString title;
        public final UiString subtitle;
        public final UiString timestamp;
        public final String kind;
        public final String state;
        public final String severity;
        public final UiString icon;
        public final UiWidget content;
        public final UiAction action;
        public TimelineItem(String itemId, UiString title, UiString subtitle, UiString timestamp, String kind, String state, String severity, UiString icon, UiWidget content, UiAction action) {
            this.itemId = itemId; this.title = title; this.subtitle = subtitle; this.timestamp = timestamp; this.kind = kind; this.state = state; this.severity = severity; this.icon = icon; this.content = content; this.action = action;
        }
    }

    public static final class TimelineGroup extends BaseWidget {
        public final String groupId;
        public final UiString title;
        public final UiString summary;
        public final UiChildren children;
        public final UiBool collapsed;
        public final UiNumber badgeCount;
        public final String groupState;
        public TimelineGroup(String groupId, UiString title, UiString summary, UiChildren children, UiBool collapsed, UiNumber badgeCount, String groupState) {
            this.groupId = groupId; this.title = title; this.summary = summary; this.children = children; this.collapsed = collapsed; this.badgeCount = badgeCount; this.groupState = groupState;
        }
    }

    public static final class TimelineLane extends BaseWidget {
        public final String laneId;
        public final UiString title;
        public final UiChildren children;
        public TimelineLane(String laneId, UiString title, UiChildren children) {
            this.laneId = laneId; this.title = title; this.children = children;
        }
    }

    public static final class UiRenderResult {
        public final String rootId;
        public final List<Map<String, Object>> components;
        public UiRenderResult(String rootId, List<Map<String, Object>> components) {
            this.rootId = rootId;
            this.components = components;
        }
    }

    public static UiRenderResult render(UiWidget root) {
        var serializer = new Serializer();
        var rootId = serializer.renderWidget(root);
        return new UiRenderResult(rootId, serializer.components);
    }

    private static final class Serializer {
        private int counter = 0;
        private final List<Map<String, Object>> components = new ArrayList<>();

        private String nextId() {
            counter += 1;
            return "ui-" + counter;
        }

        private Map<String, Object> renderChildren(UiChildren children) {
            if (children instanceof UiChildrenTemplate template) {
                var templateId = renderWidget(template.template);
                return A2ui.childrenTemplate(templateId, template.dataBinding);
            }
            var ids = new ArrayList<String>();
            for (var child : ((UiChildrenItems) children).items) {
                ids.add(renderWidget(child));
            }
            return A2ui.childrenExplicit(ids);
        }

        String renderWidget(UiWidget widget) {
            var id = widget.id() != null ? widget.id() : nextId();
            var rendered = renderKind(widget);
            var entry = A2ui.component(id, rendered.type, rendered.props, widget.weight());
            components.add(entry);
            return id;
        }

        private Rendered renderKind(UiWidget widget) {
            if (widget instanceof Text text) {
                var props = new LinkedHashMap<String, Object>();
                props.put("text", text.text.toRef());
                if (text.usageHint != null) { props.put("usageHint", text.usageHint); }
                return new Rendered("Text", props);
            }
            if (widget instanceof Image image) {
                var props = new LinkedHashMap<String, Object>();
                props.put("url", image.url.toRef());
                if (image.fit != null) { props.put("fit", image.fit); }
                if (image.usageHint != null) { props.put("usageHint", image.usageHint); }
                return new Rendered("Image", props);
            }
            if (widget instanceof Icon icon) {
                return new Rendered("Icon", mapOf("name", icon.name.toRef()));
            }
            if (widget instanceof Divider divider) {
                var props = new LinkedHashMap<String, Object>();
                if (divider.axis != null) { props.put("axis", divider.axis); }
                return new Rendered("Divider", props);
            }
            if (widget instanceof Row row) {
                var props = new LinkedHashMap<String, Object>();
                props.put("children", renderChildren(row.children));
                if (row.distribution != null) { props.put("distribution", row.distribution); }
                if (row.alignment != null) { props.put("alignment", row.alignment); }
                return new Rendered("Row", props);
            }
            if (widget instanceof Column column) {
                var props = new LinkedHashMap<String, Object>();
                props.put("children", renderChildren(column.children));
                if (column.distribution != null) { props.put("distribution", column.distribution); }
                if (column.alignment != null) { props.put("alignment", column.alignment); }
                return new Rendered("Column", props);
            }
            if (widget instanceof ListWidget list) {
                var props = new LinkedHashMap<String, Object>();
                props.put("children", renderChildren(list.children));
                if (list.direction != null) { props.put("direction", list.direction); }
                if (list.alignment != null) { props.put("alignment", list.alignment); }
                return new Rendered("List", props);
            }
            if (widget instanceof Button button) {
                var props = new LinkedHashMap<String, Object>();
                props.put("child", renderWidget(button.child));
                props.put("action", button.action.toAction());
                if (button.primary != null) { props.put("primary", button.primary); }
                return new Rendered("Button", props);
            }
            if (widget instanceof TextField field) {
                var props = new LinkedHashMap<String, Object>();
                if (field.text != null) { props.put("text", field.text.toRef()); }
                if (field.label != null) { props.put("label", field.label.toRef()); }
                if (field.textFieldType != null) { props.put("textFieldType", field.textFieldType); }
                if (field.validationRegexp != null) { props.put("validationRegexp", field.validationRegexp); }
                if (field.onSubmittedAction != null) { props.put("onSubmittedAction", field.onSubmittedAction.toAction()); }
                return new Rendered("TextField", props);
            }
            if (widget instanceof CheckBox check) {
                return new Rendered("CheckBox", mapOf("label", check.label.toRef(), "value", check.value.toRef()));
            }
            if (widget instanceof Card card) {
                return new Rendered("Card", mapOf("child", renderWidget(card.child)));
            }
            if (widget instanceof Modal modal) {
                return new Rendered("Modal", mapOf(
                    "entryPointChild", renderWidget(modal.entryPoint),
                    "contentChild", renderWidget(modal.content)));
            }
            if (widget instanceof Tabs tabs) {
                var items = new ArrayList<Map<String, Object>>();
                for (var item : tabs.tabItems) {
                    items.add(mapOf("title", item.title.toRef(), "child", renderWidget(item.child)));
                }
                return new Rendered("Tabs", mapOf("tabItems", items));
            }
            if (widget instanceof MultipleChoice choice) {
                var opts = new ArrayList<Map<String, Object>>();
                for (var option : choice.options) {
                    opts.add(mapOf("label", option.label.toRef(), "value", option.value));
                }
                var props = new LinkedHashMap<String, Object>();
                props.put("selections", choice.selections.toRef());
                props.put("options", opts);
                if (choice.maxAllowedSelections != null) { props.put("maxAllowedSelections", choice.maxAllowedSelections); }
                return new Rendered("MultipleChoice", props);
            }
            if (widget instanceof Slider slider) {
                var props = new LinkedHashMap<String, Object>();
                props.put("value", slider.value.toRef());
                if (slider.minValue != null) { props.put("minValue", slider.minValue); }
                if (slider.maxValue != null) { props.put("maxValue", slider.maxValue); }
                return new Rendered("Slider", props);
            }
            if (widget instanceof DateTimeInput input) {
                var props = new LinkedHashMap<String, Object>();
                props.put("value", input.value.toRef());
                if (input.enableDate != null) { props.put("enableDate", input.enableDate); }
                if (input.enableTime != null) { props.put("enableTime", input.enableTime); }
                if (input.firstDate != null) { props.put("firstDate", input.firstDate); }
                if (input.lastDate != null) { props.put("lastDate", input.lastDate); }
                return new Rendered("DateTimeInput", props);
            }
            if (widget instanceof AudioPlayer audio) {
                return new Rendered("AudioPlayer", mapOf("url", audio.url.toRef()));
            }
            if (widget instanceof Video video) {
                return new Rendered("Video", mapOf("url", video.url.toRef()));
            }
            if (widget instanceof Timeline timeline) {
                var props = new LinkedHashMap<String, Object>();
                props.put("children", renderChildren(timeline.children));
                if (timeline.orientation != null) { props.put("orientation", timeline.orientation); }
                if (timeline.alignment != null) { props.put("alignment", timeline.alignment); }
                if (timeline.autoFollow != null) { props.put("autoFollow", timeline.autoFollow.toRef()); }
                if (timeline.laneMode != null) { props.put("laneMode", timeline.laneMode); }
                if (timeline.currentItemId != null) { props.put("currentItemId", timeline.currentItemId.toRef()); }
                return new Rendered("Timeline", props);
            }
            if (widget instanceof TimelineItem item) {
                var props = new LinkedHashMap<String, Object>();
                if (item.itemId != null) { props.put("itemId", item.itemId); }
                if (item.title != null) { props.put("title", item.title.toRef()); }
                if (item.subtitle != null) { props.put("subtitle", item.subtitle.toRef()); }
                if (item.timestamp != null) { props.put("timestamp", item.timestamp.toRef()); }
                if (item.kind != null) { props.put("kind", item.kind); }
                if (item.state != null) { props.put("state", item.state); }
                if (item.severity != null) { props.put("severity", item.severity); }
                if (item.icon != null) { props.put("icon", item.icon.toRef()); }
                if (item.content != null) { props.put("contentChild", renderWidget(item.content)); }
                if (item.action != null) { props.put("action", item.action.toAction()); }
                return new Rendered("TimelineItem", props);
            }
            if (widget instanceof TimelineGroup group) {
                var props = new LinkedHashMap<String, Object>();
                props.put("groupId", group.groupId);
                if (group.title != null) { props.put("title", group.title.toRef()); }
                if (group.summary != null) { props.put("summary", group.summary.toRef()); }
                props.put("children", renderChildren(group.children));
                if (group.collapsed != null) { props.put("collapsed", group.collapsed.toRef()); }
                if (group.badgeCount != null) { props.put("badgeCount", group.badgeCount.toRef()); }
                if (group.groupState != null) { props.put("groupState", group.groupState); }
                return new Rendered("TimelineGroup", props);
            }
            if (widget instanceof TimelineLane lane) {
                var props = new LinkedHashMap<String, Object>();
                props.put("laneId", lane.laneId);
                if (lane.title != null) { props.put("title", lane.title.toRef()); }
                props.put("children", renderChildren(lane.children));
                return new Rendered("TimelineLane", props);
            }
            return new Rendered("Unknown", new LinkedHashMap<>());
        }
    }

    private record Rendered(String type, Map<String, Object> props) {}

    private static Map<String, Object> mapOf(Object... entries) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < entries.length; i += 2) {
            map.put((String) entries[i], entries[i + 1]);
        }
        return map;
    }
}
