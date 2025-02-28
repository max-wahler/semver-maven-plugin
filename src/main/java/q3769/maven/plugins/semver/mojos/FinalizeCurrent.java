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
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import q3769.maven.plugins.semver.Updater;

/**
 * Mojo to strip off all additional labels of the SemVer, leaving the normal numbers untouched for
 * final version.
 *
 * @author Qingtian Wang
 */
@Mojo(name = "finalize-current", defaultPhase = LifecyclePhase.NONE)
public class FinalizeCurrent extends Updater {

  /**
   * @param original to finalize
   * @return final SemVer version of the original, all labels stripped
   */
  @Override
  protected Version update(@NonNull Version original) {
    if (original.preReleaseVersion().isEmpty() && original.buildMetadata().isEmpty()) {
      logInfo("Current version: %s contains only normal version numbers, so no change.", original);
      return original;
    }
    return Version.of(original.majorVersion(), original.minorVersion(), original.patchVersion());
  }
}
