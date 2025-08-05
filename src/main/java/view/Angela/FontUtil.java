package view.Angela;

import java.awt.Font;

/**
 * Utility class to ensure consistent font usage across all UI components.
 * This prevents font rendering issues with certain uppercase letters (W, E, L, F).
 */
public class FontUtil {
    private static Font STANDARD_FONT = null;
    private static Font BOLD_FONT = null;
    private static Font SMALL_FONT = null;
    private static Font LARGE_FONT = null;
    
    /**
     * Gets the standard font for most UI components.
     * @return SansSerif plain font, size 12
     */
    public static Font getStandardFont() {
        if (STANDARD_FONT == null) {
            STANDARD_FONT = new Font("SansSerif", Font.PLAIN, 12);
        }
        return STANDARD_FONT;
    }
    
    /**
     * Gets the bold font for emphasis.
     * @return SansSerif bold font, size 12
     */
    public static Font getBoldFont() {
        if (BOLD_FONT == null) {
            BOLD_FONT = new Font("SansSerif", Font.BOLD, 12);
        }
        return BOLD_FONT;
    }
    
    /**
     * Gets a smaller font for compact displays.
     * @return SansSerif plain font, size 11
     */
    public static Font getSmallFont() {
        if (SMALL_FONT == null) {
            SMALL_FONT = new Font("SansSerif", Font.PLAIN, 11);
        }
        return SMALL_FONT;
    }
    
    /**
     * Gets a larger font for headers or emphasis.
     * @return SansSerif plain font, size 14
     */
    public static Font getLargeFont() {
        if (LARGE_FONT == null) {
            LARGE_FONT = new Font("SansSerif", Font.PLAIN, 14);
        }
        return LARGE_FONT;
    }
}