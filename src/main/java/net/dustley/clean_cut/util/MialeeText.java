package net.dustley.clean_cut.util;

import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.List;

public class MialeeText {
    /**
     * Takes a text and returns the same text but with the given int color.
     */
    public static Text withColor(Text text, int color) {
        Style style = text.getStyle().withColor(color);
        List<Text> styled = text.getWithStyle(style);
        if (styled.size() > 0) {
            return styled.get(0);
        }
        return text;
    }

    /**
     * Takes a text and returns the same text but with without italics.
     */
    public static Text withoutItalics(Text text) {
        List<Text> styled = text.getWithStyle(text.getStyle().withItalic(false));
        if (styled.size() > 0) {
            return styled.get(0);
        }
        return text;
    }
}