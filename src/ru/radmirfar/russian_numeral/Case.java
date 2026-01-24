package ru.radmirfar.russian_numeral;

/**
 * <p>Падеж:</p>
 * <ul>
 * <li>{@link #NOMINATIVE} - именительный</li>
 * <li>{@link #GENITIVE} - родительный</li>
 * <li>{@link #DATIVE} - дательный</li>
 * <li>{@link #ACCUSATIVE} - винительный</li>
 * <li>{@link #INSTRUMENTAL} - творительный</li>
 * <li>{@link #PREPOSITIONAL} - предложный</li>
 * </ul>
 */
public enum Case {
    /**
     * Именительный падеж
     */
    NOMINATIVE,
    /**
     * Родительный падеж
     */
    GENITIVE,
    /**
     * Дательный падеж
     */
    DATIVE,
    /**
     * Винительный падеж
     */
    ACCUSATIVE,
    /**
     * Творительный падеж
     */
    INSTRUMENTAL,
    /**
     * Предложный падеж
     */
    PREPOSITIONAL
}