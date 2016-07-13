package com.newtonker.jigsawdemo.model;

/**
 * 模版参数实体类
 */
public class TemplateEntity
{
    // 模版ID
    private int id;
    // 模版子图数量
    private int numOfSlots;
    // 坐标点,如0:0,0.5:0,1:0,0:1,0.5:1,1:1
    private String points;
    // 环绕规则,如0,1,4,3/1,2,5,4
    private String polygons;

    public String getPolygons()
    {
        return polygons;
    }

    public void setPolygons(String polygons)
    {
        this.polygons = polygons;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getNumOfSlots()
    {
        return numOfSlots;
    }

    public void setNumOfSlots(int numOfSlots)
    {
        this.numOfSlots = numOfSlots;
    }

    public String getPoints()
    {
        return points;
    }

    public void setPoints(String points)
    {
        this.points = points;
    }
}
