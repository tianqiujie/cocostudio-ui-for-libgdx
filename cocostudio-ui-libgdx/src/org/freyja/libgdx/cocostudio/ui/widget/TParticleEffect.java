package org.freyja.libgdx.cocostudio.ui.widget;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.StreamUtils;

import org.freyja.libgdx.cocostudio.ui.model.CCParticleBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.HashMap;

/**
 * Created by tian on 2016/3/18.
 */
public class TParticleEffect implements Disposable {
    private final Array<TParticleEmitter> emitters;
    private BoundingBox bounds;
    private boolean ownsTexture;

    public TParticleEffect () {
        emitters = new Array(8);
    }

    public TParticleEffect (TParticleEffect effect) {
        emitters = new Array(true, effect.emitters.size);
        for (int i = 0, n = effect.emitters.size; i < n; i++)
            emitters.add(new TParticleEmitter(effect.emitters.get(i)));
    }

    public void start () {
        for (int i = 0, n = emitters.size; i < n; i++)
            emitters.get(i).start();
    }

    public void reset () {
        for (int i = 0, n = emitters.size; i < n; i++)
            emitters.get(i).reset();
    }

    public void update (float delta) {
        for (int i = 0, n = emitters.size; i < n; i++)
            emitters.get(i).update(delta);
    }

    public void draw (Batch spriteBatch) {
        for (int i = 0, n = emitters.size; i < n; i++)
            emitters.get(i).draw(spriteBatch);
    }

    public void draw (Batch spriteBatch, float delta) {
        for (int i = 0, n = emitters.size; i < n; i++)
            emitters.get(i).draw(spriteBatch, delta);
    }

    public void allowCompletion () {
        for (int i = 0, n = emitters.size; i < n; i++)
            emitters.get(i).allowCompletion();
    }

    public boolean isComplete () {
        for (int i = 0, n = emitters.size; i < n; i++) {
            TParticleEmitter emitter = emitters.get(i);
            if (!emitter.isComplete()) return false;
        }
        return true;
    }

    public void setDuration (int duration) {
        for (int i = 0, n = emitters.size; i < n; i++) {
            TParticleEmitter emitter = emitters.get(i);
            emitter.setContinuous(false);
            emitter.duration = duration;
            emitter.durationTimer = 0;
        }
    }

    public void setPosition (float x, float y) {
        for (int i = 0, n = emitters.size; i < n; i++)
            emitters.get(i).setPosition(x, y);
    }

    public void setFlip (boolean flipX, boolean flipY) {
        for (int i = 0, n = emitters.size; i < n; i++)
            emitters.get(i).setFlip(flipX, flipY);
    }

    public void flipY () {
        for (int i = 0, n = emitters.size; i < n; i++)
            emitters.get(i).flipY();
    }

    public Array<TParticleEmitter> getEmitters () {
        return emitters;
    }

    /** Returns the emitter with the specified name, or null. */
    public TParticleEmitter findEmitter (String name) {
        for (int i = 0, n = emitters.size; i < n; i++) {
            TParticleEmitter emitter = emitters.get(i);
            if (emitter.getName().equals(name)) return emitter;
        }
        return null;
    }

    public void save (Writer output) throws IOException {
        int index = 0;
        for (int i = 0, n = emitters.size; i < n; i++) {
            TParticleEmitter emitter = emitters.get(i);
            if (index++ > 0) output.write("\n\n");
            emitter.save(output);
        }
    }

    public void load (FileHandle effectFile, FileHandle imagesDir) {
        loadEmitters(effectFile);
        loadEmitterImages(imagesDir);
    }

    public void load (FileHandle effectFile, TextureAtlas atlas) {
        load(effectFile, atlas, null);
    }

    public void load(CCParticleBean ccParticleBean, Pixmap pixmap){
        loadEmitters(ccParticleBean);
        loadImages(pixmap);
    }

    public void load (FileHandle effectFile, TextureAtlas atlas, String atlasPrefix) {
        loadEmitters(effectFile);
        loadEmitterImages(atlas, atlasPrefix);
    }

    public void loadEmitters(CCParticleBean ccParticleBean){
        TParticleEmitter emitter = new TParticleEmitter(ccParticleBean);
        emitters.add(emitter);
    }

    public void loadEmitters (FileHandle effectFile) {
        InputStream input = effectFile.read();
        emitters.clear();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(input), 512);
            while (true) {
                TParticleEmitter emitter = new TParticleEmitter(reader);
                emitters.add(emitter);
                if (reader.readLine() == null) break;
                if (reader.readLine() == null) break;
            }
        } catch (IOException ex) {
            throw new GdxRuntimeException("Error loading effect: " + effectFile, ex);
        } finally {
            StreamUtils.closeQuietly(reader);
        }
    }

    public void loadEmitterImages (TextureAtlas atlas) {
        loadEmitterImages(atlas, null);
    }

    public void loadEmitterImages (TextureAtlas atlas, String atlasPrefix) {
        for (int i = 0, n = emitters.size; i < n; i++) {
            TParticleEmitter emitter = emitters.get(i);
            String imagePath = emitter.getImagePath();
            if (imagePath == null) continue;
            String imageName = new File(imagePath.replace('\\', '/')).getName();
            int lastDotIndex = imageName.lastIndexOf('.');
            if (lastDotIndex != -1) imageName = imageName.substring(0, lastDotIndex);
            if (atlasPrefix != null) imageName = atlasPrefix + imageName;
            Sprite sprite = atlas.createSprite(imageName);
            if (sprite == null) throw new IllegalArgumentException("SpriteSheet missing image: " + imageName);
            emitter.setSprite(sprite);
        }
    }

    public void loadEmitterImages (FileHandle imagesDir) {
        ownsTexture = true;
        HashMap<String, Sprite> loadedSprites = new HashMap<String, Sprite>(emitters.size);
        for (int i = 0, n = emitters.size; i < n; i++) {
            TParticleEmitter emitter = emitters.get(i);
            String imagePath = emitter.getImagePath();
            if (imagePath == null) continue;
            String imageName = new File(imagePath.replace('\\', '/')).getName();
            Sprite sprite = loadedSprites.get(imageName);
            if (sprite == null) {
                sprite = new Sprite(loadTexture(imagesDir.child(imageName)));
                loadedSprites.put(imageName, sprite);
            }
            emitter.setSprite(sprite);
        }
    }

    /**
     *
     * @param pixmap
     */
    public void loadImages(Pixmap pixmap){
        HashMap<String, Sprite> loadedSprites = new HashMap<String, Sprite>(emitters.size);
        for (int i = 0, n = emitters.size; i < n; i++) {
            TParticleEmitter emitter = emitters.get(i);
            String imagePath = emitter.getImagePath();
            if (imagePath == null) continue;
            String imageName = new File(imagePath.replace('\\', '/')).getName();
            Sprite sprite = loadedSprites.get(imageName);
            if (sprite == null) {
                //sprite = new Sprite(loadTexture(imagesDir.child(imageName)));
                sprite = new Sprite(new Texture(pixmap));
                loadedSprites.put(imageName, sprite);
            }
            emitter.setSprite(sprite);
        }
    }

    protected Texture loadTexture (FileHandle file) {
        return new Texture(file, false);
    }

    /** Disposes the texture for each sprite for each TParticleEmitter. */
    public void dispose () {
        if (!ownsTexture) return;
        for (int i = 0, n = emitters.size; i < n; i++) {
            TParticleEmitter emitter = emitters.get(i);
            emitter.getSprite().getTexture().dispose();
        }
    }

    /** Returns the bounding box for all active particles. z axis will always be zero. */
    public BoundingBox getBoundingBox () {
        if (bounds == null) bounds = new BoundingBox();

        BoundingBox bounds = this.bounds;
        bounds.inf();
        for (TParticleEmitter emitter : this.emitters)
            bounds.ext(emitter.getBoundingBox());
        return bounds;
    }

    public void scaleEffect (float scaleFactor) {
        for (TParticleEmitter TParticleEmitter : emitters) {
            TParticleEmitter.getScale().setHigh(TParticleEmitter.getScale().getHighMin() * scaleFactor,
                    TParticleEmitter.getScale().getHighMax() * scaleFactor);
            TParticleEmitter.getScale().setLow(TParticleEmitter.getScale().getLowMin() * scaleFactor,
                    TParticleEmitter.getScale().getLowMax() * scaleFactor);

            TParticleEmitter.getVelocity().setHigh(TParticleEmitter.getVelocity().getHighMin() * scaleFactor,
                    TParticleEmitter.getVelocity().getHighMax() * scaleFactor);
            TParticleEmitter.getVelocity().setLow(TParticleEmitter.getVelocity().getLowMin() * scaleFactor,
                    TParticleEmitter.getVelocity().getLowMax() * scaleFactor);

            TParticleEmitter.getGravity().setHigh(TParticleEmitter.getGravity().getHighMin() * scaleFactor,
                    TParticleEmitter.getGravity().getHighMax() * scaleFactor);
            TParticleEmitter.getGravity().setLow(TParticleEmitter.getGravity().getLowMin() * scaleFactor,
                    TParticleEmitter.getGravity().getLowMax() * scaleFactor);

            TParticleEmitter.getWind().setHigh(TParticleEmitter.getWind().getHighMin() * scaleFactor,
                    TParticleEmitter.getWind().getHighMax() * scaleFactor);
            TParticleEmitter.getWind().setLow(TParticleEmitter.getWind().getLowMin() * scaleFactor,
                    TParticleEmitter.getWind().getLowMax() * scaleFactor);

            TParticleEmitter.getSpawnWidth().setHigh(TParticleEmitter.getSpawnWidth().getHighMin() * scaleFactor,
                    TParticleEmitter.getSpawnWidth().getHighMax() * scaleFactor);
            TParticleEmitter.getSpawnWidth().setLow(TParticleEmitter.getSpawnWidth().getLowMin() * scaleFactor,
                    TParticleEmitter.getSpawnWidth().getLowMax() * scaleFactor);

            TParticleEmitter.getSpawnHeight().setHigh(TParticleEmitter.getSpawnHeight().getHighMin() * scaleFactor,
                    TParticleEmitter.getSpawnHeight().getHighMax() * scaleFactor);
            TParticleEmitter.getSpawnHeight().setLow(TParticleEmitter.getSpawnHeight().getLowMin() * scaleFactor,
                    TParticleEmitter.getSpawnHeight().getLowMax() * scaleFactor);

            TParticleEmitter.getXOffsetValue().setLow(TParticleEmitter.getXOffsetValue().getLowMin() * scaleFactor,
                    TParticleEmitter.getXOffsetValue().getLowMax() * scaleFactor);

            TParticleEmitter.getYOffsetValue().setLow(TParticleEmitter.getYOffsetValue().getLowMin() * scaleFactor,
                    TParticleEmitter.getYOffsetValue().getLowMax() * scaleFactor);
        }
    }

    public void setEmittersCleanUpBlendFunction (boolean cleanUpBlendFunction) {
        for (int i = 0, n = emitters.size; i < n; i++) {
            emitters.get(i).setCleansUpBlendFunction(cleanUpBlendFunction);
        }
    }
}