package ru.radmirfar.russian_numeral;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeclensionTest {

    @Test
    @DisplayName("Проверка согласования числительного")
    void supplementalDeclension() {
        Declension baseDeclension = new Declension(null, Case.INSTRUMENTAL, null, null, null);
        Declension nomDeclension = new Declension(null, Case.NOMINATIVE, null, null, null);
        Declension test0 = Declension.supplementalDeclension(0, true, baseDeclension);
        assertAll(() -> assertEquals(Count.PLURAL, test0.count),
                () -> assertEquals(Case.GENITIVE, test0.gramCase));
        Declension test1 = Declension.supplementalDeclension(21, true, baseDeclension);
        assertAll(() -> assertEquals(Count.SINGULAR, test1.count),
                () -> assertEquals(baseDeclension.gramCase, test1.gramCase));
        for (int i = 22; i < 25; i++) {
            Declension test2 = Declension.supplementalDeclension(i, true, baseDeclension);
            assertAll(() -> assertEquals(Count.PLURAL, test2.count),
                    () -> assertEquals(baseDeclension.gramCase, test2.gramCase));
            Declension test2_nom = Declension.supplementalDeclension(i, true, nomDeclension);
            assertAll(() -> assertEquals(Count.PLURAL, test2_nom.count),
                    () -> assertEquals(nomDeclension.gramCase, test2_nom.gramCase));
            Declension test2_nom_1000 = Declension.supplementalDeclension(i, false, nomDeclension);
            assertAll(() -> assertEquals(Count.SINGULAR, test2_nom_1000.count),
                    () -> assertEquals(Case.GENITIVE, test2_nom_1000.gramCase));
        }
        int[] testNums = {5, 11, 12, 13, 14};
        for (int i : testNums) {
            Declension test_others = Declension.supplementalDeclension(i, true, baseDeclension);
            assertAll(() -> assertEquals(Count.PLURAL, test_others.count),
                    () -> assertEquals(baseDeclension.gramCase, test_others.gramCase));
            Declension test_others_nom = Declension.supplementalDeclension(i, true, nomDeclension);
            assertAll(() -> assertEquals(Count.PLURAL, test_others_nom.count),
                    () -> assertEquals(Case.GENITIVE, test_others_nom.gramCase));
        }
    }
}