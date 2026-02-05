package ru.radmirfar.russian_numeral;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FractionTest {
    @Test
    @DisplayName("Проверка нормализации дроби")
    void check() {
        System.out.println("Простые дроби");
        Fraction f1 = new Fraction(1, -1);
        assertAll(() -> assertEquals(-1, f1.getNumerator()),
                () -> assertEquals(1, f1.getDenominator()));
        Fraction f2 = new Fraction(-1, -1);
        assertAll(() -> assertEquals(1, f2.numerator),
                () -> assertEquals(1, f2.denominator));
        f2.setDenominator(-2);
        assertAll(() -> assertEquals(-1, f2.numerator),
                () -> assertEquals(2, f2.denominator));
        f2.setDenominator(-3);
        assertAll(() -> assertEquals(1, f2.numerator),
                () -> assertEquals(3, f2.denominator));
        System.out.println("Смешанные дроби");
        Fraction f3 = new Fraction(1, -2, 3);
        assertAll(() -> assertEquals(-1, f3.getWhole()),
                () -> assertEquals(2, f3.getNumerator()),
                () -> assertEquals(3, f3.getDenominator()));
        Fraction f4 = new Fraction(1, 2, -3);
        assertAll(() -> assertEquals(-1, f4.getWhole()),
                () -> assertEquals(2, f4.getNumerator()),
                () -> assertEquals(3, f4.getDenominator()));
        Fraction f5 = new Fraction(-1, -2, -3);
        assertAll(() -> assertEquals(-1, f5.getWhole()),
                () -> assertEquals(2, f5.getNumerator()),
                () -> assertEquals(3, f5.getDenominator()));
        f5.setNumerator(-1);
        assertAll(() -> assertEquals(1, f5.getWhole()),
                () -> assertEquals(1, f5.getNumerator()),
                () -> assertEquals(3, f5.getDenominator()));
        f5.setDenominator(-2);
        assertAll(() -> assertEquals(-1, f5.getWhole()),
                () -> assertEquals(1, f5.getNumerator()),
                () -> assertEquals(2, f5.getDenominator()));
    }
}