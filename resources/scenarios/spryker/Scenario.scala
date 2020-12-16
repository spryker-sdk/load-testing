/*
 * Copyright 2011-2019 GatlingCorp (https://gatling.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package spryker

import scala.concurrent.duration._

object Scenario {

  val targetRps = Integer.getInteger("TARGET_RPS", 0)
  val duration = FiniteDuration(Integer.getInteger("DURATION", 0).toLong, MINUTES)

  require(targetRps > 0, s"TARGET_RPS ($targetRps) must be >= 0")
  require(duration > Duration.Zero, s"DURATION ($duration) must be >= 0")

  println()
  printf("Target RPS: %d", targetRps)
  println()
  printf("Duration: %s", duration)
  println()
  println()
}
