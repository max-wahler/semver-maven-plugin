package q3769.maven.plugins.semver.mojos;

import static org.junit.jupiter.api.Assertions.*;

import com.github.zafarkhaja.semver.Version;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FinalizeCurrentTest {
  private final FinalizeCurrent instance = new FinalizeCurrent();

  @Test
  void shouldReturnOriginalVersionIfNoPreReleaseOrBuildMetadata() {
    String versionString = "1.2.3";
    Version originalVersion = Version.parse(versionString);

    Version result = instance.update(originalVersion);

    assertEquals(originalVersion, result);
  }

  @ParameterizedTest
  @CsvSource({"1.2.3-alpha+001, 1.2.3", "2.0.0-beta, 2.0.0"})
  void shouldStripPreReleaseAndBuildMetadata(
      String inputVersionString, String expectedVersionString) {
    Version inputVersion = Version.parse(inputVersionString);
    Version expectedVersion = Version.parse(expectedVersionString);

    Version result = instance.update(inputVersion);

    assertEquals(expectedVersion, result);
  }
}
