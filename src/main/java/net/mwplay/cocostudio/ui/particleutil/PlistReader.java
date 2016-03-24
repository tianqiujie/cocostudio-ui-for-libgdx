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
package net.mwplay.cocostudio.ui.particleutil;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;

import java.io.IOException;

public class PlistReader extends XmlReader {
    SAXResult m_eResultType;
    Array<Object> m_pRootArray;
    ObjectMap<String, Object> m_pRootDict;
    ObjectMap<String, Object> m_pCurDict;
    String m_sCurKey;   ///< parsed key
    String m_sCurValue; // parsed value
    SAXState m_tState;
    Array<Object> m_pArray;

    //这个栈在cocos中不存在，但是由于libgdx XMLReader的缺点，close不带name，所以必须添加一个栈来存放open所得到的name
    Array<String> nameStack = new Array<String>();

    Array<ObjectMap<String, Object>> m_tDictStack = new Array<ObjectMap<String, Object>>();
    Array<Array<Object>> m_tArrayStack = new Array<Array<Object>>();
    Array<SAXState> m_tStateStack = new Array<SAXState>();

    enum SAXState {
        SAX_NONE,
        SAX_KEY,
        SAX_DICT,
        SAX_INT,
        SAX_REAL,
        SAX_STRING,
        SAX_ARRAY
    }

    enum SAXResult {
        SAX_RESULT_NONE,
        SAX_RESULT_DICT,
        SAX_RESULT_ARRAY
    }

    @Override
    protected String entity(String name) {
        return null;
    }

    @Override
    protected void attribute(String name, String value) {
    }

    @Override
    protected void text(String text) {
        if (m_tState == SAXState.SAX_NONE) {
            return;
        }
        SAXState curState = m_tStateStack.size == 0 ? SAXState.SAX_DICT : m_tStateStack.peek();
        switch (m_tState) {
            case SAX_KEY:
                m_sCurKey = text;
                break;
            case SAX_INT:
            case SAX_REAL:
            case SAX_STRING: {
                if (curState == SAXState.SAX_DICT) {
                    assert m_sCurKey != null && !m_sCurKey.equals("") : "key not found : <integer/real>";
                }
                m_sCurValue = text;
            }
            break;
            default:
                break;
        }
    }

    @Override
    protected void close() {
        String sName = nameStack.pop();
        SAXState curState = m_tStateStack.size == 0 ? SAXState.SAX_DICT : m_tStateStack.peek();
        if (sName.equals("dict")) {
            m_tStateStack.pop();
            m_tDictStack.pop();
            if (m_tDictStack.size > 0) {
                m_pCurDict = m_tDictStack.peek();
            }
        } else if (sName.equals("array")) {
            m_tStateStack.pop();
            m_tArrayStack.pop();
            if (m_tArrayStack.size > 0) {
                m_pArray = m_tArrayStack.peek();
            }
        } else if (sName.equals("true")) {
            if (SAXState.SAX_ARRAY == curState) {
                m_pArray.add("true");
            } else if (SAXState.SAX_DICT == curState) {
                m_pCurDict.put(m_sCurKey, "true");
            }
        } else if (sName.equals("false")) {
            if (SAXState.SAX_ARRAY == curState) {
                m_pArray.add("false");
            } else if (SAXState.SAX_DICT == curState) {
                m_pCurDict.put(m_sCurKey, "false");
            }
        } else if (sName.equals("string") || sName.equals("integer") || sName.equals("real")) {
            if (SAXState.SAX_ARRAY == curState) {
                m_pArray.add(m_sCurValue);
            } else if (SAXState.SAX_DICT == curState) {
                m_pCurDict.put(m_sCurKey, m_sCurValue);
            }
        }
        m_tState = SAXState.SAX_NONE;
    }

    @Override
    protected void open(String sName) {
        //每次open会入栈
        nameStack.add(sName);
        if (sName.equals("dict")) {
            m_pCurDict = new ObjectMap<String, Object>();
            if (m_eResultType == SAXResult.SAX_RESULT_DICT && m_pRootDict == null) {
                m_pRootDict = m_pCurDict;
            }
            m_tState = SAXState.SAX_DICT;
            SAXState preState = SAXState.SAX_NONE;
            if (m_tStateStack.size > 0) {
                preState = m_tStateStack.peek();
            }
            if (SAXState.SAX_ARRAY == preState) {
                // add the dictionary into the array
                m_pArray.add(m_pCurDict);
            } else if (SAXState.SAX_DICT == preState) {
                // add the dictionary into the pre dictionary
                assert m_tDictStack.size > 0 : "The state is wrong!";
                ObjectMap<String, Object> pPreDict = m_tDictStack.peek();
                pPreDict.put(m_sCurKey, m_pCurDict);
            }
            m_tStateStack.add(m_tState);
            m_tDictStack.add(m_pCurDict);
        } else if (sName.equals("key")) {
            m_tState = SAXState.SAX_KEY;
        } else if (sName.equals("integer")) {
            m_tState = SAXState.SAX_INT;
        } else if (sName.equals("real")) {
            m_tState = SAXState.SAX_REAL;
        } else if (sName.equals("string")) {
            m_tState = SAXState.SAX_STRING;
        } else if (sName.equals("array")) {
            m_tState = SAXState.SAX_ARRAY;
            m_pArray = new Array<Object>();
            if (m_eResultType == SAXResult.SAX_RESULT_ARRAY && m_pRootArray == null) {
                m_pRootArray = m_pArray;
            }
            SAXState preState = SAXState.SAX_NONE;
            if (m_tStateStack.size > 0) {
                preState = m_tStateStack.peek();
            }

            if (preState == SAXState.SAX_DICT) {
                m_pCurDict.put(m_sCurKey, m_pArray);
            } else if (preState == SAXState.SAX_ARRAY) {
                assert m_tArrayStack.size > 0 : "The state is wrong!";
                Array<Object> pPreArray = m_tArrayStack.peek();
                pPreArray.add(m_pArray);
            }
            // record the array state
            m_tStateStack.add(m_tState);
            m_tArrayStack.add(m_pArray);
        } else {
            m_tState = SAXState.SAX_NONE;
        }
    }

    public Array<Object> arrayWithContentsOfFile(String filePath) {
        try {
            this.parse(Gdx.files.internal(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return m_pArray;
    }

    public ObjectMap<String, Object> dictionaryWithContentsOfFile(String filePath) {
        return dictionaryWithContentsOfFile(Gdx.files.internal(filePath));
    }

    public ObjectMap<String, Object> dictionaryWithContentsOfFile(FileHandle handle) {
        try {
            this.parse(handle);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return m_pCurDict;
    }

}
