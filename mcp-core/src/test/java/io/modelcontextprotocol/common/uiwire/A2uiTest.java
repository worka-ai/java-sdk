package io.modelcontextprotocol.common.uiwire;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class UiWireTest {
    private final ObjectMapper mapper = new ObjectMapper();

    private Map<String, Object> parse(String json) throws Exception {
        return mapper.readValue(json, new TypeReference<>() {});
    }

    @Test
    void serializesCoreWidgets() throws Exception {
        var text = UiWire.text("text", UiWire.stringRefLiteral("Hello"), "h1");
        assertEquals(parse("{\"id\":\"text\",\"component\":{\"Text\":{\"text\":{\"literalString\":\"Hello\"},\"usageHint\":\"h1\"}}}"), text);

        var image = UiWire.image("img", UiWire.stringRefLiteral("https://example.com/logo.png"), "cover", "mediumFeature");
        assertEquals(parse("{\"id\":\"img\",\"component\":{\"Image\":{\"url\":{\"literalString\":\"https://example.com/logo.png\"},\"fit\":\"cover\",\"usageHint\":\"mediumFeature\"}}}"), image);

        var icon = UiWire.icon("icon", UiWire.stringRefLiteral("check"));
        assertEquals(parse("{\"id\":\"icon\",\"component\":{\"Icon\":{\"name\":{\"literalString\":\"check\"}}}}"), icon);

        var divider = UiWire.divider("divider", "horizontal");
        assertEquals(parse("{\"id\":\"divider\",\"component\":{\"Divider\":{\"axis\":\"horizontal\"}}}"), divider);

        var row = UiWire.row("row", UiWire.childrenExplicit(List.of("a", "b")), "spaceBetween", "center");
        assertEquals(parse("{\"id\":\"row\",\"component\":{\"Row\":{\"children\":{\"explicitList\":[\"a\",\"b\"]},\"distribution\":\"spaceBetween\",\"alignment\":\"center\"}}}"), row);

        var column = UiWire.column("column", UiWire.childrenTemplate("item", "/items"), "start", "stretch");
        assertEquals(parse("{\"id\":\"column\",\"component\":{\"Column\":{\"children\":{\"template\":{\"componentId\":\"item\",\"dataBinding\":\"/items\"}},\"distribution\":\"start\",\"alignment\":\"stretch\"}}}"), column);

        var list = UiWire.list("list", UiWire.childrenExplicit(List.of("one", "two")), "vertical", "start");
        assertEquals(parse("{\"id\":\"list\",\"component\":{\"List\":{\"children\":{\"explicitList\":[\"one\",\"two\"]},\"direction\":\"vertical\",\"alignment\":\"start\"}}}"), list);

        var button = UiWire.button("button", "button-text", UiWire.action("submit", List.of()), true);
        assertEquals(parse("{\"id\":\"button\",\"component\":{\"Button\":{\"child\":\"button-text\",\"action\":{\"name\":\"submit\"},\"primary\":true}}}"), button);

        var textField = UiWire.textField(
            "text-field",
            UiWire.stringRefPath("/email"),
            UiWire.stringRefLiteral("Email"),
            "shortText",
            "^.+@.+$",
            UiWire.action("submit_form", List.of())
        );
        assertEquals(parse("{\"id\":\"text-field\",\"component\":{\"TextField\":{\"text\":{\"path\":\"/email\"},\"label\":{\"literalString\":\"Email\"},\"textFieldType\":\"shortText\",\"validationRegexp\":\"^.+@.+$\",\"onSubmittedAction\":{\"name\":\"submit_form\"}}}}"), textField);

        var checkBox = UiWire.checkBox("check", UiWire.stringRefLiteral("Agree"), UiWire.boolRefPath("/agree"));
        assertEquals(parse("{\"id\":\"check\",\"component\":{\"CheckBox\":{\"label\":{\"literalString\":\"Agree\"},\"value\":{\"path\":\"/agree\"}}}}"), checkBox);

        var card = UiWire.card("card", "card-content");
        assertEquals(parse("{\"id\":\"card\",\"component\":{\"Card\":{\"child\":\"card-content\"}}}"), card);

        var modal = UiWire.modal("modal", "open-btn", "modal-content");
        assertEquals(parse("{\"id\":\"modal\",\"component\":{\"Modal\":{\"entryPointChild\":\"open-btn\",\"contentChild\":\"modal-content\"}}}"), modal);

        var tabs = UiWire.tabs(
            "tabs",
            List.of(
                UiWire.tabItem(UiWire.stringRefLiteral("Overview"), "overview"),
                UiWire.tabItem(UiWire.stringRefLiteral("Details"), "details")
            )
        );
        assertEquals(parse("{\"id\":\"tabs\",\"component\":{\"Tabs\":{\"tabItems\":[{\"title\":{\"literalString\":\"Overview\"},\"child\":\"overview\"},{\"title\":{\"literalString\":\"Details\"},\"child\":\"details\"}]}}}"), tabs);

        var multipleChoice = UiWire.multipleChoice(
            "choices",
            UiWire.stringArrayRefPath("/choices"),
            List.of(
                UiWire.choiceOption(UiWire.stringRefLiteral("One"), "1"),
                UiWire.choiceOption(UiWire.stringRefLiteral("Two"), "2")
            ),
            1
        );
        assertEquals(parse("{\"id\":\"choices\",\"component\":{\"MultipleChoice\":{\"selections\":{\"path\":\"/choices\"},\"options\":[{\"label\":{\"literalString\":\"One\"},\"value\":\"1\"},{\"label\":{\"literalString\":\"Two\"},\"value\":\"2\"}],\"maxAllowedSelections\":1}}}"), multipleChoice);

        var slider = UiWire.slider("slider", UiWire.numberRefPath("/rating"), 0.0, 10.0);
        assertEquals(parse("{\"id\":\"slider\",\"component\":{\"Slider\":{\"value\":{\"path\":\"/rating\"},\"minValue\":0.0,\"maxValue\":10.0}}}"), slider);

        var dateTime = UiWire.dateTimeInput(
            "date",
            UiWire.stringRefPath("/date"),
            true,
            false,
            "2024-01-01",
            "2024-12-31"
        );
        assertEquals(parse("{\"id\":\"date\",\"component\":{\"DateTimeInput\":{\"value\":{\"path\":\"/date\"},\"enableDate\":true,\"enableTime\":false,\"firstDate\":\"2024-01-01\",\"lastDate\":\"2024-12-31\"}}}"), dateTime);

        var timeline = UiWire.timeline(
            "timeline",
            UiWire.childrenExplicit(List.of("item-1", "item-2")),
            "vertical",
            "start",
            UiWire.boolRefLiteral(true),
            "sequential",
            UiWire.stringRefLiteral("item-2")
        );
        assertEquals(parse("{\"id\":\"timeline\",\"component\":{\"Timeline\":{\"children\":{\"explicitList\":[\"item-1\",\"item-2\"]},\"orientation\":\"vertical\",\"alignment\":\"start\",\"autoFollow\":{\"literalBoolean\":true},\"laneMode\":\"sequential\",\"currentItemId\":{\"literalString\":\"item-2\"}}}}"), timeline);

        var timelineItem = UiWire.timelineItem(
            "item",
            "item-1",
            UiWire.stringRefLiteral("Deploy"),
            UiWire.stringRefLiteral("Step 1"),
            UiWire.stringRefLiteral("2024-01-01T00:00:00Z"),
            "step",
            "inProgress",
            "info",
            UiWire.stringRefLiteral("bolt"),
            "content",
            UiWire.action("open", List.of())
        );
        assertEquals(parse("{\"id\":\"item\",\"component\":{\"TimelineItem\":{\"itemId\":\"item-1\",\"title\":{\"literalString\":\"Deploy\"},\"subtitle\":{\"literalString\":\"Step 1\"},\"timestamp\":{\"literalString\":\"2024-01-01T00:00:00Z\"},\"kind\":\"step\",\"state\":\"inProgress\",\"severity\":\"info\",\"icon\":{\"literalString\":\"bolt\"},\"contentChild\":\"content\",\"action\":{\"name\":\"open\"}}}}"), timelineItem);

        var timelineGroup = UiWire.timelineGroup(
            "group",
            "group-1",
            UiWire.stringRefLiteral("Release"),
            UiWire.stringRefLiteral("v1.0"),
            UiWire.childrenExplicit(List.of("item-1")),
            UiWire.boolRefLiteral(false),
            UiWire.numberRefLiteral(2),
            "active"
        );
        assertEquals(parse("{\"id\":\"group\",\"component\":{\"TimelineGroup\":{\"groupId\":\"group-1\",\"title\":{\"literalString\":\"Release\"},\"summary\":{\"literalString\":\"v1.0\"},\"children\":{\"explicitList\":[\"item-1\"]},\"collapsed\":{\"literalBoolean\":false},\"badgeCount\":{\"literalNumber\":2.0},\"groupState\":\"active\"}}}"), timelineGroup);

        var timelineLane = UiWire.timelineLane(
            "lane",
            "lane-1",
            UiWire.stringRefLiteral("Primary"),
            UiWire.childrenExplicit(List.of("item-1"))
        );
        assertEquals(parse("{\"id\":\"lane\",\"component\":{\"TimelineLane\":{\"laneId\":\"lane-1\",\"title\":{\"literalString\":\"Primary\"},\"children\":{\"explicitList\":[\"item-1\"]}}}}"), timelineLane);

        var audio = UiWire.audioPlayer("audio", UiWire.stringRefLiteral("https://example.com/audio.mp3"));
        assertEquals(parse("{\"id\":\"audio\",\"component\":{\"AudioPlayer\":{\"url\":{\"literalString\":\"https://example.com/audio.mp3\"}}}}"), audio);

        var video = UiWire.video("video", UiWire.stringRefLiteral("https://example.com/video.mp4"));
        assertEquals(parse("{\"id\":\"video\",\"component\":{\"Video\":{\"url\":{\"literalString\":\"https://example.com/video.mp4\"}}}}"), video);
    }
}
