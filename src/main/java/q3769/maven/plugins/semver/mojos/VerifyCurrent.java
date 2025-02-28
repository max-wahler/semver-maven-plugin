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

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import q3769.maven.plugins.semver.SemverMojo;

/**
 * Verifies the current POM version is a valid SemVer
 *
 * @author Qingtian Wang
 */
@Mojo(name = "verify-current", defaultPhase = LifecyclePhase.NONE)
public class VerifyCurrent extends SemverMojo {

  /** force the version to be output to stdout if valid */
  @Parameter(property = "force-stdout", defaultValue = "false")
  protected boolean forceStdOut;

  @Override
  protected void doExecute() throws MojoFailureException {
    final String version = originalPomVersion();
    try {
      requireValidSemVer(version);
    } catch (Exception e) {
      logError(e, "POM version '%s' is not a valid SemVer", version);
      throw new MojoFailureException(e);
    }
    logInfo("POM version '%s' is a valid SemVer", version);
    if (forceStdOut) {
      System.out.println(version);
    }
  }
}
