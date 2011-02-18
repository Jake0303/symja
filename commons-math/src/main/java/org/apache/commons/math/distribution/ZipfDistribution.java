/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.math.distribution;

/**
 * The Zipf (or zeta) Distribution.
 * <p>
 * References:
 * <ul>
 * <li><a href="http://mathworld.wolfram.com/ZipfDistribution.html">Zipf
 * Distribution</a></li>
 * </ul>
 * </p>
 *
 * @version $Revision: 1003512 $ $Date: 2010-10-01 14:46:16 +0200 (Fr, 01 Okt 2010) $
 */
public interface ZipfDistribution extends IntegerDistribution {
    /**
     * Get the number of elements (e.g. corpus size) for the distribution.
     *
     * @return the number of elements
     */
    int getNumberOfElements();

    /**
     * Get the exponent characterising the distribution.
     *
     * @return the exponent
     */
    double getExponent();
}
