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

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Class representing the indexed linked hash map. It inherits from the
 * LinkedHashMap class
 *
 * Author: quark95cos Since: Copyright(c) 2022
 *
 * @param <K>
 * @param <V>
 */
public class IndexedLinkedHashMap<K, V> extends LinkedHashMap<K, V> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    ArrayList<K> al_Index = new ArrayList<>();

    public IndexedLinkedHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public IndexedLinkedHashMap() {
    }

    /**
     * Sets the locale of the bundle.
     *
     * @param key
     * @param val
     * @return value
     */
    @Override
    public V put(K key, V val) {
        if (!super.containsKey(key)) {
            al_Index.add(key);
        }
        V returnValue = super.put(key, val);
        return returnValue;
    }

    /**
     * Get the value at index
     *
     * @param i
     * @return index
     */
    public V getValueAtIndex(int i) {
        return super.get(al_Index.get(i));
    }

    /**
     * Get the value at index
     *
     * @param i
     * @return index
     */
    public K getKeyAtIndex(int i) {
        return al_Index.get(i);
    }

    /**
     * Get the value at index of key
     *
     * @param key
     * @return index of key
     */
    public int getIndexOf(K key) {
        return al_Index.indexOf(key);
    }
}
