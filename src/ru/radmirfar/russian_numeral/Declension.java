package ru.radmirfar.russian_numeral;

/**
 * <p>Класс, описывающий грамматические признаки числительного:
 * {@link Gender грамматический род}, {@link Case падеж},
 * {@link Count грамматическое число} (для числительных типа один, тысяча),
 * {@link Type разряд}, {@link Animacy одушевлённость существительного, к которому относится числительное}.</p>
 * <p>Объект этого класса передаётся как аргумент в функции, выдающие числительное.</p>
 */
public class Declension {
    final Gender gender;
    final Case gramCase;
    final Count count;
    final Type type;
    final Animacy animacy;

    /**
     * Выдаёт грамматический род числительного.
     * @return род
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Выдаёт падеж числительного.
     * @return падеж
     */
    public Case getGramCase() {
        return gramCase;
    }

    /**
     * Выдаёт грамматическое число (для числительных типа один, тысяча).
     * @return грамматическое число
     */
    public Count getCount() {
        return count;
    }

    /**
     * Выдаёт разряд числительного.
     * @return разряд
     */
    public Type getType() {
        return type;
    }

    /**
     * Выдаёт одушевлённость существительного, к которому относится числительное.
     * @return одушевлённость
     */
    public Animacy getAnimacy() {
        return animacy;
    }

    /**
     * Конструктор.
     * @param gender род
     * @param gramCase падеж
     * @param count грамматическое число
     * @param type разряд
     * @param animacy одушевлённость
     */
    Declension(Gender gender, Case gramCase, Count count, Type type, Animacy animacy) {
        this.gender = gender;
        this.gramCase = gramCase;
        this.count = count;
        this.type = type;
        this.animacy = animacy;
    }

    /**
     * <p>Вспомогательная функция для сокращения условий.</p>
     * @return true, если падеж именительный или винительный
     */
    boolean isNomAcc() {
        return gramCase == Case.NOMINATIVE || gramCase == Case.ACCUSATIVE;
    }

    /**
     * <p>Вспомогательная функция для сокращения условий
     * (для учёта одушевлённости в числительных, подобных прилагательным).</p>
     * @return true, если падеж винительный, а также мужской род или множественное число.
     */
    boolean adjCheck() {
        return gramCase == Case.ACCUSATIVE && (gender == Gender.MASCULINE || count == Count.PLURAL);
    }
}