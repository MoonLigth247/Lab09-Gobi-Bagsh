package mn.edu.must.sqat;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ScholarshipCalculatorTest {

    private final ScholarshipCalculator calc = new ScholarshipCalculator();

    // ===== Хэсэг A: Зөвхөн happy path (5 тест) =====

    @Test
    void letterGrade_A() {
        assertEquals("A", calc.letterGrade(95));
    }

    @Test
    void letterGrade_B() {
        assertEquals("B", calc.letterGrade(85));
    }

    @Test
    void gpa_twoScores() {
        assertEquals(3.5, calc.gpa(new double[]{95, 85}), 0.01);
    }

    @Test
    void gpa_average() {
        assertEquals(3.0, calc.gpa(new double[]{95, 75}), 0.01);
    }

    @Test
    void scholarship_full() {
        assertEquals(100, calc.scholarship(3.9, 16, false));
    }
    // ===== Хэсэг B: Branch 100% хүргэх тестүүд =====

    // ----- letterGrade()-ийн exception замууд -----
    @Test
    void letterGrade_negative_throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(-1));
    }

    @Test
    void letterGrade_above100_throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(101));
    }

    // ----- gpa()-ийн exception замууд (|| 2 нөхцөл тусдаа!) -----
    @Test
    void gpa_null_throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.gpa(null));
    }

    @Test
    void gpa_empty_throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.gpa(new double[]{}));
    }

    // ----- gpa() switch-ийн бүх case -----
    @Test
    void gpa_gradeC() {
        assertEquals(2.0, calc.gpa(new double[]{75}), 0.01); // C=2
    }

    @Test
    void gpa_gradeD() {
        assertEquals(1.0, calc.gpa(new double[]{65}), 0.01); // D=1
    }

    @Test
    void gpa_gradeF() {
        assertEquals(0.0, calc.gpa(new double[]{50}), 0.01); // F=0 (default)
    }

    // ----- scholarship()-ийн || 3 нөхцөл тусдаа -----
    @Test
    void scholarship_negativeGpa_throws() {
        assertThrows(IllegalArgumentException.class,
            () -> calc.scholarship(-0.1, 15, false));  // gpa<0 → true
    }

    @Test
    void scholarship_gpaAbove4_throws() {
        assertThrows(IllegalArgumentException.class,
            () -> calc.scholarship(4.1, 15, false));   // gpa>4 → true
    }

    @Test
    void scholarship_negativeCredits_throws() {
        assertThrows(IllegalArgumentException.class,
            () -> calc.scholarship(3.5, -1, false));   // credits<0 → true
    }

    // ----- scholarship() hasViolation=true -----
    @Test
    void scholarship_violation_returnsZero() {
        assertEquals(0, calc.scholarship(3.9, 16, true));
    }

    // ----- scholarship() && F салаанууд -----
    @Test
    void scholarship_gpaHighCreditsLow_returns50() {
        // gpa>=3.8 true, credits>=15 FALSE → 2-р if руу
        assertEquals(50, calc.scholarship(3.9, 10, false));
    }

    @Test
    void scholarship_gpa34CreditsLow_returns50() {
        // gpa>=3.4 true, credits>=12 FALSE → 3-р if руу
        assertEquals(50, calc.scholarship(3.5, 10, false));
    }

    @Test
    void scholarship_70() {
        assertEquals(70, calc.scholarship(3.5, 12, false));
    }

    @Test
    void scholarship_50() {
        assertEquals(50, calc.scholarship(3.0, 10, false));
    }

    @Test
    void scholarship_gpaLow_returns0() {
        // gpa>=3.0 FALSE → return 0
        assertEquals(0, calc.scholarship(2.9, 10, false));
    }
    // ===== Хэсэг C: Давталтын зааг (0 / 1 / 2+) =====

    // 0 удаа — хоосон массив
    @Test
    void gpa_loop_zeroIterations_empty_throws() {
        assertThrows(IllegalArgumentException.class,
            () -> calc.gpa(new double[]{}));
    }

    // 0 удаа — null
    @Test
    void gpa_loop_zeroIterations_null_throws() {
        assertThrows(IllegalArgumentException.class,
            () -> calc.gpa(null));
    }

    // 1 удаа
    @Test
    void gpa_loop_oneIteration() {
        assertEquals(4.0, calc.gpa(new double[]{95}), 0.01);
    }

    // 2+ удаа
    @Test
    void gpa_loop_manyIterations() {
        assertEquals(2.5, calc.gpa(new double[]{95, 85, 75, 65}), 0.01);
    }
    // ===== Хэсэг D: Зааг утгын тестүүд (АЛДАА илрүүлнэ!) =====

    @Test
    void letterGrade_60_isD() {
        assertEquals("D", calc.letterGrade(60));
    }

    @Test
    void letterGrade_59_99_isF() {
        assertEquals("F", calc.letterGrade(59.99));
    }

    @Test
    void letterGrade_69_99_isD() {
        assertEquals("D", calc.letterGrade(69.99));
    }

    @Test
    void letterGrade_0_isF() {
        assertEquals("F", calc.letterGrade(0));
    }

    @Test
    void letterGrade_100_isA() {
        assertEquals("A", calc.letterGrade(100));
    }

    @Test
    void letterGrade_89_99_isB() {
        assertEquals("B", calc.letterGrade(89.99));
    }

    @Test
    void letterGrade_90_isA() {
        assertEquals("A", calc.letterGrade(90));
    }
}
