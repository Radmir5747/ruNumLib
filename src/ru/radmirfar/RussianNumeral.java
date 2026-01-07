package ru.radmirfar;

public class RussianNumeral {
    /**
     * <p>Грамматический род:</p>
     * <ul>
     * <li>MASCULINE - мужской</li>
     * <li>FEMININE - женский</li>
     * <li>NEUTRAL - средний</li>
     * </ul>
     */
    enum Gender {
        /**
         * Мужской род
         */
        MASCULINE,
        /**
         * Женский род
         */
        FEMININE,
        /**
         * Средний род
         */
        NEUTRAL
    }

    /**
     * <p>Падеж:</p>
     * <ul>
     * <li>NOMINATIVE - именительный</li>
     * <li>GENITIVE - родительный</li>
     * <li>DATIVE - дательный</li>
     * <li>ACCUSATIVE - винительный</li>
     * <li>INSTRUMENTAL - творительный</li>
     * <li>PREPOSITIONAL - предложный</li>
     * </ul>
     */
    enum Case {
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

    /**
     * <p>Число (для числительных типа один, тысяча):</p>
     * <ul>
     * <li>SINGULAR - единственное</li>
     * <li>PLURAL - множественное</li>
     * </ul>
     */
    enum Count {
        /**
         * Единственное число
         */
        SINGULAR,
        /**
         * Множественное число
         */
        PLURAL
    }

    /**
     * <p>Разряд числительного:</p>
     * <ul>
     * <li>CARDINAL - количественное</li>
     * <li>ORDINAL - порядковое</li>
     * </ul>
     */
    enum Type {
        /**
         * Количественное числительное
         */
        CARDINAL,
        /**
         * Порядковое числительное
         */
        ORDINAL
    }

    /**
     * <p>Одушевлённость существительного, к которому относится числительное:</p>
     * <ul>
     * <li>ANIMATE - одушевлённое</li>
     * <li>INANIMATE - неодушевлённое</li>
     * </ul>
     */
    enum Animacy {
        /**
         * Одушевлённое существительное
         */
        ANIMATE,
        /**
         * Неодушевлённое существительное
         */
        INANIMATE
    }
}
