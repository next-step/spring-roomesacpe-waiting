import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import org.junit.jupiter.api.DisplayName;

@AnalyzeClasses(importOptions = {
    ImportOption.DoNotIncludeArchives.class, ImportOption.DoNotIncludeTests.class
})
public class DependencyTest {

    @DisplayName("nextstep -> auth 로 단방향 의존해야한다")
    @ArchTest
    void checkDependencyFromNextstepToAuth(JavaClasses javaClasses) {
        assertAll(
            // auth 패키지는 auth 자신 또는 nextstep 에서 접근해야 한다.
            () -> classes()
                .that().resideInAPackage("..auth..")
                .should().onlyBeAccessed().byAnyPackage("..auth..", "..nextstep..").check(javaClasses),

            // nextstep 패키지는 자신만 접근해야 한다.
            () -> classes()
                .that().resideInAPackage("..nextstep..")
                .should().onlyBeAccessed().byAnyPackage("..nextstep..").check(javaClasses)
        );
    }
}
