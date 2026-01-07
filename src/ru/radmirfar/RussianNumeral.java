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
    public enum Gender {
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

    /**
     * <p>Число (для числительных типа один, тысяча):</p>
     * <ul>
     * <li>SINGULAR - единственное</li>
     * <li>PLURAL - множественное</li>
     * </ul>
     */
    public enum Count {
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
    public enum Type {
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

    /**
     * Выдаёт число прописью.
     * @param num число
     * @param gender грамматический род
     * @param gramCase падеж
     * @param type разряд числительного
     * @param count грамматическое число
     * @return число прописью
     */
    public static String getNumeral(int num, Gender gender, Case gramCase, Type type, Count count) {
        if (type == Type.CARDINAL) return getCardinalNumeral(num, gender, gramCase, count);
        return getOrdinalNumeral(num, gender, gramCase, count);
    }

    /* дробные числительные бывают только количественными */
    /**
     * Выдаёт дробное количественное числительное в нужном падеже.
     * @param num десятичная дробь
     * @param gram_case падеж
     * @return число прописью
     */
    public static String getNumeral(double num, Case gram_case) {
        String res = "";
        return res;
    }

    /**
     * Выдаёт дробное количественное числительное в нужном падеже.
     * @param num простая или смешанная дробь
     * @param gram_case падеж
     * @return число прописью
     */
    public static String getNumeral(Fraction num, Case gram_case) {
        String res = "";
        return res;
    }
    /**
     * Выдаёт порядковое числительное в нужной форме.
     * @param num число
     * @param gender грамматический род
     * @param gramCase падеж
     * @param count грамматическое число
     * @return число прописью
     */
    private static String getOrdinalNumeral(int num, Gender gender, Case gramCase, Count count) {
        String res = "";
        return res;
    }
    /**
     * Выдаёт количественное числительное в нужной форме.
     * @param num число
     * @param gender грамматический род
     * @param gramCase падеж
     * @param count грамматическое число
     * @return число прописью
     */
    private static String getCardinalNumeral(int num, Gender gender, Case gramCase, Count count) {
        String res = "";
        return res;
    }

    /**
     * <p>Дробь. Не предоставляет возможности проводить какие-либо операции, служит исключительно как "обёртка".</p>
     * <p>Поддерживаются простые (2/3, 5/2) и смешанные (1 2/3) дроби, в т.ч. и отрицательные.</p>
     */
    public static class Fraction {
        int whole, numerator, denominator; // целая часть, числитель, знаменатель

        /**
         * Геттер числителя дроби
         * @return числитель
         */
        public int getNumerator() {
            return numerator;
        }

        /**
         * Сеттер числителя дроби
         * @param numerator числитель
         */
        public void setNumerator(int numerator) {
            this.numerator = numerator;
            prepareFraction();
        }

        /**
         * Геттер знаменателя дроби
         * @return знаменатель
         */
        public int getDenominator() {
            return denominator;
        }

        /**
         * Сеттер знаменателя дроби. Выдаёт исключение, если попытаться задать значение 0.
         * @param denominator знаменатель
         * @throws ArithmeticException если знаменатель равен нулю
         */
        public void setDenominator(int denominator) {
            if (denominator == 0) throw new ArithmeticException("/ by zero"); // знаменатель не может равняться нулю
            this.denominator = denominator;
            prepareFraction();
        }

        /**
         * Геттер целой части смешанной дроби
         * @return целое
         */
        public int getWhole() {
            return whole;
        }

        /**
         * Сеттер целой части смешанной дроби
         * @param whole целое
         */
        public void setWhole(int whole) {
            this.whole = whole;
            prepareFraction();
        }

        /**
         * Приведение дроби к единому виду для корректности выполнения функций.
         */
        private void prepareFraction() {
            /* имеется целая часть и числитель / знаменатель отрицательный */
            if (whole != 0 && (numerator < 0 || denominator < 0)) {
                whole *= -1; // переносим минус на целую часть
                /* убираем минусы у числителя / знаменателя */
                if (numerator < 0) numerator *= -1;
                if (denominator < 0) denominator *= -1;
            }
            /*
             * Нет целой части и отрицательный знаменатель - переносим минус в числитель.
             * Почему это работает? Если знаменатель меньше нуля и:
             * - числитель меньше нуля - дробь становится положительной
             * - числитель больше нуля - знаки числителя и знаменателя меняются местами
             * */
            else if (denominator < 0) {
                numerator *= -1;
                denominator *= -1;
            }
        }

        /**
         * Конструктор для простой дроби
         * @param numerator числитель
         * @param denominator знаменатель
         */
        public Fraction(int numerator, int denominator) {
            setNumerator(numerator);
            setDenominator(denominator);
            setWhole(0);
        }

        /**
         * Конструктор для смешанной дроби
         * @param whole целая часть
         * @param numerator числитель
         * @param denominator знаменатель
         */
        public Fraction(int whole, int numerator, int denominator) {
            setNumerator(numerator);
            setDenominator(denominator);
            setWhole(whole);
        }
    }
}
