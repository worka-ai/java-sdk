package io.modelcontextprotocol.common.a2ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class A2uiTest {
    private final ObjectMapper mapper = new ObjectMapper();

    private Map<String, Object> parse(String json) throws Exception {
        return mapper.readValue(json, new TypeReference<>() {});
    }

    @Test
    void serializesCoreWidgets() throws Exception {
        var text = A2ui.text("text", A2ui.stringRefLiteral("Hello"), "h1");
        assertEquals(parse("{\"id\":\"text\",\"component\":{\"Text\":{\"text\":{\"literalString\":\"Hello\"},\"usageHint\":\"h1\"}}}"), text);

        var image = A2ui.image("img", A2ui.stringRefLiteral("https://example.com/logo.png"), "cover", "mediumFeature");
        assertEquals(parse("{\"id\":\"img\",\"component\":{\"Image\":{\"url\":{\"literalString\":\"https://example.com/logo.png\"},\"fit\":\"cover\",\"usageHint\":\"mediumFeature\"}}}"), image);

        var icon = A2ui.icon("icon", A2ui.stringRefLiteral("check"));
        assertEquals(parse("{\"id\":\"icon\",\"component\":{\"Icon\":{\"name\":{\"literalString\":\"check\"}}}}"), icon);

        var divider = A2ui.divider("divider", "horizontal");
        assertEquals(parse("{\"id\":\"divider\",\"component\":{\"Divider\":{\"axis\":\"horizontal\"}}}"), divider);

        var row = A2ui.row("row", A2ui.childrenExplicit(List.of("a", "b")), "spaceBetween", "center");
        assertEquals(parse("{\"id\":\"row\",\"component\":{\"Row\":{\"children\":{\"explicitList\":[\"a\",\"b\"]},\"distribution\":\"spaceBetween\",\"alignment\":\"center\"}}}"), row);

        var column = A2ui.column("column", A2ui.childrenTemplate("item", "/items"), "start", "stretch");
        assertEquals(parse("{\"id\":\"column\",\"component\":{\"Column\":{\"children\":{\"template\":{\"componentId\":\"item\",\"dataBinding\":\"/items\"}},\"distribution\":\"start\",\"alignment\":\"stretch\"}}}"), column);

        var list = A2ui.list("list", A2ui.childrenExplicit(List.of("one", "two")), "vertical", "start");
        assertEquals(parse("{\"id\":\"list\",\"component\":{\"List\":{\"children\":{\"explicitList\":[\"one\",\"two\"]},\"direction\":\"vertical\",\"alignment\":\"start\"}}}"), list);

        var button = A2ui.button("button", "button-text", A2ui.action("submit", List.of()), true);
        assertEquals(parse("{\"id\":\"button\",\"component\":{\"Button\":{\"child\":\"button-text\",\"action\":{\"name\":\"submit\"},\"primary\":true}}}"), button);

        var textField = A2ui.textField(
            "text-field",
            A2ui.stringRefPath("/email"),
            A2ui.stringRefLiteral("Email"),
            "shortText",
            "^.+@.+$",
            A2ui.action("submit_form", List.of())
        );
        assertEquals(parse("{\"id\":\"text-field\",\"component\":{\"TextField\":{\"text\":{\"path\":\"/email\"},\"label\":{\"literalString\":\"Email\"},\"textFieldType\":\"shortText\",\"validationRegexp\":\"^.+@.+$\",\"onSubmittedAction\":{\"name\":\"submit_form\"}}}}"), textField);

        var checkBox = A2ui.checkBox("check", A2ui.stringRefLiteral("Agree"), A2ui.boolRefPath("/agree"));
        assertEquals(parse("{\"id\":\"check\",\"component\":{\"CheckBox\":{\"label\":{\"literalString\":\"Agree\"},\"value\":{\"path\":\"/agree\"}}}}"), checkBox);

        var card = A2ui.card("card", "card-content");
        assertEquals(parse("{\"id\":\"card\",\"component\":{\"Card\":{\"child\":\"card-content\"}}}"), card);

        var modal = A2ui.modal("modal", "open-btn", "modal-content");
        assertEquals(parse("{\"id\":\"modal\",\"component\":{\"Modal\":{\"entryPointChild\":\"open-btn\",\"contentChild\":\"modal-content\"}}}"), modal);

        var tabs = A2ui.tabs(
            "tabs",
            List.of(
                A2ui.tabItem(A2ui.stringRefLiteral("Overview"), "overview"),
                A2ui.tabItem(A2ui.stringRefLiteral("Details"), "details")
            )
        );
        assertEquals(parse("{\"id\":\"tabs\",\"component\":{\"Tabs\":{\"tabItems\":[{\"title\":{\"literalString\":\"Overview\"},\"child\":\"overview\"},{\"title\":{\"literalString\":\"Details\"},\"child\":\"details\"}]}}}"), tabs);

        var multipleChoice = A2ui.multipleChoice(
            "choices",
            A2ui.stringArrayRefPath("/choices"),
            List.of(
                A2ui.choiceOption(A2ui.stringRefLiteral("One"), "1"),
                A2ui.choiceOption(A2ui.stringRefLiteral("Two"), "2")
            ),
            1
        );
        assertEquals(parse("{\"id\":\"choices\",\"component\":{\"MultipleChoice\":{\"selections\":{\"path\":\"/choices\"},\"options\":[{\"label\":{\"literalString\":\"One\"},\"value\":\"1\"},{\"label\":{\"literalString\":\"Two\"},\"value\":\"2\"}],\"maxAllowedSelections\":1}}}"), multipleChoice);

        var slider = A2ui.slider("slider", A2ui.numberRefPath("/rating"), 0.0, 10.0);
        assertEquals(parse("{\"id\":\"slider\",\"component\":{\"Slider\":{\"value\":{\"path\":\"/rating\"},\"minValue\":0.0,\"maxValue\":10.0}}}"), slider);

        var dateTime = A2ui.dateTimeInput(
            "date",
            A2ui.stringRefPath("/date"),
            true,
            false,
            "2024-01-01",
            "2024-12-31"
        );
        assertEquals(parse("{\"id\":\"date\",\"component\":{\"DateTimeInput\":{\"value\":{\"path\":\"/date\"},\"enableDate\":true,\"enableTime\":false,\"firstDate\":\"2024-01-01\",\"lastDate\":\"2024-12-31\"}}}"), dateTime);

        var timeline = A2ui.timeline(
            "timeline",
            A2ui.childrenExplicit(List.of("item-1", "item-2")),
            "vertical",
            "start",
            A2ui.boolRefLiteral(true),
            "sequential",
            A2ui.stringRefLiteral("item-2")
        );
        assertEquals(parse("{\"id\":\"timeline\",\"component\":{\"Timeline\":{\"children\":{\"explicitList\":[\"item-1\",\"item-2\"]},\"orientation\":\"vertical\",\"alignment\":\"start\",\"autoFollow\":{\"literalBoolean\":true},\"laneMode\":\"sequential\",\"currentItemId\":{\"literalString\":\"item-2\"}}}}"), timeline);

        var timelineItem = A2ui.timelineItem(
            "item",
            "item-1",
            A2ui.stringRefLiteral("Deploy"),
            A2ui.stringRefLiteral("Step 1"),
            A2ui.stringRefLiteral("2024-01-01T00:00:00Z"),
            "step",
            "inProgress",
            "info",
            A2ui.stringRefLiteral("bolt"),
            "content",
            A2ui.action("open", List.of())
        );
        assertEquals(parse("{\"id\":\"item\",\"component\":{\"TimelineItem\":{\"itemId\":\"item-1\",\"title\":{\"literalString\":\"Deploy\"},\"subtitle\":{\"literalString\":\"Step 1\"},\"timestamp\":{\"literalString\":\"2024-01-01T00:00:00Z\"},\"kind\":\"step\",\"state\":\"inProgress\",\"severity\":\"info\",\"icon\":{\"literalString\":\"bolt\"},\"contentChild\":\"content\",\"action\":{\"name\":\"open\"}}}}"), timelineItem);

        var timelineGroup = A2ui.timelineGroup(
            "group",
            "group-1",
            A2ui.stringRefLiteral("Release"),
            A2ui.stringRefLiteral("v1.0"),
            A2ui.childrenExplicit(List.of("item-1")),
            A2ui.boolRefLiteral(false),
            A2ui.numberRefLiteral(2),
            "active"
        );
        assertEquals(parse("{\"id\":\"group\",\"component\":{\"TimelineGroup\":{\"groupId\":\"group-1\",\"title\":{\"literalString\":\"Release\"},\"summary\":{\"literalString\":\"v1.0\"},\"children\":{\"explicitList\":[\"item-1\"]},\"collapsed\":{\"literalBoolean\":false},\"badgeCount\":{\"literalNumber\":2.0},\"groupState\":\"active\"}}}"), timelineGroup);

        var timelineLane = A2ui.timelineLane(
            "lane",
            "lane-1",
            A2ui.stringRefLiteral("Primary"),
            A2ui.childrenExplicit(List.of("item-1"))
        );
        assertEquals(parse("{\"id\":\"lane\",\"component\":{\"TimelineLane\":{\"laneId\":\"lane-1\",\"title\":{\"literalString\":\"Primary\"},\"children\":{\"explicitList\":[\"item-1\"]}}}}"), timelineLane);

        var audio = A2ui.audioPlayer("audio", A2ui.stringRefLiteral("https://example.com/audio.mp3"));
        assertEquals(parse("{\"id\":\"audio\",\"component\":{\"AudioPlayer\":{\"url\":{\"literalString\":\"https://example.com/audio.mp3\"}}}}"), audio);

        var video = A2ui.video("video", A2ui.stringRefLiteral("https://example.com/video.mp4"));
        assertEquals(parse("{\"id\":\"video\",\"component\":{\"Video\":{\"url\":{\"literalString\":\"https://example.com/video.mp4\"}}}}"), video);
    }
}
