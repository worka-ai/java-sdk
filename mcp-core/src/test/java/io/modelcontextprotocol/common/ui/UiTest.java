package io.modelcontextprotocol.common.ui;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class UiTest {
    private void assertKind(Ui.UiWidget widget, String expected) {
        var result = Ui.render(widget);
        var entry = result.components.get(0);
        var component = (java.util.Map<String, Object>) entry.get("component");
        assertTrue(component.containsKey(expected));
    }

    @Test
    void serializesTypedWidgets() {
        assertKind(new Ui.Text(new Ui.UiStringLiteral("Hello"), "h1"), "Text");
        assertKind(new Ui.Image(new Ui.UiStringLiteral("https://example.com"), null, null), "Image");
        assertKind(new Ui.Icon(new Ui.UiStringLiteral("check")), "Icon");
        assertKind(new Ui.Divider("horizontal"), "Divider");
        assertKind(new Ui.Row(new Ui.UiChildrenItems(java.util.List.of()), null, null), "Row");
        assertKind(new Ui.Column(new Ui.UiChildrenItems(java.util.List.of()), null, null), "Column");
        assertKind(new Ui.ListWidget(new Ui.UiChildrenItems(java.util.List.of()), "vertical", null), "List");
        assertKind(new Ui.Button(new Ui.Text(new Ui.UiStringLiteral("Click"), null), new Ui.UiAction("submit"), true), "Button");
        assertKind(new Ui.TextField(new Ui.UiStringLiteral("value"), null, null, null, null), "TextField");
        assertKind(new Ui.CheckBox(new Ui.UiStringLiteral("Agree"), new Ui.UiBoolLiteral(true)), "CheckBox");
        assertKind(new Ui.Card(new Ui.Text(new Ui.UiStringLiteral("Card"), null)), "Card");
        assertKind(new Ui.Modal(new Ui.Text(new Ui.UiStringLiteral("Open"), null), new Ui.Text(new Ui.UiStringLiteral("Body"), null)), "Modal");
        assertKind(new Ui.Tabs(java.util.List.of(new Ui.TabItem(new Ui.UiStringLiteral("Tab"), new Ui.Text(new Ui.UiStringLiteral("Body"), null)))), "Tabs");
        assertKind(new Ui.MultipleChoice(new Ui.UiStringArrayPath("/choices"), java.util.List.of(new Ui.ChoiceOption(new Ui.UiStringLiteral("One"), "1")), 1), "MultipleChoice");
        assertKind(new Ui.Slider(new Ui.UiNumberLiteral(1), null, null), "Slider");
        assertKind(new Ui.DateTimeInput(new Ui.UiStringLiteral("2024-01-01"), null, null, null, null), "DateTimeInput");
        assertKind(new Ui.AudioPlayer(new Ui.UiStringLiteral("https://example.com/audio.mp3")), "AudioPlayer");
        assertKind(new Ui.Video(new Ui.UiStringLiteral("https://example.com/video.mp4")), "Video");
        assertKind(new Ui.Timeline(new Ui.UiChildrenItems(java.util.List.of()), null, null, null, null, null), "Timeline");
        assertKind(new Ui.TimelineItem("item-1", null, null, null, null, null, null, null, null, null), "TimelineItem");
        assertKind(new Ui.TimelineGroup("group-1", null, null, new Ui.UiChildrenItems(java.util.List.of()), null, null, null), "TimelineGroup");
        assertKind(new Ui.TimelineLane("lane-1", null, new Ui.UiChildrenItems(java.util.List.of())), "TimelineLane");
    }
}
