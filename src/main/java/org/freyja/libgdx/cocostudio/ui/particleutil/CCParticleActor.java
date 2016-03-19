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
package org.freyja.libgdx.cocostudio.ui.particleutil;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

import java.nio.charset.Charset;

import static com.badlogic.gdx.graphics.g2d.Batch.*;

public class CCParticleActor extends Actor implements Disposable {
    public CCParticleActor() {
        init();
    }

    public CCParticleActor(int totalCount) {
        initWithTotalParticles(totalCount);
    }

    public CCParticleActor(String filePath) {
        //this.setTransform(false);
        initWithFile(filePath);
    }

    public CCParticleActor(Texture texture) {
        init(texture);
    }

    public CCParticleActor(int totalCount, Texture texture) {
        initWithTotalParticles(totalCount, texture);
    }

    public CCParticleActor(String filePath, Texture texture) {
        initWithFileAndTexture(filePath, texture);
    }

    @Override
    public boolean remove() {
        if (super.remove()) {
            if (m_pTexture != null && ownesTexture) {
                m_pTexture.dispose();
                m_pTexture = null;
            }
            return true;
        }
        return false;
    }

    protected void initWithFileAndTexture(String filePath, Texture texture) {
        FileHandle handle = Gdx.files.internal(filePath);
        String dir = handle.parent().path();
        ObjectMap<String, Object> map = LyU.createDictionaryWithContentsOfFile(handle);
        initWithDictionary(map, dir, texture);
    }

    protected boolean ownesTexture;

    @Override
    public void dispose() {
        if (ownesTexture) {
            m_pTexture.dispose();
            m_pTexture = null;
        }
    }

    public void init() {
        initWithTotalParticles(150);
    }

    public void init(Texture texture) {
        initWithTotalParticles(150, texture);
    }

    public void initWithFile(String filePath) {
        FileHandle handle = Gdx.files.internal(filePath);
        String dir = handle.parent().path();
        ObjectMap<String, Object> map = LyU.createDictionaryWithContentsOfFile(handle);
        initWithDictionary(map, dir);
    }

    public void initWithDictionary(ObjectMap<String, Object> dic) {
        initWithDictionary(dic, "");
    }

    public void initWithDictionary(ObjectMap<String, Object> dictionary, String dir, Texture texture) {
        //int maxParticles = Integer.parseInt((String) dictionary.get("maxParticles"));
        int maxParticles = (int) Float.parseFloat((String) dictionary.get("maxParticles"));
        this.initWithTotalParticles(maxParticles);
        m_fAngle = Float.parseFloat((String) dictionary.get("angle"));
        m_fAngleVar = Float.parseFloat((String) dictionary.get("angleVariance"));
        // duration
        m_fDuration = Float.parseFloat((String) dictionary.get("duration"));

        blendSrc = Integer.parseInt((String) dictionary.get("blendFuncSource"));
        blendDst = Integer.parseInt((String) dictionary.get("blendFuncDestination"));

        m_tStartColor.r = Float.parseFloat((String) dictionary.get("startColorRed"));
        m_tStartColor.g = Float.parseFloat((String) dictionary.get("startColorGreen"));
        m_tStartColor.b = Float.parseFloat((String) dictionary.get("startColorBlue"));
        m_tStartColor.a = Float.parseFloat((String) dictionary.get("startColorAlpha"));

        m_tStartColorVar.r = Float.parseFloat((String) dictionary.get("startColorVarianceRed"));
        m_tStartColorVar.g = Float.parseFloat((String) dictionary.get("startColorVarianceGreen"));
        m_tStartColorVar.b = Float.parseFloat((String) dictionary.get("startColorVarianceBlue"));
        m_tStartColorVar.a = Float.parseFloat((String) dictionary.get("startColorVarianceAlpha"));

        m_tEndColor.r = Float.parseFloat((String) dictionary.get("finishColorRed"));
        m_tEndColor.g = Float.parseFloat((String) dictionary.get("finishColorGreen"));
        m_tEndColor.b = Float.parseFloat((String) dictionary.get("finishColorBlue"));
        m_tEndColor.a = Float.parseFloat((String) dictionary.get("finishColorAlpha"));

        m_tEndColorVar.r = Float.parseFloat((String) dictionary.get("finishColorVarianceRed"));
        m_tEndColorVar.g = Float.parseFloat((String) dictionary.get("finishColorVarianceGreen"));
        m_tEndColorVar.b = Float.parseFloat((String) dictionary.get("finishColorVarianceBlue"));
        m_tEndColorVar.a = Float.parseFloat((String) dictionary.get("finishColorVarianceAlpha"));

        // particle size
        m_fStartSize = Float.parseFloat((String) dictionary.get("startParticleSize"));
        m_fStartSizeVar = Float.parseFloat((String) dictionary.get("startParticleSizeVariance"));
        m_fEndSize = Float.parseFloat((String) dictionary.get("finishParticleSize"));
        m_fEndSizeVar = Float.parseFloat((String) dictionary.get("finishParticleSizeVariance"));

        // position
        float x = Float.parseFloat((String) dictionary.get("sourcePositionx"));
        float y = Float.parseFloat((String) dictionary.get("sourcePositiony"));
        m_tSourcePosition.x = x;
        m_tSourcePosition.y = y;
        this.setPosition(x, y);
        m_tPosVar.x = Float.parseFloat((String) dictionary.get("sourcePositionVariancex"));
        m_tPosVar.y = Float.parseFloat((String) dictionary.get("sourcePositionVariancey"));

        // Spinning
        m_fStartSpin = Float.parseFloat((String) dictionary.get("rotationStart"));
        m_fStartSpinVar = Float.parseFloat((String) dictionary.get("rotationStartVariance"));
        m_fEndSpin = Float.parseFloat((String) dictionary.get("rotationEnd"));
        m_fEndSpinVar = Float.parseFloat((String) dictionary.get("rotationEndVariance"));

        float em = Float.parseFloat((String) dictionary.get("emitterType"));
        m_nEmitterMode = (int) em;

        if (m_nEmitterMode == ParticleModeGravity) {
            // gravity
            modeA.gravity.x = Float.parseFloat((String) dictionary.get("gravityx"));
            modeA.gravity.y = Float.parseFloat((String) dictionary.get("gravityy"));
            // speed
            modeA.speed = Float.parseFloat((String) dictionary.get("speed"));
            modeA.speedVar = Float.parseFloat((String) dictionary.get("speedVariance"));
            // radial acceleration
            modeA.radialAccel = Float.parseFloat((String) dictionary.get("radialAcceleration"));
            modeA.radialAccelVar = Float.parseFloat((String) dictionary.get("radialAccelVariance"));
            // tangential acceleration
            modeA.tangentialAccel = Float.parseFloat((String) dictionary.get("tangentialAcceleration"));
            modeA.tangentialAccelVar = Float.parseFloat((String) dictionary.get("tangentialAccelVariance"));
            // rotation is dir
            modeA.rotationIsDir = Boolean.parseBoolean((String) dictionary.get("rotationIsDir"));
        } else if (m_nEmitterMode == ParticleModeRadius) {
            modeB.startRadius = Float.parseFloat((String) dictionary.get("maxRadius"));
            modeB.startRadiusVar = Float.parseFloat((String) dictionary.get("maxRadiusVariance"));
            modeB.endRadius = Float.parseFloat((String) dictionary.get("minRadius"));
            modeB.endRadiusVar = 0.0f;
            modeB.rotatePerSecond = Float.parseFloat((String) dictionary.get("rotatePerSecond"));
            modeB.rotatePerSecondVar = Float.parseFloat((String) dictionary.get("rotatePerSecondVariance"));
        } else {
            throw new IllegalArgumentException("Invalid emitterType in config file");
        }
        // life span
        m_fLife = Float.parseFloat((String) dictionary.get("particleLifespan"));
        m_fLifeVar = Float.parseFloat((String) dictionary.get("particleLifespanVariance"));
        // emission Rate
        m_fEmissionRate = m_uTotalParticles / m_fLife;
        if (texture != null) {
            ownesTexture = false;
            m_pTexture = texture;
        } else {
            ownesTexture = true;
            String textureName = (String) dictionary.get("textureFileName");
            FileHandle handle;
            if (dir.equals("")) {
                handle = Gdx.files.internal(textureName);
            } else {
                handle = Gdx.files.internal(dir + "/" + textureName);
            }
            if (handle.exists() && !handle.isDirectory()) {
                m_pTexture = new Texture(handle);
            } else {
                String textureData = new String(((String) dictionary.get("textureImageData")).getBytes(),
                    Charset.forName("UTF-8"));
                int dataLen = textureData.length();
                if (dataLen > 0) {
                    byte[] decodeData = Base64Coder.decode(textureData);
                    byte[] imageData = LyU.unGzip(decodeData);
                    Pixmap image = new Pixmap(imageData, 0, imageData.length);
                    m_pTexture = new Texture(image);
                    image.dispose();
                }
            }
        }
    }

    public void initWithDictionary(ObjectMap<String, Object> dictionary, String dir) {
        initWithDictionary(dictionary, dir, null);
    }

    public void initWithTotalParticles(int numberOfParticles) {
        initWithTotalParticles(numberOfParticles, null);
    }

    public void initWithTotalParticles(int numberOfParticles, Texture texture) {
        m_uTotalParticles = numberOfParticles;
        m_pParticles = new Particle[m_uTotalParticles];
        vertices = new float[m_uTotalParticles][20];
        m_uAllocatedParticles = numberOfParticles;
        m_bIsActive = true;
        // default blend function
        blendSrc = GL20.GL_ONE;
        blendDst = GL20.GL_ONE_MINUS_SRC_ALPHA;
        m_ePositionType = PositionTypeFree;
        m_nEmitterMode = ParticleModeGravity;
        m_bIsAutoRemoveOnFinish = true;
        if (texture != null) {
            ownesTexture = false;
            setTexture(m_pTexture);
        } else {
            ownesTexture = true;
        }
    }

    public void setAutoRemoveOnFinish(boolean flag) {
        m_bIsAutoRemoveOnFinish = flag;
    }


    public void setBlendAdditive(boolean additive) {
        if (additive) {
            blendSrc = GL20.GL_SRC_ALPHA;
            blendDst = GL20.GL_ONE;
        } else {
            if (!preMultipliedAlpha) {
                blendSrc = GL20.GL_SRC_ALPHA;
                blendDst = GL20.GL_ONE_MINUS_SRC_ALPHA;
            } else {
                blendSrc = GL20.GL_ONE;
                blendDst = GL20.GL_ONE_MINUS_SRC_ALPHA;
            }
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        update(delta);
    }

    private Vector2 currentPosition = new Vector2();
    private Vector2 radial = new Vector2();
    private Vector2 tangential = new Vector2();

    protected void update(float dt) {
        if (m_bIsActive && m_fEmissionRate != 0) {
            float rate = 1.0f / m_fEmissionRate;
            //issue #1201, prevent bursts of particles, due to too high emitCounter
            if (m_uParticleCount < m_uTotalParticles) {
                m_fEmitCounter += dt;
            }
            while (m_uParticleCount < m_uTotalParticles && m_fEmitCounter > rate) {
                this.addParticle();
                m_fEmitCounter -= rate;
            }
            m_fElapsed += dt;
            if (m_fDuration != ParticleDurationInfinity && m_fDuration < m_fElapsed) {
                this.stopSystem();
            }
        }

        m_uParticleIdx = 0;
        currentPosition.setZero();
        if (m_ePositionType == PositionTypeFree) {
            tmpVector2.setZero();
            //currentPosition.set(this.localToStageCoordinates(new Vector2()));
            currentPosition.set(this.localToStageCoordinates(tmpVector2));
        } else if (m_ePositionType == PositionTypeRelative) {
            currentPosition.set(getX(), getY());
        }

        if (isVisible()) {
            while (m_uParticleIdx < m_uParticleCount) {
                Particle p = m_pParticles[m_uParticleIdx];
                p.timeToLive -= dt;
                if (p.timeToLive > 0) {
                    // Mode A: gravity, direction, tangential accel & radial accel
                    if (m_nEmitterMode == ParticleModeGravity) {
                        //Vector2 tmp, radial, tangential;
                        //Vector2 tmp;

                        radial.setZero();

                        //radial = new Vector2(p.pos);
                        radial.set(p.pos);

                        // radial acceleration
                        if (p.pos.x != 0 || p.pos.y != 0) {
                            radial.nor();
                        }
                        tangential.setZero();

                        //tangential = new Vector2(radial);
                        tangential.set(radial);

                        radial.scl(p.modeA.radialAccel);
                        // tangential acceleration
                        float newy = tangential.x;
                        tangential.x = -tangential.y;
                        tangential.y = newy;
                        tangential.scl(p.modeA.tangentialAccel);
                        // (gravity + radial + tangential) * dt
//                        tmp = new Vector2(radial.x+tangential.x+modeA.gravity.x,
//                                radial.y+tangential.y+modeA.gravity.y);
                        tmpVector2.setZero();
                        tmpVector2.set(radial.x + tangential.x + modeA.gravity.x,
                            radial.y + tangential.y + modeA.gravity.y);

                        tmpVector2.scl(dt);
                        p.modeA.dir.add(tmpVector2);
                        tmpVector2.set(p.modeA.dir.x * dt, p.modeA.dir.y * dt);
                        p.pos.add(tmpVector2);

//                        tmp.scl(dt);
//                        p.modeA.dir.add(tmp);
//                        tmp.set(p.modeA.dir.x * dt, p.modeA.dir.y * dt);
//                        p.pos.add(tmp);
                    } else {
                        // Update the angle and radius of the particle.
                        p.modeB.angle += p.modeB.degreesPerSecond * dt;
                        p.modeB.radius += p.modeB.deltaRadius * dt;
                        p.pos.x = -MathUtils.cos(p.modeB.angle) * p.modeB.radius;
                        p.pos.y = -MathUtils.sin(p.modeB.angle) * p.modeB.radius;

                        tmpVector2.setZero();
                        tmpVector2.set(this.getX(), this.getY());
                        p.pos.add(tmpVector2);
                    }
                    // 粒子颜色
                    p.color.r += (p.deltaColor.r * dt);
                    p.color.g += (p.deltaColor.g * dt);
                    p.color.b += (p.deltaColor.b * dt);
                    p.color.a += (p.deltaColor.a * dt);
                    // 粒子大小
                    p.size += (p.deltaSize * dt);
                    p.size = Math.max(0, p.size);
                    // 粒子旋转
                    p.rotation += (p.deltaRotation * dt);
//                    Vector2 newPos;
                    tmpVector2.setZero();
                    if (m_ePositionType == PositionTypeFree || m_ePositionType == PositionTypeRelative) {
                        //newPos = new Vector2(p.pos.x-currentPosition.x+p.startPos.x, p.pos.y-currentPosition.y+p.startPos.y);
//                    	newPos = new Vector2(p.pos.x-currentPosition.x+p.startPos.x, p.pos.y-currentPosition.y+p.startPos.y);
                        tmpVector2.set(p.pos.x - currentPosition.x + p.startPos.x, p.pos.y - currentPosition.y + p.startPos.y);
                    } else {
//                        newPos = new Vector2(p.pos);
                        tmpVector2.set(p.pos);
                    }
//                    updateQuadWithParticle(p, newPos);

                    //System.out.println("-->"+p.pos+","+p.startPos+","+currentPosition);
                    //int a = 1/0;
                    updateQuadWithParticle(p, tmpVector2);

                    ++m_uParticleIdx;
                } else {
                    if (m_uParticleIdx != m_uParticleCount - 1) {
                        m_pParticles[m_uParticleIdx] = m_pParticles[m_uParticleCount - 1];
                    }
//                    System.out.println("m_uParticleCount:"+m_uParticleCount+",m_pParticles.size:"+m_pParticles.length+","+","+m_pParticles[m_uParticleCount-1]);
                    --m_uParticleCount;

                    //G.free(p);
                    Pools.free(p);
                    //System.err.println("free p:"+p);
                    if (m_uParticleCount == 0 && m_bIsAutoRemoveOnFinish) {
                        remove();
                        return;
                    }
                }
            }
        }
    }

    private Color tmpColor = new Color();

    protected void updateQuadWithParticle(Particle particle, Vector2 newPosition) {
        float[] toUpdate = vertices[m_uParticleIdx];
        Color color = (m_bOpacityModifyRGB)
            ? tmpColor.set(particle.color.r * particle.color.a, particle.color.g * particle.color.a, particle.color.b * particle.color.a, particle.color.a) //new Color( particle.color.r*particle.color.a, particle.color.g*particle.color.a, particle.color.b*particle.color.a, particle.color.a)
            : particle.color;
        float colorFloat = color.toFloatBits();
        toUpdate[C1] = colorFloat;
        toUpdate[C2] = colorFloat;
        toUpdate[C3] = colorFloat;
        toUpdate[C4] = colorFloat;
        toUpdate[U1] = 0;
        toUpdate[V1] = 1;
        toUpdate[U2] = 0;
        toUpdate[V2] = 0;
        toUpdate[U3] = 1;
        toUpdate[V3] = 0;
        toUpdate[U4] = 1;
        toUpdate[V4] = 1;
        float size_2 = particle.size / 2;
        if (particle.rotation != 0) {
            float x1 = -size_2;
            float y1 = -size_2;
            float x2 = size_2;
            float y2 = size_2;
            float x = newPosition.x;
            float y = newPosition.y;
            float cr = MathUtils.cosDeg(particle.rotation);
            float sr = MathUtils.sinDeg(particle.rotation);
            float ax = x1 * cr - y1 * sr + x;
            float ay = x1 * sr + y1 * cr + y;
            float bx = x2 * cr - y1 * sr + x;
            float by = x2 * sr + y1 * cr + y;
            float cx = x2 * cr - y2 * sr + x;
            float cy = x2 * sr + y2 * cr + y;
            float dx = x1 * cr - y2 * sr + x;
            float dy = x1 * sr + y2 * cr + y;
            // bottom-left
            toUpdate[X1] = ax;
            toUpdate[Y1] = ay;
            // bottom-right vertex:
            toUpdate[X4] = bx;
            toUpdate[Y4] = by;
            // top-left vertex:
            toUpdate[X2] = dx;
            toUpdate[Y2] = dy;
            // top-right vertex:
            toUpdate[X3] = cx;
            toUpdate[Y3] = cy;
            //System.out.println("111");
        } else {
            //System.out.println("222 "+newPosition);
            // bottom-left vertex:
            toUpdate[X1] = newPosition.x - size_2;
            toUpdate[Y1] = newPosition.y - size_2;
            // bottom-right vertex:
            toUpdate[X4] = newPosition.x + size_2;
            toUpdate[Y4] = newPosition.y - size_2;
            // top-left vertex:
            toUpdate[X2] = newPosition.x - size_2;
            toUpdate[Y2] = newPosition.y + size_2;
            // top-right vertex:
            toUpdate[X3] = newPosition.x + size_2;
            toUpdate[Y3] = newPosition.y + size_2;
        }
    }

    public void setBlendFunc(int src, int dst) {
        if (blendSrc != src || blendDst != dst) {
            blendSrc = src;
            blendDst = dst;
            updateBlendFunc();
        }
    }

    protected void updateBlendFunc() {
        m_bOpacityModifyRGB = false;
        if (blendSrc == GL20.GL_ONE && blendDst == GL20.GL_ONE_MINUS_SRC_ALPHA) {
            if (preMultipliedAlpha) {
                m_bOpacityModifyRGB = true;
            } else {
                blendSrc = GL20.GL_SRC_ALPHA;
                blendDst = GL20.GL_ONE_MINUS_SRC_ALPHA;
            }
        }
    }

    public void setTexture(Texture texture, boolean preMultipliedAlpha) {
        if (this.m_pTexture != texture) {
            if (m_pTexture != null && ownesTexture) {
                m_pTexture.dispose();
            }
            m_pTexture = texture;
            this.preMultipliedAlpha = preMultipliedAlpha;
            updateBlendFunc();
        }
    }

    public void setTexture(Texture texture) {
        setTexture(texture, false);
    }

    public void setOpacityModifyRGB(boolean value) {
        m_bOpacityModifyRGB = value;
    }

    public boolean isOpacityModifyRGB() {
        return m_bOpacityModifyRGB;
    }
//    @Override
//    protected void drawChildren(Batch batch, float parentAlpha) {
//        super.drawChildren(batch, parentAlpha);
//        drawParticles(batch);
//    }

    public void draw(Batch batch, float parentAlpha) {
        drawParticles(batch);

    }

    //
    protected void drawParticles(Batch batch) {
        int srcFunc = batch.getBlendSrcFunc();
        int dstFunc = batch.getBlendDstFunc();

        //System.out.println(blendSrc+","+GL20.GL_ONE+"|"+blendDst+","+GL20.GL_ONE);
        batch.setBlendFunction(blendSrc, blendDst);


        for (int i = 0; i < m_uParticleCount; i++) {
            batch.draw(m_pTexture, vertices[i], 0, 20);
        }
        batch.setBlendFunction(srcFunc, dstFunc);
//        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void stopSystem() {
        m_bIsActive = false;
        m_fElapsed = m_fDuration;
        m_fEmitCounter = 0;
    }

    public boolean isFull() {
        return (m_uParticleCount == m_uTotalParticles);
    }

    static final Pool<Particle> particlePool = Pools.get(Particle.class, 640);

    public boolean addParticle() {
        if (this.isFull()) {
            return false;
        }
//        m_pParticles[ m_uParticleCount ] = new Particle();
        // System.out.println(" m_pParticles[ m_uParticleCount ]:"+ m_pParticles[ m_uParticleCount ]+",size:"+(m_pParticles.length)+",m_uParticleCount:"+m_uParticleCount);
        m_pParticles[m_uParticleCount] = particlePool.obtain();//Pools.obtain(Particle.class);
        this.initParticle(m_pParticles[m_uParticleCount]);
        ++m_uParticleCount;
        return true;
    }


    private Color start = new Color();
    private Color end = new Color();
    private Vector2 tmpVector2 = new Vector2();

    public void initParticle(Particle particle) {
        //life
        particle.timeToLive = m_fLife + m_fLifeVar * MathUtils.random(-1f, 1f);
        particle.timeToLive = Math.max(0, particle.timeToLive);
        //color
        //Color start = new Color();
        start.r = MathUtils.clamp(m_tStartColor.r + m_tStartColorVar.r * MathUtils.random(-1f, 1f), 0, 1);
        start.g = MathUtils.clamp(m_tStartColor.g + m_tStartColorVar.g * MathUtils.random(-1f, 1f), 0, 1);
        start.b = MathUtils.clamp(m_tStartColor.b + m_tStartColorVar.b * MathUtils.random(-1f, 1f), 0, 1);
        start.a = MathUtils.clamp(m_tStartColor.a + m_tStartColorVar.a * MathUtils.random(-1f, 1f), 0, 1);
        // Color end = new Color();
        end.r = MathUtils.clamp(m_tEndColor.r + m_tEndColorVar.r * MathUtils.random(-1f, 1f), 0, 1);
        end.g = MathUtils.clamp(m_tEndColor.g + m_tEndColorVar.g * MathUtils.random(-1f, 1f), 0, 1);
        end.b = MathUtils.clamp(m_tEndColor.b + m_tEndColorVar.b * MathUtils.random(-1f, 1f), 0, 1);
        end.a = MathUtils.clamp(m_tEndColor.a + m_tEndColorVar.a * MathUtils.random(-1f, 1f), 0, 1);
        particle.color.set(start);
        particle.deltaColor.r = (end.r - start.r) / particle.timeToLive;
        particle.deltaColor.g = (end.g - start.g) / particle.timeToLive;
        particle.deltaColor.b = (end.b - start.b) / particle.timeToLive;
        particle.deltaColor.a = (end.a - start.a) / particle.timeToLive;
        //size
        float startS = m_fStartSize + m_fStartSizeVar * MathUtils.random(-1f, 1f);
        startS = Math.max(0, startS); // No negative value
        particle.size = startS;
        if (m_fEndSize == ParticleStartSizeEqualToEndSize) {
            particle.deltaSize = 0;
        } else {
            float endS = m_fEndSize + m_fEndSizeVar * MathUtils.random(-1f, 1f);
            endS = Math.max(0, endS); // No negative values
            particle.deltaSize = (endS - startS) / particle.timeToLive;
        }
        // rotation
        float startA = m_fStartSpin + m_fStartSpinVar * MathUtils.random(-1f, 1f);
        float endA = m_fEndSpin + m_fEndSpinVar * MathUtils.random(-1f, 1f);
        particle.rotation = startA;
        particle.deltaRotation = (endA - startA) / particle.timeToLive;
        //position
        particle.pos.x = m_tSourcePosition.x + m_tPosVar.x * MathUtils.random(-1f, 1f);
        particle.pos.y = m_tSourcePosition.y + m_tPosVar.y * MathUtils.random(-1f, 1f);
        if (m_ePositionType == PositionTypeFree) {
//            particle.startPos.set(this.localToStageCoordinates(new Vector2()));
            tmpVector2.setZero();
            particle.startPos.set(this.localToStageCoordinates(tmpVector2));
        } else if (m_ePositionType == PositionTypeRelative) {
            particle.startPos.set(getX(), getY());
        }
        // direction
        float a = MathUtils.degRad * (m_fAngle + m_fAngleVar * MathUtils.random(-1f, 1f));
        // Mode Gravity: A
        if (m_nEmitterMode == ParticleModeGravity) {
            // Vector2 v = new Vector2(MathUtils.cos(a), MathUtils.sin(a));
            tmpVector2.setZero();
            Vector2 v = tmpVector2;
            v.set(MathUtils.cos(a), MathUtils.sin(a));

            float s = modeA.speed + modeA.speedVar * MathUtils.random(-1f, 1f);
            // direction
            particle.modeA.dir.set(v.x * s, v.y * s);
            // radial accel
            particle.modeA.radialAccel = modeA.radialAccel + modeA.radialAccelVar * MathUtils.random(-1f, 1f);
            // tangential accel
            particle.modeA.tangentialAccel = modeA.tangentialAccel + modeA.tangentialAccelVar * MathUtils.random(-1f, 1f);
            // rotation is dir
            if (modeA.rotationIsDir)
                particle.rotation = -particle.modeA.dir.angle();
        } else {
            // Set the default diameter of the particle from the source position
            float startRadius = modeB.startRadius + modeB.startRadiusVar * MathUtils.random(-1f, 1f);
            float endRadius = modeB.endRadius + modeB.endRadiusVar * MathUtils.random(-1f, 1f);
            particle.modeB.radius = startRadius;
            if (modeB.endRadius == ParticleStartRadiusEqualToEndRadius) {
                particle.modeB.deltaRadius = 0;
            } else {
                particle.modeB.deltaRadius = (endRadius - startRadius) / particle.timeToLive;
            }
            particle.modeB.angle = a;
            particle.modeB.degreesPerSecond = MathUtils.degRad * (modeB.rotatePerSecond + modeB.rotatePerSecondVar * MathUtils.random(-1f, 1f));
        }
    }

    public void setEmissionRate(float emissionRate) {
        assert emissionRate > 0;
        m_fEmissionRate = emissionRate;
    }

    public void setPositionType(int type) {
        if (type != 0 && type != 1 && type != 2) {
            throw new IllegalArgumentException("type error!");
        }
        m_ePositionType = type;
    }


  /*  public void setParticlePosition(float x,float y){
        m_tSourcePosition.x = x;
    	m_tSourcePosition.y = y;
    	setPosition(x, y);
    }*/


    public void positionChanged() {
        m_tSourcePosition.x = this.getX();
        m_tSourcePosition.y = this.getY();
    }

    public void setParticleMode(int mode) {
        if (mode != 0 && mode != 1) {
            throw new IllegalArgumentException("mode error!");
        }
        m_nEmitterMode = mode;
    }

    //持续时间无限
    public static final float ParticleDurationInfinity = -1;
    //开始大小等于结束大小
    public static final float ParticleStartSizeEqualToEndSize = -1;
    //开始半径等于结束半径
    public static final float ParticleStartRadiusEqualToEndRadius = -1;

    public static final int ParticleModeGravity = 0;
    public static final int ParticleModeRadius = 1;

    public static final int PositionTypeFree = 0;
    public static final int PositionTypeRelative = 1;
    public static final int PositionTypeGrouped = 2;


    static class ModeA {
        Vector2 gravity = new Vector2();
        float speed;
        float speedVar;
        float tangentialAccel;
        float tangentialAccelVar;
        float radialAccel;
        float radialAccelVar;
        boolean rotationIsDir;
    }

    ;

    static class ModeB {
        float startRadius;
        float startRadiusVar;
        float endRadius;
        float endRadiusVar;
        float rotatePerSecond;
        float rotatePerSecondVar;
    }

    ;

    float m_fElapsed;
    Particle[] m_pParticles;
    float m_fEmitCounter;
    int m_uParticleIdx;
    int m_uAllocatedParticles;
    boolean m_bIsActive;
    int m_uParticleCount;
    float m_fDuration;
    public Vector2 m_tSourcePosition = new Vector2();
    Vector2 m_tPosVar = new Vector2();
    float m_fLife, m_fLifeVar;
    public float m_fAngle, m_fAngleVar;
    float m_fStartSize, m_fStartSizeVar;
    float m_fEndSize, m_fEndSizeVar;
    Color m_tStartColor = new Color(), m_tStartColorVar = new Color();
    Color m_tEndColor = new Color(), m_tEndColorVar = new Color();
    float m_fStartSpin, m_fStartSpinVar;
    float m_fEndSpin, m_fEndSpinVar;
    float m_fEmissionRate;
    int m_uTotalParticles;
    Texture m_pTexture;
    int blendSrc, blendDst;
    int m_ePositionType;
    boolean m_bIsAutoRemoveOnFinish;
    int m_nEmitterMode;
    float[][] vertices;
    ModeA modeA = new ModeA();
    ModeB modeB = new ModeB();
    boolean m_bOpacityModifyRGB;
    boolean preMultipliedAlpha;

    public static Texture getDefaultTexture() {
        Pixmap pImage = new Pixmap(__firePngData, 0, __firePngData.length);
        Texture texture = new Texture(pImage);
        pImage.dispose();
        return texture;
    }

    public static byte[] __firePngData = {
        (byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47, (byte) 0x0D, (byte) 0x0A, (byte) 0x1A, (byte) 0x0A, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0D, (byte) 0x49, (byte) 0x48, (byte) 0x44, (byte) 0x52,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x20, (byte) 0x08, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x73, (byte) 0x7A, (byte) 0x7A,
        (byte) 0xF4, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x67, (byte) 0x41, (byte) 0x4D, (byte) 0x41, (byte) 0x00, (byte) 0x00, (byte) 0xAF, (byte) 0xC8, (byte) 0x37, (byte) 0x05, (byte) 0x8A,
        (byte) 0xE9, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x19, (byte) 0x74, (byte) 0x45, (byte) 0x58, (byte) 0x74, (byte) 0x53, (byte) 0x6F, (byte) 0x66, (byte) 0x74, (byte) 0x77, (byte) 0x61, (byte) 0x72,
        (byte) 0x65, (byte) 0x00, (byte) 0x41, (byte) 0x64, (byte) 0x6F, (byte) 0x62, (byte) 0x65, (byte) 0x20, (byte) 0x49, (byte) 0x6D, (byte) 0x61, (byte) 0x67, (byte) 0x65, (byte) 0x52, (byte) 0x65, (byte) 0x61,
        (byte) 0x64, (byte) 0x79, (byte) 0x71, (byte) 0xC9, (byte) 0x65, (byte) 0x3C, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x64, (byte) 0x49, (byte) 0x44, (byte) 0x41, (byte) 0x54, (byte) 0x78, (byte) 0xDA,
        (byte) 0xC4, (byte) 0x97, (byte) 0x89, (byte) 0x6E, (byte) 0xEB, (byte) 0x20, (byte) 0x10, (byte) 0x45, (byte) 0xBD, (byte) 0xE1, (byte) 0x2D, (byte) 0x4B, (byte) 0xFF, (byte) 0xFF, (byte) 0x37, (byte) 0x5F,
        (byte) 0x5F, (byte) 0x0C, (byte) 0xD8, (byte) 0xC4, (byte) 0xAE, (byte) 0x2D, (byte) 0xDD, (byte) 0xA9, (byte) 0x6E, (byte) 0xA7, (byte) 0x38, (byte) 0xC1, (byte) 0x91, (byte) 0xAA, (byte) 0x44, (byte) 0xBA,
        (byte) 0xCA, (byte) 0x06, (byte) 0xCC, (byte) 0x99, (byte) 0x85, (byte) 0x01, (byte) 0xE7, (byte) 0xCB, (byte) 0xB2, (byte) 0x64, (byte) 0xEF, (byte) 0x7C, (byte) 0x55, (byte) 0x2F, (byte) 0xCC, (byte) 0x69,
        (byte) 0x56, (byte) 0x15, (byte) 0xAB, (byte) 0x72, (byte) 0x68, (byte) 0x81, (byte) 0xE6, (byte) 0x55, (byte) 0xFE, (byte) 0xE8, (byte) 0x62, (byte) 0x79, (byte) 0x62, (byte) 0x04, (byte) 0x36, (byte) 0xA3,
        (byte) 0x06, (byte) 0xC0, (byte) 0x9B, (byte) 0xCA, (byte) 0x08, (byte) 0xC0, (byte) 0x7D, (byte) 0x55, (byte) 0x80, (byte) 0xA6, (byte) 0x54, (byte) 0x98, (byte) 0x67, (byte) 0x11, (byte) 0xA8, (byte) 0xA1,
        (byte) 0x86, (byte) 0x3E, (byte) 0x0B, (byte) 0x44, (byte) 0x41, (byte) 0x00, (byte) 0x33, (byte) 0x19, (byte) 0x1F, (byte) 0x21, (byte) 0x43, (byte) 0x9F, (byte) 0x5F, (byte) 0x02, (byte) 0x68, (byte) 0x49,
        (byte) 0x1D, (byte) 0x20, (byte) 0x1A, (byte) 0x82, (byte) 0x28, (byte) 0x09, (byte) 0xE0, (byte) 0x4E, (byte) 0xC6, (byte) 0x3D, (byte) 0x64, (byte) 0x57, (byte) 0x39, (byte) 0x80, (byte) 0xBA, (byte) 0xA3,
        (byte) 0x00, (byte) 0x1D, (byte) 0xD4, (byte) 0x93, (byte) 0x3A, (byte) 0xC0, (byte) 0x34, (byte) 0x0F, (byte) 0x00, (byte) 0x3C, (byte) 0x8C, (byte) 0x59, (byte) 0x4A, (byte) 0x99, (byte) 0x44, (byte) 0xCA,
        (byte) 0xA6, (byte) 0x02, (byte) 0x88, (byte) 0xC7, (byte) 0xA7, (byte) 0x55, (byte) 0x67, (byte) 0xE8, (byte) 0x44, (byte) 0x10, (byte) 0x12, (byte) 0x05, (byte) 0x0D, (byte) 0x30, (byte) 0x92, (byte) 0xE7,
        (byte) 0x52, (byte) 0x33, (byte) 0x32, (byte) 0x26, (byte) 0xC3, (byte) 0x38, (byte) 0xF7, (byte) 0x0C, (byte) 0xA0, (byte) 0x06, (byte) 0x40, (byte) 0x0F, (byte) 0xC3, (byte) 0xD7, (byte) 0x55, (byte) 0x17,
        (byte) 0x05, (byte) 0xD1, (byte) 0x92, (byte) 0x77, (byte) 0x02, (byte) 0x20, (byte) 0x85, (byte) 0xB7, (byte) 0x19, (byte) 0x18, (byte) 0x28, (byte) 0x4D, (byte) 0x05, (byte) 0x19, (byte) 0x9F, (byte) 0xA1,
        (byte) 0xF1, (byte) 0x08, (byte) 0xC0, (byte) 0x05, (byte) 0x10, (byte) 0x57, (byte) 0x7C, (byte) 0x4F, (byte) 0x01, (byte) 0x10, (byte) 0xEF, (byte) 0xC5, (byte) 0xF8, (byte) 0xAC, (byte) 0x76, (byte) 0xC8,
        (byte) 0x2E, (byte) 0x80, (byte) 0x14, (byte) 0x99, (byte) 0xE4, (byte) 0xFE, (byte) 0x44, (byte) 0x51, (byte) 0xB8, (byte) 0x52, (byte) 0x14, (byte) 0x3A, (byte) 0x32, (byte) 0x22, (byte) 0x00, (byte) 0x13,
        (byte) 0x85, (byte) 0xBF, (byte) 0x52, (byte) 0xC6, (byte) 0x05, (byte) 0x8E, (byte) 0xE5, (byte) 0x63, (byte) 0x00, (byte) 0x86, (byte) 0xB6, (byte) 0x9C, (byte) 0x86, (byte) 0x38, (byte) 0xAB, (byte) 0x54,
        (byte) 0x74, (byte) 0x18, (byte) 0x5B, (byte) 0x50, (byte) 0x58, (byte) 0x6D, (byte) 0xC4, (byte) 0xF3, (byte) 0x89, (byte) 0x6A, (byte) 0xC3, (byte) 0x61, (byte) 0x8E, (byte) 0xD9, (byte) 0x03, (byte) 0xA8,
        (byte) 0x08, (byte) 0xA0, (byte) 0x55, (byte) 0xBB, (byte) 0x40, (byte) 0x40, (byte) 0x3E, (byte) 0x00, (byte) 0xD2, (byte) 0x53, (byte) 0x47, (byte) 0x94, (byte) 0x0E, (byte) 0x38, (byte) 0xD0, (byte) 0x7A,
        (byte) 0x73, (byte) 0x64, (byte) 0x57, (byte) 0xF0, (byte) 0x16, (byte) 0xFE, (byte) 0x95, (byte) 0x82, (byte) 0x86, (byte) 0x1A, (byte) 0x4C, (byte) 0x4D, (byte) 0xE9, (byte) 0x68, (byte) 0xD5, (byte) 0xAE,
        (byte) 0xB8, (byte) 0x00, (byte) 0xE2, (byte) 0x8C, (byte) 0xDF, (byte) 0x4B, (byte) 0xE4, (byte) 0xD7, (byte) 0xC1, (byte) 0xB3, (byte) 0x4C, (byte) 0x75, (byte) 0xC2, (byte) 0x36, (byte) 0xD2, (byte) 0x3F,
        (byte) 0x2A, (byte) 0x7C, (byte) 0xF7, (byte) 0x0C, (byte) 0x50, (byte) 0x60, (byte) 0xB1, (byte) 0x4A, (byte) 0x81, (byte) 0x18, (byte) 0x88, (byte) 0xD3, (byte) 0x22, (byte) 0x75, (byte) 0xD1, (byte) 0x63,
        (byte) 0x5C, (byte) 0x80, (byte) 0xF7, (byte) 0x19, (byte) 0x15, (byte) 0xA2, (byte) 0xA5, (byte) 0xB9, (byte) 0xB5, (byte) 0x5A, (byte) 0xB7, (byte) 0xA4, (byte) 0x34, (byte) 0x7D, (byte) 0x03, (byte) 0x48,
        (byte) 0x5F, (byte) 0x17, (byte) 0x90, (byte) 0x52, (byte) 0x01, (byte) 0x19, (byte) 0x95, (byte) 0x9E, (byte) 0x1E, (byte) 0xD1, (byte) 0x30, (byte) 0x30, (byte) 0x9A, (byte) 0x21, (byte) 0xD7, (byte) 0x0D,
        (byte) 0x81, (byte) 0xB3, (byte) 0xC1, (byte) 0x92, (byte) 0x0C, (byte) 0xE7, (byte) 0xD4, (byte) 0x1B, (byte) 0xBE, (byte) 0x49, (byte) 0xF2, (byte) 0x04, (byte) 0x15, (byte) 0x2A, (byte) 0x52, (byte) 0x06,
        (byte) 0x69, (byte) 0x31, (byte) 0xCA, (byte) 0xB3, (byte) 0x22, (byte) 0x71, (byte) 0xBD, (byte) 0x1F, (byte) 0x00, (byte) 0x4B, (byte) 0x82, (byte) 0x66, (byte) 0xB5, (byte) 0xA7, (byte) 0x37, (byte) 0xCF,
        (byte) 0x6F, (byte) 0x78, (byte) 0x0F, (byte) 0xF8, (byte) 0x5D, (byte) 0xC6, (byte) 0xA4, (byte) 0xAC, (byte) 0xF7, (byte) 0x23, (byte) 0x05, (byte) 0x6C, (byte) 0xE4, (byte) 0x4E, (byte) 0xE2, (byte) 0xE3,
        (byte) 0x95, (byte) 0xB7, (byte) 0xD3, (byte) 0x40, (byte) 0xF3, (byte) 0xA5, (byte) 0x06, (byte) 0x1C, (byte) 0xFE, (byte) 0x1F, (byte) 0x09, (byte) 0x2A, (byte) 0xA8, (byte) 0xF5, (byte) 0xE6, (byte) 0x3D,
        (byte) 0x00, (byte) 0xDD, (byte) 0xAD, (byte) 0x02, (byte) 0x2D, (byte) 0xC4, (byte) 0x4D, (byte) 0x66, (byte) 0xA0, (byte) 0x6A, (byte) 0x1F, (byte) 0xD5, (byte) 0x2E, (byte) 0xF8, (byte) 0x8F, (byte) 0xFF,
        (byte) 0x2D, (byte) 0xC6, (byte) 0x4F, (byte) 0x04, (byte) 0x1E, (byte) 0x14, (byte) 0xD0, (byte) 0xAC, (byte) 0x01, (byte) 0x3C, (byte) 0xAA, (byte) 0x5C, (byte) 0x1F, (byte) 0xA9, (byte) 0x2E, (byte) 0x72,
        (byte) 0xBA, (byte) 0x49, (byte) 0xB5, (byte) 0xC7, (byte) 0xFA, (byte) 0xC0, (byte) 0x27, (byte) 0xD2, (byte) 0x62, (byte) 0x69, (byte) 0xAE, (byte) 0xA7, (byte) 0xC8, (byte) 0x04, (byte) 0xEA, (byte) 0x0F,
        (byte) 0xBF, (byte) 0x1A, (byte) 0x51, (byte) 0x50, (byte) 0x61, (byte) 0x16, (byte) 0x8F, (byte) 0x1B, (byte) 0xD5, (byte) 0x5E, (byte) 0x03, (byte) 0x75, (byte) 0x35, (byte) 0xDD, (byte) 0x09, (byte) 0x6F,
        (byte) 0x88, (byte) 0xC4, (byte) 0x0D, (byte) 0x73, (byte) 0x07, (byte) 0x82, (byte) 0x61, (byte) 0x88, (byte) 0xE8, (byte) 0x59, (byte) 0x30, (byte) 0x45, (byte) 0x8E, (byte) 0xD4, (byte) 0x7A, (byte) 0xA7,
        (byte) 0xBD, (byte) 0xDA, (byte) 0x07, (byte) 0x67, (byte) 0x81, (byte) 0x40, (byte) 0x30, (byte) 0x88, (byte) 0x55, (byte) 0xF5, (byte) 0x11, (byte) 0x05, (byte) 0xF0, (byte) 0x58, (byte) 0x94, (byte) 0x9B,
        (byte) 0x48, (byte) 0xEC, (byte) 0x60, (byte) 0xF1, (byte) 0x09, (byte) 0xC7, (byte) 0xF1, (byte) 0x66, (byte) 0xFC, (byte) 0xDF, (byte) 0x0E, (byte) 0x84, (byte) 0x7F, (byte) 0x74, (byte) 0x1C, (byte) 0x8F,
        (byte) 0x58, (byte) 0x44, (byte) 0x77, (byte) 0xAC, (byte) 0x59, (byte) 0xB5, (byte) 0xD7, (byte) 0x67, (byte) 0x00, (byte) 0x12, (byte) 0x85, (byte) 0x4F, (byte) 0x2A, (byte) 0x4E, (byte) 0x17, (byte) 0xBB,
        (byte) 0x1F, (byte) 0xC6, (byte) 0x00, (byte) 0xB8, (byte) 0x99, (byte) 0xB0, (byte) 0xE7, (byte) 0x23, (byte) 0x9D, (byte) 0xF7, (byte) 0xCF, (byte) 0x6E, (byte) 0x44, (byte) 0x83, (byte) 0x4A, (byte) 0x45,
        (byte) 0x32, (byte) 0x40, (byte) 0x86, (byte) 0x81, (byte) 0x7C, (byte) 0x8D, (byte) 0xBA, (byte) 0xAB, (byte) 0x1C, (byte) 0xA7, (byte) 0xDE, (byte) 0x09, (byte) 0x87, (byte) 0x48, (byte) 0x21, (byte) 0x26,
        (byte) 0x5F, (byte) 0x4A, (byte) 0xAD, (byte) 0xBA, (byte) 0x6E, (byte) 0x4F, (byte) 0xCA, (byte) 0xFB, (byte) 0x23, (byte) 0xB7, (byte) 0x62, (byte) 0xF7, (byte) 0xCA, (byte) 0xAD, (byte) 0x58, (byte) 0x22,
        (byte) 0xC1, (byte) 0x00, (byte) 0x47, (byte) 0x9F, (byte) 0x0B, (byte) 0x7C, (byte) 0xCA, (byte) 0x73, (byte) 0xC1, (byte) 0xDB, (byte) 0x9F, (byte) 0x8C, (byte) 0xF2, (byte) 0x17, (byte) 0x1E, (byte) 0x4E,
        (byte) 0xDF, (byte) 0xF2, (byte) 0x6C, (byte) 0xF8, (byte) 0x67, (byte) 0xAF, (byte) 0x22, (byte) 0x7B, (byte) 0xF3, (byte) 0xEB, (byte) 0x4B, (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0xB8, (byte) 0x21,
        (byte) 0x72, (byte) 0x89, (byte) 0x08, (byte) 0x10, (byte) 0x07, (byte) 0x7D, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x49, (byte) 0x45, (byte) 0x4E, (byte) 0x44, (byte) 0xAE, (byte) 0x42,
        (byte) 0x60, (byte) 0x82
    };


}
