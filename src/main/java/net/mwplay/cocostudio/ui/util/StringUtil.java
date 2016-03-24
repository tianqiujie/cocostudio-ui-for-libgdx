/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.mwplay.cocostudio.ui.util;

public class StringUtil {
    /**
     * 去除重复字符
     */
    public static String removeRepeatedChar(String text) {
        char[] chars = text.toCharArray();
        char[] existChar = new char[chars.length];
        int i = 0;
        StringBuffer sb = new StringBuffer();
        for (char ch : chars) {
            if (isExistsChar(existChar, ch)) {
                continue;
            }
            existChar[i] = ch;
            sb.append(ch);
            i++;
        }

        if (chars.length == i) {// 没有重复项避免创建String
            return text;
        }
        return sb.toString();
    }

    /**
     * 检查是否存在字符
     */
    static boolean isExistsChar(char[] chars, char ch) {
        for (char c : chars) {
            if (c == ch) {
                return true;
            }
        }
        return false;
    }
}
