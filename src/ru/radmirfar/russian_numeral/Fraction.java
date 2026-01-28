package ru.radmirfar.russian_numeral;

/**
 * <p>Дробь. Не предоставляет возможности проводить какие-либо операции, служит исключительно как "обёртка".</p>
 * <p>Поддерживаются простые (2/3, 5/2) и смешанные (1 2/3) дроби, в т.ч. и отрицательные.</p>
 */
public class Fraction {
    /**
     * Целая часть
     */
    int whole;
    /**
     * Числитель
     */
    int numerator;
    /**
     * Знаменатель
     */
    int denominator;

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