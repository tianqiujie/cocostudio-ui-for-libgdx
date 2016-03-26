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
package net.mwplay.cocostudio.ui.widget;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import net.mwplay.cocostudio.ui.model.Size;

/**
 * author Tian
 * email:tqj.zyy@gmail.com
 */
public class TLoadingBar extends Actor {
    private int value = 100;
    private TextureRegion bar;
    private Size rect;

    public TextureRegion getBar() {
        return bar;
    }

    public TLoadingBar(TextureRegion bar) {
        this.bar = bar;
        rect = new Size();
        rect.setX(bar.getRegionWidth());
        rect.setY(bar.getRegionHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(bar, getX(), getY(), getOriginX(), getOriginY(), bar.getRegionWidth(), bar.getRegionHeight(),
                getScaleX(), getScaleY(), getRotation());
    }

    public void setValue(int value) {
        this.value = value;
        bar.setRegion(0, 0, (int) (rect.getX() * value / 100f), (int)(rect.getY()));
    }

    public int getValue() {
        return value;
    }
}
