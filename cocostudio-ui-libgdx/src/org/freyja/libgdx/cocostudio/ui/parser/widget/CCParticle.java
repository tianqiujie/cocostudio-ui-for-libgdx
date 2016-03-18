package org.freyja.libgdx.cocostudio.ui.parser.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Base64Coder;

import net.sf.plist.NSDictionary;
import net.sf.plist.NSString;
import net.sf.plist.io.PropertyListException;
import net.sf.plist.io.PropertyListParser;
import net.sf.plist.util.Base64;

import org.freyja.libgdx.cocostudio.ui.CocoStudioUIEditor;
import org.freyja.libgdx.cocostudio.ui.model.CCParticleBean;
import org.freyja.libgdx.cocostudio.ui.model.ObjectData;
import org.freyja.libgdx.cocostudio.ui.parser.WidgetParser;
import org.freyja.libgdx.cocostudio.ui.widget.ParticleEffectActor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.InflaterOutputStream;
import java.util.zip.ZipException;

/**
 * Created by tian on 2016/3/18.
 */
public class CCParticle extends WidgetParser{
    @Override
    public String getClassName() {
        return "ParticleObjectData";
    }

//    {
//        "FileData": {
//        "Type": "Default",
//                "Path": "Default/defaultParticle.plist",
//                "Plist": ""
//    },
//        "BlendFunc": {
//        "Src": 775,
//                "Dst": 1
//    },
//        "AnchorPoint": {},
//        "Position": {
//        "X": 787.0,
//                "Y": 129.0
//    },
//        "Scale": {
//        "ScaleX": 1.0,
//                "ScaleY": 1.0
//    },
//        "CColor": {},
//        "IconVisible": true,
//            "PrePosition": {
//        "X": 0.8198,
//                "Y": 0.2016
//    },
//        "PreSize": {
//        "X": 0.0,
//                "Y": 0.0
//    },
//        "LeftMargin": 787.0,
//            "RightMargin": 173.0,
//            "TopMargin": 511.0,
//            "BottomMargin": 129.0,
//            "Tag": 12,
//            "ActionTag": 50242526,
//            "Size": {
//        "X": 0.0,
//                "Y": 0.0
//    },
//        "Name": "Particle_1",
//            "ctype": "ParticleObjectData"
//    }
    @Override
    public Actor parse(CocoStudioUIEditor editor, ObjectData widget) {
        ParticleEffect effect = new ParticleEffect();
        NSDictionary dictionary = null;
        try {
            dictionary = (NSDictionary) PropertyListParser.parse(editor.findParticePath(widget, widget
                    .getFileData().getPath
                            ()).file());
        } catch (PropertyListException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        CCParticleBean particleBean = parsePlist(dictionary);
        if (particleBean.getTextureImageData() != null)
        {
           byte[] bytes = base64_gzip(dictionary.get("textureImageData").toString());
           Pixmap pixmap = new Pixmap(bytes, 0, bytes.length);
           //PixmapIO.writePNG(Gdx.files.external("xxoo.png"), pixmap);
       }

        // ParticleEffectActor effectActor = new ParticleEffectActor();
        return null;
    }

    private CCParticleBean parsePlist(NSDictionary dictionary){
        CCParticleBean ccParticleBean = new CCParticleBean();
        ccParticleBean.setAngle(dictionary.get("angle").toInteger());
        ccParticleBean.setAngleVariance(dictionary.get("angleVariance").toInteger());
        ccParticleBean.setBlendFuncDestination(dictionary.get("blendFuncDestination").toInteger());
        ccParticleBean.setBlendFuncSource(dictionary.get("blendFuncSource").toInteger());
        ccParticleBean.setDuration(dictionary.get("duration").toInteger());

        ccParticleBean.setEmitterType(dictionary.get("emitterType").toDouble());
        ccParticleBean.setFinishColorAlpha(dictionary.get("finishColorAlpha").toDouble());
        ccParticleBean.setFinishColorBlue(dictionary.get("finishColorBlue").toDouble());
        ccParticleBean.setFinishColorGreen(dictionary.get("finishColorGreen").toDouble());
        ccParticleBean.setFinishColorRed(dictionary.get("finishColorRed").toDouble());

        ccParticleBean.setFinishColorAlpha(dictionary.get("finishColorVarianceAlpha").toDouble());
        ccParticleBean.setFinishColorBlue(dictionary.get("finishColorVarianceBlue").toDouble());
        ccParticleBean.setFinishColorGreen(dictionary.get("finishColorVarianceGreen").toDouble());
        ccParticleBean.setFinishColorRed(dictionary.get("finishColorVarianceRed").toDouble());

        ccParticleBean.setFinishParticleSize(dictionary.get("finishParticleSize").toDouble());
        ccParticleBean.setFinishParticleSizeVariance(dictionary.get("finishParticleSizeVariance")
                .toDouble());

        ccParticleBean.setGravityx(dictionary.get("gravityx").toDouble());
        ccParticleBean.setGravityy(dictionary.get("gravityy").toDouble());

        ccParticleBean.setMaxParticles(dictionary.get("maxParticles").toInteger());
        ccParticleBean.setMaxRadius(dictionary.get("maxRadius").toInteger());

        ccParticleBean.setMaxRadiusVariance(dictionary.get("maxRadiusVariance").toDouble());
        ccParticleBean.setMinRadius(dictionary.get("minRadius").toDouble());
        ccParticleBean.setParticleLifespan(dictionary.get("particleLifespan").toDouble());

        ccParticleBean.setParticleLifespanVariance(dictionary.get("particleLifespanVariance").toDouble());
        ccParticleBean.setRadialAccelVariance(dictionary.get("radialAccelVariance").toDouble());
        ccParticleBean.setRadialAcceleration(dictionary.get("radialAcceleration").toDouble());
        ccParticleBean.setRotatePerSecond(dictionary.get("rotatePerSecond").toDouble());

        ccParticleBean.setRotatePerSecondVariance(dictionary.get("rotatePerSecondVariance").toDouble());
        ccParticleBean.setRotationEnd(dictionary.get("rotationEnd").toDouble());
        ccParticleBean.setRotationEndVariance(dictionary.get("rotationEndVariance").toDouble());
        ccParticleBean.setRotationStart(dictionary.get("rotationStart").toDouble());
        ccParticleBean.setRotationStartVariance(dictionary.get("rotationStartVariance").toDouble());

        ccParticleBean.setSourcePositionVariancex(dictionary.get("sourcePositionVariancex").toInteger());
        ccParticleBean.setSourcePositionVariancey(dictionary.get("sourcePositionVariancey")
                .toInteger());

        ccParticleBean.setSourcePositionx(dictionary.get("sourcePositionx").toDouble());
        ccParticleBean.setSourcePositiony(dictionary.get("sourcePositiony").toDouble());
        ccParticleBean.setSpeed(dictionary.get("speed").toDouble());

        ccParticleBean.setSpeedVariance(dictionary.get("speedVariance").toDouble());
        ccParticleBean.setStartColorAlpha(dictionary.get("startColorAlpha").toDouble());
        ccParticleBean.setStartColorBlue(dictionary.get("startColorBlue").toDouble());
        ccParticleBean.setStartColorGreen(dictionary.get("startColorGreen").toDouble());
        ccParticleBean.setStartColorRed(dictionary.get("startColorRed").toDouble());

        ccParticleBean.setStartColorVarianceAlpha(dictionary.get("startColorVarianceAlpha")
                .toDouble());
        ccParticleBean.setStartColorVarianceBlue(dictionary.get("startColorVarianceBlue").toDouble());
        ccParticleBean.setStartColorVarianceGreen(dictionary.get("startColorVarianceGreen").toDouble());
        ccParticleBean.setStartColorVarianceRed(dictionary.get("startColorVarianceRed").toDouble());

        ccParticleBean.setStartParticleSize(dictionary.get("startParticleSize").toDouble());
        ccParticleBean.setStartParticleSizeVariance(dictionary.get("startParticleSizeVariance").toInteger());
        ccParticleBean.setTangentialAccelVariance(dictionary.get("tangentialAccelVariance").toDouble());
        ccParticleBean.setTangentialAcceleration(dictionary.get("tangentialAcceleration").toDouble());

        ccParticleBean.setTextureFileName(dictionary.get("textureFileName").toString());
        ccParticleBean.setTextureImageData(dictionary.get("textureImageData").toString());

        return ccParticleBean;
    }

    private byte[] base64_gzip(String args){
        byte decode[] = Base64Coder.decode(args);
        try {
            DataInputStream Bin = new DataInputStream(new GZIPInputStream(new ByteArrayInputStream(decode)));
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int readBytes = 0;
            while ((readBytes = Bin.read(buffer)) != -1) {
                bytes.write(buffer, 0, readBytes);
            }
            return bytes.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}


