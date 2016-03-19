package org.freyja.libgdx.cocostudio.ui.particleutil;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

//每一个粒子的属性
public class Particle implements Poolable{
    public class ModeGravity{ //重力模式
        public Vector2 dir = new Vector2(); //重力的方向
        public float        radialAccel;  //径向加速度
        public float        tangentialAccel;//切向加速度
        
        public void reset(){
        	this.dir.setZero();
        	this.radialAccel = 0;
        	this.tangentialAccel = 0;
        }
    }
    public class ModeRadius{//中心模式
        public float        angle;              //单位是弧度
        public float        degreesPerSecond; //弧度差
        public float        radius;            //半径
        public float        deltaRadius;      //半径差
        
        public void reset(){
        	this.angle = 0;
        	this.degreesPerSecond = 0;
        	this.radius = 0;
        	this.deltaRadius = 0;
        }
    }
    public Vector2 pos = new Vector2();      //位置
    public Vector2 startPos = new Vector2();//初始位置
    public Color color = new Color();    //颜色
    public Color deltaColor = new Color();//颜色差
    public float        size;                 //大小
    public float        deltaSize;           //大小差
    public float        rotation;           //旋转
    public float        deltaRotation;     //旋转差
    public float        timeToLive;        //生存时间
    public ModeGravity modeA = new ModeGravity();
    public ModeRadius modeB = new ModeRadius();
    
    
	@Override
	public void reset() {
		this.pos.setZero();
		this.startPos.setZero();
		this.color.set(Color.CLEAR);
		this.deltaColor.set(Color.CLEAR);
		this.size = 0;
		this.deltaSize = 0;
		this.deltaRotation = 0;
		this.timeToLive = 0;
		modeA.reset();
		modeB.reset();
		
		
	}
}
