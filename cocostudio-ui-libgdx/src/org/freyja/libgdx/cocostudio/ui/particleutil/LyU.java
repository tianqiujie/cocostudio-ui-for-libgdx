package org.freyja.libgdx.cocostudio.ui.particleutil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ObjectMap;

public class LyU {
    private LyU(){}

    public static ObjectMap<String,Object> createDictionaryWithContentsOfFile(FileHandle handle)
    {
        PlistReader reader = new PlistReader();
        return reader.dictionaryWithContentsOfFile(handle);
    }

    //Gzip解压缩
    public static byte[] unGzip(byte[] encode){
        ByteArrayInputStream bais = new ByteArrayInputStream(encode);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] result = null;
        try{
            GZIPInputStream gis = new GZIPInputStream(bais);
            int count;
            byte data[] = new byte[1024];
            while ((count = gis.read(data, 0, 1024)) != -1) {
                baos.write(data, 0, count);
            }
            gis.close();
            result = baos.toByteArray();
            baos.flush();
            baos.close();
            bais.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

//    public static Parkour getGame(){
//        return (Parkour) Gdx.app.getApplicationListener();
//    }
//
//    public static AssetManager getAssetManager(){
//        return getGame().getAssetManager();
//    }

    public static GridPoint2 parsePoint(String vector){
        GridPoint2 res = new GridPoint2();
        res.x = Integer.parseInt(vector.substring(vector.indexOf('{')+1,vector.indexOf(',')).trim());
        res.y = Integer.parseInt(vector.substring(vector.indexOf(',')+1,vector.indexOf('}')).trim());
        return res;
    }
    public static Rectangle parseRect(String vector){
        Rectangle res = new Rectangle();
        String[] strs = vector.split(",");
        if(strs.length != 4){
            throw new RuntimeException("parseRect error!!");
        }
        res.x = Integer.parseInt(strs[0].substring(strs[0].lastIndexOf('{') + 1, strs[0].length()).trim());
        res.y = Integer.parseInt(strs[1].substring(0,strs[1].indexOf('}')).trim());
        res.width = Integer.parseInt(strs[2].substring(strs[2].indexOf('{')+1,strs[2].length()).trim());
        res.height = Integer.parseInt(strs[3].substring(0,strs[3].indexOf('}')).trim());
        return res;
    }
}
