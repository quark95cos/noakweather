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
package noakweather;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author quark95cos
 */
public class NoakWeatherMainTest {

    public NoakWeatherMainTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        System.out.println("Setup class");
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        System.out.println("Setup before each test");
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of main method, of class NoakWeatherMain.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = new String[4];
        args[0] = "m";
        args[1] = "kclt";
        args[2] = "y";
        args[3] = "i";
        NoakWeatherMain.main(args);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

}
