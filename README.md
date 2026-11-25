# Лаборатори №9 — JaCoCo код хамралт

**Оюутан:** Н.Мөнхбаяр
**Оюутны код:** B242270058
**Хичээл:** F.CSA313 — Программ хангамжийн чанарын баталгаа ба тест 

---

## Орчны хувилбар

### `java -version`
debain13@debian:~/Lab09-Gobi-Bagsh$ java -version
openjdk version "17.0.21" 2026-10-20
OpenJDK Runtime Environment Temurin-17.0.21+9 (build 17.0.21+9)
OpenJDK 64-Bit Server VM Temurin-17.0.21+9 (build 17.0.21+9, mixed mode, sharing)

### `mvn -version`
debain13@debian:~/Lab09-Gobi-Bagsh$ mvn -version
Apache Maven 3.9.9
Maven home: /usr/share/maven
Java version: 17.0.21, vendor: Eclipse Adoptium, runtime: /usr/lib/jvm/temurin-17-jdk-amd64
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "6.12.117+deb13-amd64", arch: "amd64", family: "unix"

---

## Хэсэг A — Happy path тестүүд ба эхний хэмжилт

### Бичсэн тестүүд (5 тест)

| # | Тест | Хүлээлт |
|---|------|---------|
| 1 | `letterGrade_A` | `letterGrade(95) = "A"` |
| 2 | `letterGrade_B` | `letterGrade(85) = "B"` |
| 3 | `gpa_twoScores` | `gpa([95, 85]) = 3.5` |
| 4 | `gpa_average` | `gpa([95, 75]) = 3.0` |
| 5 | `scholarship_full` | `scholarship(3.9, 16, false) = 100` |

**Тестийн үр дүн:** `Tests run: 5, Failures: 0, Errors: 0, Skipped: 0` → **BUILD SUCCESS**

### Анхны хамралтын үзүүлэлт (`results/jacoco-A.csv`)

| Хэмжигдэхүүн | Хамрагдсан (COVERED) | Хамрагдаагүй (MISSED) | Нийт | Хамралт % |
|--------------|---------------------|----------------------|------|-----------|
| Instruction  | 104                 | 48                   | 152  | **68.4%** |
| Branch       | 20                  | 21                   | 41   | **48.8%** |
| Line         | 16                  | 9                    | 25   | **64.0%** |
| Method       | 4                   | 0                    | 4    | **100.0%**|
| Complexity   | 9                   | 17                   | 26   | **34.6%** |

**Томьёо:** `COVERED / (COVERED + MISSED) × 100`
- Branch: `20 / (20 + 21) × 100 = 20/41 = 48.78% ≈ 48.8%`
- Line: `16 / (16 + 9) × 100 = 16/25 = 64.0%`
- Instruction: `104 / (104 + 48) × 100 = 104/152 = 68.42% ≈ 68.4%`

Лабын зааврын хүлээлттэй таарч байна:

### Шар ромбо ба улаан мөрүүдийн ажиглалт

**Шар ромбо (хагас хамрагдсан салаанууд):**

- `letterGrade()` доторх `if (score < 0 || score > 100)` — зөвхөн `false` салаа хамрагдсан (хэвийн оноо), харин `true` салаа (exception шидэх) хүрээгүй. Мөн `||` операторын 2 нөхцөл тус бүрийн үнэн утга хүрээгүй.
- `gpa()` доторх `if (scores == null || scores.length == 0)` — зөвхөн `false` салаа л хамрагдсан, `||`-ийн хоёр нөхцөл тусдаа шалгагдаагүй.
- `gpa()` доторх `switch`-ийн `case "D"` ба `default` (F) салаанууд хүрээгүй.
- `scholarship()` доторх `if (hasViolation)` — `true` салаа хүрээгүй (зөрчилтэй кейс тестлээгүй).
- `scholarship()` доторх `gpa >= 3.8 && credits >= 15` — `credits >= 15` нөхцлийн `false` утга хүрээгүй.
- `scholarship()` доторх `gpa >= 3.4 && credits >= 12` — хоёр нөхцлийн аль аль `false` салаа хүрээгүй.
- `scholarship()` доторх `gpa >= 3.0` — `false` салаа (доод тэтгэлэг 0) хүрээгүй.

**Улаан мөр (огт хүрээгүй алдааны замууд):**

- `letterGrade()` дахь `throw new IllegalArgumentException(...)` мөр — 0-ээс бага, 100-аас их онооны кейс тестлээгүй.
- `gpa()` дахь `throw new IllegalArgumentException(...)` мөр — `null` эсвэл хоосон массив тестлээгүй.
- `scholarship()` дахь `throw new IllegalArgumentException(...)` мөр — сөрөг gpa, 4-өөс их gpa, сөрөг кредитийн кейс тестлээгүй.
- `gpa()` доторх `case "D": total += 1;` — D үсгэн дүнтэй оноо тестлээгүй.
- `scholarship()` доторх `return 70;`, `return 50;`, `return 0;` мөрүүд — холбогдох нөхцлүүд хүрээгүй.

---

## Хэсэг B — CFG ба Branch 100%

### CFG (`cfg.png`)

`scholarship()` функцын CFG-ийг гараар зурж, зангилаа (дугуй) ба ирмэг (сум) бүрийг Лекц 9-ийн хэв маягаар тэмдэглэсэн. Ирмэг бүрийн хажууд түүнийг дайрч буй **тестийн нэрийг** бичсэн. Зураг: **`cfg.png`** (репогийн root-д).

**CFG-ийн зангилаа:**

| Зангилаа | Агуулга |
|----------|---------|
| N1 | `gpa<0 \|\| gpa>4 \|\| credits<0` |
| N2 | `throw IllegalArgumentException` |
| N3 | `hasViolation` |
| N4 | `return 0` |
| N5 | `gpa>=3.8 && credits>=15` |
| N6 | `return 100` |
| N7 | `gpa>=3.4 && credits>=12` |
| N8 | `return 70` |
| N9 | `gpa>=3.0` |
| N10 | `return 50` |
| N11 | `return 0` |

**Ирмэг бүр дээр буулгасан тестийн нэр:**

| Ирмэг | Тестийн нэр |
|-------|-------------|
| N1→N2 (T) | `scholarship_negativeGpa_throws`, `scholarship_gpaAbove4_throws`, `scholarship_negativeCredits_throws` |
| N1→N3 (F) | `scholarship_full` |
| N3→N4 (T) | `scholarship_violation_returnsZero` |
| N3→N5 (F) | `scholarship_full` |
| N5→N6 (T) | `scholarship_full` |
| N7→N9 (F) | `scholarship_gpa34CreditsLow_returns50`, `scholarship_gpaHighCreditsLow_returns50` |
| N7→N8 (T) | `scholarship_70` |
| N7→N9 (F) | `scholarship_gpa34CreditsLow_returns50` |
| N9→N10 (T) | `scholarship_50` |
| N9→N11 (F) | `scholarship_gpaLow_returns0` |

### Branch 100% хүргэхэд шаардагдсан тестийн тоо

**Нийт тест:** **21** (Хэсэг A-ийн 5 + Хэсэг B-ийн 16)

`results/mvn-verify-B.txt`-ийн `Tests run:` тоо = **21**

### Эцсийн хамралт (`results/jacoco-B.csv`)

| Хэмжигдэхүүн | Хамрагдсан | Хамрагдаагүй | Нийт | Хамралт % |
|--------------|-----------|-------------|------|-----------|
| Instruction  | 152       | 0           | 152  | **100.0%** |
| Branch       | 41        | **0**       | 41   | **100.0%** |
| Line         | 25        | **0**       | 25   | **100.0%** |
| Method       | 4         | 0           | 4    | **100.0%** |
| Complexity   | 26        | 0           | 26   | **100.0%** |

**`BRANCH_MISSED = 0`, `LINE_MISSED = 0`** — шаардлага хангасан 

### Зааврын анхааруулга хэрхэн биелүүлсэн

- **3 тусдаа assertThrows** — `||` операторын 3 нөхцөлд:
  - `scholarship_negativeGpa_throws` → `gpa<0`
  - `scholarship_gpaAbove4_throws` → `gpa>4`
  - `scholarship_negativeCredits_throws` → `credits<0`
- **`gpa()` switch-ийн default (F)** — `gpa_gradeF` тест
- **`&&` нөхцлүүдийн F утгууд**:
  - `scholarship_gpaHighCreditsLow_returns50` → `gpa>=3.8` true, `credits>=15` **FALSE**
  - `scholarship_gpa34CreditsLow_returns50` → `gpa>=3.4` true, `credits>=12` **FALSE**

---

## Хэсэг C — Давталтын заагийн тестүүд

`gpa()` доторх `for` давталтад 0/1/2+ дүрмийг хэрэгжүүлсэн:

| Давталтын тоо | Тест | Хүлээлт |
|--------------|------|---------|
| **0 удаа** (хоосон массив) | `gpa_loop_zeroIterations_empty_throws` | `IllegalArgumentException` |
| **0 удаа** (null) | `gpa_loop_zeroIterations_null_throws` | `IllegalArgumentException` |
| **1 удаа** | `gpa_loop_oneIteration` | `gpa([95]) = 4.0` |
| **2+ удаа** | `gpa_loop_manyIterations` | `gpa([95, 85, 75, 65]) = 2.5` |

### Хамралт өсгөдөггүй ч ЯАГААД хэрэгтэй вэ?

Эдгээр тестүүд branch/line хамралтыг (аль хэдийн 100%) өсгөхгүй. Гэсэн ч 0 удаагийн тестүүд нь хоосон/null оролтод `gpa()` exception шидэхийг, 1 ба 2+ удаагийн тестүүд нэг болон олон элементтэй үед нийлбэр ба хуваалт зөв ажиллахыг шалгана. Хамралт нь "код ажилласан эсэх"-ийг хэмждэг, "давталт 0/1/2+ үед зөв утга буцаадаг эсэх"-ийг хэмждэггүй тул эдгээр тестүүд хэрэгтэй хэвээр байна. (`gpa_loop_zeroIterations_*` нь B хэсгийн `gpa_empty_throws`, `gpa_null_throws`-тэй ижил нөхцөл боловч давталтын 0 удаагийн заагийг нэрлэж тэмдэглэсэн.)
---

## Хэсэг D — Хамралт 100%, гэхдээ алдаа амьд (лабын гол хэсэг)

### Унасан тест (`results/mvn-test-D-fail.txt`)

**Тестийн нэр:** `letterGrade_60_isD`

**Алдааны мөр:**
expected: <D> but was: <F>

### Алдааны шалтгаан

`letterGrade()` дотор буруу байсан мөр:

```
if (score > 60) return "D";   // БУРУУ
```

Спецификаци: 60-69.99 → "D", өөрөөр хэлбэл `score >= 60` байх ёстой. `score > 60` нь 60 оноог "F" болгож байсан.

### Засвар

Commit: `Fix letterGrade: > 60 -> >= 60, spec says 60 is D`

```java
if (score >= 60) return "D";  // ЗӨВ
```

### Эцсийн хамралт (`results/jacoco-final.csv`)

| Хэмжигдэхүүн | Хамрагдсан | Хамрагдаагүй | Нийт | % |
|---|---|---|---|---|
| Instruction | 152 | 0 | 152 | 100.0% |
| Branch | 41 | 0 | 41 | 100.0% |
| Line | 25 | 0 | 25 | 100.0% |
| Method | 4 | 0 | 4 | 100.0% |

`BRANCH_MISSED = 0`, `LINE_MISSED = 0`. Эцсийн mvn verify: Tests run: 32, BUILD SUCCESS.

### Гол дүгнэлт

Branch хамралт 100% байхад энэ алдаа "үл үзэгдэгч" байсан, учир нь `score > 60` ба `score >= 60` хоёул ижил true/false салаануудыг үүсгэдэг. JaCoCo зөвхөн салаа гүйцэтгэгдсэн эсэхийг хэмждэг, ямар зааг утга дээр үнэн/худал болохыг хэмждэггүй. 59.99, 69.99 тестүүд хоёулаа салааг гүйцэтгэсэн ч 60 гэсэн зааг утгыг шалгаагүй тул алдаа илрээгүй. Зөвхөн 60-ыг шалгасан тест ялгааг илрүүлнэ. Хамралт нь код гүйцэтгэгдсэн эсэхийг хэмждэг боловч код зөв эсэхийг хэмждэггүй: хамралт бол зайлшгүй хэрэгсэл, гэхдээ хангалттай нотолгоо биш.

## results/ файлуудын жагсаалт

| Файл | Агуулга |
|---|---|
| results/mvn-verify-A.txt | A хэсгийн mvn verify (Tests run: 5) |
| results/jacoco-A.csv | A хэсгийн JaCoCo CSV |
| results/mvn-verify-B.txt | B хэсгийн mvn verify (Tests run: 21) |
| results/jacoco-B.csv | B хэсгийн JaCoCo CSV (MISSED=0) |
| results/mvn-verify-C.txt | C хэсгийн mvn verify (Tests run: 25) |
| results/jacoco-C.csv | C хэсгийн JaCoCo CSV |
| results/mvn-test-D-fail.txt | D хэсгийн унасан тест |
| results/mvn-verify-final.txt | Засвар дараах эцсийн mvn verify (Tests run: 32) |
| results/jacoco-final.csv | Эцсийн JaCoCo CSV |

## AI туслах ашигласан эсэх

AI туслахыг README-ийн бүтэц зохиох ба тестийн кодыг эхэлж бичихэд ашигласан. Бүх тестийг өөрөө ажиллуулж, хамралтын тоог results/ файлуудтай тулгасан.
