/*
 * noakweather(TM) is a Java library for parsing weather data
 * Copyright (C) 2022 quark95cos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package noakweather.utils;

/**
 * Class representing the exception handling
 *
 * Author: quark95cos Since: Copyright(c) 2022
 */
@SuppressWarnings("serial")
public class UtilsException extends Exception {

    /**
     * The record that caused the exception.
     */
    protected String record = null;

    /**
     * Default constructor
     */
    public UtilsException() {
        super();
    }

    /**
     * Create exception with error message
     *
     * @param message The error message for this exception
     */
    public UtilsException(String message) {
        super(message);
    }

    /**
     * Create exception based on an existing Throwable
     *
     * @param cause The throwable on which we'll base this exception
     */
    public UtilsException(Throwable cause) {
        super(cause);
    }

    /**
     * Create an exception with custom message and throwable info
     *
     * @param message The message
     * @param cause The target Throwable
     */
    public UtilsException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Get record
     *
     * @return Returns the record.
     */
    public String getRecord() {
        return record;
    }

    /**
     * Set record
     *
     * @param record The record to set.
     */
    public void setRecord(String record) {
        this.record = record;
    }
}
