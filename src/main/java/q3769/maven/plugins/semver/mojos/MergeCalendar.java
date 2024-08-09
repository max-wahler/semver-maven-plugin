/*
 * MIT License
 *
 * Copyright (c) 2020 Qingtian Wang
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package q3769.maven.plugins.semver.mojos;

import com.github.zafarkhaja.semver.Version;
import lombok.NonNull;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import q3769.maven.plugins.semver.SemverNormalVersion;
import q3769.maven.plugins.semver.Updater;

/**
 * Merge this POM's version with another SemVer passed in as parameter, and set the merge result as
 * the updated POM version. If the current POM version is newer, no update will be performed.
 * Otherwise, the update version will be decided this way: Take the intended SemVer category of the
 * current POM version; increment the passed in SemVer on the same category number as the currently
 * intended category, and use the incremented result as the new POM version. The pre-release and
 * build metadata labels of the new POM version are the same as the original POM's.
 *
 * @author Qingtian Wang
 */
@Mojo(name = "merge-calendar", defaultPhase = LifecyclePhase.NONE)
public class MergeCalendar extends Updater {
  /** The other SemVer to be merged with current local POM's version */
  @Parameter(property = "semver", defaultValue = "NOT_SET")
  protected String otherSemVer;

  @Override
  protected Version update(@NonNull final Version original) throws MojoFailureException {
    logDebug("Merging current POM version %s with provided version %s", original, otherSemVer);
    final Version other = requireValidSemVer(otherSemVer);
    if (original.isHigherThan(other)) {
      logDebug(
          "Current POM version %s is newer than provided version %s, current unchanged is the merge result: %s",
          original, other, original);
      return original;
    }
    logDebug("Provided version %s is newer than current POM version %s", other, original);
    SemverNormalVersion pomIncrementedNormalVersion =
        SemverNormalVersion.getLastIncrementedNormalVersion(original);
    logDebug(
        "Last incremented normal version of current pom semver is %s", pomIncrementedNormalVersion);
    Version provisionalMergedVersion;
    try {
      provisionalMergedVersion =
          CalendarVersionFormatter.calendarIncrement(other, pomIncrementedNormalVersion);
    } catch (Exception e) {
      logError(
          e,
          "Failed to calendar-merge provided version %s with the POM version %s",
          other,
          original);
      throw new MojoFailureException(e);
    }
    logDebug(
        "Incrementing provided version %s on POM semver incremented normal version %s, provisional merge version: %s",
        other, pomIncrementedNormalVersion, provisionalMergedVersion);
    Version.Builder versionBuilder = provisionalMergedVersion.toBuilder();
    original.preReleaseVersion().ifPresent(versionBuilder::setPreReleaseVersion);
    original.buildMetadata().ifPresent(versionBuilder::setBuildMetadata);
    Version finalMergedVersion = versionBuilder.build();
    logDebug(
        "Keeping all label(s) of POM semver %s, final merge version: %s",
        original, finalMergedVersion);
    return finalMergedVersion;
  }
}
