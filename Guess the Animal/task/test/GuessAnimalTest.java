import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;

import java.io.File;
import java.util.stream.Stream;

public class GuessAnimalTest extends StageTest<String> {
    final String[] script = new String[]{
            "the-first-question",
            "positive-answers",
            "negative-answers",
            "unclear-answers",
            "file-formats"
    };

    @DynamicTest(data = "script")
    CheckResult runScripts(final String script) {
        return new Scenario(script).check();
    }

    @DynamicTest
    CheckResult testFileFormats() {
        deleteTestFiles();
        final var result = new Scenario("file-formats").check();
        deleteTestFiles();
        return result;
    }

    private void deleteTestFiles() {
        Stream.of("yaml", "json", "xml")
                .map("animals."::concat)
                .map(File::new)
                .filter(File::exists)
                .forEach(File::delete);
    }
}