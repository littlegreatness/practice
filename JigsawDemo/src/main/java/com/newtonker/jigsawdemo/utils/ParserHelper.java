package com.newtonker.jigsawdemo.utils;

import android.content.Context;

import com.newtonker.jigsawdemo.model.JigsawType;
import com.newtonker.jigsawdemo.model.TemplateEntity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 具体的排版信息
 */
public class ParserHelper {
    private static HashMap<JigsawType, List<TemplateEntity>> mEntityHashMap;

    private static ParserHelper instance;

    private ParserHelper(Context context) {
        init(context);
    }

    public static ParserHelper getInstance(Context context) {
        if (null == instance) {
            instance = new ParserHelper(context.getApplicationContext());
        }

        return instance;
    }

    public static void init(Context context) {
        try {
            mEntityHashMap = parseXml(context.getAssets().open("templates.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<TemplateEntity> getEntityList(JigsawType type) {
        if (null == mEntityHashMap) {
            return null;
        }

        return mEntityHashMap.get(type);
    }

    /**
     * 解析xml
     *
     * @param is
     * @return
     */
    private static HashMap<JigsawType, List<TemplateEntity>> parseXml(InputStream is) {
        List<TemplateEntity> entityList = new ArrayList<>();
        try {
            TemplateEntity entity = null;

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(is, "utf-8");
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String tagName = parser.getName();
                        if (null != tagName && tagName.equals("layout")) {
                            entity = new TemplateEntity();
                            int numOfSlots = Integer.parseInt(parser.getAttributeValue(null, "numOfSlots"));
                            entity.setNumOfSlots(numOfSlots);
                        }

                        if (null != tagName && tagName.equals("id") && null != entity) {
                            String id = parser.nextText();
                            entity.setId(Integer.parseInt(id));
                        }

                        if (null != tagName && tagName.equals("points") && null != entity) {
                            String points = parser.nextText();
                            entity.setPoints(points);
                        }

                        if (null != tagName && tagName.equals("polygons") && null != entity) {
                            String polygons = parser.nextText();
                            entity.setPolygons(polygons);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("layout")) {
                            entityList.add(entity);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            return null;
        }

        // 对list解析，并放到SparseArray中
        HashMap<JigsawType, List<TemplateEntity>> hashMap = new HashMap<>();

        List<TemplateEntity> tempList0 = new ArrayList<>();
        List<TemplateEntity> tempList1 = new ArrayList<>();
        List<TemplateEntity> tempList2 = new ArrayList<>();
        List<TemplateEntity> tempList3 = new ArrayList<>();
        List<TemplateEntity> tempList4 = new ArrayList<>();

        for (TemplateEntity temp : entityList) {
            switch (temp.getNumOfSlots()) {
                case 1:
                    tempList0.add(temp);
                    break;
                case 2:
                    tempList1.add(temp);
                    break;
                case 3:
                    tempList2.add(temp);
                    break;
                case 4:
                    tempList3.add(temp);
                    break;
                case 5:
                    tempList4.add(temp);
                    break;

                default:
                    break;
            }
        }

        // 在循环结束后将模版的集合按照键值对方式存放
        hashMap.put(JigsawType.ONE_PHOTO, tempList0);
        hashMap.put(JigsawType.TWO_PHOTO, tempList1);
        hashMap.put(JigsawType.THREE_PHOTO, tempList2);
        hashMap.put(JigsawType.FOUR_PHOTO, tempList3);
        hashMap.put(JigsawType.FIVE_PHOTO, tempList4);

        return hashMap;
    }
}
