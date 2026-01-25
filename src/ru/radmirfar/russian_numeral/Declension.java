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
        if (gramCase == null) throw new IllegalArgumentException("Missing grammatical case");
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

    /**
     * <p>Вспомогательная функция, выдаёт набор грамматических характеристик для согласования разрядов в
     * числительных типа одна тысяча, пять миллионов и проч.</p>
     * @param num число, отражающее три цифры перед разрядом
     * @param d грамматические характеристики исходного числа
     * @param isThousand является ли разряд тысячей
     * @return грамматические характеристики разряда
     */
    static Declension supplementalDeclension(int num, boolean isThousand, Declension d) {
        /*
         * Приводим число к двузначному за исключением случаев, когда оно больше двух знаков и кратно 100
         * (для избежания ошибок типа 200 000 - *двумястами тысяч)
         */
        if (!(num % 100 == 0 && num / 100 != 0)) num %= 100;
        if (num == 0) {
            /*
             Ноль управляет словами, ставя их в форму родительного падежа множественного числа:
             ноль тысяч, нуля тысяч, нулю тысяч, нулём тысяч, о нуле тысяч
            */
            return new Declension(null, Case.GENITIVE, Count.PLURAL, null, null);
        }
        if (num % 10 == 1 && num != 11) {
            // если число оканчивается на 1, то оно ставит слово в форму единственного числа, сохраняя его падеж
            return new Declension(null, d.gramCase, Count.SINGULAR, null, null);
        }
        if ((num % 10 == 2 && num != 12) || (num % 10 == 3 && num != 13) || (num % 10 == 4 && num != 14)) {
            /*
             Числа, оканчивающиеся на 2, 3, 4 кроме чисел 12, 13, 14.
             Для числительных миллион, миллиард и т.п. именительный и винительный падеж принимают форму
             родительного падежа единственного числа: два миллиона (не *два миллионы)
            */
            if (d.isNomAcc() && !isThousand) {
                return new Declension(null, Case.GENITIVE, Count.SINGULAR, null, null);
            }
            // в остальных падежах ставим слово в форму множественного числа, сохраняя его падеж
            return new Declension(null, d.gramCase, Count.PLURAL, null, null);
        }
        /*
         Для всех остальных чисел.
         Именительный и винительный падеж принимают форму родительного падежа множественного числа:
         пять тысяч, миллионов (не *пять тысячи, *пять миллионы). Иначе падеж сохраняется.
        */
        return new Declension(null, d.isNomAcc() ? Case.GENITIVE : d.gramCase, Count.PLURAL, null, null);
    }
}