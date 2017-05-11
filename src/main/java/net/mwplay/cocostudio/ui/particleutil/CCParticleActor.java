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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

import java.nio.charset.Charset;

import static com.badlogic.gdx.graphics.g2d.Batch.*;

public class CCParticleActor extends Actor implements Disposable {
    protected boolean ownesTexture;
    private Vector2 tmp = new Vector2();
    private Vector2 radial = new Vector2();
    private Vector2 tangential = new Vector2();

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

        _positionType = PositionTypeGrouped;


        m_fAngle = Float.parseFloat((String) dictionary.get("angle"));
        m_fAngleVar = Float.parseFloat((String) dictionary.get("angleVariance"));

        // duration
        _duration = Float.parseFloat((String) dictionary.get("duration"));

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
        _endSize = Float.parseFloat((String) dictionary.get("finishParticleSize"));
        _endSizeVar = Float.parseFloat((String) dictionary.get("finishParticleSizeVariance"));

        // position
        float x = Float.parseFloat((String) dictionary.get("sourcePositionx"));
        float y = Float.parseFloat((String) dictionary.get("sourcePositiony"));

//        m_tSourcePosition.x = x;
//        m_tSourcePosition.y = y;
        this.setPosition(x, y);

        m_tPosVar.x = Float.parseFloat((String) dictionary.get("sourcePositionVariancex"));
        m_tPosVar.y = Float.parseFloat((String) dictionary.get("sourcePositionVariancey"));

        // Spinning
        m_fStartSpin = Float.parseFloat((String) dictionary.get("rotationStart"));
        m_fStartSpinVar = Float.parseFloat((String) dictionary.get("rotationStartVariance"));
        m_fEndSpin = Float.parseFloat((String) dictionary.get("rotationEnd"));
        m_fEndSpinVar = Float.parseFloat((String) dictionary.get("rotationEndVariance"));

        float em = Float.parseFloat((String) dictionary.get("emitterType"));
        _emitterMode = (int) em;

        if (_emitterMode == ParticleModeGravity) {
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
        } else if (_emitterMode == ParticleModeRadius) {
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
        _emissionRate = _totalParticles / m_fLife;
        if (texture != null) {
            ownesTexture = false;
            m_pTexture = texture;
        } else {
            ownesTexture = true;
            String textureName = (String) dictionary.get("textureFileName");
            FileHandle handle;
            if ("".equals(dir)) {
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
        _totalParticles = numberOfParticles;
        m_pParticles = new Particle[_totalParticles];
        vertices = new float[_totalParticles][20];
        m_uAllocatedParticles = numberOfParticles;
        _isActive = true;
        // default blend function
        blendSrc = GL20.GL_ONE;
        blendDst = GL20.GL_ONE_MINUS_SRC_ALPHA;
        _positionType = PositionTypeFree;
        _emitterMode = ParticleModeGravity;
        //
        _isAutoRemoveOnFinish = false;
        if (texture != null) {
            ownesTexture = false;
            setTexture(m_pTexture);
        } else {
            ownesTexture = true;
        }


    }

    public void setAutoRemoveOnFinish(boolean flag) {
        _isAutoRemoveOnFinish = flag;
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

    protected void update(float dt) {
        if (_isActive && _emissionRate != 0) {
            float rate = 1.0f / _emissionRate;
            //issue #1201, prevent bursts of particles, due to too high emitCounter
            if (_particleCount < _totalParticles) {
                _emitCounter += dt;

                if (_emitCounter < 0) {
                    _emitCounter = 0;
                }

            }

            int emitCount = (int) Math.min(_totalParticles - _particleCount, _emitCounter / rate);

            addParticle(emitCount);

            _emitCounter -= rate * emitCount;

            _elapsed += dt;
            if (_elapsed < 0) {
                _elapsed = 0;
            }

            if (_duration != DURATION_INFINITY && _duration < _elapsed) {
                this.stopSystem();
            }
        }


        currentPosition.setZero();
        if (_positionType == PositionTypeFree) {
            currentPosition = this.localToStageCoordinates(currentPosition);
        } else if (_positionType == PositionTypeRelative) {
            //currentPosition = m_tSourcePosition;
            currentPosition.set(this.getX(), this.getY());

        }

        for (int i = 0; i < _particleCount; ++i) {
            Particle _particleData = m_pParticles[i];
            //System.out.println("_particleData:"+_particleData.hashCode());
            _particleData.timeToLive -= dt;
            if (_particleData.timeToLive <= 0f) {
                if (i != _particleCount - 1) {
                    m_pParticles[i] = m_pParticles[_particleCount - 1];
                }

                --_particleCount;
                //G.free(_particleData);
                Pools.free(_particleData);
                if (_particleCount == 0 && _isAutoRemoveOnFinish) {
                    remove();
                    return;
                }
            } else {
                if (_emitterMode == ParticleModeGravity) {
                    tmp.setZero();
                    radial.setZero();
                    tangential.setZero();

                    if (_particleData.pos.x != 0 || _particleData.pos.y != 0) {
                        // nomalize_point(_particleData.pos.x,_particleData.pos.y,radial);
                        radial.set(_particleData.pos.x, _particleData.pos.y)
                            .nor();
                    }

                    tangential.set(radial);

                    radial.x *= _particleData.modeA.radialAccel;
                    radial.y *= _particleData.modeA.radialAccel;

                    // swap
                    float ttmp = tangential.x;
                    tangential.x = tangential.y;
                    tangential.y = ttmp;

                    tangential.x *= -_particleData.modeA.tangentialAccel;
                    tangential.y *= _particleData.modeA.tangentialAccel;

                    tmp.x = radial.x + tangential.x + modeA.gravity.x;
                    tmp.y = radial.y + tangential.y + modeA.gravity.y;
                    tmp.x *= dt;
                    tmp.y *= dt;

                    _particleData.modeA.dir.x += tmp.x;
                    _particleData.modeA.dir.y += tmp.y;

                    tmp.x = _particleData.modeA.dir.x * dt * _yCoordFlipped; // TODO
                    // _yCoordFlipped
                    tmp.y = _particleData.modeA.dir.y * dt * _yCoordFlipped;

                    _particleData.pos.x += tmp.x;
                    _particleData.pos.y += tmp.y;

                } else {
                    _particleData.modeB.angle += _particleData.modeB.degreesPerSecond
                        * dt;

                    _particleData.modeB.radius += _particleData.modeB.deltaRadius
                        * dt;

                    // _particleData.pos.x = -
                    // MathUtils.cos(_particleData.modeB.angle)*_particleData.modeB.radius;
                    _particleData.pos.x = (float) (-MathUtils
                        .cos(_particleData.modeB.angle) * _particleData.modeB.radius);

                    _particleData.pos.y = (float) (-MathUtils
                        .sin(_particleData.modeB.angle)
                        * _particleData.modeB.radius * _yCoordFlipped); // TODO
                    // _yCoordFlipped
                }

                _particleData.color.r += _particleData.deltaColor.r * dt;
                _particleData.color.g += _particleData.deltaColor.g * dt;
                _particleData.color.b += _particleData.deltaColor.b * dt;
                _particleData.color.a += _particleData.deltaColor.a * dt;

                _particleData.size += _particleData.deltaSize * dt;
                _particleData.size = Math.max(0, _particleData.size);

                _particleData.rotation += _particleData.deltaRotation * dt;
            }

            updateParticleQuads(_particleData, i);

        }

    }


    private void transformPoint(Vector3 v) {
        //transformVector(point->x, point->y, point->z, 1.0f, point)
        // MathUtil::transformVec4(m, x, y, z, w, (float*)dst);
        float w = 1;
        Matrix4 m4 = this.getStage().getCamera().combined;
        //m4.inv();
        float[] m = m4.getValues();
        v.x = v.x * m[0] + v.y * m[4] + v.z * m[8] + w * m[12];
        v.y = v.x * m[1] + v.y * m[5] + v.z * m[9] + w * m[13];
        v.z = v.x * m[2] + v.y * m[6] + v.z * m[10] + w * m[14];
    }


    private void updateParticleQuads(Particle _particleData, int idx) {
        if (_particleCount <= 0) {
            return;
        }

        pos.setZero();
        if (_positionType == PositionTypeFree) {
            //Vector3 p1 = new Vector3();
            p1.setZero();
            p1.set(currentPosition.x, currentPosition.y, 0);

            transformPoint(p1);

            p2.setZero();
            newPos.setZero();

            p2.set(_particleData.startPos.x, _particleData.startPos.y, 0);
            // matrix4.trn(p2);
            // matrix4.getTranslation(p2);

            transformPoint(p2);

            newPos.set(_particleData.pos.x, _particleData.pos.y);

            p2.x = p1.x - p2.x;
            p2.y = p1.y - p2.y;

            newPos.x -= p2.x - pos.x;
            newPos.y -= p2.y - pos.y;

            updatePosWithParticle(_particleData, newPos, _particleData.size, _particleData.rotation, idx);


        } else if (_positionType == PositionTypeRelative) {
            newPos.setZero();
            newPos.set(_particleData.pos.x, _particleData.pos.y);
            newPos.x = _particleData.pos.x - (currentPosition.x - _particleData.startPos.x);
            newPos.y = _particleData.pos.y - (currentPosition.y - _particleData.startPos.y);
            newPos.add(pos);
            updatePosWithParticle(_particleData, newPos, _particleData.size, _particleData.rotation, idx);
        } else {
            newPos.setZero();
            newPos.set(_particleData.pos.x + pos.x, _particleData.pos.y + pos.y);
            updatePosWithParticle(_particleData, newPos, _particleData.size, _particleData.rotation, idx);
        }


        Color color = new Color();
        if (m_bOpacityModifyRGB) {
            //for(int i=0;i<_particleCount;++i){

            //	Particle _particleData = m_pParticles[i];

            color.set(_particleData.color.r * _particleData.color.a, _particleData.color.g * _particleData.color.a, _particleData.color.b * _particleData.color.a, _particleData.color.a);

            float[] toUpdate = vertices[idx];
            float colorFloat = color.toFloatBits();
            toUpdate[C1] = colorFloat;
            toUpdate[C2] = colorFloat;
            toUpdate[C3] = colorFloat;
            toUpdate[C4] = colorFloat;
            //}
        } else {
            //for(int i=0;i<_particleCount;++i){
            //	Particle _particleData = m_pParticles[i];
            float[] toUpdate = vertices[idx];
            float colorFloat = _particleData.color.toFloatBits();
            toUpdate[C1] = colorFloat;
            toUpdate[C2] = colorFloat;
            toUpdate[C3] = colorFloat;
            toUpdate[C4] = colorFloat;
            //}
        }
    }

    Vector2 currentPosition = new Vector2();
    Vector3 p1 = new Vector3();
    Vector3 p2 = new Vector3();
    Vector2 newPos = new Vector2();


    private void updatePosWithParticle(Particle _particleData, Vector2 newPosition, float size, float rotation, int pidx) {

        float[] toUpdate = vertices[pidx];

        float size_2 = size / 2;
        float x1 = -size_2;
        float y1 = -size_2;

        float x2 = size_2;
        float y2 = size_2;

        float x = newPosition.x + this.getX();
        float y = newPosition.y + this.getY();

        float r = (float) -CC_DEGREES_TO_RADIANS(rotation);
        float cr = (float) MathUtils.cos(r);
        float sr = (float) MathUtils.sin(r);

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


        toUpdate[U1] = 0;
        toUpdate[V1] = 1;

        toUpdate[U2] = 0;
        toUpdate[V2] = 0;

        toUpdate[U3] = 1;
        toUpdate[V3] = 0;

        toUpdate[U4] = 1;
        toUpdate[V4] = 1;

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


    public void draw(Batch batch, float parentAlpha) {
        drawParticles(batch);

    }

    protected void drawParticles(Batch batch) {
        int srcFunc = batch.getBlendSrcFunc();
        int dstFunc = batch.getBlendDstFunc();
        batch.setBlendFunction(blendSrc, blendDst);
        //System.out.println("_particleCount:"+_particleCount);
        for (int i = 0; i < _particleCount; i++) {
            batch.draw(m_pTexture, vertices[i], 0, 20);
        }
        batch.setBlendFunction(srcFunc, dstFunc);
//        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void stopSystem() {
        _isActive = false;
        _elapsed = _duration;
        _emitCounter = 0;
    }

    public boolean isFull() {
        return (_particleCount == _totalParticles);
    }

    static final Pool<Particle> particlePool = Pools.get(Particle.class, 1000);

    private float RANDOM_M11() {
        return MathUtils.random(-1f, 1f);
    }


    //postion
    Vector2 pos = new Vector2();
    Vector2 v = new Vector2();
    Vector2 dir = new Vector2();

    private void addParticle(int count) {

        int start = _particleCount;


        _particleCount += count;

        pos.setZero();
        if (_positionType == PositionTypeFree) {
            pos = this.localToStageCoordinates(pos);
        } else if (_positionType == PositionTypeRelative) {
            //pos = m_tSourcePosition;
            pos.set(this.getX(), this.getY());
        }


        for (int i = start; i < _particleCount; ++i) {
//    		m_pParticles[i] =  new Particle();;
            m_pParticles[i] = particlePool.obtain();

            //life
            float theLife = m_fLife + m_fLifeVar * RANDOM_M11();
            m_pParticles[i].timeToLive = Math.max(0, theLife);

            //postion
            m_pParticles[i].pos.x = m_tSourcePosition.x + m_tPosVar.x * RANDOM_M11();
            m_pParticles[i].pos.y = m_tSourcePosition.y + m_tPosVar.y * RANDOM_M11();


            float r = MathUtils.clamp(m_tStartColor.r + m_tStartColorVar.r * RANDOM_M11(), 0, 1);
            float g = MathUtils.clamp(m_tStartColor.g + m_tStartColorVar.g * RANDOM_M11(), 0, 1);
            float b = MathUtils.clamp(m_tStartColor.b + m_tStartColorVar.b * RANDOM_M11(), 0, 1);
            float a = MathUtils.clamp(m_tStartColor.a + m_tStartColorVar.a * RANDOM_M11(), 0, 1);

            m_pParticles[i].color.set(r, g, b, a);


            //end color
            r = MathUtils.clamp(m_tEndColor.r + m_tEndColorVar.r * RANDOM_M11(), 0, 1);
            g = MathUtils.clamp(m_tEndColor.g + m_tEndColorVar.g * RANDOM_M11(), 0, 1);
            b = MathUtils.clamp(m_tEndColor.b + m_tEndColorVar.b * RANDOM_M11(), 0, 1);
            a = MathUtils.clamp(m_tEndColor.a + m_tEndColorVar.a * RANDOM_M11(), 0, 1);

            m_pParticles[i].deltaColor.set(r, g, b, a);

            Color deltaColor = m_pParticles[i].deltaColor;
            Color color = m_pParticles[i].color;
            r = (deltaColor.r - color.r) / m_pParticles[i].timeToLive;
            g = (deltaColor.g - color.g) / m_pParticles[i].timeToLive;
            b = (deltaColor.b - color.b) / m_pParticles[i].timeToLive;
            a = (deltaColor.a - color.a) / m_pParticles[i].timeToLive;

            deltaColor.set(r, g, b, a);

            Particle _particleData = m_pParticles[i];
            _particleData.size = m_fStartSize + m_fStartSizeVar * RANDOM_M11();
            _particleData.size = Math.max(0, _particleData.size);


            _particleData.rotation = m_fStartSpin + m_fStartSpinVar * RANDOM_M11();


            float endA = m_fEndSpin + m_fEndSpinVar * RANDOM_M11();
            _particleData.deltaRotation = (endA - _particleData.deltaRotation) / _particleData.timeToLive;

            _particleData.startPos.x = pos.x;
            _particleData.startPos.y = pos.y;


            if (_endSize != START_SIZE_EQUAL_TO_END_SIZE) {
                float endSize = _endSize + _endSizeVar * RANDOM_M11();
                endSize = Math.max(0, endSize);
                _particleData.deltaSize = (endSize - _particleData.size) / _particleData.timeToLive;
            } else {
                m_pParticles[i].deltaSize = 0;
            }

            if (_emitterMode == ParticleModeGravity) {
                _particleData.modeA.radialAccel = modeA.radialAccel + modeA.radialAccelVar * RANDOM_M11();
                _particleData.modeA.tangentialAccel = modeA.tangentialAccel + modeA.tangentialAccelVar * RANDOM_M11();

                if (modeA.rotationIsDir) {
                    float ar = CC_DEGREES_TO_RADIANS(m_fAngle + m_fAngleVar * RANDOM_M11());
                    v.setZero();
                    v.x = (float) Math.cos(ar);
                    v.y = (float) Math.sin(ar);

                    float s = modeA.speed + modeA.speedVar * RANDOM_M11();
                    // Vector2 dir = new Vector2();
                    dir.setZero();
                    dir.mulAdd(v, s);

                    _particleData.modeA.dir.x = dir.x;
                    _particleData.modeA.dir.y = dir.y;
                    _particleData.rotation = -CC_RADIANS_TO_DEGREES(dir.angleRad());
                } else {
                    float ar = CC_DEGREES_TO_RADIANS(m_fAngle + m_fAngleVar * RANDOM_M11());
                    v.setZero();
                    v.x = (float) MathUtils.cos(ar);
                    v.y = (float) MathUtils.sin(ar);

                    float s = modeA.speed + modeA.speedVar * RANDOM_M11();
                    //Vector2 dir = new Vector2();
                    dir.setZero();
                    dir.mulAdd(v, s);

                    _particleData.modeA.dir.x = dir.x;
                    _particleData.modeA.dir.y = dir.y;
                }
            } else {
                _particleData.modeB.radius = modeB.startRadius + modeB.startRadiusVar * RANDOM_M11();

                _particleData.modeB.angle = CC_DEGREES_TO_RADIANS(m_fAngle + m_fAngleVar * RANDOM_M11());

                _particleData.modeB.degreesPerSecond = CC_DEGREES_TO_RADIANS(modeB.rotatePerSecond + modeB.rotatePerSecondVar * RANDOM_M11());

                if (modeB.endRadius == START_RADIUS_EQUAL_TO_END_RADIUS) {
                    _particleData.modeB.deltaRadius = 0;
                } else {
                    float endRadius = modeB.endRadius + modeB.endRadiusVar * RANDOM_M11();
                    _particleData.modeB.deltaRadius = (endRadius - _particleData.modeB.radius) / _particleData.timeToLive;
                }
            }

        }
    }


    private float CC_RADIANS_TO_DEGREES(float angle) {
        return MathUtils.radiansToDegrees * angle;
    }

    private float CC_DEGREES_TO_RADIANS(float angle) {
        return MathUtils.degRad * angle;
    }


    public void setEmissionRate(float emissionRate) {
        assert emissionRate > 0;
        _emissionRate = emissionRate;
    }

    public void setPositionType(int type) {
        if (type != 0 && type != 1 && type != 2) {
            throw new IllegalArgumentException("type error!");
        }
        _positionType = type;
    }


    public void setParticleMode(int mode) {
        if (mode != 0 && mode != 1) {
            throw new IllegalArgumentException("mode error!");
        }
        _emitterMode = mode;
    }

    //持续时间无限
    public static final float DURATION_INFINITY = -1;
    //开始大小等于结束大小
    public static final float START_SIZE_EQUAL_TO_END_SIZE = -1;
    //开始半径等于结束半径
    public static final float START_RADIUS_EQUAL_TO_END_RADIUS = -1;

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

    static class ModeB {
        float startRadius;
        float startRadiusVar;
        float endRadius;
        float endRadiusVar;
        float rotatePerSecond;
        float rotatePerSecondVar;
    }

    float _elapsed;
    Particle[] m_pParticles;
    float _emitCounter;
    int m_uParticleIdx;
    int m_uAllocatedParticles;
    boolean _isActive;
    int _particleCount;
    float _duration;
    public Vector2 m_tSourcePosition = new Vector2();
    Vector2 m_tPosVar = new Vector2();
    float m_fLife, m_fLifeVar;
    public float m_fAngle, m_fAngleVar;
    float m_fStartSize, m_fStartSizeVar;
    float _endSize, _endSizeVar;
    Color m_tStartColor = new Color(), m_tStartColorVar = new Color();
    Color m_tEndColor = new Color(), m_tEndColorVar = new Color();
    float m_fStartSpin, m_fStartSpinVar;
    float m_fEndSpin, m_fEndSpinVar;
    float _emissionRate;
    int _totalParticles;
    Texture m_pTexture;
    int blendSrc, blendDst;
    int _positionType;
    boolean _isAutoRemoveOnFinish;
    int _emitterMode;
    float[][] vertices;
    ModeA modeA = new ModeA();
    ModeB modeB = new ModeB();
    boolean m_bOpacityModifyRGB;
    boolean preMultipliedAlpha;

    int _yCoordFlipped = 1;

    enum PostionType {
        FREE,
        RELATVE,
        GROUPED
    }

    enum Mode {
        GRAVITY,
        RADIUS,
    }

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
