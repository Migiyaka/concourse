/*
 * Copyright (c) 2013-2015 Cinchapi, Inc.
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
package org.cinchapi.concourse.util;

import java.text.MessageFormat;

import javax.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

/**
 * Yet another collection of utility functions for Strings.
 * 
 * @author Jeff Nelson
 */
public final class Strings {

    /**
     * Split a string on a delimiter as long as that delimiter is not wrapped in
     * double or single quotes.
     * 
     * @param string
     * @param delimiter
     * @return the tokens that result from the split
     */
    public static String[] splitStringByDelimiterButRespectQuotes(
            String string, String delimiter) {
        // This is pretty inefficient: convert all single quotes to double
        // quotes (except one off single quotes that are used as apostrophes) so
        // the regex below works
        string = string.replaceAll(" '", " \"");
        string = string.replaceAll("' ", "\" ");
        string = string.replaceAll("'$", "\"");
        return string.split(delimiter + "(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
    }

    /**
     * Split a string, using whitespace as a delimiter, as long as the
     * whitespace is not wrapped in double or single quotes.
     * 
     * @param string
     * @return the tokens that result from the split
     */
    public static String[] splitButRespectQuotes(String string) {
        return splitStringByDelimiterButRespectQuotes(string, " ");
    }

    /**
     * This method efficiently tries to parse {@code value} into a
     * {@link Number} object if possible. If the string is not a number, then
     * the method returns {@code null} as quickly as possible.
     * 
     * @param value
     * @return a Number object that represents the string or {@code null} if it
     *         is not possible to parse the string into a number
     */
    @Nullable
    public static Number tryParseNumber(String value) {
        if(value == null) {
            return null;
        }
        int size = value.length();
        boolean decimal = false;
        for (int i = 0; i < size; ++i) {
            char c = value.charAt(i);
            if(!Character.isDigit(c)) {
                if(i == 0 && c == '-') {
                    continue;
                }
                else if(c == '.') {
                    decimal = true;
                }
                else {
                    return null;
                }
            }
        }
        try {
            return decimal ? Objects.firstNonNull(Floats.tryParse(value),
                    Doubles.tryParse(value)) : Objects.firstNonNull(
                    Ints.tryParse(value), Longs.tryParse(value));
        }
        catch (NullPointerException e) {
            throw new NumberFormatException(MessageFormat.format(
                    "{0} appears to be a number cannot be parsed as such",
                    value));
        }
    }

    private Strings() {/* noop */}

}
