
package com.ak.android.akplaza.common;

/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;

import com.ak.android.akplaza.R;

public class XMLController {

    public static final String TAG = "XMLController";

    // /더미//
    static public List<Map<String, String>> getAlarmList(Context context) {
        List<Map<String, String>> list = null;

        String url = Const.URL_LIB + context.getString(R.string.url_getAlarmList);
        String sessionId = SharedUtil.getSharedString(context, "login", "SESSIONID");

        XMLLoader loader = new XMLLoader(context, url, sessionId);
        Document doc = loader.getDocument();
        try {
            list = document2List(doc, "alarm");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            list = null;
        }
        return list;
    }

    // /////////////////////////////////////////////////////////
    private static List<Map<String, String>> document2List(Document doc, String findValue)
            throws UnsupportedEncodingException {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        NodeList menu = doc.getElementsByTagName(findValue);
        if (menu != null) {
            for (int j = 0; j < menu.getLength(); j++) {
                Node nodeList = menu.item(j);
                if (nodeList.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList childs = nodeList.getChildNodes();

                    for (int k = 0; k < childs.getLength(); k++) {
                        Map<String, String> rowData = new HashMap<String, String>();
                        Node child = childs.item(k);
                        if (child.getNodeType() == Node.ELEMENT_NODE) {
                            NodeList items = child.getChildNodes();
                            for (int l = 0; l < items.getLength(); l++) {
                                Node item = items.item(l);
                                if (item.getNodeType() == Node.ELEMENT_NODE) {
                                    String nodeName = item.getNodeName();
                                    String value = XMLLoader.getNodeValue(item);
                                    rowData.put(nodeName, URLDecoder.decode(value, "UTF-8"));
                                }
                            }
                            list.add(rowData);
                        }
                    }
                }
            }
        } else {
            list = null;
        }
        return list;
    }

    private static List document2List1(Document doc, String findValue)
            throws UnsupportedEncodingException {
        List list = new ArrayList();
        NodeList menu = doc.getElementsByTagName(findValue);
        if (menu != null) {

            for (int k = 0; k < menu.getLength(); k++) {
                Map rowData = new HashMap();
                Node child = menu.item(k);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList items = child.getChildNodes();
                    for (int l = 0; l < items.getLength(); l++) {
                        Node item = items.item(l);
                        if (item.getNodeType() == Node.ELEMENT_NODE) {
                            String nodeName = item.getNodeName();
                            String value = XMLLoader.getNodeValue(item);
                            rowData.put(nodeName, URLDecoder.decode(value, "UTF-8"));
                        }
                    }
                    list.add(rowData);
                }
            }
        } else {
            list = null;
        }
        return list;
    }
}
