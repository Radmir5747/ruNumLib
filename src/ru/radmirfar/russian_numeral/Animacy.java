package ru.radmirfar.russian_numeral;

/**
 * <p>Одушевлённость существительного, к которому относится числительное:</p>
 * <ul>
 * <li>{@link #ANIMATE} - одушевлённое</li>
 * <li>{@link #INANIMATE} - неодушевлённое</li>
 * </ul>
 */
public enum Animacy {
    /**
     * Одушевлённое существительное
     */
    ANIMATE,
    /**
     * Неодушевлённое существительное
     */
    INANIMATE
}