/*
 * Copyright 2012-2013 Ray Holder
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.rholder.fauxflake.api;

/**
 * Implementations of this interface encapsulate unique identifier
 * representations for the underlying identifier type.
 */
public interface Id {

    /**
     * Return a unique identifier as a long.
     */
    public long asLong();

    /**
     * Return a unique identifier as bytes.
     */
    public byte[] asBytes();

    /**
     * Return a unique identifier as a String.
     */
    public String asString();
}
